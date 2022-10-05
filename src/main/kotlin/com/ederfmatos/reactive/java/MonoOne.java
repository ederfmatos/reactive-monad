package com.ederfmatos.reactive.java;

import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MonoOne<T> implements One<T> {

    private final Mono<T> value;

    public MonoOne(Mono<T> value) {
        this.value = value;
    }

    @Override
    public <R> One<R> map(Function<T, R> transformer) {
        return new MonoOne<>(value.map(transformer));
    }

    @Override
    public <R> One<R> flatMap(Function<T, One<R>> transformer) {
        return new MonoOne<>(value.flatMap(t -> oneToMono(transformer.apply(t))));
    }

    @Override
    public <R> One<R> then(Supplier<One<R>> transformer) {
        return new MonoOne<>(value.then(Mono.defer(() -> oneToMono(transformer.get()))));
    }

    @Override
    public <R> One<R> thenConsume(Supplier<R> transformer) {
        return new MonoOne<>(value.then(Mono.defer(() -> Mono.justOrEmpty(transformer.get()))));
    }

    @Override
    public One<T> ifEmpty(Supplier<One<T>> supplier) {
        return new MonoOne(value.switchIfEmpty(Mono.defer(() -> oneToMono(supplier.get()))));
    }

    @Override
    public One<T> ifEmptyReturn(Supplier<T> supplier) {
        return new MonoOne(value.switchIfEmpty(Mono.fromSupplier(supplier)));
    }

    @Override
    public One<T> filter(Predicate<T> filter) {
        return new MonoOne<>(value.filter(filter));
    }

    @Override
    public One<T> filterNot(Predicate<T> filter) {
        return new MonoOne<>(value.filter(filter.negate()));
    }

    @Override
    public One<T> onNext(Consumer<T> consumer) {
        return new MonoOne(value.doOnNext(consumer));
    }

    @Override
    public One<T> onError(Consumer<Throwable> consumer) {
        return new MonoOne(value.doOnError(consumer));
    }

    @Override
    public One<T> onEmpty(Runnable runnable) {
        return new MonoOne(value.switchIfEmpty(Mono.defer(() -> {
            runnable.run();
            return Mono.empty();
        })));
    }

    @Override
    public One<T> onSuccess(Consumer<T> consumer) {
        return new MonoOne(value.doOnSuccess(consumer));
    }

    @Override
    public One<T> onComplete(Runnable runnable) {
        return new MonoOne(value.doOnTerminate(runnable));
    }

    private <R> Mono<R> oneToMono(One<R> one) {
        return ((MonoOne<R>) one).value;
    }

    public static <T> One<T> from(T value) {
        return new MonoOne<>(Mono.justOrEmpty(value));
    }

    public static <T> One<T> empty() {
        return new MonoOne<>(Mono.empty());
    }

}
