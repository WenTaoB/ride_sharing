package traits


import scala.collection.mutable.Map


trait Task {
  //这是一个接口，每一个Task都会去继承
  //def exec(tasks : Map[String, List[Task]], currentTime:String ){
    
  //}
  
  def exec(currentTime : Int)
}