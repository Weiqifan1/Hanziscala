package services

import dataClasses.{inputSystemCombinedMap, inputSystemHanziInfo, inputSystemHanziInfoList}
import serialization.InputSystemSerialization.readInputSystemFromFileWithJava

import scala.collection.mutable.ListBuffer
import scala.math.Ordering.Implicits.seqOrdering

object inputMethodService {

  def loadData(): (inputSystemCombinedMap) = {
    val zhengma = readInputSystemFromFileWithJava("zhengmaSerialized.txt")
    return (zhengma)
  }

  def getInfoListFromCode(code: String, inputSystem: inputSystemCombinedMap): List[inputSystemHanziInfo] ={
    val lookup: Option[inputSystemHanziInfoList] = inputSystem.codeToInfo.content.get(code)
    if (lookup.isEmpty) return List() else return lookup.get.content
  }

  def getSortedInfoListsFromCodes(codes: List[String], inputSystem: inputSystemCombinedMap): List[inputSystemHanziInfo] = {
    val infoList: List[inputSystemHanziInfo] = codes.map(i => getInfoListFromCode(i, inputSystem)).flatten
    val removeDublicates = removeInfoWithSameCharacter(infoList)
    val sorted = removeDublicates.sortBy(_.traditionalFrequency.sorted)

    return sorted
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
