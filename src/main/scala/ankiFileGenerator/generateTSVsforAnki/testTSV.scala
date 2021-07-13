package ankiFileGenerator.generateTSVsforAnki

import ankiFileGenerator.flashcardDataClasses.{cedictFreqObject, flashcardLineObject, rawLineObject, storyObject}
import java.io.{File, PrintWriter}
import scala.collection.mutable.ListBuffer
import scala.sys.process.buildersToProcess

object testTSV {

  val NEWLINE: String = "<br>"//<br>
  val COLUMNBREAK: String = ";"

  def writeTSVfileTEST(storyToWrite: storyObject, title: String): Unit = {
    val writer = new PrintWriter(new File("outputFiles/" + title + ".txt"))
    val textToWriteToFile: String = generateTSVText(storyToWrite)
    writer.write(textToWriteToFile)
    writer.close()
  }

  def findNumberOfDelims(eachRow: String, delimitor: Char): Int = {
    val charactersInRow: List[Char] = eachRow.toList
    var counter: Int = 0
    for (eachChar <- charactersInRow) {
      if (eachChar.equals(delimitor)) {
        counter += 1
      }
    }
    return counter
  }

  def findNumberOfColumns(listOfCardRows: List[String], delimitor: Char): List[Int] = {
    val counter: List[Int] = listOfCardRows.map(i => findNumberOfDelims(i, delimitor))
    return counter
  }

  def compareColumnsWithTargetNumber(listOfColumns: List[Int], target: Int): Unit = {
    println("target columns: " + target)
    var columnsThatFailToMeetTarget: Int = 0
    for (eachNumber <- listOfColumns) {
      if (!eachNumber.equals(target)){
        columnsThatFailToMeetTarget += 1
      }
    }
    println("columns that dont match target: " + columnsThatFailToMeetTarget)
  }

  private def generateTSVText(storyToWrite: storyObject): String = {
    val flashcardLineObjectsList: List[flashcardLineObject] = storyToWrite.flashcardlineObjects
    val listOfCardRows: List[String] = generateCardRow(flashcardLineObjectsList)
    val listOfColumns: List[Int] = findNumberOfColumns(listOfCardRows, ';')
    compareColumnsWithTargetNumber(listOfColumns, 4)
    val finalText: String = listOfCardRows.mkString("\n")
    return finalText
  }


  def recursiveGenerateIntersperced(tempList: List[String],
                                    firstLines: List[String],
                                    secondLines: List[String],
                                    sizeOfEachChunk: Int): List[String] = {
    if (firstLines.length <= sizeOfEachChunk && secondLines.length <= sizeOfEachChunk){
      val lastMergedList = tempList ++ firstLines ++ secondLines
      return lastMergedList
    }else {
      val splitFirst = firstLines.splitAt(sizeOfEachChunk)
      val splitSecond = secondLines.splitAt(sizeOfEachChunk)
      val newTempList = tempList ++ splitFirst._1 ++ splitSecond._1
      val newRecursion = recursiveGenerateIntersperced(newTempList, splitFirst._2, splitSecond._2, sizeOfEachChunk)
      return newRecursion
    }
  }


  def intersperceHanziAndPinyinLines(firstLines: List[String],
                                     secondLines: List[String], sizeOfEachChunk: Int): List[String] = {
    var result = new ListBuffer[String]
    val tempList: List[String] = List()

    if (firstLines.length == secondLines.length){
      val intersperceNestedLists: List[String] = recursiveGenerateIntersperced(tempList, firstLines, secondLines, sizeOfEachChunk)
      result.addAll(intersperceNestedLists)
    }else {
      val intersperceNestedLists: List[String] = firstLines ++ secondLines
      result.addAll(intersperceNestedLists)
    }
    return result.toList
  }

  private def generateCardRow(flashcardLineObjectsList: List[flashcardLineObject]): List[String] = {
    val listOfRowsHanziToInfo: List[String] = flashcardLineObjectsList.map(i => generateEachCardHanziToInfo(i))
    val listOfRowsPinyinToInfo: List[String] = flashcardLineObjectsList.map(i => generateEachCardPinyinToInfo(i))
    //val resultLines: List[String] = List.concat(listOfRowsHanziToInfo, listOfRowsPinyinToInfo)
    //create a function that will "intersperce hanzi lines and pinyin lines"
    val resultLines: List[String] = intersperceHanziAndPinyinLines(listOfRowsHanziToInfo, listOfRowsPinyinToInfo, 5)
    return resultLines
  }

  /*generateHanziTextFiled(previousLineObj: rawLineObject,
                                       lineObj: rawLineObject,
                                       nextLineObject: rawLineObject,
                                       newCedictEntries: List[cedictFreqObject]): String*/
  private def generateEachCardHanziToInfo(eachCard: flashcardLineObject): String = {
    //val hanziTextField: String = generateHanziTextFiled(eachCard.previousLineObj, eachCard.lineObj, eachCard.nextLineObject, eachCard.newCedictEntries)
    //  .replace('\r', ' ')
    //  .replace(';', ':')
    //val pinyinTextField: String = generatePinyinTextField(eachCard.previousLineObj, eachCard.lineObj, eachCard.nextLineObject, eachCard.newCedictEntries)

    val hanziInitialInf: String = generateHanziInitialInfo(
      eachCard.previousLineObj, eachCard.lineObj, eachCard.nextLineObject, eachCard.newCedictEntries)

    val hanziCurrentLineInf: String = generateHanziCurrentLineInfo(
      eachCard.previousLineObj, eachCard.lineObj, eachCard.nextLineObject, eachCard.newCedictEntries)

    val hanziSubsequentInf: String = generateHanziSubsequentInfo(
      eachCard.previousLineObj, eachCard.lineObj, eachCard.nextLineObject, eachCard.newCedictEntries)

    val mainLineOnTheBack: String = eachCard.lineObj.originallLine

    val totalInfoField: String = generateTotalInfoField(eachCard.previousLineObj, eachCard.lineObj, eachCard.nextLineObject, eachCard.newCedictEntries)
      .replace('\r', ' ')
      .replace(';', ':')

    val resultString: String =
      "\"" + hanziInitialInf + "\"" + COLUMNBREAK +
      "\"" + hanziCurrentLineInf + "\"" + COLUMNBREAK +
      "\"" + hanziSubsequentInf + "\"" + COLUMNBREAK +
      "\"" + mainLineOnTheBack + "\"" + COLUMNBREAK +
      "\"" + totalInfoField + "\""

    //return "\"" + hanziTextField + "\"" + COLUMNBREAK + "\"" + totalInfoField + "\""
    return resultString
  }
/*
  private def generateEachCardPinyinToInfo(eachCard: flashcardLineObject): String = {
    //val hanziTextField: String = generateHanziTextFiled(eachCard.previousLineObj, eachCard.lineObj, eachCard.nextLineObject, eachCard.newCedictEntries)
    val pinyinTextField: String = generatePinyinTextField(eachCard.previousLineObj, eachCard.lineObj, eachCard.nextLineObject, eachCard.newCedictEntries)
      .replace('\r', ' ')
      .replace(';', ':')
    val totalInfoField: String = generateTotalInfoField(eachCard.previousLineObj, eachCard.lineObj, eachCard.nextLineObject, eachCard.newCedictEntries)
      .replace('\r', ' ')
      .replace(';', ':')
    return "\"" + pinyinTextField + "\"" + COLUMNBREAK + "\"" + totalInfoField + "\""
  }*/

  private def generateEachCardPinyinToInfo(eachCard: flashcardLineObject): String = {
    val pinyinInitialInf: String = generatePinyinInitialInfo(
      eachCard.previousLineObj, eachCard.lineObj, eachCard.nextLineObject, eachCard.newCedictEntries)

    val piniynCurrentLineInf: String = generatePinyinCurrentLineInfo(
      eachCard.previousLineObj, eachCard.lineObj, eachCard.nextLineObject, eachCard.newCedictEntries)

    val pinyinSubsequentInf: String = generatePinyinSubsequentInfo(
      eachCard.previousLineObj, eachCard.lineObj, eachCard.nextLineObject, eachCard.newCedictEntries)

    val mainLineOnTheBack: String = eachCard.lineObj.originallLine

    val totalInfoField: String = generateTotalInfoField(eachCard.previousLineObj, eachCard.lineObj, eachCard.nextLineObject, eachCard.newCedictEntries)
      .replace('\r', ' ')
      .replace(';', ':')

    val resultString: String =
      "\"" + pinyinInitialInf + "\"" + COLUMNBREAK +
        "\"" + piniynCurrentLineInf + "\"" + COLUMNBREAK +
        "\"" + pinyinSubsequentInf + "\"" + COLUMNBREAK +
        "\"" + mainLineOnTheBack + "\"" + COLUMNBREAK +
        "\"" + totalInfoField + "\""

    //return "\"" + hanziTextField + "\"" + COLUMNBREAK + "\"" + totalInfoField + "\""
    return resultString
  }


  //***********************************************************************************************
  //                    hanzi field
  //***********************************************************************************************
/*
  private def generateHanziTextFiled(previousLineObj: rawLineObject,
                                     lineObj: rawLineObject,
                                     nextLineObject: rawLineObject,
                                     newCedictEntries: List[cedictFreqObject]): String = {
    val newEntriesOnlyHanzi: String = generateNewEntriesOnlyHanzi(newCedictEntries)
    val currentLineOnlyHanzi: String = generateSubsectionWithOnlyHanzi(lineObj)
    val previousLineOnlyHanzi: String = generateSubsectionWithOnlyHanzi(previousLineObj)
    val nextLineOnlyHanzi: String = generateSubsectionWithOnlyHanzi(nextLineObject)
    val lineInfo: String = lineObj.lineInfo
    val storyInfo: String = lineObj.storyInfo1of2 + NEWLINE + lineObj.storyInfo2of2
    return "new entries:" + NEWLINE + newEntriesOnlyHanzi + NEWLINE+NEWLINE +
      "current line:" + NEWLINE + currentLineOnlyHanzi + NEWLINE+NEWLINE +
      "previous line:" + NEWLINE + previousLineOnlyHanzi + NEWLINE+NEWLINE +
      "next line:" + NEWLINE + nextLineOnlyHanzi + NEWLINE+NEWLINE +
      "lineInfo: " + NEWLINE + lineInfo + NEWLINE +
      "story info:" + NEWLINE + storyInfo
  }*/

  def generateHanziInitialInfo(previousLineObj: rawLineObject,
                               lineObj: rawLineObject,
                               nextLineObject: rawLineObject,
                               newCedictEntries: List[cedictFreqObject]): String = {
    val newEntriesOnlyHanzi: String = generateNewEntriesOnlyHanzi(newCedictEntries)
    return "new entries:" + NEWLINE + newEntriesOnlyHanzi + NEWLINE+NEWLINE
  }

  def generateHanziCurrentLineInfo(previousLineObj: rawLineObject,
                                   lineObj: rawLineObject,
                                   nextLineObject: rawLineObject,
                                   newCedictEntries: List[cedictFreqObject]): String = {
    val currentLineOnlyHanzi: String = lineObj.originallLine//generateSubsectionWithOnlyHanzi(lineObj)
    //return "current line:" + NEWLINE + currentLineOnlyHanzi + NEWLINE+NEWLINE
    return currentLineOnlyHanzi + NEWLINE+NEWLINE
  }

  def generateHanziSubsequentInfo(previousLineObj: rawLineObject,
                                  lineObj: rawLineObject,
                                  nextLineObject: rawLineObject,
                                  newCedictEntries: List[cedictFreqObject]): String = {
    val previousLineOnlyHanzi: String = generateSubsectionWithOnlyHanzi(previousLineObj)
    val nextLineOnlyHanzi: String = generateSubsectionWithOnlyHanzi(nextLineObject)
    val lineInfo: String = lineObj.lineInfo
    val storyInfo: String = lineObj.storyInfo1of2 + NEWLINE + lineObj.storyInfo2of2
    return "previous line:" + NEWLINE + previousLineOnlyHanzi + NEWLINE+NEWLINE +
      "next line:" + NEWLINE + nextLineOnlyHanzi + NEWLINE+NEWLINE +
      "lineInfo: " + NEWLINE + lineInfo + NEWLINE +
      "story info:" + NEWLINE + storyInfo
  }

  private def generateNewEntriesOnlyHanzi(someLineObj: List[cedictFreqObject]): String = {
    val tradHanzi: String = someLineObj.map(i => i.traditionalHanzi.mkString("_")).mkString(" * ")
    val simpHanzi: String = someLineObj.map(i => i.simplifiedHanzi.mkString("_")).mkString(" * ")
    return "traditional: " + tradHanzi + NEWLINE +
      "simplified: " + simpHanzi
  }

  private def generateSubsectionWithOnlyHanzi(lineObj: rawLineObject): String = {
    val originalLine: String = lineObj.originallLine
    val traditional: String = lineObj.cedictEntries.map(i => i.traditionalHanzi(0)).mkString(" * ")
    val simplified: String = lineObj.cedictEntries.map(i => i.simplifiedHanzi(0)).mkString(" * ")
    val finalText: String = "original: " + originalLine + NEWLINE +
      "traditional: " + traditional + NEWLINE +
      "simplified: " + simplified
    return finalText
  }


  //***********************************************************************************************
  //                    pinyin field
  //***********************************************************************************************
  private def generatePinyinTextField(previousLineObj: rawLineObject,
                                      lineObj: rawLineObject,
                                      nextLineObject: rawLineObject,
                                      newCedictEntries: List[cedictFreqObject]): String = {
    val newEntriesOnlyPinyin: String = generateNewEntriesOnlyPinyin(newCedictEntries)
    val currentLineOnlyPinyin: String = generateSubsectionWithOnlyPinyin(lineObj)
    val previousLineOnlyPinyin: String = generateSubsectionWithOnlyPinyin(previousLineObj)
    val nextLineOnlyPinyin: String = generateSubsectionWithOnlyPinyin(nextLineObject)
    val lineInfo: String = lineObj.lineInfo
    val storyInfo: String = lineObj.storyInfo1of2 + NEWLINE + lineObj.storyInfo2of2
    return "new entries:" + NEWLINE + newEntriesOnlyPinyin + NEWLINE+NEWLINE +
      "current line:" + NEWLINE + currentLineOnlyPinyin + NEWLINE+NEWLINE +
      "previous line:" + NEWLINE + previousLineOnlyPinyin + NEWLINE+NEWLINE +
      "next line:" + NEWLINE + nextLineOnlyPinyin + NEWLINE+NEWLINE +
      "lineInfo: " + NEWLINE + lineInfo + NEWLINE +
      "story info:" + NEWLINE + storyInfo
  }

  def generatePinyinInitialInfo(previousLineObj: rawLineObject,
                                lineObj: rawLineObject,
                                nextLineObject: rawLineObject,
                                newCedictEntries: List[cedictFreqObject]): String = {
    val newEntriesOnlyPinyin: String = generateNewEntriesOnlyPinyin(newCedictEntries)
    return "new entries:" + NEWLINE + newEntriesOnlyPinyin + NEWLINE+NEWLINE
  }

  def generatePinyinCurrentLineInfo(previousLineObj: rawLineObject,
                                    lineObj: rawLineObject,
                                    nextLineObject: rawLineObject,
                                    newCedictEntries: List[cedictFreqObject]): String = {
    val currentLineOnlyPinyin: String = generateSubsectionWithOnlyPinyin(lineObj)
    return currentLineOnlyPinyin + NEWLINE+NEWLINE
  }

  def generatePinyinSubsequentInfo(previousLineObj: rawLineObject,
                                   lineObj: rawLineObject,
                                   nextLineObject: rawLineObject,
                                   newCedictEntries: List[cedictFreqObject]): String = {
    val previousLineOnlyPinyin: String = generateSubsectionWithOnlyPinyin(previousLineObj)
    val nextLineOnlyPinyin: String = generateSubsectionWithOnlyPinyin(nextLineObject)
    val lineInfo: String = lineObj.lineInfo
    val storyInfo: String = lineObj.storyInfo1of2 + NEWLINE + lineObj.storyInfo2of2
    return "previous line:" + NEWLINE + previousLineOnlyPinyin + NEWLINE+NEWLINE +
      "next line:" + NEWLINE + nextLineOnlyPinyin + NEWLINE+NEWLINE +
      "lineInfo: " + NEWLINE + lineInfo + NEWLINE +
      "story info:" + NEWLINE + storyInfo
  }


  private def generateNewEntriesOnlyPinyin(newCedictEntries: List[cedictFreqObject]): String = {
    val eachEntry: List[String] = newCedictEntries.map(i => i.pinyin.mkString("_"))
    val joinedEntries: String = eachEntry.mkString(" * ")
    return joinedEntries
  }

  private def generateSubsectionWithOnlyPinyin(someLineObj: rawLineObject): String = {
    val pinyin: String = createPinyinLineFromEntries(someLineObj.cedictEntries)
    return pinyin
  }


  //***********************************************************************************************
  //                    total info fields
  //***********************************************************************************************

  private def generateTotalInfoField(previousLineObj: rawLineObject,
                                     lineObj: rawLineObject,
                                     nextLineObject: rawLineObject,
                                     newCedictEntries: List[cedictFreqObject]): String = {
    val generateNewCedictEntriesField: String = generateNewEntriesWithAllInfo(newCedictEntries)
    val generateCurrentLineField: String = generateSubsectionWithAllInfo(lineObj)
    val generatePreviousLineField: String = generateSubsectionWithAllInfo(previousLineObj)
    val generateNextLineField: String = generateSubsectionWithAllInfo(nextLineObject)
    val lineInfo: String = lineObj.lineInfo
    val storyInfo: String = lineObj.storyInfo1of2 + NEWLINE + lineObj.storyInfo2of2
    return "new entries:" + NEWLINE + generateNewCedictEntriesField + NEWLINE+NEWLINE +
      "current line:" + NEWLINE + generateCurrentLineField + NEWLINE+NEWLINE +
      "previous line:" + NEWLINE + generatePreviousLineField + NEWLINE+NEWLINE +
      "next line:" + NEWLINE + generateNextLineField + NEWLINE+NEWLINE +
      "lineInfo: " + NEWLINE + lineInfo + NEWLINE +
      "story info:" + NEWLINE + storyInfo
  }

  private def generateNewEntriesWithAllInfo(newCedictEntries: List[cedictFreqObject]): String = {
    val eachEntry: List[String] = newCedictEntries.map(i => singleEntryFullInfo(i))
    val joinedEntries: String = eachEntry.mkString(NEWLINE)
    return joinedEntries
  }

  private def generateSubsectionWithAllInfo(someLineObj: rawLineObject): String = {
    val originalLine: String = someLineObj.originallLine
    val pinyin: String = createPinyinLineFromEntries(someLineObj.cedictEntries)
    val translationLines: String = createEntryFullInfoLines(someLineObj.cedictEntries)
    return originalLine + NEWLINE + pinyin + NEWLINE + translationLines
  }

  private def createPinyinLineFromEntries(cedictEntries: List[cedictFreqObject]): String = {
    val allPintins: String = cedictEntries.map(i => i.pinyin.mkString("_")).mkString(" * ")
    return allPintins
  }

  private def createEntryFullInfoLines(cedictEntries: List[cedictFreqObject]): String = {
    val entries: List[String] = cedictEntries.map(i => singleEntryFullInfo(i))
    val entriesAddedTogether: String = entries.mkString(NEWLINE)
    return entriesAddedTogether
  }

  private def singleEntryFullInfo(singleEntry: cedictFreqObject): String = {
    val chineseWordTraditional: String = singleEntry.traditionalHanzi.mkString("*")
    val chineseWordSimplified: String = singleEntry.simplifiedHanzi.mkString("*")
    val pinyin: String = singleEntry.pinyin.mkString("_")
    val traditionaFrequency: String = singleEntry.traditionalFrequency.mkString(" ")
    val simplifiedFrequency: String = singleEntry.simplifiedFrequency.mkString(" ")
    val translations: String = singleEntry.translation.mkString(" ")
    val result: String =
      chineseWordTraditional + " " +
        traditionaFrequency + " " +
        chineseWordSimplified + " " +
        simplifiedFrequency + " " +
        pinyin + " " +
        translations
    return result
  }
}