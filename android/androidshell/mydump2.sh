/root/tcpdump -i wlan0 -s 54 -c 100 -etnv $1 > /storage/emulated/0/mecds/tmp/$2
mv /storage/emulated/0/mecds/tmp/$2 /storage/emulated/0/mecds
