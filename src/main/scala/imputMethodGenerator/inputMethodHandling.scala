package imputMethodGenerator

import dataClasses.{cedictMaps, cedictObject, codeToMultipleTextsList, codeToMultipleTextsObject, codeToTextList, codeToTextObject, frequencyMaps, inputSystemCombinedMap, inputSystemHanziInfo, inputSystemTemp, textToMultipleCodesList, textToMultipleCodesObject}
import org.graalvm.compiler.graph.Node.Input

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

object inputMethodHandling {

  /*
  case class inputSystemHanziInfo(hanzi: String,
                                  traditional: Boolean,
                                  simplified: Boolean,
                                  code: String,
                                  pronounciation: String,
                                  translation: String,
                                  traditionalFrequency: List[Int],
                                  simplifiedFrequency: List[Int])*/
/*
  def createInputSystemMap(inputSystem: codeToTextList): Unit ={

    val zhengmaAdvanced: inputSystemTemp = createNestedInputSystemListTupple(inputSystem)

    val codeToList = zhengmaAdvanced.codeToList
    val hanziToList = zhengmaAdvanced.hanziToList


  }
*/

  def createNestedInputSystemListTupple(inputSystem: codeToTextList): inputSystemTemp = {
    val codeFirst: List[(String, codeToTextList)] =
      createNestedSystemListHelper(inputSystem, true)
    val hanziFirst: List[(String, codeToTextList)] =
      createNestedSystemListHelper(inputSystem, false)
    val systemTemp = new inputSystemTemp(codeFirst, hanziFirst)
    return systemTemp
  }

  def createNestedSystemListHelper(inputSystem: codeToTextList, codeFirst: Boolean): List[(String, codeToTextList)] = {
    val codeSorted = if (codeFirst) {inputSystem.content.sortBy(_.code)} else {inputSystem.content.sortBy(_.hanzi)}
    var codeToHanziList: ListBuffer[(String, codeToTextList)] = new ListBuffer[(String, codeToTextList)]
    var tempList: ListBuffer[codeToTextObject] = new ListBuffer[codeToTextObject]
    var previousCode: String = if (codeFirst) {codeSorted(0).code} else {codeSorted(0).hanzi}

    val lastCode: String = if (codeFirst) {codeSorted(codeSorted.length - 1).code} else {codeSorted(codeSorted.length - 1).hanzi}
    var last: ListBuffer[codeToTextObject] = new ListBuffer[codeToTextObject]
    for (eachObj: codeToTextObject <- codeSorted) {
      val currentCode = if (codeFirst) {eachObj.code} else {eachObj.hanzi}
      if (currentCode.equals(lastCode)) {
        val newCodeToTextTuple: (String, codeToTextList) = (previousCode, codeToTextList(tempList.toSet.toList))
        codeToHanziList += newCodeToTextTuple
        last += eachObj
      } else if (currentCode.equals(previousCode)) {
        tempList += eachObj
      } else {
        val newCodeToTextTuple: (String, codeToTextList) = (previousCode, codeToTextList(tempList.toSet.toList))
        codeToHanziList += newCodeToTextTuple
        previousCode = currentCode
        tempList = new ListBuffer[codeToTextObject]
        tempList += eachObj
      }
    }
    val finalCodeToTextTupple: (String, codeToTextList) = (lastCode, codeToTextList(last.toSet.toList))
    codeToHanziList += finalCodeToTextTupple

    val result = codeToHanziList.toList
    return result
  }

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
    //scala.io.Source.fromFile("src/main/resources/hanzifilesRaw/zz201906_test.txt").mkString.split("\n").toList
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


/*
  def createInputMethodMap(inputList: codeToTextList, cedict: cedictMaps, frequencyMaps: frequencyMaps): inputSystemCombinedMap ={
    var mapOfObjects: ListBuffer[inputSystemHanziInfo] = new ListBuffer[inputSystemHanziInfo]()
    for (eachMapping: codeToTextObject <- inputList.content){
      val hanzi = eachMapping.hanzi
      val code = eachMapping.code
      val cedictSimpObjects: List[cedictObject] = cedict.simplifiedMap(hanzi)
      val cedictTradObjects: List[cedictObject] = cedict.traditionalMap(hanzi)
      val simp = if (cedictSimpObjects.nonEmpty) {true} else {false}
      val trad = if (cedictTradObjects.nonEmpty) {true} else {false}

    }
    return null
  }

  //                                hanzi: String,
  //                                traditional: Boolean,
  //                                simplified: Boolean,
  //                                code: String,
  //                                pronounciation: String,
  //                                translation: String,
  //                                traditionalFrequency: List[Int],
  //                                simplifiedFrequency: List[Int]*/
