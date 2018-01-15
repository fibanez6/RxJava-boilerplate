package com.fibanez.rx.extension;

import rx.Observable;

/**
 *
 * cache() keeps a copy of all notifications internally.
 * cache() + infinite stream = disaster known as OutOfMemoryError
 */
public class MultipleSubscribers {

    private static void log(Object msg) {
        System.out.println(Thread.currentThread().getName() +": "+msg);
    }


    public static Observable<Integer> getObservableNoCache() {
        return Observable.create(subscriber -> {
            log("create");
            subscriber.onNext(42);
            subscriber.onCompleted();
        });
    }

    public static Observable<Integer> getObservableCache() {
        return Observable.<Integer>create(subscriber -> {
            log("create");
            subscriber.onNext(42);
            subscriber.onCompleted();
        }).cache();
    }

    public static void main(String args[]) {

        Observable<Integer> ints = getObservableNoCache();
        Observable<Integer> intsCached = getObservableCache();

        log("Starting");
        ints.subscribe(i -> log("Element A: " + i));
        ints.subscribe(i -> log("Element B: " + i));
        log("Exit");

        System.out.println();
        System.out.println();

        log("Starting");
        intsCached.subscribe(i -> log("Element A: " + i));
        intsCached.subscribe(i -> log("Element B: " + i));
        log("Exit");
    }

}
