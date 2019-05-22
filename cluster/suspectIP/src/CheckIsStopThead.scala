import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

class CheckIsStopThead(analyzeThread: AnalyzeThread) extends Thread{
  @transient
  val hdfs:FileSystem = FileSystem.get(new Configuration())

  override def run(): Unit = {
      val filepath = "/signal/stopAnalyze"
      while(!hdfs.exists(new Path(filepath))){
        //println(filepath+" no exists")
        Thread.sleep(500)
      }
      analyzeThread.setIsStop(true)
  }
}
