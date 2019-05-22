<?php
$latitude = (double)$_POST["latitude"];
$longitude = (double)$_POST["longitude"];
//print_r($latitude."\n ");
//print_r($longitude."\n ");
$filein = fopen("./mecServerTable.txt","r") or die("Unable to open file");
$result = "";
$min_distance = 10000000.000001 ;
$min_ip = "1.1.1.1";
while(!feof($filein))
{
	$line = fgets($filein);
	//echo $line."\n ";
	$strs = explode(" ",$line);
	$latitude_tmp = (double)$strs[0] ;
	$longitude_tmp = (double)$strs[1] ;
	$dis = ($latitude-$latitude_tmp)*($latitude-$latitude_tmp)+($longitude-$longitude_tmp)*($longitude-$longitude_tmp) ; 
	//echo $dis."\n " ;
	if($dis <= 0.000002){
		if($result == "") $result = $result.substr($strs[2],0,strlen($strs[2])-1);
		else $result = $result.":".substr($strs[2],0,strlen($strs[2])-1);
	}
	if($dis < $min_distance){
		$min_distance = $dis ;
		$min_ip = $strs[2];
	}
}
if($result == "") $result = $min_ip;
fclose($filein);
//print_r($min_ip." ");
print_r($result);
?>
