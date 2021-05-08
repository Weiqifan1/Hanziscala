package imputMethodGenerator

import dataClasses.{cedictMaps, cedictObject, cedictTempTuple}

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}
import scala.collection.mutable.ListBuffer

//cedictObject(traditionalHanzi: String, simplifiedHanzi: String, pinyin: String, translation: String)
object cedictHandling {

  def createCedictMap2(objectList: List[cedictObject]): cedictMaps ={
    var protoTradMap: ListBuffer[cedictTempTuple] = new ListBuffer[cedictTempTuple]
    var protoSimpMap: ListBuffer[cedictTempTuple] = new ListBuffer[cedictTempTuple]

    val tradSortedObjects: List[cedictObject] = objectList.sortBy(_.traditionalHanzi)
    val simpSortedObjects: List[cedictObject] = objectList.sortBy(_.simplifiedHanzi)

    var currentChars: String = tradSortedObjects(0).traditionalHanzi
    var tempTrad: ListBuffer[cedictObject] = new ListBuffer[cedictObject]//tradSortedObjects.filter(_.traditionalHanzi.equals(eachTrad.traditionalHanzi))
    for (eachTrad <- tradSortedObjects) {
      if (eachTrad.traditionalHanzi.equals(currentChars)) {
        tempTrad += eachTrad
      }else {
        protoTradMap += cedictTempTuple((eachTrad.traditionalHanzi, tempTrad.toList))
        currentChars = eachTrad.traditionalHanzi
        tempTrad = new ListBuffer[cedictObject]
        tempTrad += eachTrad
      }
    }
    protoTradMap += cedictTempTuple((tradSortedObjects(tradSortedObjects.length-1).traditionalHanzi, List(tradSortedObjects(tradSortedObjects.length-1))))


    currentChars = simpSortedObjects(0).simplifiedHanzi
    var tempSimp: ListBuffer[cedictObject] = new ListBuffer[cedictObject]//tradSortedObjects.filter(_.traditionalHanzi.equals(eachTrad.traditionalHanzi))
    for (eachSimp <- simpSortedObjects) {
      if (eachSimp.simplifiedHanzi.equals(currentChars)) {
        tempSimp += eachSimp
      }else {
        protoSimpMap += cedictTempTuple((eachSimp.traditionalHanzi, tempTrad.toList))
        currentChars = eachSimp.simplifiedHanzi
        tempSimp = new ListBuffer[cedictObject]
        tempSimp += eachSimp
      }
    }
    protoSimpMap += cedictTempTuple((simpSortedObjects(simpSortedObjects.length-1).simplifiedHanzi, List(simpSortedObjects(simpSortedObjects.length-1))))

    val finalTrad: List[cedictTempTuple] = protoTradMap.toList
    val finalSimp: List[cedictTempTuple] = protoSimpMap.toList

    println("finalTraddoubleListlength")
    println(finalSimp.filter(each => each.tuple._2.length > 1).length)
    println("finalTraddoubleListlength")
    println(finalSimp.filter(each => each.tuple._2.length > 1).length)

    val finalTradMap2: Map[String, List[cedictObject]] = finalTrad.map(tup => tup.tuple._1 -> tup.tuple._2).toMap
    val finalSimpMap2: Map[String, List[cedictObject]] = finalSimp.map(tup => tup.tuple._1 -> tup.tuple._2).toMap
    return cedictMaps(finalTradMap2, finalSimpMap2)
  }

  def getAllTradDubletWordsFromCedict(objectList: List[cedictObject]): Set[String] ={
    val tradSortedObjects: List[cedictObject] = objectList.sortBy(_.traditionalHanzi)

    var traditionalOldList: ListBuffer[String] = new ListBuffer[String]
    var traditionalList: ListBuffer[String] = new ListBuffer[String]

    for (each <- tradSortedObjects) {
      if (traditionalOldList.contains(each.traditionalHanzi)) {
        traditionalList += each.traditionalHanzi
      }
      traditionalOldList += each.traditionalHanzi
    }

    val result = traditionalList.toSet
    return result
  }
  def getAllSimpDubletWordsFromCedict(objectList: List[cedictObject]): Set[String] ={
    val simpSortedObjects: List[cedictObject] = objectList.sortBy(_.simplifiedHanzi)

    var simplifiedOldList: ListBuffer[String] = new ListBuffer[String]
    var simplifiedList: ListBuffer[String] = new ListBuffer[String]

    for (each <- simpSortedObjects) {
      if (simplifiedOldList.contains(each.simplifiedHanzi)) {
        simplifiedList += each.simplifiedHanzi
      }
      simplifiedOldList += each.simplifiedHanzi
    }

    val result = simplifiedList.toSet
    return result
  }

  def getCedictObjectList(): List[cedictObject] = {
    val filePath = "src/main/resources/frequencyfilesRaw/cedict_ts.txt"
    val hanzilines: List[String] = scala.io.Source.fromFile(filePath).mkString.split("\n").toList
    val splitlines: List[Array[String]] = hanzilines map {line => line.split("(\\s+\\[)|(\\]\\s+)")}
    var objectList: List[cedictObject] =
      splitlines.map(each => cedictObject(each(0).toString.split("\\s")(0),
        each(0).toString.split("\\s")(1),
        each(1).toString,
        each(2).toString))
    return objectList
  }

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



    //var jundaHashmap: Map[String, cedictObject] = null
    //if (traditionalCharacters){

    //********************************
    // start med lister
    //********************************

    //val cedictTradMap: Map[String, List[String]] = cedictLines.map(i => i(0) -> i).toMap
    //val cedictSimpMap: Map[String, List[String]] = cedictLines.map(i => i(1) -> i).toMap

    val test: String = "hello"

    /*
    var traditionalHanzi: Map[String, cedictObject] = splitlines.map(i => i(0).split("\\s")(0) ->
        cedictObject(i(0).split("\\s")(0), i(0).split("\\s")(1), i(1), i(2))).toMap
    //}else {
    var simplifiedHanzi: Map[String, cedictObject] = splitlines.map(i => i(0).split("\\s")(1) ->
        cedictObject(i(0).split("\\s")(0), i(0).split("\\s")(1), i(1), i(2))).toMap
    //}
     */
    return null//cedictMaps(traditionalHanzi, simplifiedHanzi)
  }
}

