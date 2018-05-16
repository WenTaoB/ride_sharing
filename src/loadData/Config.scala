package loadData

object Config {
  val WAITING_TIME = 600  //默认等待时间是10分钟
  
  object status extends Enumeration{
    type PassengerStatus = Value//这里仅仅是为了将Enumration.Value的类型暴露出来给外界使用而已
    val UNKNOWN, WAITING, PICKUP, MISSING, FINISH = Value//在这里定义具体的枚举实例
  }
  
  
  
  
}