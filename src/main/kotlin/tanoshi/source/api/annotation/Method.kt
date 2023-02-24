package tanoshi.source.api.annotation

import tanoshi.source.api.enum.MethodStatus

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Method(
    val status : MethodStatus
)
