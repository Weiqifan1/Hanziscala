package ankiFileGenerator.dataClasses

case class cedictFreqObject(traditionalHanzi: String,
                            simplifiedHanzi: String,
                            pinyin: List[String],
                            translation: List[String],
                            traditionalFrequency: List[Int],
                            simplifiedFrequency: List[Int])