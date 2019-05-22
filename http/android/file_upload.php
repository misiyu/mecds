<?php

$content = file_get_contents('php://input');
$post_filename = $_POST["filename"];
$filename="./data/$post_filename";
//while(file_exists($filename)) {
	//usleep(500000) ;  # 1000000 = 1s
//}
$myfile = fopen($filename,"w")or die("Unable to open file");
fwrite($myfile , $content);
fclose($myfile);

exec("./dspark.sh $post_filename");
//unlink($filename);

//$filein = fopen("./data/result","r") or die("Unable to open file");
//echo fread($filein , filesize("./data/result"));
//fclose($filein);
print_r("server receive your file "+$filename);

?>
