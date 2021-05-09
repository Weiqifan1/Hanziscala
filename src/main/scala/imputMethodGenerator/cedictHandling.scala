package imputMethodGenerator

import dataClasses.{cedictMaps, cedictObject, cedictTempTuple}

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

//cedictObject(traditionalHanzi: String, simplifiedHanzi: String, pinyin: String, translation: String)
object cedictHandling {

  def listBufferContainsCedictObject(cedictObj: cedictObject, buffer: ListBuffer[cedictObject]): Boolean ={
    var result = false
    for (each: cedictObject <- buffer.toList) {
      val obj1 = cedictObj.traditionalHanzi
      val each1 = each.traditionalHanzi
      val first = obj1.equals(each1)

      val obj2 = cedictObj.simplifiedHanzi
      val each2 = each.simplifiedHanzi
      val second = obj2.equals(each2)

      val obj3 = cedictObj.pinyin
      val each3 = each.pinyin
      val third = obj3.equals(each3)

      val obj4 = cedictObj.translation
      val each4 = each.translation
      val forth = obj4.equals(each4)

      if (first && second && third && forth){
        result = true
      }
    }
    return result
  }

  def createCedictMap(objectList: List[cedictObject]): cedictMaps ={
    var protoTradMap: ListBuffer[cedictTempTuple] = new ListBuffer[cedictTempTuple]
    var protoSimpMap: ListBuffer[cedictTempTuple] = new ListBuffer[cedictTempTuple]

    val tradSortedObjects: List[cedictObject] = objectList.sortBy(_.traditionalHanzi)
    val simpSortedObjects: List[cedictObject] = objectList.sortBy(_.simplifiedHanzi)

    var currentChars: String = tradSortedObjects(0).traditionalHanzi
    var tempTrad: ListBuffer[cedictObject] = new ListBuffer[cedictObject]//tradSortedObjects.filter(_.traditionalHanzi.equals(eachTrad.traditionalHanzi))
    for (eachTrad <- tradSortedObjects) {
      if (eachTrad.traditionalHanzi.equals(currentChars) && !listBufferContainsCedictObject(eachTrad, tempTrad)) {
        val mytest = ""
        tempTrad += eachTrad
      }else {
        protoTradMap += cedictTempTuple((currentChars, tempTrad.toSet.toList))
        currentChars = eachTrad.traditionalHanzi
        tempTrad = new ListBuffer[cedictObject]
        tempTrad += eachTrad
      }
    }
    protoTradMap += cedictTempTuple((tradSortedObjects(tradSortedObjects.length-1).traditionalHanzi, List(tradSortedObjects(tradSortedObjects.length-1))))

    currentChars = simpSortedObjects(0).simplifiedHanzi
    var tempSimp: ListBuffer[cedictObject] = new ListBuffer[cedictObject]//tradSortedObjects.filter(_.traditionalHanzi.equals(eachTrad.traditionalHanzi))

    val finalChar = simpSortedObjects(simpSortedObjects.length-1).simplifiedHanzi
    val finalCharList: ListBuffer[cedictObject] = new ListBuffer[cedictObject]
    for (eachSimp <- simpSortedObjects) {
      if (eachSimp.simplifiedHanzi.equals("\uD873\uDE88")){
        val string = ""
      }
      if (finalChar.equals(eachSimp.simplifiedHanzi)) {
        finalCharList += eachSimp
      } else {
        if (eachSimp.simplifiedHanzi.equals(currentChars) && !listBufferContainsCedictObject(eachSimp, tempSimp)) {
          val mytest = ""
          tempSimp += eachSimp
        }else {
          protoSimpMap += cedictTempTuple((currentChars, tempSimp.toSet.toList))
          currentChars = eachSimp.simplifiedHanzi
          tempSimp = new ListBuffer[cedictObject]
          tempSimp += eachSimp
        }
      }
    }
    protoSimpMap += cedictTempTuple((finalChar, finalCharList.toList))

    val finalTrad: List[cedictTempTuple] = protoTradMap.toList
    val finalSimp: List[cedictTempTuple] = protoSimpMap.toList

    println("finalTraddoubleListlength")
    val printOutTrad = finalTrad.filter(each => each.tuple._2.length > 1)
    println(printOutTrad.length)
    println("finalSimpdoubleListlength")
    val printoutSimp = finalSimp.filter(each => each.tuple._2.length > 1)
    println(printoutSimp.length)

    val finalTradMap2: Map[String, List[cedictObject]] = finalTrad.map(tup => tup.tuple._1 -> tup.tuple._2).toMap
    val finalSimpMap2: Map[String, List[cedictObject]] = finalSimp.map(tup => tup.tuple._1 -> tup.tuple._2).toMap
    return cedictMaps(finalTradMap2, finalSimpMap2)
  }

  def getAllTradDubletWordsFromCedict(objectList: List[cedictObject]): Set[String] ={
    val tradSortedObjects: List[cedictObject] = objectList.sortBy(_.traditionalHanzi)

    var traditionalOldList: mutable.SortedSet[String] = scala.collection.mutable.SortedSet[String]()
    var traditionalList: mutable.SortedSet[String] = scala.collection.mutable.SortedSet[String]()

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

    var simplifiedOldList: mutable.SortedSet[String] = scala.collection.mutable.SortedSet[String]()
    var simplifiedList: mutable.SortedSet[String] = scala.collection.mutable.SortedSet[String]()

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
    val objectList: List[cedictObject] = splitlines.map(each => cedictObject(each(0).toString.split("\\s")(0),
        each(0).toString.split("\\s")(1),
        each(1).toString,
        each(2).toString))
    return objectList
  }

  def getCedictHanziToTranslationMap(): cedictMaps ={
    val getcedictObjecyList = getCedictObjectList()

    val finalCedictMaps: cedictMaps = createCedictMap(getcedictObjecyList)

    val oos = new ObjectOutputStream(new FileOutputStream("src/main/resources/serializedDataFiles/cedictMaps.txt"))
    oos.writeObject(finalCedictMaps)
    oos.close

    println("serialized cedict saved to file")

    val ois = new ObjectInputStream(new FileInputStream("src/main/resources/serializedDataFiles/cedictMaps.txt"))
    val readMapsIn = ois.readObject.asInstanceOf[cedictMaps]
    ois.close

    println("serialized cedict readIn")

    val tradset = getAllTradDubletWordsFromCedict(getcedictObjecyList)
    val simpset = getAllSimpDubletWordsFromCedict(getcedictObjecyList)
    println("dubletTradWords")
    println(tradset.toSet.size)
    println("dubletSimplifiedWords")
    println(simpset.toSet.size)
    val firstTrad = tradset.toList(0)
    val fristsimp = simpset.toList(0)

    println(readMapsIn.traditionalMap(firstTrad))
    println(readMapsIn.simplifiedMap(fristsimp))
    println("success")

    return null//cedictMaps(traditionalHanzi, simplifiedHanzi)
  }
}

