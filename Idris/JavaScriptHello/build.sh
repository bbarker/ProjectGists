#!/bin/sh
idris -p effects --codegen javascript hello.idr -o hello.js
 closure-compiler --compilation_level ADVANCED_OPTIMIZATIONS --js hello.js > hello_opt.js
