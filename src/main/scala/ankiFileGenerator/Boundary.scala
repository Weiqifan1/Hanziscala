package ankiFileGenerator

import ankiFileGenerator.frequencyFileHandling.loadFrequencyFiles.{readCedictMapsFromFile, readJundaAndTzaiMapsFromFile}
import ankiFileGenerator.frequencyFileHandling.prepareTextHandlingMethods.getListOfWordsFromText
import dataClasses.cedictObject
import imputMethodGenerator.inputMethodHandling.{frequencyInfoSimplifiedFromString, frequencyInfoTraditionalFromString}

object Boundary {

  def runAnkiFileGenerator(): Unit = {
    println("hej lykke")

    val cedictMap = readCedictMapsFromFile()
    /*val cedictTrad: Option[List[cedictObject]] = cedictMap.traditionalMap.get("我")
    println(cedictTrad)

    val frequency = readJundaAndTzaiMapsFromFile()
    val traditionalFrequency: List[String] = frequencyInfoTraditionalFromString("癮", frequency)
    val simplifiedFrequency: List[String] = frequencyInfoSimplifiedFromString("癮", frequency)
    println(traditionalFrequency)
    println(simplifiedFrequency)
*/

   val wordList = getListOfWordsFromText("如果123我說  衝,不停的麻煩大了",true, cedictMap)


    println("end")
  }

}
