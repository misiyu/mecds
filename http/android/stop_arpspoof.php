<?php
$kill_ip=$_GET["killIp"];
echo $kill_ip;
exec("echo \"&&(not host $kill_ip)\" >> ./signal/mydumpFilter") ;
exec("./kill_arpspoof.sh $kill_ip");
?>
