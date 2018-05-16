package task

import traits.Task
import model.Passenger
import org.slf4j.LoggerFactory
import loadData.Config.status.MISSING
import loadData.Parser.encodeTime
import simulate.simulate

class RemovePassengerTask (passenger : Passenger) extends Task{
  val logger = LoggerFactory.getLogger(this.getClass());
    
  def exec(currentTime : Int){
    passenger.status = MISSING  //更改用户状态
    logger.info("在"+ encodeTime(currentTime) +", 有乘客在"+ passenger.StartGridID + 
                " 格子被系统移除,该乘客的状态为"+ passenger.status+ "\n");
    //在全局的乘客列表中移除该用户
    simulate.getPassengerList().remove(simulate.getPassengerList().indexOf(passenger))
    
  }
  
}