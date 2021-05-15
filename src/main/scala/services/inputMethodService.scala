package services

import dataClasses.{cedictObject, inputSystemCombinedMap, inputSystemHanziInfo, inputSystemHanziInfoList}
import serialization.InputSystemSerialization.readInputSystemFromFileWithJava

import scala.collection.mutable.ListBuffer
import scala.math.Ordering.Implicits.seqOrdering

object inputMethodService {

  def printableCodeListResults(codes: List[String], inputSystem: inputSystemCombinedMap): String ={
    val hanziInfoList: List[inputSystemHanziInfo] = getSortedInfoListsFromCodes(codes, inputSystem)
    var output = ""
    for (eachInfo: inputSystemHanziInfo <- hanziInfoList) {
      val eachPrint: String = getStringFromHanziInfo(eachInfo)
      output += eachPrint + "\n"
    }
    return output
  }

  def printableTextResults(hanzi: String, inputSystem: inputSystemCombinedMap): String = {
      val hanziInfoList: List[inputSystemHanziInfo] = getSortedInfoListsFromText(hanzi, inputSystem)
      var output = ""
      for (eachInfo <- hanziInfoList) {
        val eachPrint = getStringFromHanziInfo(eachInfo)
        output += eachPrint + "\n"
      }
      return output
  }

  def getSortedInfoListsFromText(hanzi: String, inputSystem: inputSystemCombinedMap): List[inputSystemHanziInfo] ={
    val lookup: Option[inputSystemHanziInfoList] = inputSystem.hanziToInfo.content.get(hanzi)
    if (lookup.isEmpty){
      return List()
    }else {
      val sorted = lookup.get.content.sorted
      return sorted
    }
  }

  def getSortedInfoListsFromCodes(codes: List[String], inputSystem: inputSystemCombinedMap): List[inputSystemHanziInfo] = {
    val infoList: List[inputSystemHanziInfo] = codes.map(i => getInfoListFromCode(i, inputSystem)).flatten
    val removeDublicates = removeInfoWithSameCharacter(infoList)
    val sorted = removeDublicates.sorted
    return sorted
  }

  def getStringFromHanziInfo(hanziInfo: inputSystemHanziInfo): String = {
    var output = ""
    val primaryHanzi: String = hanziInfo.hanzi.toString
    val codes: String = getCodes(hanziInfo)
    val frequency = "tradFreq:" + hanziInfo.traditionalFrequency.toString() +
                    " simpFreq:" + hanziInfo.simplifiedFrequency.toString()
    val cedictLines: String = getCedictLines(hanziInfo)
    val unicode: String = primaryHanzi.codePoints.toArray.toList.toString()

    output = primaryHanzi  +
             " " + codes +
             " " + frequency +
             " unicode:" + unicode +
             "\n\t" + cedictLines
    return output.trim
  }


  def getCodes(hanziInfo: inputSystemHanziInfo): String ={
    val codeList = hanziInfo.codes
    var outputString = ""
    for (eachCode <- codeList) {
      outputString += eachCode + " "
    }
    outputString = "[" + outputString.trim + "]"
    return outputString
  }

  def getCedictLines(hanziInfo: inputSystemHanziInfo): String ={
    val traditionalCedict: List[cedictObject] = if (!hanziInfo.cedictTrad.isEmpty) hanziInfo.cedictTrad.get else List()
    val simplifiedCedict: List[cedictObject] = if (!hanziInfo.cedictSimp.isEmpty) hanziInfo.cedictSimp.get else List()

    var textOutput: String = "\n\t" + "Traditional:" + "\n"
    for (cedictElem <- traditionalCedict) {
      textOutput += "\t" + cedictElem.traditionalHanzi +
                        " " + cedictElem.simplifiedHanzi +
                        " " + cedictElem.pinyin +
                        " " + cedictElem.translation + "\n"
    }
    textOutput += "\t" + "Simplified:" + "\n"
    for (cedictElem <- simplifiedCedict) {
      textOutput += "\t" + cedictElem.traditionalHanzi +
        " " + cedictElem.simplifiedHanzi +
        " " + cedictElem.pinyin +
        " " + cedictElem.translation + "\n"
    }
    return textOutput.trim
  }

  def loadData(): (inputSystemCombinedMap) = {
    val zhengma = readInputSystemFromFileWithJava("zhengmaSerialized.txt")
    return (zhengma)
  }

  def getInfoListFromCode(code: String, inputSystem: inputSystemCombinedMap): List[inputSystemHanziInfo] ={
    val lookup: Option[inputSystemHanziInfoList] = inputSystem.codeToInfo.content.get(code)
    if (lookup.isEmpty) return List() else return lookup.get.content
  }

  def removeInfoWithSameCharacter(nestdeInfo: List[inputSystemHanziInfo]): List[inputSystemHanziInfo] = {
    var unique: ListBuffer[inputSystemHanziInfo] = ListBuffer()
    var listOfTexts: ListBuffer[String] = ListBuffer()
    for (info: inputSystemHanziInfo <- nestdeInfo) {
      val chineseText = info.hanzi
      if (!listOfTexts.contains(chineseText)) {
        unique.addOne(info)
      }
      listOfTexts.addOne(chineseText)
    }
    return unique.toList
  }



}
