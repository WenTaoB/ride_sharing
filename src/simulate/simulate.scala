package simulate


import scala.collection.mutable.ListBuffer
import scala.collection.mutable._

import org.slf4j.LoggerFactory

import loadData.Parser.parserTime
import traits.Task
import loadData.LoadPassengerData

import loadData.Config.status.UNKNOWN
import loadData.Config.status.WAITING
import loadData.Config.status.PICKUP
import loadData.Config.status.MISSING
import loadData.Config.status.FINISH

import model.Passenger

object simulate {
  
  /***
   * 
   */
  val passengerFilePath = "2013-09-01__passengerData.csv"
  var tasks : Map[Int, ListBuffer[Task]] = Map()  //记录了所有的task任务
  
  var passengerList : ListBuffer[Passenger] = ListBuffer()  //记录所有的乘客信息包括状态
  val logger = LoggerFactory.getLogger(this.getClass());
  
  def main(args: Array[String]){
    loadData()
    
    StartSimulate()

  }
  
  
  //程序的模拟过程
  def StartSimulate()={
    //用时间模拟还是用个数
    val StartTime = parserTime("00:00:00")  // 一天的开始时间
    val EndTime = parserTime("23:59:59")   //一天的结束时间

    val TimeListInMap = tasks.keys.toList  //获取所有的时间阶段
    //println(TimeListInMap)
    //很重要，，List  to  BufferList
    val TimeListOrderByKey = TimeListInMap.sorted.to[ListBuffer]  //按照时间戳去排序
    //println(TimeListOrderByKey)   
    
    var currentTime = TimeListOrderByKey.head
    
    while(currentTime <= EndTime + 1 && !TimeListOrderByKey.isEmpty){ //当前的时间戳
      //println(currentTime)
       val currentTasks = tasks.get(currentTime).get.toList
       //遍历当前时间戳的所有的task
       for (task <- currentTasks){
         task.exec(currentTime)
       }
       
       //明天可以来对比一下性能
       tasks.remove(currentTime)  //移除已经经过的时间节点，应该会有提升
       
       TimeListOrderByKey.remove(0)
       if(TimeListOrderByKey.size != 0){
         currentTime = TimeListOrderByKey.head
       }
    }//end-while
  }//end-simulate
  
  
  //获取当前的tasks
  def getTasks():Map[Int, ListBuffer[Task]]={
    return tasks
  }
  
  def getPassengerList() : ListBuffer[Passenger]={
    return passengerList
  }
  

  
  //加载各种数据
  def loadData()={
    loadPassenger(passengerFilePath)
  }
  
  
  
  
  //加载乘客数据
  def loadPassenger(passengerFilePath : String)={
     LoadPassengerData.getPassengerData(passengerFilePath)
    
     println(tasks.size)
     println(passengerList.size)
  }
  
  
}