object Main {
  def main(args: Array[String]): Unit = {
    val at = new AnalyzeThread
    at.start()
    val checkIsStopThead = new CheckIsStopThead(at)
    checkIsStopThead.start()
  }

}
