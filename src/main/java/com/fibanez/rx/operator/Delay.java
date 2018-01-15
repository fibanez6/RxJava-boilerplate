package com.fibanez.rx.operator;

import rx.Observable;

import java.util.concurrent.TimeUnit;

import static rx.Observable.timer;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Postponing Events using the delay() operator
 */
public class Delay {

    public static void main(String args[]) throws InterruptedException {

       Observable
               .just("Loren","ipsum","dolor","sit","amet","consectetur","adipiscing","elit")
               .delay(word -> timer(word.length(), SECONDS))
               .subscribe(System.out::println);

        TimeUnit.SECONDS.sleep(15);

        System.out.println();
        System.out.println("equivalent using flatMap()");
        System.out.println();

        Observable
                .just("Loren","ipsum","dolor","sit","amet","consectetur","adipiscing","elit")
                .flatMap(word ->
                        timer(word.length(), SECONDS).map(x -> word))
                .subscribe(System.out::println);

        TimeUnit.SECONDS.sleep(15);

    }
}
