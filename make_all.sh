#!/bin/bash
cd ./po-uilib
make
cd ..
cd ./ggc-core
make
cd ..
cd ./ggc-app
make
