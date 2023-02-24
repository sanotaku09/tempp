package tanoshi.source.api.model

import tanoshi.source.api.type.Anime
import tanoshi.source.api.type.Episode
import tanoshi.source.api.type.Video

interface AnimeSource : Source<Anime> {

    fun fetchEpisodeList( anime : Anime) : HashMap<String, Episode>

    fun fetchEpisodeLinks( episode : Episode ) : List<Video>

}