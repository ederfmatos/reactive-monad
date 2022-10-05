package com.ederfmatos.reactive.java;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface One<T> {

    <R> One<R> map(Function<T, R> transformer);

    <R> One<R> flatMap(Function<T, One<R>> transformer);

    <R> One<R> then(Supplier<One<R>> transformer);

    <R> One<R> thenConsume(Supplier<R> transformer);

    One<T> ifEmpty(Supplier<One<T>> supplier);

    One<T> ifEmptyReturn(Supplier<T> supplier);

    One<T> filter(Predicate<T> filter);

    One<T> filterNot(Predicate<T> filter);

    One<T> onNext(Consumer<T> consumer);

    One<T> onError(Consumer<Throwable> consumer);

    One<T> onEmpty(Runnable runnable);

    One<T> onSuccess(Consumer<T> consumer);

    One<T> onComplete(Runnable runnable);

    static <T> One<T> from(T value) {
        return MonoOne.from(value);
    }

    static <T> One<T> empty() {
        return MonoOne.empty();
    }
}
