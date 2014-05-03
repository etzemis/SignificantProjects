train = load('StatMLExam/seedsTrain.dt');


trainIns = train(: , 1:7)
trainIns = NORM(trainIns)
trainLab = train(: , 8)
%%
t1 = ones(4,7);
ct1 = 1;
t2 = ones(4,7);
ct2 = 1;
t3 = ones(4,7);
ct3 = 1;
for i = 1:length(trainIns)
   if trainLab(i) == 0
       t1(ct1,:) = trainIns(i,:)
       ct1 = ct1+1      
   elseif trainLab(i) == 1
       t2(ct2,:) = trainIns(i,:)
       ct2=ct2+1     
   elseif trainLab(i) == 2
       t3(ct3,:) = trainIns(i,:)
       ct3=ct3+1
   end
    
end


%% LDA

m1 = mean(t1);
m2 = mean(t2);
m3 = mean(t3);
S1 = zeros(7,7);
for i = 1:length(t1)
    Temp = (t1(i,:) - m1);
    S1 = S1+ (Temp'*Temp); 
end

S2 = zeros(7,7);
for i = 1:length(t2)
    Temp = (t2(i,:) - m2);
    S2 = S2+  Temp'*Temp; 
end

S3 = zeros(7,7);
for i = 1:length(t3)
    Temp = (t3(i,:) - m3);
    S3 = S3+  Temp'*Temp ;
end


STotal = (S1+S2+S3)/(110 - 3)

%% Find error for the training elements


Pr1 = length(t1)/length(train);
Pr2 = length(t2)/length(train);
Pr3 = length(t3)/length(train);
err = 0;
for i = 1:length(train)
   delta = zeros(1,3);
   delta(1) = (trainIns(i,:)* inv(STotal)*m1') - (0.5 * (m1*inv(STotal)*m1') ) + log(Pr1);
   delta(2) = (trainIns(i,:)* inv(STotal)*m2') - (0.5 * (m2*inv(STotal)*m2') ) + log(Pr2);
   delta(3) = (trainIns(i,:)* inv(STotal)*m3') - (0.5 * (m3*inv(STotal)*m3') ) + log(Pr3);
  
   [e,ind] = max(delta)
   if((ind-1) ~= trainLab(i))
       err = err+1;
   end
end

LDAtraining_err = err/110
   
%% Find error for the test elements
test = load('StatMLExam/seedsTest.dt');

testIns = test(:,1:7)
testIns = NORM(testIns)
testLab = test(: , 8)


err = 0;
for i = 1:length(test)
   delta = zeros(1,3);
   delta(1) = (testIns(i,:)* inv(STotal)*m1') - (0.5 * (m1*inv(STotal)*m1') ) + log(Pr1);
   delta(2) = (testIns(i,:)* inv(STotal)*m2') - (0.5 * (m2*inv(STotal)*m2') ) + log(Pr2);
   delta(3) = (testIns(i,:)* inv(STotal)*m3') - (0.5 * (m3*inv(STotal)*m3') ) + log(Pr3);
  
   [e,ind] = max(delta)
   if((ind - 1) ~= testLab(i))
       err = err+1;
   end
end

 LDAtest_err = err/length(test);
 
 
 %% 2.2 KNN
 %For testing data
 clas1 = knnclassify(testIns , trainIns, trainLab, 1);
 clas3 = knnclassify(testIns , trainIns, trainLab, 3);
 clas5 = knnclassify(testIns , trainIns, trainLab, 5);
 clas7 = knnclassify(testIns , trainIns, trainLab, 7);
 
 
 testerr1 = 0;
 testerr3 = 0;
 testerr5 = 0;
 testerr7 = 0;
 for i = 1:length(test)
     if(clas1(i) ~= testLab(i))
         testerr1 = testerr1 + 1;
     end
     if(clas3(i) ~= testLab(i))
         testerr3 = testerr3 + 1;
     end
     if(clas5(i) ~= testLab(i))
         testerr5 = testerr5 + 1;
     end
     if(clas7(i) ~= testLab(i))
         testerr7 = testerr7 + 1;
     end
     
 end
 testerr1 = testerr1/length(test);
 testerr3 = testerr3/length(test);
 testerr5 = testerr5/length(test);
 testerr7 = testerr7/length(test);
 
 KNNtesterror = min([testerr1 testerr3 testerr5 testerr7])
 
 %% for training data

 clas1 = knnclassify(trainIns , trainIns, trainLab, 1);
 clas3 = knnclassify(trainIns , trainIns, trainLab, 3);
 clas5 = knnclassify(trainIns , trainIns, trainLab, 5);
 clas7 = knnclassify(trainIns , trainIns, trainLab, 7);
 
 
 trainerr1 = 0;
 trainerr3 = 0;
 trainerr5 = 0;
 trainerr7 = 0;
 for i = 1:length(train)
     if(clas1(i) ~= trainLab(i))
         trainerr1 = trainerr1 + 1;
     end
     if(clas3(i) ~= trainLab(i))
         trainerr3 = trainerr3 + 1;
     end
     if(clas5(i) ~= trainLab(i))
         trainerr5 = trainerr5 + 1;
     end
     if(clas7(i) ~= trainLab(i))
         trainerr7 = trainerr7 + 1;
     end
     
 end
 trainerr1 = trainerr1/length(train);
 trainerr3 = trainerr3/length(train);
 trainerr5 = trainerr5/length(train);
 trainerr7 = trainerr7/length(train);
 
 KNNtrainerror = min([trainerr1 trainerr3 trainerr5 trainerr7])
