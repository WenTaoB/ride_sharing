package loadData

import java.io.File
import java.io.FileWriter
import java.io.BufferedWriter
import java.io.PrintWriter

import traits.Task
import scala.collection.mutable._
import task.AddPassengerTask
import model.Passenger
import loadData.Parser.parserTime
import simulate.simulate


object LoadPassengerData {
  //首先初始化一个SparkSession对象
  val spark = org.apache.spark.sql.SparkSession.builder
                .master("local")
                .appName("Spark CSV Reader")
                .config("spark.sql.warehouse.dir", "file:///")
                .getOrCreate;
  
  val dictpath = "F:/software/Eclipse/Scala_workSpace/Taxi_Try/"
  val filename = "2013-09-01__passengerData.csv"
  val writer = new PrintWriter(new File(dictpath + filename.substring(0, 10) + ".log"))

  
  
  //def main(args: Array[String]){
    //然后使用SparkSessions对象加载CSV成为DataFrame
    
    /***
     * val df = spark.read
             .format("com.databricks.spark.csv")
             .option("header", "true") //reading the headers
             .option("mode", "DROPMALFORMED")
             .load(filename); //.csv("csv/file/path") //spark 2.0 api
    //得到数据
    val passengerRDD = df.select("StartTime", "StartGridID", "EndGridID").rdd
    
    //模拟打车
    writer.write("今天是"+filename.substring(0, 10)+ "\n开始模拟\n")
    passengerRDD.foreach{ row => writer.append ("在  " + row.get(0) + 
                      " 有一个乘车请求,他要从第 " + row.get(1) + " 个格子到第 " + row.get(2) +" 个格子\n" )}
    writer.close()
     */
    

  //}
  
  def getPassengerData(filename : String) ={
    //然后使用SparkSessions对象加载CSV成为DataFrame
    val df = spark.read
             .format("com.databricks.spark.csv")
             .option("header", "true") //reading the headers
             .option("mode", "DROPMALFORMED")
             .load(filename); //.csv("csv/file/path") //spark 2.0 api
    //得到数据
    val passengerRDD = df.select("StartTime", "StartGridID", "EndGridID").rdd
    
    //对数据进行处理，添加到Tasks列表
    passengerRDD.foreach { row => InitPassengerMap(row.get(0).toString(),
                        row.get(1).toString().toInt, row.get(2).toString().toInt) }
    
  }
  
  
  /**
   * 每次来先判断一下 ，key是否存在，如果存在直接append List ，如果不存在，则new List<Task>
   */
  def InitPassengerMap(StartTime : String, 
            StartGridID : Int, EndGridID : Int)={
      if(simulate.getTasks().contains(parserTime(StartTime))){ //存在该ID，可以直接添加
          val passenger  = new Passenger(StartTime, StartGridID, EndGridID)
          simulate.getPassengerList().append(passenger)
          simulate.getTasks().get(parserTime(StartTime)).get.+=(new AddPassengerTask(passenger))
          //println(tasks.size)
      }else{  //当前ID尚不存在，需要添加map
          val newListBuffer : ListBuffer[Task] = ListBuffer()
          val passenger  = new Passenger(StartTime, StartGridID, EndGridID)
          simulate.getPassengerList().append(passenger)
          newListBuffer.+=(new AddPassengerTask(passenger))
          simulate.getTasks() += (parserTime(StartTime) -> newListBuffer)
          //println(tasks.size)
      } 
  }

}