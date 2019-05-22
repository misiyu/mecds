/home/misiyu/program/hadoop/bin/hadoop fs -get /library/outputcap ./data/
cat ./data/outputcap/part* > ./data/result
rm -r ./data/outputcap
