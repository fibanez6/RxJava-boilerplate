package com.fibanez.rx.extension;

import rx.Observable;
import rx.Subscriber;

/**
 * Subscriber decides it no longer wants to receive more items, it can unsubscribe itself.
 */
public class Unsubscribe {

    public static void main(String args[]) {

        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("done");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Integer integer) {
                if (integer < 0) {
                    System.out.println("unsubscribed");
                    unsubscribe();
                }
                System.out.println("Number " + integer);
            }
        };

        Observable<Integer> o = Observable.from(new Integer[] {1,2,-1,3});
        o.subscribe(subscriber);
    }
}
