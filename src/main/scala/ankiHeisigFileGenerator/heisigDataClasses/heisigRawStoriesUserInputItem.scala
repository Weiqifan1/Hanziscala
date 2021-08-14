package ankiHeisigFileGenerator.heisigDataClasses
/*
framenr,kanji,keyword,public,last_edited,story
9,九,"nine",0,2012-08-18 19:08:59,"as a primitive, this means a baseball bat (my-heisig keyword)."
* */
case class heisigRawStoriesUserInputItem(framenrInt: Int,
                                    framenr: String,
                                    character: String,
                                    keyword: String,
                                    story: String)
