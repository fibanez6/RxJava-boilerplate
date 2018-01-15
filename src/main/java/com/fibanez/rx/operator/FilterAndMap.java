package com.fibanez.rx.operator;

import rx.Observable;

/**
 * First filter() does not remove 9 from Observable.just(8,9,10). Instead, it creates a new Observable that, when
 * subscribe to, will eventually emit values 8 and 10. The same principle applies to most of the operators:
 *      - They do not modify the contents or behaviour of an existing Observable, the create new ones.
 */
public class FilterAndMap {

    public static void main(String args[]) {

        Observable.just(8,9,10)
                .doOnNext(i -> System.out.println("A: "+ i))
                .filter(i -> i % 3 > 0)
                .doOnNext(i -> System.out.println("B: "+ i))
                .map(i -> "#" + i * 10)
                .doOnNext(i -> System.out.println("C: "+ i))
                .filter(s -> s.length() < 4)
                .subscribe(s -> System.out.println("D: "+ s));
    }
}
