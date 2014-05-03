train = load('StatMLExam/quasarsStarsStatMLTrain.dt');
test = load('StatMLExam/quasarsStarsStatMLTest.dt');

%% Q4
%Calculate the Jakkolla parameter.

counter = 1;
G = zeros(803700 , 1);
for i=1:size(train,1)
   if train(i,6) == 0
       for j=1: size(train,1)
          if(train(j,6) == 1)
                G(counter) = norm(train(i,1:5) - train(j,1:5));
                counter = counter + 1;
          end
       end
   end
end
Sjakkolla = median(G)
Gjakkolla = 1/(2*(Sjakkolla^2))

%% define hyperparameters gama and C
b = 2;
Cost = [1/b 6 b b^2 b^3];
i = [-3 -2 -1 0 1 2 3];
Gama = Gjakkolla*(b.^i);

train_inst = sparse(train(:,1:5));
train_label = train(:,6);
%train_label(train_label == 0) = -1;
bestScore = 0;
for i=1:size(Cost,2)
    for j = 1:size(Gama,2)
        out = svmtrain(train_label , train_inst, sprintf('-v 5 -c %d -g %d' , Cost(i) , Gama(j)));  %5 cross-validation
        if out > bestScore
            bestScore = out
            bestG = j;
            bestC = i;
        end
    end
end

%% after founding best G and C train your system
GamaChosen = Gama(bestG);
CostChosen = Cost(bestC);
trainOut = svmtrain(train_label , train_inst, sprintf('-c %d -g %d' , Cost(bestC) , Gama(bestG)));
% now we have the training and we want to make predictions in the test and
% the training data

SvmTrainOut = svmpredict(train_label , train_inst, trainOut);
TrainLoss = sum(abs(SvmTrainOut - train_label))
TrainLossPercentage = 100*(length(train)-TrainLoss)/length(train)
test_inst = sparse(test(:,1:5));
test_label = test(:,6);

SvmTestOut = svmpredict(test_label , test_inst, trainOut);
TestLoss = sum(abs(SvmTestOut - test_label))
TestLossPercentage = 100*(length(test)-TestLoss)/length(test)