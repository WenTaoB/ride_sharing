package loadData

import java.text.SimpleDateFormat
import java.util.Date

object Parser {
  
  //解析时间
  def parserTime(time : String):Int = {
    var hour = time.substring(0, 2).toInt
    var minute = time.substring(3, 5).toInt
    var second = time.substring(6,time.length()).toInt
    
    var binaryTimeSeq = hour*3600 + minute * 60 + second
    
    return binaryTimeSeq
  }
  
  //编码时间
  def encodeTime(time : Int):String = {
    var hour = time/3600%24
    var minute = time%3600/60
    var second = time%3600%60
    return hour + ":" + minute + ":" + second
  }
  
}