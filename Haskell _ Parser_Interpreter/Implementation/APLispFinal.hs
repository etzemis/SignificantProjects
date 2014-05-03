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
type Environment = M.Map String SExp
---------------------------------------------------------------------
emptyEnv :: Environment
emptyEnv =  M.empty 
----------------------------------------------------------------------
-- Part 1 --
true :: SExp -> Bool
true (ListExp l) = not (null l)
true _ = True
bindVar :: String -> SExp -> Environment -> Environment
bindVar = M.insert
----------------------------------------------------------------------
-- Part 2 --
newtype APLispExec a = RC { runLisp :: Environment -> Either String a} 
instance Monad APLispExec where
         return x = RC $ \_ -> Right x
         m >>= f  = RC $ \env -> case runLisp m env of
                                   Left e -> Left $ show e
                                   Right r -> runLisp (f r) env
local :: (Environment -> Environment) -> APLispExec a -> APLispExec a
local f m = RC $ \env -> let env' = f env
                         in runLisp m env'
                         
lookupExpr :: String -> Environment -> Maybe SExp
lookupExpr = M.lookup

ask :: APLispExec Environment
ask = RC $ \env -> Right env

invalid :: APLispExec a
invalid = RC $ \_ -> Left "Wrong Operator"

lookupVar :: String -> APLispExec SExp
lookupVar name = do env <- ask
                    case lookupExpr name env of
                         Just c  -> return c
                         Nothing -> invalid
interp :: String -> Either String SExp
interp s = case parseString s of
                   Left e -> Left $ show e
                   Right exp1 -> runLisp (eval exp1) emptyEnv
evalSExp :: SExp -> APLispExec (String, SExp)
evalSExp (ListExp [SymbolVal vname, exp1])= do
              d <- eval exp1
              local (bindVar vname d)$ do
              return ( vname,d)
              
combinetoTuple :: [SExp]->[SExp]->[(String, SExp)]
combinetoTuple [] _ = []
combinetoTuple (SymbolVal s : var) val = 
        ((s, head val): xs)
        where
                xs = combinetoTuple var (tail val) 
                                
addToEnv :: [(String, SExp)] -> Environment -> Environment
addToEnv [] env = env
addToEnv ((name, c):cs) env =
  addToEnv cs (bindVar name c env)
                        
eval :: SExp -> APLispExec SExp
eval (SymbolVal s) = lookupVar s
eval (IntVal s) = return $ IntVal s
eval (ListExp []) = return $ ListExp []
------------------------------------------- Part 3 Lamda functions --------------------------------
eval (ListExp [SymbolVal "let",ListExp [ListExp [SymbolVal "f",ListExp [SymbolVal "lambda",ListExp largs,ListExp (SymbolVal s :lbody)]]],ListExp [SymbolVal "funcall",SymbolVal "f",ListExp args]]) = do 
                                                                if (xc == cs) 
                                                                   then do
                                                                        ListExp evalArgs <- eval (ListExp args)
                                                                        local ( addToEnv (combinetoTuple largs evalArgs) ) $ do
                                                                              evalLbody <- evalInt lbody
                                                                              apply (SymbolVal s) evalLbody
                                                                                      
                                                                                        
                                                                else fail "Lamda function called with wrong number of elements!"
                                                                                
                                                                where  xc = length largs
                                                                       cs = length (tail args)
----------------------------------------Let function -----------------------------------------------
eval (ListExp [SymbolVal "let", ListExp bindings, ListExp (SymbolVal s :args)]) =do 
                                                                case bindings of 
                                                                 (ListExp x:xs) -> do                   ---if more than one bindings
                                                                        cs <- mapM evalSExp bindings
                                                                        local (addToEnv cs) $ do
                                                                        xs <- evalInt args
                                                                        apply (SymbolVal s) xs
                                                                 otherwise -> do                        -- if only one binding
                                                                        cs <- mapM evalSExp [ListExp bindings]
                                                                        local (addToEnv cs) $ do
                                                                        xs <- evalInt args
                                                                        apply (SymbolVal s) xs
                                                                 
                                                              
eval (ListExp [SymbolVal "quote", x]) = return x
eval (ListExp (SymbolVal "quote":_)) = fail "Quote called with wrong number of arguments"
eval (ListExp (SymbolVal s:args)) = do
                                        xs <- evalInt args
                                        apply (SymbolVal s) xs
evalInt :: [SExp] -> APLispExec [SExp] 
evalInt sexpres = if null sexpres    
                         then return []
                         else do
                             initi <- eval (head sexpres)
                             rest <- evalInt (tail sexpres)
                             return (initi:rest)       
apply :: SExp -> [SExp] -> APLispExec SExp
apply (IntVal _) _ = fail "Cannot apply integers!"
apply (ListExp _) _ = fail "Cannot apply lists!"
apply (SymbolVal "+") [(IntVal x),(IntVal y)] = return $ IntVal (x + y)
apply (SymbolVal "+") ((IntVal x):xs) = do
        IntVal xxs <- apply(SymbolVal "+") (xs)
        return $ IntVal (x + xxs)
apply (SymbolVal "-") [(IntVal x),(IntVal y)] = return $ IntVal (x - y)
apply (SymbolVal "-") ((IntVal x):xs) = do
        IntVal xxs <- apply(SymbolVal "+") (xs)
        return $ IntVal (x - xxs)
apply (SymbolVal "*") [(IntVal x),(IntVal y)] = return $ IntVal (x * y)
apply (SymbolVal "*") ((IntVal x):xs) = do
        IntVal xxs <- apply(SymbolVal "*") (xs)
        return $ IntVal (x * xxs)
apply (SymbolVal "/") (IntVal x:[IntVal xs]) = if 
                                                xs /= 0 
                                                 then return $ IntVal (div x xs)
                                                 else fail "Division with zero(0)"
apply (SymbolVal "=") (IntVal x:[IntVal xs]) = if (IntVal x) == (IntVal xs) 
                                                        then return $ SymbolVal "True" 
                                                        else return $ ListExp []
apply (SymbolVal "!=") (IntVal x:[IntVal xs]) = if (IntVal x) /= (IntVal xs) 
                                                        then return $ SymbolVal "True" 
                                                        else return $ ListExp []
apply (SymbolVal "<") (IntVal x:[IntVal xs]) = if (IntVal x) < (IntVal xs) 
                                                        then return $ SymbolVal "True" 
                                                        else return $ ListExp []
apply (SymbolVal ">") (IntVal x:[IntVal xs]) = if (IntVal x) > (IntVal xs) 
                                                        then return $ SymbolVal "True" 
                                                        else return $ ListExp []
apply (SymbolVal "<=") (IntVal x:[IntVal xs]) = if (IntVal x) <= (IntVal xs) 
                                                        then return $ SymbolVal "True" 
                                                        else return $ ListExp []
apply (SymbolVal ">=") (IntVal x:[IntVal xs]) = if (IntVal x) >= (IntVal xs) 
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

----------------------------------------------------------------------
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
         

a = interp "(* 3 3 5)"
a2 = interp "()"
a3 = interp "2"
a6 = interp "(let (x 2) (+ x 2))"
a7 = interp "(let ((f (lambda (x y) (+ x y x)))) (funcall f (list 5 7)))"
a9= interp "(let ((x 5) (y (+ 23 12))) (+ x y))"
a10= interp "(let ((x 5) (y (+ 23 2)) (z 6)) (+ x y 7 z))"