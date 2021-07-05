package ankiFileGenerator.generateAudioFiles

//import scalaj.http.{Http, HttpOptions, HttpResponse}

//2021 07 05 kl. 22.33 -- proev med amazon polly
//https://github.com/mslinn/awslib_scala
//https://aws.amazon.com/polly/features/?nc=sn&loc=3
object generateAudio {
/*
  def getAudioFile(chineseText: String) = {

    val POSTMESSAGE_1: String = """{
                                  |  "engine": "Google",
                                  |  "data": {
                                  |    "text": """.stripMargin
    val POSTMESSAGE_2: String = """,
                                  |    "voice": "cmn-Hant-TW"
                                  |  }
                                  |}""".stripMargin

    val result: HttpResponse[String] = Http("https://api.soundoftext.com")
      .postData(POSTMESSAGE_1 + "\"" + chineseText + "\"" + POSTMESSAGE_2)
      .header("Content-Type", "application/json")
      .header("Charset", "UTF-8")
      .option(HttpOptions.readTimeout(10000)).asString

    val test: String = "test"
    }
*/



}
