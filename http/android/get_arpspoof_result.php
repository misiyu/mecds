<?php
exec("./get_HDFS_res.sh");
$filein = fopen("./data/result","r") or die("Unable to open file");
echo fread($filein , filesize("./data/result"));
fclose($filein);

?>
