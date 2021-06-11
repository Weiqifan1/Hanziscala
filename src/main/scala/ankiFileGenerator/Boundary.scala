package ankiFileGenerator

import ankiFileGenerator.frequencyFileHandling.loadFrequencyFiles.{readCedictMapsFromFile, readJundaAndTzaiMapsFromFile}
import dataClasses.cedictObject
import imputMethodGenerator.inputMethodHandling.{frequencyInfoSimplifiedFromString, frequencyInfoTraditionalFromString}

object Boundary {

  def runAnkiFileGenerator(): Unit = {
    println("hej lykke")

    val cedictMap = readCedictMapsFromFile()
    val cedictTrad: Option[List[cedictObject]] = cedictMap.traditionalMap.get("我")
    println(cedictTrad)

    val frequency = readJundaAndTzaiMapsFromFile()
    val traditionalFrequency: List[String] = frequencyInfoTraditionalFromString("我", frequency)
    val simplifiedFrequency: List[String] = frequencyInfoSimplifiedFromString("我", frequency)
    println(traditionalFrequency)
    println(simplifiedFrequency)


    println("end")
  }

}
