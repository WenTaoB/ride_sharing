package model

import loadData.Config.status.UNKNOWN

class Passenger (StartTime_p : String, StartGridID_p : Int, EndGridID_p : Int) {
  val StartTime = StartTime_p
  val StartGridID = StartGridID_p
  val EndGridID = EndGridID_p
  //以一种什么样的形式去组织状态呢？，枚举还是五位二进制数
  var status = UNKNOWN
  val waitingTime = loadData.Config.WAITING_TIME
  
}