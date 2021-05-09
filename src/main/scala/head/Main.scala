package head

import dataClasses.{codeToTextList, codeToTextObject, inputSystemTemp}
import imputMethodGenerator.cedictHandling.getCedictHanziToTranslationMap
import imputMethodGenerator.inputMethodHandling
import imputMethodGenerator.inputMethodHandling.{createCodeToMultipleTexts, createNestedInputSystemListTupple, createTextToMultipleCodes}
import imputMethodGenerator.jundaAndTzaiHandling.{getJundaAndTzaiMaps, getJundaCharToNumMap, getTzaiCharToNumMap}
import testPreparation.hashmapTestPrepare.{listOf5000Simplified, listOf5000Traditional}

import scala.collection.mutable.ListBuffer

object Main {

  def main(args: Array[String]): Unit = {
    println("hej lykke")
    //val cedict = getCedictHanziToTranslationMap()
    //val frequencyMapsObj = getJundaAndTzaiMaps()

    val zhengma: codeToTextList = inputMethodHandling.createInputMethodObject(
      """\"[a-z]+\"=\".+""".r,
      ",",
      "=",
      true,
      "\"<>",
      "src/main/resources/hanzifilesRaw/zz201906_allcodes.txt")

    val zhengmaAdvanced: inputSystemTemp = createNestedInputSystemListTupple(zhengma)



    println("end")

    /*
    val zhengma: codeToTextList = inputMethodHandling.createInputMethodObject(
      """\"[a-z]+\"=\".+""".r,
      ",",
      "=",
      true,
      "\"<>",
      "src/main/resources/hanzifilesRaw/zz201906_test.txt")
    val zhengmaTestResult = qualityCheckInput(zhengma)
    val textToMultiCodes = createTextToMultipleCodes(zhengma)
    val codeToMultiText = createCodeToMultipleTexts(zhengma)
*/

    //create code to handle jundaAndTzai based characters hierakies.
    /*
    val frequencyMaps = getJundaAndTzaiMaps()
    println(frequencyMaps.simplified.get("十"))
    println(frequencyMaps.traditional.get("十"))
*/
    /*
    //create code to handle cedict translations (a primitive translation is good enough)
    val cedict = getCedictHanziToTranslationMap()
    println(cedict.traditionalMap.get("十"))
    println(cedict.simplifiedMap.get("十"))
*/

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
