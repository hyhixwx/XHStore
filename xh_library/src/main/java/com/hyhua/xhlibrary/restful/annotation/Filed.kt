package com.hyhua.xhlibrary.restful.annotation

/**
 * ```kotlin
 * @BaseUrl("https://api.xx.xxx/v1/")
 * fun test(@Filed("province") provinceId: Int)
 * ```
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Filed(val value: String)
