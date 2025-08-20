package com.hyhua.xhlibrary.restful.annotation

/**
 * ```kotlin
 * @GET("/cities/{province}")
 * fun test(@Path("province") provinceId: Int)
 * ```
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Path(val value: String)
