import java.io.File

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

class AnalyzeThread extends Thread with Serializable {

  //本地与群集转换，改3处

  @transient
  val conf = new SparkConf().setAppName("dIPAnalyze")
  conf.setMaster("spark://192.168.60.1:7077")
  //conf.setMaster("local")
  @transient
  val sc = new SparkContext(conf)
  // SparkContext不可序列化，但其它变量需要序列化才能传入rdd算子。变量前加上@transient可忽略变量

  @transient
  val hdfs:FileSystem = FileSystem.get(new Configuration())

  var ipRdd:RDD[(String,Int)] = null
  var portRdd:RDD[(String,Int)] = null
  var ipPortRdd:RDD[(String,Int)] = null
  var first = true

  private val synCode = "[S]"//"0x00000002"
  private val ackCode = "[R]"//"0x00000010"
  private val synAckCode ="[S.]" //"0x00000012"
  private val srcipIndex = 1
  private val ttlIndex = 0
  private val dstPortIndex = 10
  private val tcpflagIndex = 11
  /*private val suspectThresholdRed = 50
  private val suspectThresholdYellow = 30*/
  private val suspectThresholdGreen = 10
  private val upThreshold = 70

  private val localInputFileName = "/home/misiyu/nosense/tcpdump/cap"
  private val localoOutputDirName = "/home/misiyu/nosense/suspectIpOut/suspectIp"

  private var suspectIp:RDD[(String,Int)] = null

  private var inputContent:RDD[String] = null

  private var capSeq = 0

  private var isStop = false


  override def run(): Unit = {
      while(!isStop){
        input
        //localInput
        analyze
        output
        //localOutput
      }

  }

  private def localInput ={
    var inputFile = new File(localInputFileName)
    while (!inputFile.exists()){
      println(localInputFileName + " no exists")
      Thread.sleep(500)
      inputFile = new File(localInputFileName)
    }
    inputContent = sc.textFile("/home/misiyu/nosense/tcpdump/cap")
  }
  private def localOutput = {
    var outputFile = new File(localoOutputDirName)
    while (outputFile.exists()){
      Thread.sleep(500)
      println("===============wait for consumer")
      outputFile = new File(localoOutputDirName)
    }

    suspectIp.saveAsTextFile("file://"+localoOutputDirName)
  }


  private def input = {
    val filepath = "/library/cap"+this.capSeq
    while(!hdfs.exists(new Path(filepath)) && !this.isStop){
      println(filepath+" no exists")
      Thread.sleep(500)
    }
    inputContent = sc.textFile(filepath)

  }


  private def output = {
    val outPath = "/library/outputcap"//+this.capSeq
    while(hdfs.exists(new Path(outPath))){
      Thread.sleep(500)
      println("===============wait for consumer")
      hdfs.delete(new Path(outPath),true)
    }
    suspectIp.saveAsTextFile(outPath)
    hdfs.delete(new Path("/library/cap"+this.capSeq) , true)   // true表示递归删除
    this.capSeq = (this.capSeq +1)
  }


  private def analyze={
    //



    val synAckLines = inputContent.filter(line => (line.contains(synCode)||line.contains(ackCode)))
    //synAckLines.foreach(s => println(s))
    val temp = synAckLines.map(line => line.replace(".",","))
    //temp.foreach(ele => println(ele))
    /*temp.foreach(ele => {
      for(e <- ele) print(e+" ")
      println("")
    }  )*/
    val temp1 = temp.map(ele => ele.split(","))
    val temp2 = temp1.map(markSynAck)
    val iptemp = temp2.map(ele => (ele._1,ele._4))
    val porttemp = temp2.map(ele => (ele._2+":"+ele._3,ele._4))
    if(first){
      first = false
      ipRdd = iptemp
      portRdd = porttemp;
    }else{
      ipRdd = ipRdd.union(iptemp)
      portRdd = portRdd.union(porttemp);
    }


    ipRdd = ipRdd.reduceByKey(_+_)
    portRdd = portRdd.reduceByKey(_+_)

    ipRdd = ipRdd.filter(_._2 > 0)
    portRdd = portRdd.filter(_._2 > 0)
    val suspectTemp = ipRdd.union(portRdd);
    println("======================================")
    //suspectTemp.foreach(ele => println(ele._1+"\t"+ele._2))
    println("======================================")
    val suspectIpTmp = suspectTemp.filter(ele => (ele._2 >= suspectThresholdGreen ))
    suspectIp = suspectIpTmp.map(ele => (ele._1 , ele._2)).sortBy(_._2,false)
  }

  private def markSynAck(list:Array[String]):(String,String,String,Int)={
    val ip = list(srcipIndex)+"."+list(srcipIndex+1)+"."+list(srcipIndex+2)+"."+list(srcipIndex+3)
    val ttl = list(ttlIndex)
    val dstPort = list(dstPortIndex)
    var value = 1
    if(list(tcpflagIndex) == ackCode) value = -1
    return (ip,dstPort,ttl,value)
  }

  def setIsStop(b:Boolean) = {
    this.isStop = b
  }
}
