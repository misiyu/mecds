rm -rf /var/www/html/android/data/*
hadoop fs -rm -r /library/*
echo "(tcp)" > ./signal/mydumpFilter
mkdir ./data/tmp
