package ankiHeisigFileGenerator

import ankiHeisigFileGenerator.frequencyFileHandling.loadFrequencyFiles.{readCedictMapsFromFile, readJundaAndTzaiMapsFromFile}

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

    println("end of heisig")
  }
}
