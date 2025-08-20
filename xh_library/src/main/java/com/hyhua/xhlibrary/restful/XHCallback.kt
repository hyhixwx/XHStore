package com.hyhua.xhlibrary.restful

interface XHCallback<T> {
    fun onSuccess(response: XHResponse<T>)
    fun onFailed(throwable: Throwable)
}