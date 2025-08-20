package com.hyhua.xhlibrary.restful.annotation

/**
 * ```kotlin
 * @BaseUrl("https://api.xx.xxx/v1")
 * fun test(@Filed("province") provinceId: Int)
 * ```
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseUrl(val value: String)
