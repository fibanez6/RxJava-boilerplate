package com.fibanez.rx.extension;

import rx.Observable;
import rx.Subscription;

import java.util.concurrent.TimeUnit;

public class TimerAndInterval {

    private static void log(Object msg) {
        System.out.println(Thread.currentThread().getName() +": "+msg);
    }

    public static void main(String args[]) throws InterruptedException {

        Subscription s1 = Observable
                .timer(1, TimeUnit.SECONDS)
                .subscribe((Long i) -> log(i));

        Subscription s2 = Observable
                .interval(500, TimeUnit.MILLISECONDS)
                .subscribe(i -> log(i));


        TimeUnit.SECONDS.sleep(5);

        s2.unsubscribe();
    }
}
