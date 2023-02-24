package tanoshi.source.api.storage

import tanoshi.source.api.Helper.validateDir

object Preferences {

    val cacheDir : String
        get() = "${System.getProperty("user.dir")}/.cache/tanoshi/".validateDir()


    val configDir : String
        get() = "${System.getProperty("user.dir")}/.config/tanoshi/".validateDir()

}