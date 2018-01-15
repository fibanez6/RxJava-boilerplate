package com.fibanez.rx.extension;

import rx.Observable;

/**
 * The important thing to understand here is that most Observable function pipelines are synchronous.
 */
public class UnsafeCreate {

    public static void main(String args[]) {

        Observable<Integer> o = Observable.unsafeCreate(s -> {
            s.onNext(1);
            s.onNext(2);
            s.onNext(3);
            s.onCompleted();
        });

        o.map(i -> "Number " + i).subscribe(System.out::println);
    }
}
