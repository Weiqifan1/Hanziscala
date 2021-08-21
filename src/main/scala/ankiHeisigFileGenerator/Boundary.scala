package ankiHeisigFileGenerator

import ankiHeisigFileGenerator.frequencyFileHandling.loadFrequencyFiles.{readCedictMapsFromFile, readJundaAndTzaiMapsFromFile}
import ankiHeisigFileGenerator.heisigDataClasses.{heisigRawResourceCollection, heisigRawStoriesUserInputItem, heisigResource}
import ankiHeisigFileGenerator.readHeisigResources.createHeisigMap.{createHeisigMap, generateHeisigMap}
import ankiHeisigFileGenerator.readHeisigResources.readHeisigAllBookResourceRawFile.readHeisigResourceRawFile
import ankiHeisigFileGenerator.readHeisigResources.readHeisigStoriesUserInputRawFile.readHeisigStoriesTradHanziUserInputRawFile

object Boundary {



  def runAnkiHeisigFileGenerator(): Unit = {
    println("hej lykke")

    val cedictMap = readCedictMapsFromFile()
    val jundaAndTzai = readJundaAndTzaiMapsFromFile()


    //look up a character>
    //太慘了, 金姆, 我想到了
    val testlook = cedictMap.traditionalMap.get("了")
    val testsimp = cedictMap.simplifiedMap.get("想")

    println("trad: *************************************\n"  + testlook.get(0).translation)
    println("simp: *************************************\n" + testsimp.get(0).translation)

    val heisig: heisigRawResourceCollection = readHeisigResourceRawFile("src/main/resources/heisigResourcesRaw/HeisigAllBooks2021BackupNotAllInfo.csv")

    val story: List[List[String]] = readHeisigStoriesTradHanziUserInputRawFile("src/userInputHeisigFiles/ChrLykke_stories_backupcopy.csv")

    val heisigFinalResource: List[heisigResource] = createHeisigMap(heisig, story)

    //generateHeisigMap()

    println("end of heisig")
  }
}
