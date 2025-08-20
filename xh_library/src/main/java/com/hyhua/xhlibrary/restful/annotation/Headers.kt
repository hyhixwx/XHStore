package com.hyhua.xhlibrary.restful.annotation

/**
 * ```kotlin
 * @Headers({"connection:keep-alive", "auth-token:token"})
 * fun test(@Filed("province") provinceId: Int)
 * ```
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Headers(vararg val value: String)
