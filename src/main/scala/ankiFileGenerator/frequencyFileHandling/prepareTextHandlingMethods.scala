package ankiFileGenerator.frequencyFileHandling

import ankiFileGenerator.dataClasses.cedictFreqObject
import dataClasses.{cedictMaps, cedictObject, frequencyMaps}

/*
case class cedictFreqObject(traditionalHanzi: String,
                            simplifiedHanzi: String,
                            pinyin: String,
                            translation: String,
                            traditionalFrequency: List[Int],
                            simplifiedFrequency: List[Int])*/



object prepareTextHandlingMethods {

  //def getCompleteInfoFromWord()

  def getNaiveInfoFromWord(word: String, traditional: Boolean, cedict: cedictMaps, frequency: frequencyMaps): cedictFreqObject = {
    val cedictMap: Map[String, List[cedictObject]] = if (traditional) {cedict.traditionalMap} else {cedict.simplifiedMap}
    val cedictResult: Option[List[cedictObject]] = cedictMap.get(word)

    val traditionalWord: String = if (cedictResult.isEmpty) word else cedictResult.get(0).traditionalHanzi
    val simplifiedWord: String =  if (cedictResult.isEmpty) word else cedictResult.get(0).simplifiedHanzi
    val pinyin: List[String] = if (cedictResult.isEmpty) List("") else cedictResult.get.map(i => i.pinyin)
    val translation: List[String] = if (cedictResult.isEmpty) List("") else cedictResult.get.map(i => i.pinyin)
    val traditionalFreq: List[Int] = getHanziListFromText(traditionalWord).map(i =>
        {val optionFreq = frequency.traditional.get(i)
        if (optionFreq.isEmpty) 0 else optionFreq.get.toInt})
    val simplifiedFreq: List[Int] = getHanziListFromText(simplifiedWord).map(i =>
        {val optionFreq = frequency.simplified.get(i)
        if (optionFreq.isEmpty) 0 else optionFreq.get.toInt})

    val finalResult: cedictFreqObject =
      new cedictFreqObject(traditionalWord, simplifiedWord, pinyin, translation, traditionalFreq, simplifiedFreq)
    return finalResult
  }

  private def getHanziListFromText(text: String): List[String] = {
    val stream: List[Int] = text.codePoints.toArray.toList
    val backToString: List[String] = stream.map(i => Character.toChars(i).mkString)
    return backToString
  }

}
