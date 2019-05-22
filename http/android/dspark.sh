#filename=$1
#if [ ! -f ./data/$filename ]
#then
	#echo "./data/$filename no exist"
	#exit 1
#fi
#/home/misiyu/program/hadoop/bin/hadoop fs -put ./data/$filename /library/

if [ -f ./data/hasoneDspark ]
then
	exit 0
fi
if [ ! -d ./data/tmp ]
then
	mkdir ./data/tmp
fi
touch ./data/hasoneDspark
mv ./data/cap* ./data/tmp/
/home/misiyu/program/hadoop/bin/hadoop fs -put ./data/tmp/cap* /library/
rm ./data/tmp/*
rm ./data/hasoneDspark
