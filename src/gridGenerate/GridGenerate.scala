package gridGenerate

import java.util.function.DoubleToIntFunction
import scala.collection.mutable.ListBuffer;
import java.io.File
import java.io.PrintWriter

object GridGenerate {
  
    val ANGLE_CHUNK = 0.005  //格子长宽大小
    val MIN_LAT = 29.96      //最小的纬度
    val MAX_LAT = 31.36      //最大的纬度
    val MIN_LON = 113.69     //最小的经度
    val MAX_LON = 115.08     //最大的经度
    // ‪C:\Users\Robert\Desktop\Grid.txt
    //val NUM_OF_LAT_BINS = (math.round(((MAX_LAT - MIN_LAT) / ANGLE_CHUNK))).toInt ;  //纬度方向的格子个数
    //val NUM_OF_LON_BINS = (math.round(((MAX_LON - MIN_LON) / ANGLE_CHUNK))).toInt ;  //经度方向的格子个数
    val NUM_OF_LAT_BINS = ((MAX_LAT - MIN_LAT) / ANGLE_CHUNK).toInt + 1 ;  //纬度方向的格子个数
    val NUM_OF_LON_BINS = ((MAX_LON - MIN_LON) / ANGLE_CHUNK).toInt ;  //经度方向的格子个数
    
    val filePathString : String =  "C:\\Users\\Robert\\Desktop\\Grid.txt"
    var out = new java.io.FileWriter(filePathString, false) 
    //type GridID = Map[Int , (Double,Double)]   // 自定义类型,用来保存一个格子的信息，key是GridID,value是格子中心经纬度数据
    //var GridList : ListBuffer[GridID] = ListBuffer()
    
  //def main(args:Array[String]){
    
    //println(NUM_OF_LAT_BINS)
    //println(NUM_OF_LON_BINS)
    
    //val Lat_LonBin = getLat_LonBin(29.962, 113.692)
    //val Lat_LonBin = getLat_LonBin(30.30, 114.56)
    //val Lat_LonBin = getLat_LonBin(31.359, 115.079)
    //val GridID = generateGridID(31.359,115.079)
    //println(GridID)
    
    //generateGridDataSet()
    
 // }
  
  //生成各自数据集，并写入文件
  def generateGridDataSet() = {
    var LATData = MIN_LAT
    for (LAT_ID <- 1 to NUM_OF_LAT_BINS){
        var LONData = MIN_LON
        for (LON_ID <- 1 to NUM_OF_LON_BINS){
            var currentGridID = getGrid(LAT_ID, LON_ID)      
            var centralData = getCentralLat_LonData(LATData, LONData)
            //println(currentGridID, centralData)  
            val line = currentGridID +" "+ centralData._1 +" " +centralData._2+"\n"
            out.append(line)
            LONData += ANGLE_CHUNK
        }
        LATData += ANGLE_CHUNK
     }
    out.close()
  }
  
  //返回当前格子的中心坐标
  def getCentralLat_LonData(LAT : Double, LON : Double) : (Double,Double) = {
    return (LAT + ANGLE_CHUNK/2 , LON + ANGLE_CHUNK/2)
  }
  
  //若传入当前的经纬度，返回当前经纬度所在格子编号
  def generateGridID(RealLat:Double , RealLon:Double):Int ={
    val Lat_LonBin = getLat_LonBin(RealLat, RealLon)
    return getGrid(Lat_LonBin._1, Lat_LonBin._2)
  }
  
  //若传入当前的经纬度，以元组的形式返回当前点所处的格子的行列值
    def getLat_LonBin(RealLat:Double , RealLon:Double) : (Int,Int)={
      return ((((RealLat - MIN_LAT) / ANGLE_CHUNK +1)).toInt ,
          (((RealLon - MIN_LON) / ANGLE_CHUNK) + 1).toInt)
    }
  
  //若传入格子的编号，以元组的形式返回某一编号格子的行列值
    def decodeLat_LonBin(id : Int) : (Int, Int) = {
        return ((id / NUM_OF_LON_BINS).toInt, (id % NUM_OF_LON_BINS).toInt)
    }

 //传入格子的行列数可以得到该格子的编号
    def getGrid(latBin : Int, lonBin : Int ):Int = {
        return ((latBin-1) * NUM_OF_LON_BINS + lonBin).toInt;
    }
    
}