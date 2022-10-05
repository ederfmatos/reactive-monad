package com.ederfmatos.reactive.kotlin

import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize(using = SyncOneSerializer::class)
class SyncOne<T : Any>(val value: T?) : One<T> {

    override fun <R : Any> map(block: (T) -> R): One<R> {
        if (value == null) return empty()
        return block(value).let(::SyncOne)
    }

    override fun <R : Any> flatMap(block: (T) -> One<R>): One<R> {
        if (value == null) return empty()
        return block(value)
    }

    override fun ifEmpty(block: () -> One<T>): One<T> {
        if (value == null) return block()
        return this
    }

    override fun ifEmptyReturn(block: () -> T): One<T> {
        if (value == null) return block().let(::SyncOne)
        return this
    }

    override fun filter(block: (T) -> Boolean): One<T> {
        return if (value == null || block.invoke(value)) this
        else empty()
    }

    override fun filterNot(block: (T) -> Boolean): One<T> {
        return if (value == null || !block.invoke(value)) this
        else empty()
    }

    override fun onNext(block: (T) -> Unit): One<T> {
        if (value == null) return empty()
        block(value)
        return this
    }

    override fun onError(block: (Throwable) -> Unit): One<T> {
        if (value == null) return empty()
        if (value is Throwable) block(value)
        return this
    }

    override fun onEmpty(block: () -> Unit): One<T> {
        if (value == null) block()
        return this
    }

    override fun onComplete(block: () -> Unit): One<T> {
        block()
        return this
    }

    override fun onSuccess(block: (T) -> Unit): One<T> {
        if (value == null) return empty()
        block(value)
        return this
    }

    companion object {
        fun <T : Any> empty(): SyncOne<T> {
            return SyncOne(null)
        }

        fun <T : Any> from(value: T): One<T> {
            return SyncOne(value)
        }
    }
}