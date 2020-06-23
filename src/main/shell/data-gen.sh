#!/usr/bin/env bash

# Script to create 1GB with same words repeating 100000 times
# This script runs in the current directory and the output will be in the current directory

function recursiveCat() {
 local file=$1
 local counter=$2

 # 10894 * 10 to power 6 = 1.0894 gigabytes
 if [[ ${counter} == 6 ]]; then
  mv ${file} ${ONE_GB_FILE}
  rm -rf .tmp
  echo `ls -lh ${ONE_GB_FILE}`
  echo "Finished"
  exit 0
 fi

 local out=${filename_prefix}_${counter}.txt
  for i in `seq 10`; do
   cat ${file} >> ${out}
  done

  [[ -e ${file} ]] && { rm ${file}; }
  counter=$((counter+1))
  recursiveCat ${out} ${counter}
}
## main
[[ -e .tmp ]] && { rm ${file}; }
mkdir .tmp
filename_prefix=".tmp/output"
ONE_GB_FILE=1gb-data.txt

cp data.txt .tmp/

#start
recursiveCat .tmp/data.txt 1
