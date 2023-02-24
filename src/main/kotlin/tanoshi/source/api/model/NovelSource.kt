package tanoshi.source.api.model

import tanoshi.source.api.type.Novel
import tanoshi.source.api.type.NovelChapter

interface NovelSource : Source<Novel> {

    fun fetchChapterList( novel : Novel ) : HashMap<String,NovelChapter>

    fun fetchChapterContent( chapter : NovelChapter ) : String

}