package imputMethodGenerator

import dataClasses.{cedictMaps, cedictObject, cedictTempTuple}

//cedictObject(traditionalHanzi: String, simplifiedHanzi: String, pinyin: String, translation: String)
object cedictHandling {

  def getCedictHanziToTranslationMap(): cedictMaps ={
    val getcedictObjecyList = getCedictObjectList()

    val finalCedictMaps: cedictMaps = createCedictMap2(getcedictObjecyList)
    val oos = new ObjectOutputStream(new FileOutputStream("src/main/resources/serializedDataFiles/cedictMaps.txt"))
    oos.writeObject(finalCedictMaps)
    oos.close

    val ois = new ObjectInputStream(new FileInputStream("src/main/resources/serializedDataFiles/cedictMaps.txt"))
    val readMapsIn = ois.readObject.asInstanceOf[cedictMaps]
    ois.close



    val tradset = getAllTradDubletWordsFromCedict(getcedictObjecyList)
    val simpset = getAllSimpDubletWordsFromCedict(getcedictObjecyList)
    println("dubletTradWords")
    println(tradset.size)
    println("dubletSimplifiedWords")
    println(simpset.size)
    val firstTrad = tradset.toList(0)
    val fristsimp = simpset.toList(0)

    println(readMapsIn.traditionalMap(firstTrad))
    println(readMapsIn.simplifiedMap(fristsimp))
    println("success")


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
