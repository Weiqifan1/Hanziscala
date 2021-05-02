package imputMethodGenerator

import dataClasses.{codeToTextList, codeToTextObject}

import scala.util.matching.Regex

object inputMethodHandling {

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
