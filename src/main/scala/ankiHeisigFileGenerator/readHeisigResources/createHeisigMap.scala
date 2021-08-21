package ankiHeisigFileGenerator.readHeisigResources

import ankiHeisigFileGenerator.heisigDataClasses.{char, heisigRawResourceCollection, heisigRawResourceItem, heisigRawStoriesUserInputItem, heisigResource}
import com.ibm.watson.text_to_speech.v1.model.Word.PartOfSpeech

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object createHeisigMap {
  def generateHeisigMap(hesigResource: heisigRawResourceCollection): Unit ={
    println("this is a map")

  }

  def createCharObject(item: heisigRawResourceItem, story:List[List[String]], frameNumber: Int, frameNumberString: String,
                       character: String, keyword: String, partOfSpeech: String,
                       firstReading: String, secondReading: String, lesson: String): char = {

    var finalStory: String = ""
    var finalStoryKeyword: String = ""
    var stories: ListBuffer[ListBuffer[String]] = new ListBuffer[ListBuffer[String]];
    for (eachStoryList <- story) {
      val storyframeNumber = eachStoryList(0)
      val storycharacter = "\"" + eachStoryList(1) + "\""
      val storykeyword = eachStoryList(2)
      val storyStory = eachStoryList(3)
      if (character.equals(storycharacter)) {
        finalStory = storyStory
        finalStoryKeyword = storykeyword
      }
    }

    val finalChar: char = new char(
      finalStory, finalStoryKeyword, frameNumber, frameNumberString, character, partOfSpeech, keyword,
      firstReading, secondReading, lesson, item.checkKanjiAndHanzi, item.checkSimplifiedAndTraditional, item.compareKeywordsKanjiAndTraditional,
      item.compareKeywordsSimplifiedAndTraditional, item.visualCheckKanjiAndHanzi)
    return finalChar
  }

  def createTraditionalCharFromHeisigRawItem(item: heisigRawResourceItem,
                                             story: List[List[String]]): char = {
    val frameNumber: Int = item.traditionalRTHNumber
    val frameNumberString: String = frameNumber.toString
    val character: String = item.traditionalChar
    val keyword: String = item.traditionalKeyword
    val partOfSpeech: String = item.hanziPartOfSpeech
    val firstReading: String = item.hanziFirstReading
    val secondReading: String = item.hanziSecondReading
    val lesson: String = item.traditionalLesson

    val chrObj: char = createCharObject(item, story, frameNumber, frameNumberString,
      character, keyword, partOfSpeech, firstReading, secondReading, lesson)
    return chrObj
  }

  def createSimplifiedCharFromHeisigRawItem(item: heisigRawResourceItem, story: List[List[String]]): char = {
    var finalStory: String = ""
    val frameNumber: Int = item.simplifiedRSHNumber
    val frameNumberString: String = frameNumber.toString
    val character: String = item.simplifiedChar
    val keyword: String = item.simplifiedKeyword
    val partOfSpeech: String = item.hanziPartOfSpeech
    val firstReading: String = item.hanziFirstReading
    val secondReading: String = item.hanziSecondReading
    val lesson: String = item.simplifiedLesson

    val chrObj: char = createCharObject(item, story, frameNumber, frameNumberString,
      character, keyword, partOfSpeech, firstReading, secondReading, lesson)
    return chrObj
  }

  def createKanjiFromHeisigRawItem(item: heisigRawResourceItem, story: List[List[String]]): char = {
    var finalStory: String = ""
    val frameNumber: Int = item.kanjiRTKNumber
    val frameNumberString: String = frameNumber.toString
    val character: String = item.kanjiChar
    val keyword: String = item.kanjiKeyword
    val partOfSpeech: String = item.hanziPartOfSpeech
    val firstReading: String = item.hanziFirstReading
    val secondReading: String = item.hanziSecondReading
    val lesson: String = item.kanjiLesson

    val chrObj: char = createCharObject(item, story, frameNumber, frameNumberString,
      character, keyword, partOfSpeech, firstReading, secondReading, lesson)
    return chrObj
  }

  def createHeisigResource(item: heisigRawResourceItem, story: List[List[String]]): heisigResource = {
    val traditional: char = createTraditionalCharFromHeisigRawItem(item, story)
    val simplified: char = createSimplifiedCharFromHeisigRawItem(item, story)
    val kanji: char = createKanjiFromHeisigRawItem(item, story)
    val finalResource: heisigResource = new heisigResource(traditional, simplified, kanji)
    return finalResource
  }

  def checkIfAllStoriesAreUsed(heisigResource: List[heisigResource], story: List[List[String]]): Boolean = {
    val storyCount = story.length
    //test that all 2255 (minus the first one) are present in the traditional heisig objects
    var count: ListBuffer[String] = new ListBuffer[String]
    for (eachresource <- heisigResource) {
      val trad = eachresource.trad
      val story = trad.story
      if (story != null && story.length != null && story.length > 0) {
        count += story
      }
    }

    val storySet: Set[String] = count.toSet

    var missingStories: ListBuffer[List[String]] = new ListBuffer[List[String]]

    for (eachStory <- story) {
      if (!storySet.contains(eachStory(3))) {
        missingStories += eachStory
      }
    }

    val missingStoriesBool: Boolean = missingStories.length.equals(1)
    val countStorySetBoolean: Boolean = storySet.toList.length.equals(story.length-1)

    return missingStoriesBool && countStorySetBoolean

  }

  def createHeisigMap(heisig: heisigRawResourceCollection,
                      story: List[List[String]]):
         List[heisigResource] = {
    //create a list of heisigResources and check if I get All the stories in the traditional objects
    val heisigResourceObj: List[heisigResource] = heisig.heisigItems.map(item => createHeisigResource(item, story))
    val allStoriesAreUsed: Boolean = checkIfAllStoriesAreUsed(heisigResourceObj, story)

    //create a map from character to heisigResource objects.
    //I need a map for each three.
    //val tradCharMap: Map[String, heisigResource]
    ////////////var tradPreMap: ListBuffer[]

    val tradMap = createNestedHeisigResourceListToPrepareForMap(heisigResourceObj, "trad")
    val simpMap = createNestedHeisigResourceListToPrepareForMap(heisigResourceObj, "simp")
    val kanjiMap = createNestedHeisigResourceListToPrepareForMap(heisigResourceObj, "kanji")

    val doubleMapOfTrad = tradMap.toList.filter(each => each._2.length > 1)

    return null;
  }

  def createNestedHeisigResourceListToPrepareForMap(heisigResourceObj: List[heisigResource],
                                                    tradsimpkanji: String):
                    Map[String, List[heisigResource]] ={
    var mutableResult = scala.collection.mutable.Map[String, List[heisigResource]]()

    val testIfAllCharsOfTypeAreUnique: ListBuffer[String] = new ListBuffer[String]
    if (tradsimpkanji.equals("trad")){
      testIfAllCharsOfTypeAreUnique.addAll(heisigResourceObj.map(each => each.trad.char))
    }else if (tradsimpkanji.equals("simp")){
      testIfAllCharsOfTypeAreUnique.addAll(heisigResourceObj.map(each => each.simp.char))
    }else if (tradsimpkanji.equals("kanji")) {
      testIfAllCharsOfTypeAreUnique.addAll(heisigResourceObj.map(each => each.kanji.char))
    }

    val uniqueCharsOfType: Set[String] = testIfAllCharsOfTypeAreUnique.toSet
    var typeBuffer: ListBuffer[heisigResource] = new ListBuffer[heisigResource]
    for (eachChar <- uniqueCharsOfType) {
      for (eachHeisigResource <- heisigResourceObj) {
        if (tradsimpkanji.equals("trad") && eachChar.equals(eachHeisigResource.trad.char)) {
          typeBuffer += eachHeisigResource
        } else if (tradsimpkanji.equals("simp") && eachChar.equals(eachHeisigResource.simp.char)) {
          typeBuffer += eachHeisigResource
        }else if (tradsimpkanji.equals("kanji") && eachChar.equals(eachHeisigResource.kanji.char)){
          typeBuffer += eachHeisigResource
        }
      }
      mutableResult += eachChar -> typeBuffer.toList
      typeBuffer = new ListBuffer[heisigResource]
    }

    val finalResult: Map[String, List[heisigResource]] = mutableResult.toMap
    return finalResult
  }

}
