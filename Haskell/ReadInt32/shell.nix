######
#
# Author: Brandon Barker
#
######

with import <nixpkgs> { };
stdenv.mkDerivation {
  name = "hico-stack";
  buildInputs = [
    gmp
    stack
    zlib
  ];
  shellHook = ''
    export LD_LIBRARY_PATH=${gmp}/lib:${zlib}/lib:$LD_LIBRARY_PATH
  '';  
}

