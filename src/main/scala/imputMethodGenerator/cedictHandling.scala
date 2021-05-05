package imputMethodGenerator

import dataClasses.{cedictMaps, cedictObject}

//cedictObject(traditionalHanzi: String, simplifiedHanzi: String, pinyin: String, translation: String)
object cedictHandling {

  def getCedictHanziToTranslationMap(): cedictMaps ={
    val filePath = "src/main/resources/frequencyfilesRaw/cedict_ts.txt"
    val hanzilines: List[String] = scala.io.Source.fromFile(filePath).mkString.split("\n").toList
    //linje eksempel: 21三體綜合症 21三体综合症 [er4 shi2 yi1 san1 ti3 zong1 he2 zheng4] /trisomy/Down's syndrome/

    val splitlines = hanzilines map {line => line.split("(\\s+\\[)|(\\]\\s+)")}
    //var jundaHashmap: Map[String, cedictObject] = null
    //if (traditionalCharacters){
    var traditionalHanzi: Map[String, cedictObject] = splitlines.map(i => i(0).split("\\s")(0) ->
        cedictObject(i(0).split("\\s")(0), i(0).split("\\s")(1), i(1), i(2))).toMap
    //}else {
    var simplifiedHanzi: Map[String, cedictObject] = splitlines.map(i => i(0).split("\\s")(1) ->
        cedictObject(i(0).split("\\s")(0), i(0).split("\\s")(1), i(1), i(2))).toMap
    //}
    return cedictMaps(traditionalHanzi, simplifiedHanzi)
  }
}
