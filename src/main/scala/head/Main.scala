package head

object Main {

  def computeNumber(): Int = 5+2

  def main(args: Array[String]): Unit = {
    println("hej lykke")
    
    val number = computeNumber()

    val lines: String = scala.io.Source.fromFile("src/main/resources/hanzifiles/zz201906_test.txt").mkString

    val eachline: Array[String] = lines.split("\n")


    println("farvel lykke " + eachline.length)
  }

}
