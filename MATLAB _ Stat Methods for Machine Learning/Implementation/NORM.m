function z = NORM(x)
    m = mean(x)
    s = (diag(cov(x))'.^0.5)
    for i=1:size(x,1)
       z(i,:)  = (x(i,:) - m)./s
    end
end