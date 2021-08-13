package ankiStoryFileGenerator.flashcardDataClasses

import io.reactivex.Maybe

case class charFreqObject(traditionalHanzi: List[String],
                          simplifiedHanzi: List[String],
                          pinyin: List[String],
                          translation: List[String],
                          traditionalFrequency: List[Int],
                          simplifiedFrequency: List[Int],
                          traditional: Boolean)
