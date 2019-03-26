#!/bin/sh
idris -p effects --codegen javascript haha.idr -o haha.js && \
closure-compiler --compilation_level ADVANCED_OPTIMIZATIONS --js haha.js > haha_opt.js
