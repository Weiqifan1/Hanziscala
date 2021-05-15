package dataClasses

import upickle.default.{ReadWriter => RW, macroRW}

case class inputSystemHanziInfo(hanzi: String,
                                codes: List[String],
                                cedictSimp: Option[List[cedictObject]],
                                cedictTrad: Option[List[cedictObject]],
                                traditionalFrequency: List[String],
                                simplifiedFrequency: List[String])
/*
object inputSystemHanziInfo{
  implicit val rw: RW[inputSystemHanziInfo] = macroRW
}*/
