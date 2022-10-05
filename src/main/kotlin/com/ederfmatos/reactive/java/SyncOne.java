package com.ederfmatos.reactive.java;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@JsonSerialize(using = SyncOneSerializer.class)
public class SyncOne<T> implements One<T> {

    final T value;

    private SyncOne(T value) {
        this.value = value;
    }

    @Override
    public <R> One<R> map(Function<T, R> transformer) {
        if (isEmpty()) return empty();
        return new SyncOne<>(transformer.apply(value));
    }

    @Override
    public <R> One<R> flatMap(Function<T, One<R>> transformer) {
        if (isEmpty()) return empty();
        return transformer.apply(value);
    }

    @Override
    public <R> One<R> then(Supplier<One<R>> transformer) {
        return transformer.get();
    }

    @Override
    public <R> One<R> thenConsume(Supplier<R> transformer) {
        return new SyncOne<>(transformer.get());
    }

    @Override
    public One<T> ifEmpty(Supplier<One<T>> supplier) {
        if (isEmpty()) return supplier.get();
        return this;
    }

    @Override
    public One<T> ifEmptyReturn(Supplier<T> supplier) {
        if (isEmpty()) return new SyncOne<>(supplier.get());
        return this;
    }

    @Override
    public One<T> filter(Predicate<T> filter) {
        if (isEmpty() || filter.test(value)) return this;
        return empty();
    }

    @Override
    public One<T> filterNot(Predicate<T> filter) {
        if (isEmpty() || !filter.test(value)) return this;
        return empty();
    }

    @Override
    public One<T> onNext(Consumer<T> consumer) {
        if (isNotEmpty()) consumer.accept(value);
        return this;
    }

    @Override
    public One<T> onError(Consumer<Throwable> consumer) {
        if (value instanceof Throwable throwable) consumer.accept(throwable);
        return this;
    }

    @Override
    public One<T> onEmpty(Runnable runnable) {
        if (isEmpty()) runnable.run();
        return this;
    }

    @Override
    public One<T> onSuccess(Consumer<T> consumer) {
        if (isNotEmpty()) consumer.accept(value);
        return this;
    }

    @Override
    public One<T> onComplete(Runnable runnable) {
        if (isNotEmpty()) runnable.run();
        return this;
    }

    private boolean isEmpty() {
        return value == null;
    }

    private boolean isNotEmpty() {
        return value != null;
    }

    public static <T> One<T> empty() {
        return new SyncOne<>(null);
    }

    public static <T> One<T> from(T value) {
        return new SyncOne<>(value);
    }
}
