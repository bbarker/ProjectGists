function [outData] = displayLb(modelIn)

joinedColumns = {modelIn.rxnNames(find(modelIn.lb)); num2cell(modelIn.lb(find(modelIn.lb)))}
outData = horzcat(joinedColumns{:});
disp(outData);