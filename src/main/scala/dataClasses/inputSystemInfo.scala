package dataClasses

case class inputSystemInfo(hanzi: String,
                           traditional: Boolean,
                           simplified: Boolean,
                           code: String,
                           pronounciation: String,
                           translation: String,
                           traditionalFrequency: List[Int],
                           simplifiedFrequency: List[Int])
