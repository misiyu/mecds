import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.{SparkConf, SparkContext}

object IPAnalyze {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("IPAnalyze")
    conf.setMaster("spark://192.168.60.1:7077")
    //conf.setMaster("local")
    //conf.setJars(List("/home/misiyu/IdeaProjects/untitled/out/artifacts/untitled_jar/untitled.jar"))
    val sc = new SparkContext(conf)
    //val lines = sc.textFile("file:///var/www/html/android/data/newfile.txt")
    val lines = sc.textFile("/library/newfile.txt")
    val ip = lines.flatMap(line => line.split(" "))
    val pairs = ip.map(word => (word,1))
    val ipCount = pairs.reduceByKey(_+_)
    val suspectIp = ipCount.filter(_._2 > 2)
    //suspectIp.saveAsTextFile("file:///home/misiyu/nosense/output2")
    //suspectIp.saveAsTextFile("file:///var/www/html/android/data/output")
    suspectIp.saveAsTextFile("/library/output")

    val hdfs:FileSystem = FileSystem.get(new Configuration())
    println("start")
    println(hdfs.isFile(new Path("/library")) +" ===================================")
    println(hdfs.exists(new Path("/library"))+"======================================")

    println("hello")
    sc.stop()
  }
}
