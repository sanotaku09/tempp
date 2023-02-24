package tanoshi.source.api.model

sealed interface Source<T> {

    val name : String

    val arrayOfDomain : Array<String>

    var selectedDomain : String?

    fun search( name : String , pageIndex : Int = 1 ) : List<T>

    fun latest( pageIndex: Int = 1 ) : List<T>

}