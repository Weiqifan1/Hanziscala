package ankiFileGenerator.generateAudioFiles

import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.text_to_speech.v1.TextToSpeech
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions
import com.ibm.watson.text_to_speech.v1.util.WaveUtils
import java.io.FileOutputStream
import java.io.IOException

//prøv i stedet med IBM: https://cloud.ibm.com/docs?tab=tutorials&filters=lite-account&page=1&pageSize=20
//"https://api.eu-gb.text-to-speech.watson.cloud.ibm.com/v1/synthesize
object generateAudio {

//4Dm9hNplQNmu2twG3pjh86nAqR9_-px5eifVqXyKI7w2
  def getAudioFile(chineseText: String) = {
    val authenticator = new IamAuthenticator("4Dm9hNplQNmu2twG3pjh86nAqR9_-px5eifVqXyKI7w2")
    val textToSpeech = new TextToSpeech(authenticator)
    textToSpeech.setServiceUrl("https://api.eu-gb.text-to-speech.watson.cloud.ibm.com")

    try {
      val synthesizeOptions = new SynthesizeOptions.Builder()
        .text(chineseText)
        .accept("audio/wav")
        .voice("zh-CN_ZhangJingVoice").build //zh-CN_ZhangJingVoice //en-US_AllisonV3Voice
      val inputStream = textToSpeech.synthesize(synthesizeOptions).execute.getResult
      val in = WaveUtils.reWriteWaveHeader(inputStream)
      val out = new FileOutputStream("hello_world.wav")
      var buffer = new Array[Byte](1024)

      var length = in.read(buffer)
      while (length > 0) {
        out.write(buffer, 0, length)
        length = in.read(buffer)
      }

      out.close()
      in.close()
      inputStream.close()
    } catch {
      case e: IOException =>
        e.printStackTrace()
    }
  }



}
