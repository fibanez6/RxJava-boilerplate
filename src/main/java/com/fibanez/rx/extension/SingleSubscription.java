package com.fibanez.rx.extension;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * Single handler to the underlying resource; for example, HTTP connection to stream of Twitter status updates.
 *
 * version 1:
 *  Each Subscriber establishes a new connection.
 *
 * publish().refCount() version:
 *  The connection is not established until we get the first Subscriber. But, more important, the second subscriber does
 *  not initiate a new connection, it does not even touch the original Observable.
 *
 *      refCount: operator which count how many active Subscribers there are.
 */
public class SingleSubscription {

    private static void log(Object msg) {
        System.out.println(Thread.currentThread().getName() +": "+msg);
    }

    private static Observable createObservable() {
        return Observable.create(
                subscriber -> {
                    System.out.println("Created");
                    subscriber.add(Subscriptions.create( () -> System.out.println("terminated")));
                });
    }

    public static void printer(Observable observable) {
        Subscription sub1 = observable.map(l -> "A number "+ l).subscribe(s -> log(s));
        System.out.println("Subscribed A");

        Subscription sub2 = observable.map(l -> "B number "+ l).subscribe(s -> log(s));
        System.out.println("Subscribed B");

        sub1.unsubscribe();
        System.out.println("Unsubscribed A");
        sub2.unsubscribe();
        System.out.println("Unsubscribed B");
    }

    public static void main(String args[]) {

        System.out.println("observable with its own resources");
        System.out.println("----");
        Observable multiple = createObservable();
        printer(multiple);

        System.out.println();
        System.out.println();
        System.out.println("publish().refCount() with sharing resources");
        System.out.println("----");

        Observable lazy = multiple.publish().refCount();
        printer(lazy);
    }
}
