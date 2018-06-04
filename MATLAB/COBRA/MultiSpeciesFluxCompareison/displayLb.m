function [outData] = displayLb(modelIn)

rxnsOfIntrest = (modelIn.lb > -1000) & findExcRxns(modelIn);
joinedColumns = {modelIn.rxnNames(rxnsOfIntrest); num2cell(modelIn.lb(rxnsOfIntrest))};
outData = horzcat(joinedColumns{:});
disp(outData);