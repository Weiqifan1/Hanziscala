package ankiHeisigFileGenerator.readHeisigResources

import ankiHeisigFileGenerator.heisigDataClasses.{heisigRawStoriesUserInputItem}

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.matching.Regex

object readHeisigStoriesUserInputRawFile {


  def readHeisigStoriesUserInputRawFile(filePath: String): List[heisigRawStoriesUserInputItem] = {
    val fileContent: String = Source.fromFile(filePath)("UTF-8").mkString
    val splitText: List[heisigRawStoriesUserInputItem] =  parseHeisigUserInputResourceLines(fileContent)
    return splitText
  }

  private def parseHeisigUserInputResourceLines(fileContent: String): List[heisigRawStoriesUserInputItem] = {
    val nestedSplitLines: ListBuffer[ListBuffer[String]] = splitHeisigUserInputStoryLines(fileContent)
    val heisigUseritemList: List[heisigRawStoriesUserInputItem] = nestedSplitLines.map(
      each => createHeisigStpryBufferItem(each)).toList
    return heisigUseritemList
  }

  /*                                    (framenr: Int,
                                         character: String,
                                         keyword: String,
                                         story: String,
                                         TradSimpKanji: String)*/
/*
  def createHeisigStoryBuffer(nestedSplitLines: ListBuffer[ListBuffer[String]]): List[heisigRawStoriesUserInputItem]  = {
    val heisigObjects = nestedSplitLines.map(each => createHeisigStpryBufferItem(each)
      )
    return new ListBuffer[heisigRawStoriesUserInputItem].addAll(heisigObjects)
  }*/


  private def createHeisigStpryBufferItem(each: ListBuffer[String]): heisigRawStoriesUserInputItem = {
    if (each.length < 4) {
      return new heisigRawStoriesUserInputItem(parseHeisigNumbers(each(0)), each(0), each(1), each(2), "")
    }else {
      return new heisigRawStoriesUserInputItem(parseHeisigNumbers(each(0)), each(0), each(1), each(2), each(3))
    }
  }

  //new heisigRawStoriesUserInputItem(parseHeisigNumbers(each(0)), each(0), each(1), each(2), each(4)))

  private def parseHeisigNumbers(heisigNumberField: String): Int ={
    //check if the field can be parsed as an integer:
    var finalInteger: Int = 0;
    try {
      finalInteger = heisigNumberField.toInt
    } catch {
      case e: NumberFormatException => finalInteger = 0;
    }
    return finalInteger;
  }

  private def splitHeisigUserInputStoryLines(fileContent: String): ListBuffer[ListBuffer[String]] = {
    val firstSplit: List[String] = fileContent.split("\r\n").toList
    val trymatcher: Regex = "^[0-9]+,\\D,.*".r
    val mutableNested: ListBuffer[ListBuffer[String]] = createMutableNestedList(firstSplit, trymatcher)
    val mergeLists: List[String] = mutableNested.map(nestedList => {nestedList.mkString("\n")}).toList
    val nested: List[ListBuffer[String]] = mergeLists.map(each => splitToGetNumberAndCharacter(each))
    return new ListBuffer[ListBuffer[String]].addAll(nested)
  }

  private def splitToGetNumberAndCharacter(heisigUserInputStoryString: String): ListBuffer[String] = {
    val split: List[String] = heisigUserInputStoryString.split(",").toList
    val mutable: ListBuffer[String] = new ListBuffer[String]
    mutable.addOne(split(0))
    mutable.addOne(split(1))
    mutable.addOne(split(2))
    mutable.addOne(split.drop(3).mkString(","))
    return mutable
  }


  private def createMutableNestedList(firstSplit: List[String], trymatcher: Regex): ListBuffer[ListBuffer[String]] = {
    var mutableNesteList = new ListBuffer[String]
    var largerMutable = new ListBuffer[ListBuffer[String]]
    for (each: String <- firstSplit) {
      if (trymatcher.matches(each)) {
        largerMutable.addOne(mutableNesteList)
        mutableNesteList = new ListBuffer[String]
        mutableNesteList.addOne(each)
      } else {
        mutableNesteList.addOne(each)
      }
    }
    largerMutable.addOne(mutableNesteList)
    return largerMutable
  }


}
