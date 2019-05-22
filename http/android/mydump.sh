if [ -f ./signal/mydumpRun ]
then
	exit 0
fi
rm ./signal/stopSignal
touch ./signal/mydumpRun
basic_filename=cap
seq=0
while [ ! -f "./signal/stopSignal" ]
do
	filename="$basic_filename"_tmp_"$seq"
	echo $filename
	filter=$(cat ./signal/mydumpFilter)
	echo 5133 | sudo -S /usr/sbin/tcpdump -i wlp2s0 -s 54 -c 100 -tnv "$filter" > ./data/$filename
	final_filename="$basic_filename""$seq"
	sed '$!N;s/\n\|>\|:/,/g;s/ \|ttl\|Flags//g' ./data/$filename | cut -d , -f 2,8,9,10 > ./data/"$final_filename" 
	rm ./data/"$filename"
	bash ./dspark.sh "$final_filename"&
	let "seq++"
done
#/usr/sbin/tcpdump -i wlp2s0 -s 54 -c 100 -tnv $1 > ./data/$filename
