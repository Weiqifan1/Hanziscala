package head

import dataClasses.{cedictMaps, codeToTextList, codeToTextObject, frequencyMaps, inputSystemCombinedMap, inputSystemHanziInfo, inputSystemHanziInfoList, inputSystemTemp}
import serialization.InputSystemSerialization.{readInputSystemFromFileWithBinary, readInputSystemFromFileWithJava, serializeInputSystems}
import services.inputMethodService.{generateCodeListFromInput, getSortedInfoListsFromCodes, printableCodeListResults, runConsoleProgram}
import testPreparation.hashmapTestPrepare.{listOf5000Simplified, listOf5000Traditional}

//json
import scala.collection.mutable._
import net.liftweb.json._
import net.liftweb.json.Serialization.write

import scala.collection.mutable.ListBuffer
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http
import com.twitter.util.{Await, Future}

object Main {

  def main(args: Array[String]): Unit = {
    println("hej lykke")

    //serializeCedictAndFrequencyFiles()
    //serializeInputSystems()
    val zhengma = readInputSystemFromFileWithJava("zhengmaSerialized.txt")
    //val printing: String = printableCodeListResults(List("zz","ab", "aa", "aavv", "psli", "klg", "boji"), zhengma)
    //println(printing)

    //*********** run console program
    runConsoleProgram(zhengma)

    //************* run api data:
    //val result = apiMethod("sd", zhengma)


    println("farvel lykke ")
  }

  def apiMethod(input: String, inputSystem: inputSystemCombinedMap): String ={
    val smallLetters: String = input.toLowerCase()
    val codeList: List[String] = generateCodeListFromInput(smallLetters)
    val hanziInfoList: List[inputSystemHanziInfo] = getSortedInfoListsFromCodes(codeList, inputSystem)
    val outpuObject: inputSystemHanziInfoList = inputSystemHanziInfoList(hanziInfoList)
    // create a JSON string from the Person, then print it
    implicit val formats = DefaultFormats
    val jsonString = write(outpuObject)
    return jsonString
  }


  //*********************** serialize data to json
  //tasks>
  //1: get a function

  //***********************


  //return a single tupple with the simplifiedTop5000 and traditionalTop5000 missing character count

  def qualityCheckInput(input: codeToTextList): (Integer, Integer) = {
    val systemCharacterStrings: Predef.Set[String] = input.content.map(i => i.hanzi).toSet
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
