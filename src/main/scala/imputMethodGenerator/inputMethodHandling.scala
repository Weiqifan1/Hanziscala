package imputMethodGenerator

import dataClasses.{codeToMultipleTextsList, codeToMultipleTextsObject, codeToTextList, codeToTextObject, textToMultipleCodesList, textToMultipleCodesObject}
import org.graalvm.compiler.graph.Node.Input

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

object inputMethodHandling {
  def createTextToMultipleCodes(input: codeToTextList): textToMultipleCodesList = {
    val listOfObject: List[codeToTextObject] = input.content
    val finalObjectBuffer = new ListBuffer[textToMultipleCodesObject]()

    for (eachObj: codeToTextObject <- listOfObject){
      val chineseText = eachObj.hanzi
      val codes = new ListBuffer[String]()
      codes += eachObj.code
      for (subObj: codeToTextObject <- listOfObject){
        if (subObj.hanzi.equals(chineseText)){
          codes += subObj.code
        }
      }
      val actualList = codes.toSet.toList.sorted
      val newObj = textToMultipleCodesObject(chineseText, actualList)
      finalObjectBuffer += newObj
    }

    val finalContent = finalObjectBuffer.toSet.toList
    val returnobject = textToMultipleCodesList(finalContent)
    return returnobject
  }

  def createCodeToMultipleTexts(input: codeToTextList): codeToMultipleTextsList = {
    val listOfObject: List[codeToTextObject] = input.content
    val finalObjectBuffer = new ListBuffer[codeToMultipleTextsObject]()

    for (eachObj: codeToTextObject <- listOfObject){
      val currentcode = eachObj.code
      val chineseTexts = new ListBuffer[String]()
      chineseTexts += eachObj.hanzi
      for (subObj: codeToTextObject <- listOfObject){
        if(subObj.code.equals(currentcode)){
          chineseTexts += subObj.hanzi
        }
      }
      val actualList = chineseTexts.toSet.toList.sorted
      val newObj = codeToMultipleTextsObject(currentcode, actualList)
      finalObjectBuffer += newObj
    }

    val finalContent = finalObjectBuffer.toSet.toList
    val returnObject = codeToMultipleTextsList(finalContent)
    return returnObject
  }

  def createInputMethodObject(lineRegex: Regex,
                              splitLine: String,
                              splitCodeAndText: String,
                              codeFirst: Boolean,
                              removeChars: String,
                              filepath: String): codeToTextList ={
    //val regex: Regex = """\"[a-z]+\"=\".+""".r//zhengma.lineMatchRegexList()
    val hanzilines: List[String] = scala.io.Source.fromFile(filepath).mkString.split("\n").toList
    //scala.io.Source.fromFile("src/main/resources/hanzifiles/zz201906_test.txt").mkString.split("\n").toList
    val charsToRemoveSet = removeChars.toSet//"\"<>".toSet

    var myobjects: List[codeToTextObject] = List()
    for (line: String <- hanzilines){
      val matching = lineRegex findFirstIn line
      if (!None.equals(matching)){
        val listOf1ormany: List[String] = line.split(splitLine).toList//eachMatch.source.toString.split("=").toList
        for (part <- listOf1ormany){
          val subListOf2 = part.split(splitCodeAndText).toList
          if (codeFirst){
            val firstStr: String = subListOf2(0).filterNot(charsToRemoveSet)
            val secondStr: String = subListOf2(1).filterNot(charsToRemoveSet)
            val codeToText = codeToTextObject(firstStr, secondStr)
            myobjects ::= codeToText
          }else {
            val firstStr: String = subListOf2(1).filterNot(charsToRemoveSet)
            val secondStr: String = subListOf2(0).filterNot(charsToRemoveSet)
            val codeToText = codeToTextObject(firstStr, secondStr)
            myobjects ::= codeToText
          }
        }
      }
    }
    val inputSystem = codeToTextList(myobjects)
    return inputSystem
  }


}
