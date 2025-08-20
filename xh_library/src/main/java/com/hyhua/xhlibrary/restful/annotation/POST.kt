package com.hyhua.xhlibrary.restful.annotation

/**
 * ```kotlin
 * @POST("/cities/all")
 * fun test(@Filed("province") provinceId: Int)
 * ```
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class POST(val value: String, val formPost: Boolean = true)
