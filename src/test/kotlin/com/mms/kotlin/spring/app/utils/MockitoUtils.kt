package com.mms.kotlin.spring.app.utils

import org.mockito.Mockito

@Suppress("UNCHECKED_CAST")
class MockitoUtils {
    companion object {
        public fun <T> any(): T {
            Mockito.any<T>()
            return null as T
        }
    }
}