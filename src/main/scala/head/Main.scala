package head

import dataClasses.{codeToTextList, codeToTextObject}
import imputMethodGenerator.inputMethodHandling
import testPreparation.hashmapTestPrepare.{listOf5000Simplified, listOf5000Traditional}

import scala.util.matching.Regex

object Main {

  def main(args: Array[String]): Unit = {
    println("hej lykke")


    val zhengma: codeToTextList = inputMethodHandling.createInputMethodObject(
      """\"[a-z]+\"=\".+""".r,
      ",",
      "=",
      true,
      "\"<>",
      "src/main/resources/hanzifiles/zz201906_test.txt")



    println("farvel lykke ")
  }

}
