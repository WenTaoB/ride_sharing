package passengerGenerate

import org.apache.spark._
import org.apache.spark.sql._

import gridGenerate.GridGenerate.generateGridID
import org.apache.spark.sql.types.IntegerType

import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.{StructType,StructField,StringType,DoubleType};
import java.io.File

object passengerGenerate {
   //首先初始化一个SparkSession对象
    val spark = org.apache.spark.sql.SparkSession.builder
                .master("local")
                .appName("Spark CSV Reader")
                .config("spark.sql.warehouse.dir", "file:///")
                .getOrCreate;
    
     val dictpath = "F:/software/Eclipse/Scala_workSpace/HelloWorld/ExportOD/"
     val outputPath = "F:/software/Eclipse/Scala_workSpace/HelloWorld/generatePassager/"
     
   def main(args: Array[String]){
   

     
     val fileList = Array( "2013-09-01_OD.csv", "2013-09-02_OD.csv",
                           "2013-09-03_OD.csv", "2013-09-04_OD.csv",
                           "2013-09-05_OD.csv", "2013-09-06_OD.csv",
                           "2013-09-07_OD.csv", "2013-09-08_OD.csv",
                           "2013-09-09_OD.csv", "2013-09-10_OD.csv",
                           "2013-09-11_OD.csv", "2013-09-12_OD.csv",
                           "2013-09-13_OD.csv", "2013-09-14_OD.csv",
                           "2013-09-15_OD.csv", "2013-09-16_OD.csv",
                           "2013-09-17_OD.csv", "2013-09-18_OD.csv",
                           "2013-09-19_OD.csv", "2013-09-20_OD.csv",
                           "2013-09-21_OD.csv", "2013-09-22_OD.csv",
                           "2013-09-23_OD.csv", "2013-09-24_OD.csv",
                           "2013-09-25_OD.csv", "2013-09-26_OD.csv",
                           "2013-09-27_OD.csv", "2013-09-28_OD.csv",
                           "2013-09-29_OD.csv", "2013-09-30_OD.csv",
                           "2013-10-01_OD.csv", "2013-10-02_OD.csv",
                           "2013-10-03_OD.csv", "2013-10-04_OD.csv",
                           "2013-10-05_OD.csv", "2013-10-06_OD.csv",
                           "2013-10-07_OD.csv", "2013-10-08_OD.csv",
                           "2013-10-09_OD.csv", "2013-10-10_OD.csv",
                           "2013-10-11_OD.csv", "2013-10-12_OD.csv",
                           "2013-10-13_OD.csv", "2013-10-14_OD.csv",
                           "2013-10-15_OD.csv", "2013-10-16_OD.csv",
                           "2013-10-17_OD.csv", "2013-10-18_OD.csv",
                           "2013-10-19_OD.csv", "2013-10-20_OD.csv",
                           "2013-10-21_OD.csv", "2013-10-22_OD.csv",
                           "2013-10-23_OD.csv", "2013-10-24_OD.csv",
                           "2013-10-25_OD.csv", "2013-10-26_OD.csv",
                           "2013-10-27_OD.csv", "2013-10-28_OD.csv",
                           "2013-10-29_OD.csv", "2013-10-30_OD.csv",
                           "2013-10-31_OD.csv")
     
     fileList.map { line => ProcessPassengerData(line) }
     
     
    
  } 

  def ProcessPassengerData(filename : String)={ 
     //然后使用SparkSessions对象加载CSV成为DataFrame
     val df = spark.read
               .format("com.databricks.spark.csv")
               .option("header", "true") //reading the headers
               .option("mode", "DROPMALFORMED")
               .load(dictpath+filename); //.csv("csv/file/path") //spark 2.0 api


     val originalRDD = df.rdd
     
     //---------------------起始格子ID-------------------//
     val StartLAT_LON = df.select("StartX", "StartY").rdd
     
     //StartLAT_LON.foreach(println)
     
     val StartGridID = StartLAT_LON.map { line => generateGridID((line.get(1)).
         toString().toDouble, (line.get(0)).toString().toDouble)}
     
     //---------------------终止格子ID-------------------//
     val EndLAT_LON = df.select("EndX", "EndY").rdd
     //EndLAT_LON.foreach(println)
     val EndGridID = EndLAT_LON.map { line => generateGridID((line.get(1)).
         toString().toDouble, (line.get(0)).toString().toDouble)}

     val GridIDRDD = StartGridID.zip(EndGridID)
     
     val unionRDD = originalRDD.zip(GridIDRDD)

     val ResultRDD = unionRDD.map { line => (line._1.get(0), line._1.get(2) , 
         line._1.get(3), line._1.get(4), line._1.get(5), line._1.get(6), line._1.get(7),
         line._1.get(8), line._1.get(9), line._1.get(10), line._1.get(11), line._1.get(12),
         line._1.get(13), line._2._1, line._2._2)}
    
     //ResultRDD.take(20).foreach(println) 
     
     def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) { 
       val p = new java.io.PrintWriter(f);
       p.write("ID,StartDate,StartTime,EndDate,EndTime,StartX,StartY,EndX,EndY,"+
        "Duration,Distance,AvgSpeed,MoveDistance,StartGridID,EndGridID\n") 
       try { op(p) } 
       finally { p.close() } 
       
     } 

    val avgs = ResultRDD.map( line => (line._1,line._2,line._3,line._4,line._5,
        line._6,line._7,line._8,line._9,line._10,line._11,line._12,line._13,
        line._14,line._15).toString().replaceAll("\\(","").replaceAll("\\)","")) .collect() 
    printToFile(new File(outputPath+filename.substring(0, 11) + "_passengerData" + ".csv")) 
    { p => avgs.foreach(p.println) }
    
  }
}