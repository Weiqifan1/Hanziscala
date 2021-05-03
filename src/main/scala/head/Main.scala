package head

import dataClasses.{codeToTextList, codeToTextObject}
import imputMethodGenerator.inputMethodHandling
import testPreparation.hashmapTestPrepare.{listOf5000Simplified, listOf5000Traditional}

import java.awt.datatransfer.StringSelection
import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

object Main {

  def main(args: Array[String]): Unit = {
    println("hej lykke")

/*
    val zhengma: codeToTextList = inputMethodHandling.createInputMethodObject(
      """\"[a-z]+\"=\".+""".r,
      ",",
      "=",
      true,
      "\"<>",
      "src/main/resources/hanzifiles/zz201906_allcodes.txt")
*/

    val zhengma: codeToTextList = inputMethodHandling.createInputMethodObject(
      """\"[a-z]+\"=\".+""".r,
      ",",
      "=",
      true,
      "\"<>",
      "src/main/resources/hanzifiles/zz201906_test.txt")

    println(zhengma)


    val zhengmaTestResult = qualityCheckInput(zhengma)
    println(zhengma.content.length)
    println(zhengmaTestResult)

    println("farvel lykke ")
  }

  //return a single tupple with the simplifiedTop5000 and traditionalTop5000 missing character count
  def qualityCheckInput(input: codeToTextList): (Integer, Integer) = {
    val systemCharacterStrings: Set[String] = input.content.map(i => i.hanzi).toSet
    val jundacharacters = listOf5000Simplified()//scala.io.Source.fromFile("src/main/resources/frequencyfiles/testJunda.txt").mkString.split("")
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
