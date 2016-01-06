#!/bin/bash

A=`ls CLO*`;

cat header.CLO.tsv > $1;

for i in $A; do 
    ./pullInfo.pl $i >> $1;
done
