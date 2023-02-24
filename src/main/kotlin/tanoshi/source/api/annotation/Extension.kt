package tanoshi.source.api.annotation

import tanoshi.source.api.enum.SupportedType

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Extension(
    val type : SupportedType
)