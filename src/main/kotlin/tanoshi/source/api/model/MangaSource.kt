package tanoshi.source.api.model

import tanoshi.source.api.type.Manga
import tanoshi.source.api.type.MangaChapter

interface MangaSource : Source<Manga> {

    fun fetchChapterList( manga : Manga ) : HashMap<String,MangaChapter>

    fun fetchChapterPages( chapter : MangaChapter ) : HashMap<Int,String>

}