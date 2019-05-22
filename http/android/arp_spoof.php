<?php
$arp_target=$_POST["arp_target"];
$spoof_ip=$_POST["spoof_ip"];

exec("bash ./arp_spoof.sh \"$arp_target\" \"$spoof_ip\"");
echo "ok";
?>
