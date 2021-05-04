package imputMethodGenerator

object jundaAndTzaiHandling {
  def readJunda(): Unit ={
    val filePath = "src/main/resources/frequencyfiles/Junda2005.txt"
    val hanzilines: List[String] = scala.io.Source.fromFile(filePath).mkString.split("\n").toList



    println("junda:")
    println(hanzilines.length)

  }
}
