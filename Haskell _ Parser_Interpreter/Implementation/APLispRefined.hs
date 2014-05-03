module APLisp where
import qualified Data.Map as M
import Text.ParserCombinators.ReadP
import Data.Char(isSpace,isDigit)
import Control.Monad

data SExp = IntVal Int
          | SymbolVal String          
          | ListExp [SExp]
            deriving (Show, Eq, Ord)
type Program            = SExp 
type Environment = M.Map String SExp   -- It is a mapping from Strings to SExpressions
type Error = String
-------------------------------------------------------------------------------------------------
-- Parser --
type Parser a = ReadP a
(<|>) = (+++)
parse = readP_to_S
token            :: Parser a -> Parser a
token p          = skipSpaces >> p
symbol           = token . string
schar            = token . char
numberOrSymbol :: Parser SExp
numberOrSymbol = token $ do s <- munch1 $ \c -> not(isSpace c || c `elem` "()")
                            return $ if all isDigit s then IntVal $ read s
                                     else SymbolVal s
sexp :: Parser SExp
sexp = numberOrSymbol
       <|> between (schar '(') (schar ')') sexps
  where sexps = many sexp >>= return . ListExp
parseString :: String -> Either String SExp
parseString s =
  case parse (do {e <- sexp; token eof; return e}) s of
      [(e, [])] -> Right e
      _         -> Left "Parse error" 
-------------------------------------------------------------------------------------------------------
interp :: String -> Either String SExp
interp s = case parseString s of
                   Left e -> Left $ show e
                   Right exp1 -> runLisp (eval exp1) emptyEnv

-- it takes an expression ( used for the if statement)
true :: SExp -> Bool
true (ListExp []) = False
true _ = True
--
bindVar :: String -> SExp -> Environment -> Environment
bindVar = M.insert  -- insert it inside the mapping
--
emptyEnv :: Environment
emptyEnv = M.empty
--
addToEnv :: [(String, SExp)] -> Environment -> Environment
addToEnv [] env = env
addToEnv ((name, c):cs) env =
  addToEnv cs (bindVar name c env)
--
ask :: APLispExec Environment
ask = RC $ \env -> Right env
--
invalid :: APLispExec a
invalid = RC $ \_ -> Left "Wrong Operator"
--
lookupVar :: String -> APLispExec SExp
lookupVar name = do env <- ask
                    case M.lookup name env of
                         Just c  -> return c
                         Nothing -> invalid
--
newtype APLispExec a = RC { runLisp :: Environment -> Either Error a}
instance Monad APLispExec where
         return x = RC $ \_ -> Right x
         m >>= f  = RC $ \env -> case runLisp m env of
                                      Left e  ->  Left e 
                                      Right x ->  runLisp (f x) env
--
local :: (Environment -> Environment) -> APLispExec a -> APLispExec a
local f x = RC $ \env -> do   runLisp x (f env)
--
eval :: SExp -> APLispExec SExp
eval e@(ListExp (SymbolVal "lambda":ListExp _:_)) = return e
eval (ListExp (SymbolVal "lambda":_)) = fail "Bad lambda"
eval (SymbolVal s) = lookupVar s
eval (IntVal v) = return $ IntVal v
eval (ListExp []) = return $ ListExp []
eval (ListExp [SymbolVal "quote", x]) = return x
eval (ListExp (SymbolVal "quote":_)) = fail "Quote called with wrong number of arguments"
-- What happens if the input is (1 2 3) ?? What do we want to implement
{-eval (ListExp (IntVal s:args)) = do
     ListExp evaluatedArgs <- eval (ListExp args)
     return $ ListExp (IntVal s:evaluatedArgs)
-}
-- 
eval (ListExp [SymbolVal "if", condition, trueExpr, falseExpr]) = 
     if true condition
     then eval trueExpr
     else eval falseExpr 
--
eval (ListExp [SymbolVal "let", ListExp [var, val], body]) =  do
     bindTuples <- handleBindings (ListExp [var,val])
     local(addToEnv [bindTuples]) $ do
     eval body
eval (ListExp [SymbolVal "let", ListExp (b:bindings), body]) = do
     bindTuples <- mapM handleBindings (b:bindings)
     local(addToEnv bindTuples) $ do
     eval body           
--
eval (ListExp (SymbolVal s:args)) = do
     evaluatedArgs <- evalArgumentsofList args  -- if there are variables inside then it will handle it
     apply (SymbolVal s) evaluatedArgs 
--
evalArgumentsofList :: [SExp] -> APLispExec [SExp]
evalArgumentsofList []  = return []
evalArgumentsofList (x:xs) =  do
                    h <- eval x
                    t <- evalArgumentsofList xs
                    return (h:t) 
--
handleBindings :: SExp -> APLispExec (String,SExp)
handleBindings (ListExp [SymbolVal s, value]) = do
               d <- eval value
               return (s,d)
--
apply :: SExp -> [SExp] -> APLispExec SExp
apply (IntVal _) _ = fail "Cannot apply integers!"
apply (ListExp _) _ = fail "Cannot apply lists!"
apply (SymbolVal "list") xs = return $ ListExp xs
apply (SymbolVal "+") [IntVal x,IntVal y] = return $ IntVal (x + y)
apply (SymbolVal "+") (IntVal x:xs) = do
        IntVal xxs <- apply(SymbolVal "+") xs
        return $ IntVal (x + xxs)
apply (SymbolVal "-") [IntVal x,IntVal y] = return $ IntVal (x - y)
apply (SymbolVal "-") (IntVal x:xs) = do
        IntVal xxs <- apply(SymbolVal "+") xs
        return $ IntVal (x - xxs)
apply (SymbolVal "*") [IntVal x,IntVal y] = return $ IntVal (x * y)
apply (SymbolVal "*") (IntVal x:xs) = do
        IntVal xxs <- apply(SymbolVal "*") xs
        return $ IntVal (x * xxs)
apply (SymbolVal "/") [IntVal x,IntVal y] = if y /= 0 
                                                 then return $ IntVal (div x y)
                                                 else fail "Division with zero(0)"
apply (SymbolVal "=") [IntVal x,IntVal y] = if (IntVal x) == (IntVal y) 
                                                        then return $ SymbolVal "True" 
                                                        else return $ ListExp []
apply (SymbolVal "!=") [IntVal x,IntVal y] = if (IntVal x) /= (IntVal y) 
                                                        then return $ SymbolVal "True" 
                                                        else return $ ListExp []
apply (SymbolVal "<") [IntVal x,IntVal y] = if (IntVal x) < (IntVal y) 
                                                        then return $ SymbolVal "True" 
                                                        else return $ ListExp []
apply (SymbolVal ">") [IntVal x,IntVal y] = if (IntVal x) > (IntVal y) 
                                                        then return $ SymbolVal "True" 
                                                        else return $ ListExp []
apply (SymbolVal "<=") [IntVal x,IntVal y] = if (IntVal x) <= (IntVal y) 
                                                        then return $ SymbolVal "True" 
                                                        else return $ ListExp []
apply (SymbolVal ">=") [IntVal x,IntVal y] = if (IntVal x) >= (IntVal y) 
                                                        then return $ SymbolVal "True" 
                                                        else return $ ListExp []                            
apply (SymbolVal "list") xs                   = return $ ListExp xs 
apply (SymbolVal "car") xs                    = if ((head xs) == ListExp []) 
                                                        then fail "Empty list in car function" 
                                                        else return $ head xs         
apply (SymbolVal "cdr") xs                    = if ((last xs) == ListExp []) 
                                                        then fail "Empty list in cdr function" 
                                                        else return $ last xs  
apply (SymbolVal "cons") ([IntVal x,ListExp xs]) = return $ ListExp ((IntVal x):xs)
apply _ _ = return $ ListExp []  --fail " Something is wrong in your input"
---------------------------------------------------------
a = interp "(/ 3 3 4)"
b = interp "(if () (+ 1 2) (+ 5 5))"
c = interp "(let ((x 5) (y 4) (z 3)) (+ x y z 2))"
d = interp "(let (x 5) (+ x 2))"
e = interp "(let (f (lambda (x y) (+ x y))) (funcall f (list 2 3)))"