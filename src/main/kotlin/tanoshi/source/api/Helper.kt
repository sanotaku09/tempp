package tanoshi.source.api

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import tanoshi.source.api.model.Source
import tanoshi.source.api.storage.Preferences
import java.io.File
import java.net.URI
import java.net.URL

object Helper {

    val defaultHeaders = arrayOf(
        Pair( "user-agent" , "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36" )
    )

    fun String.validateDir() : String {
        this.toFile().run {
            if ( !isDirectory ) mkdirs()
        }
        return this
    }

    fun String.toFile() = File( this )

    fun String.validateFile() : String {
        this.toFile().run {
            if ( !isFile ) createNewFile()
        }
        return this
    }

    fun <T> saveSelectedDomain( source : Source<T> ) = source.run {
        val location = "${Preferences.configDir}/${source::class.java.packageName}/".validateDir()
        "$location/selectedDomain".validateFile().toFile().writeText(
            selectedDomain?.run {
                this
            } ?: arrayOfDomain[0]
        )
    }

    fun <T> loadConfig( source: Source<T> ) = source.run {
        "${Preferences.configDir}/${source::class.java.packageName}/selectedDomain".toFile().run {
            if ( isFile ) selectedDomain = readText()
        }
    }

    fun String.toHtml(
        vararg headers : Pair<String,String> = defaultHeaders
    ) = Request.Builder().run {
        url( this@toHtml )
        get()
        for ( i in headers ) addHeader( i.first , i.second )
        OkHttpClient()
            .newCall( build() )
            .execute()
            .body().string()
    }

    fun String.toJsoup() : Document? = Jsoup.parse( this )

    fun String.toURL() : URL = URI(
        this.let { newString ->
            newString.trimEnd()
                .trimStart().run {
                    (if ( startsWith( "https://" ) ) this else "https://$this" )
                        .replace( "  " , " " )
                        .replace( " " , "%20" )
                }
        }
    ).toURL()

}