package imputMethodGenerator

import scala.collection.mutable.ListBuffer

object jundaAndTzaiHandling {
  def getJundaCharToNumMap(): Map[String, String] ={
    val filePath = "src/main/resources/frequencyfiles/Junda2005.txt"
    val hanzilines: List[String] = scala.io.Source.fromFile(filePath).mkString.split("\n").toList

    //foerste er en integer og anden er et tegn
    val splitlines = hanzilines map {line => line.split("\\s+").slice(0,2)}
    val jundaHashmap = splitlines.map(i => i(1) -> i(0)).toMap
    return jundaHashmap
  }

  def getTzaiCharToNumMap(): Map[String, String] ={
    val filePath = "src/main/resources/frequencyfiles/Tzai2006.txt"
    val hanzilines: List[String] = scala.io.Source.fromFile(filePath).mkString.split("\n").toList

    //foerste er tegn andet er totale antal forekomster
    var i = 0
    val splitlines = hanzilines map {line => line.split("\\s+").slice(0,2)}

    var updatedSplitLines = ListBuffer[List[String]]()
    for (eachLine <- splitlines){
      i += 1
      updatedSplitLines += List(eachLine(0), i.toString)
    }
    val finalList = updatedSplitLines.toList
    val jundaHashmap = finalList.map(i => i(0).toString -> i(1).toString).toMap

    return jundaHashmap
  }
}
