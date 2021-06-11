package head

import dataClasses.{cedictMaps, codeToTextList, codeToTextObject, frequencyMaps, inputSystemCombinedMap, inputSystemHanziInfo, inputSystemHanziInfoList, inputSystemTemp}
import inpuSystemLookup.Boundary.runInputSystemLookup
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
    runInputSystemLookup()
  }

}

