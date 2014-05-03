train = load('StatMLExam/seedsTrain.dt');
%%
trainInst = NORM(train(:,1:7));
trainLabel = train(:,8);
[Coef,Score , e] = princomp(trainInst);
figure;
plot(e);
%%
figure;
[IDX,C] = kmeans(Score(:,1:2) , 3)
hold on
plot(Score(trainLabel == 0 ,1), Score(trainLabel == 0,2) , '*g')
plot(Score(trainLabel == 1 ,1), Score(trainLabel == 1,2) , '*r')
plot(Score(trainLabel == 2 ,1), Score(trainLabel == 2,2) , '*b')
plot(C(:,1) , C(:,2) , '+k')
hold off