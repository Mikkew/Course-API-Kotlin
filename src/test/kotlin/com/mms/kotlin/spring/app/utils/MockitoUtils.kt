package com.mms.kotlin.spring.app.utils

import org.mockito.Mockito

fun <T> anyMockito(): T {
    Mockito.any<T>()
    return null as T
}