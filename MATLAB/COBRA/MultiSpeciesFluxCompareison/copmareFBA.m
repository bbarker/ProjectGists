function [msObj, origObj, msFlux, origFlux] = copmareFBA(modelIn)

msModel =  createMultipleSpeciesModel({modelIn}, {'modelIn'});

solOrig = optimizeCbModel(modelIn);
origObj = solOrig.f;
origFlux = solOrig.v;

solOrig = optimizeCbModel(msModel);
msObj = solOrig.f;
msFlux = solOrig.v;