package head

import dataClasses.{cedictMaps, codeToTextList, codeToTextObject, frequencyMaps, inputSystemCombinedMap, inputSystemTemp}
import imputMethodGenerator.cedictHandling.{createCedictMap, getCedict, getCedictHanziToTranslationMap}
import imputMethodGenerator.inputMethodHandling
import imputMethodGenerator.inputMethodHandling.{createCodeToMultipleTexts, createNestedInputSystemListTupple, createTextToMultipleCodes, frequencyInfoTraditionalFromString, generateInputSystemMap}
import imputMethodGenerator.jundaAndTzaiHandling.{getJundaAndTzaiMaps, getJundaCharToNumMap, getTzaiCharToNumMap}
import serialization.FrequencyFileSerialization.{readCedictMapsFromFile, readJundaAndTzaiMapsFromFile, serializeCedict, serializeJundaAndTzai}
import serialization.InputSystemSerialization.{readInputSystemFromFileWithBinary, readInputSystemFromFileWithJava, serializeInputSystems}
import services.inputMethodService.{getSortedInfoListsFromCodes, printableCodeListResults}
import testPreparation.hashmapTestPrepare.{listOf5000Simplified, listOf5000Traditional}

import scala.collection.mutable.ListBuffer

object Main {

  def main(args: Array[String]): Unit = {
    println("hej lykke")

    //serializeInputSystems()
    val zhengma = readInputSystemFromFileWithJava("zhengmaSerialized.txt")

    val result = getSortedInfoListsFromCodes(List("zz", "ab", "aa", "aavv", "psli", "klg", "boji"), zhengma)

    val printing: String = printableCodeListResults(List("zz","ab", "aa", "aavv", "psli", "klg", "boji"), zhengma)

    println(printing)

    println("farvel lykke ")
  }



  //return a single tupple with the simplifiedTop5000 and traditionalTop5000 missing character count

  def qualityCheckInput(input: codeToTextList): (Integer, Integer) = {
    val systemCharacterStrings: Set[String] = input.content.map(i => i.hanzi).toSet
    val jundacharacters = listOf5000Simplified()//scala.io.Source.fromFile("src/main/resources/frequencyfilesRaw/testJunda.txt").mkString.split("")
    val tzaicharacters = listOf5000Traditional()

    var missingJunda = new ListBuffer[String]()
    for (character: String <- jundacharacters) {
      val existInSet: Boolean = systemCharacterStrings.contains(character)
      if (!existInSet){
        missingJunda.addOne(character)
      }
    }

    var missingTzai = new ListBuffer[String]()
    for (character: String <- tzaicharacters) {
      val existInSet: Boolean = systemCharacterStrings.contains(character)
      if (!existInSet){
        missingTzai.addOne(character)
      }
    }

    return (missingJunda.length, missingTzai.length)
  }
}
