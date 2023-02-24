package tanoshi.source.api.type

import tanoshi.source.api.model.DataClass
import tanoshi.source.api.model.Source

class Anime( source : Source<Anime> ) : DataClass {

    override var id: String? = null

    override var url: String? = null

    override var title: String? = null

    override var thumbnail_url: String? = null

    override var description: String? = null

    override val source : String = source::class.java.name

    var episodeList : HashMap<String,Episode>? = null

}