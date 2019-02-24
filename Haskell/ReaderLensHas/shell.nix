######
#
# Author: Brandon Barker
#
######

with import <nixpkgs> { };
stdenv.mkDerivation {
  name = "hico-stack";
  buildInputs = [
    cabal-install
    gmp
    stack
  ];
  shellHook = ''
    export LD_LIBRARY_PATH=${gmp}/lib:$LD_LIBRARY_PATH
  '';  
}

