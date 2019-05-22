hadoop fs -rm /signal/stopAnalyze
spark-submit --master spark://192.168.60.1:7077 --deploy-mode client --class Main ipAnalyze.jar
