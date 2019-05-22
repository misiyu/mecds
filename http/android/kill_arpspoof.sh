pid=$(ps -ef | grep arpspoof | grep -v sudo | sed 's/  */,/g' | cut -d , -f 2 | head -n 1)
echo $pid > ./data/return1
echo 5133 | sudo -S kill -s 9 "$pid"
echo $? > ./data/return0
./stop_mydump.sh
