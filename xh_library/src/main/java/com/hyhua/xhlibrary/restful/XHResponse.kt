package com.hyhua.xhlibrary.restful

/**
 * 响应报文
 */
open class XHResponse<T> {

    companion object {
        val SUCCESS: Int = 0
    }

    /**
     * 原始数据
     */
    var rawData: String? = null
    /**
     * 业务状态码
     */
    var code = 0
    /**
     * 业务数据
     */
    var data: T? = null
    /**
     * 错误状态下的数据
     */
    var errorData: Map<String, String>? = null
    /**
     * 错误信息
     */
    var msg: String? = null
}