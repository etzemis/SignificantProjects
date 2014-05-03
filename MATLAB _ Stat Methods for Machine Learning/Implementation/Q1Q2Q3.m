Tr = load('StatMLExam/sunspotsTrainStatML.dt')
test  = load('StatMLExam/sunspotsTestStatML.dt')
%each year in a seperate row and six parameters per year

TrainInp(:,2:6) = Tr(:,1:5);
Ytrain(:,1) = Tr(:,6);

TestInp(:,2:6) = test(:,1:5);
Ytest(:,1) = test(:,6);

%%  Q1  LINEAR REGRESSION

%% find train error
TrainInp(:,1) = ones(size(Tr , 1),1);
B = pinv(TrainInp);
W = B*Ytrain;
Y = TrainInp*W;  
Dif = Ytrain-Y;
Dif = Dif(:).^2;
RMSTrain = sqrt(sum(Dif)/size(Tr,1));
%% find test error
TestInp(:,1) = ones(size(test , 1),1);
YLinear = TestInp*W; 
Dif = Ytest-YLinear;
Dif = Dif(:).^2;
RMSTest = sqrt(sum(Dif)/size(test,1));

%% Q2  NON-LINEAR REGRESSION


inputs = TrainInp';
targets = Ytrain';

% Create a Fitting Network
hiddenLayerSize = 6;
net = fitnet(hiddenLayerSize);
% Setup Division of Data for Training, Validation, Testing
%net.divideFcn = 'dividerand';  % Divide data randomly
net.divideParam.trainRatio = 100/100;
net.divideParam.valRatio = 0/100;
net.divideParam.testRatio = 0/100;
net.trainFcn = 'trainlm';  % Levenberg-Marquardt
net.performFcn = 'mse';  % Mean squared error
% Train the Network
[net,tr] = train(net,inputs,targets);
% Test the Network
Y = net(inputs);
Y = Y';
Dif = Ytrain-Y;
Dif = Dif(:).^2;
RMSTrainNonL = sqrt(sum(Dif)/size(Tr,1));

YNonLinear = net(TestInp');
Y = YNonLinear';
Dif = Ytest-Y;
Dif = Dif(:).^2;
RMSTestNonL = sqrt(sum(Dif)/size(test,1));
    


%%  Q3  VISUALIZATION OF RESULTS
years = 1916:1:2011;
figure;
set(gcf(),'numbertitle','off','name','!916-2011 green-> actual values  blue-> Linear  red ->NonLinear')
hold on
actual = plot(years , test(:,6))
set(actual,'Color','green')
Linear = plot(years , YLinear) 
set(Linear,'Color','blue')
NonLinear = plot(years , YNonLinear') 
set(NonLinear,'Color','red')
hold off
