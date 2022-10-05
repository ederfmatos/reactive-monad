package com.ederfmatos.reactive.kotlin

interface One<T: Any> {
    fun <R: Any> map(block: (T) -> R): One<R>
    fun <R: Any> flatMap(block: (T) -> One<R>): One<R>
    fun ifEmpty(block: () -> One<T>): One<T>
    fun ifEmptyReturn(block: () -> T): One<T>
    fun filter(block: (T) -> Boolean): One<T>
    fun filterNot(block: (T) -> Boolean): One<T>
    fun onNext(block: (T) -> Unit): One<T>
    fun onError(block: (Throwable) -> Unit): One<T>
    fun onEmpty(block: () -> Unit): One<T>
    fun onSuccess(block: (T) -> Unit): One<T>
    fun onComplete(block: () -> Unit): One<T>

    companion object {
        @JvmStatic
        inline fun <reified T : Any> from(value: T): One<T> {
            return MonoOne.from(value)
        }

        @JvmStatic
        inline fun <reified T : Any> empty(): One<T> {
            return MonoOne.empty()
        }
    }
}