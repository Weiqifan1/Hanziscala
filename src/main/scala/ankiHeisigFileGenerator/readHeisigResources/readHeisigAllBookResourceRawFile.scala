package ankiHeisigFileGenerator.readHeisigResources

import ankiHeisigFileGenerator.heisigDataClasses.{heisigRawResourceCollection, heisigRawResourceItem, heisigResource}

import scala.collection.mutable.ListBuffer
import scala.io.Source

object readHeisigAllBookResourceRawFile {

  /*def createStoryObjectFromFile(filePath: String,
                                cedictEntriesToIgnore: List[String],
                                traditional: Boolean,
                                cedictMapTradAndSimp: cedictMaps,
                                freqMapsTzaiAndJunda: frequencyMaps): storyObject = {*/

  //val finalStory: storyObject = createStoryObjectFromFile("src/inputSystemFilesRaw/麻辣女孩01_01b.txt", List(), true, cedictMap, jundaAndTzai)
  //val fileContent: String = Source.fromFile(filePath).mkString

  def readHeisigResourceRawFile(filePath: String): heisigRawResourceCollection = {
    val fileContent: String = Source.fromFile(filePath)("UTF-8").mkString
    val splitText: heisigRawResourceCollection =  parseHeisigResourceLines(fileContent)
    return splitText
  }

  private def parseHeisigResourceLines(heisigResourceContentRaw: String): heisigRawResourceCollection = {
    val splitText = heisigResourceContentRaw.split("\n")

    val secondline: List[heisigRawResourceItem] = splitText.map(item => parseHeisigRawLine(item)).toList //parseHeisigRawLine(splitText(1))

    val heisigresource: heisigRawResourceCollection = heisigRawResourceCollection(secondline)

    return heisigresource
  }

  private def parseHeisigRawLine(heisigResourceRawLine: String): heisigRawResourceItem = {
    val original: List[String] = heisigResourceRawLine.split(",").toList
    var line = new ListBuffer[String]()
    line.addAll(original)

    while (line.length < 20) {
      line.addOne("")
    }
    val item: heisigRawResourceItem = new heisigRawResourceItem(
      parseHeisigNumbers(line(0)), parseHeisigNumbers(line(1)), parseHeisigNumbers(line(2)), line(3), line(4), line(5), line(6), line(7), line(8), line(9),
      line(10), line(11), line(12), line(13), line(14), line(15), line(16), line(17), line(18), line(19))
    return item
  }

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

}
