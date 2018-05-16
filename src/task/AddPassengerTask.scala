package task

import traits.Task
import model.Passenger
import org.slf4j.LoggerFactory
import loadData.Parser.encodeTime
import simulate.simulate
import scala.collection.mutable.ListBuffer
import loadData.Config.status.WAITING

class AddPassengerTask(passenger : Passenger) extends Task {
  
  val logger = LoggerFactory.getLogger(this.getClass());
  
  //def exec(tasks : Map[String, List[Task]], currentTime:String)={
  //   println("在  " + p.StartTime + " 有一个乘车请求,他要从第 " + p.StartGridID + 
  //       " 个格子到第 " + p.EndGridID +" 个格子\n" );
  //}
  
  def exec(currentTime : Int)={
    passenger.status = WAITING  //更改用户状态
    logger.info("在  " + passenger.StartTime + " 有一个乘车请求,他要从第 " + passenger.StartGridID + 
         " 个格子到第 " + passenger.EndGridID +" 个格子,他现在的状态是" + passenger.status );
    val removeTime = currentTime + passenger.waitingTime
    logger.info("该用户将在  " + encodeTime(removeTime) + " 时刻,在格子"+ passenger.StartGridID +"被移除\n")
    
    //如果一定时间后还没来车 ，就在waitingTime时刻加一个remove操作
    
    if(simulate.getTasks().contains(removeTime)){ //存在该ID，可以直接添加
          simulate.getTasks().get(removeTime).get.+=(new RemovePassengerTask(passenger))
    }else{  //当前ID尚不存在，需要添加map
          val newListBuffer : ListBuffer[Task] = ListBuffer()
          simulate.getPassengerList().append(passenger)
          newListBuffer.+=(new RemovePassengerTask(passenger))
          simulate.getTasks() += (removeTime -> newListBuffer)
    } 
  }
  
}