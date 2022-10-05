package com.ederfmatos.reactive.kotlin

import reactor.core.CoreSubscriber
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

class MonoOne<T : Any>(private val value: Mono<T>) : Mono<T>(), One<T> {
    override fun <R : Any> map(block: (T) -> R): One<R> {
        return value.map(block).let(::MonoOne)
    }

    override fun <R : Any> flatMap(block: (T) -> One<R>): One<R> {
        return MonoOne(value.flatMap { (block.invoke(it) as MonoOne<R>).value })
    }

    override fun filter(block: (T) -> Boolean): One<T> {
        return MonoOne(value.filter(block))
    }

    override fun filterNot(block: (T) -> Boolean): One<T> {
        return MonoOne(value.filter { !block.invoke(it) })
    }

    override fun onNext(block: (T) -> Unit): One<T> {
        return MonoOne(value.doOnNext(block))
    }

    override fun ifEmpty(block: () -> One<T>): One<T> {
        return MonoOne(value.switchIfEmpty {
            (block() as MonoOne<T>).value
        })
    }

    override fun ifEmptyReturn(block: () -> T): One<T> {
        return MonoOne(value.switchIfEmpty {
            just(block())
        })
    }

    override fun onError(block: (Throwable) -> Unit): One<T> {
        return MonoOne(value.doOnError(block))
    }

    override fun onEmpty(block: () -> Unit): One<T> {
        return value.switchIfEmpty(defer {
            block()
            Mono.empty()
        }).let(::MonoOne)
    }

    override fun onComplete(block: () -> Unit): One<T> {
        return value.doOnSuccess { block() }.let(::MonoOne)
    }

    override fun onSuccess(block: (T) -> Unit): One<T> {
        return value.doOnSuccess(block).let(::MonoOne)
    }

    override fun subscribe(actual: CoreSubscriber<in T>) {
        value.subscribe(actual)
    }

    companion object {
        fun <T : Any> from(value: T): One<T> = MonoOne(just(value))
        fun <T : Any> empty(): One<T> = MonoOne(Mono.empty())
    }
}