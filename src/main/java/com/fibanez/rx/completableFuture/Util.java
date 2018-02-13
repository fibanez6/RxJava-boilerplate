package com.fibanez.rx.completableFuture;

import rx.Observable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Util {

    /**
     * Turnig CompletableFuture into Observable with single item
     * @param future
     * @param <T>
     * @return
     */
    static <T> Observable<T> observe(CompletableFuture<T> future) {
        return Observable.create( subscriber -> {
            future.whenComplete( (value,exception) -> {
                if (exception != null) {
                    subscriber.onError(exception);
                } else {
                    subscriber.onNext(value);
                    subscriber.onCompleted();
                }
            });
        });
    }

    /**
     * Exactly one emitted value.
     * Each invocation of this transformer will subscribe again; it is just a design choice.
     * @param observable
     * @param <T>
     * @return
     */
    static <T> CompletableFuture<T> toFuture(Observable<T> observable) {
        CompletableFuture<T> promise = new CompletableFuture<>();
        observable
                .single()
                .subscribe(
                        promise::complete,
                        promise::completeExceptionally
                );
        return promise;
    }

    /**
     * It completes when all events from upstream Observable are emitted and the stream completes
     * @param observable
     * @param <T>
     * @return
     */
    static <T> CompletableFuture<List<T>> toFutureList(Observable<T> observable) {
        return toFuture(observable.toList());
    }

}
