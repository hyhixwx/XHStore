package com.hyhua.xhlibrary.restful.annotation

/**
 * ```kotlin
 * @GET("/cities/all")
 * fun test(@Filed("province") provinceId: Int)
 * ```
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class GET(val value: String)
