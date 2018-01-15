package com.fibanez.rx.operator;

import rx.Observable;

import static rx.Observable.just;
import static rx.Observable.empty;

/**
 * Use FlatMap() fro the following situation:
 *  - The resulkt of transformation in map() must be an Observable. For example: performing long-running, asynchronous
 *  operation on each element of the stream without blocking.
 *
 *  - You need a one-to-many transformations, a single event is expanded into multiple sub-events. For examples, a
 *  stream of customers is translated into stream of their orders, for which each customer can have an arbitrary number
 *  of orders.
 */
public class Flatmap {

    enum Sound { DI, DAH}

    public static Observable<Sound> toMorseCode(char ch) {
        switch (ch) {
            case 'a': return just(Sound.DI, Sound.DAH);
            case 'b': return just(Sound.DAH, Sound.DI, Sound.DI, Sound.DI);
            case 'c': return just(Sound.DAH, Sound.DI, Sound.DAH, Sound.DI);
            // ..
            case 'p': return just(Sound.DI, Sound.DAH, Sound.DAH, Sound.DI);
            case 'r': return just(Sound.DI, Sound.DAH, Sound.DI);
            case 's': return just(Sound.DI, Sound.DI, Sound.DI);
            case 't': return just(Sound.DAH);
            // ...
            default: return empty();
        }
    }

    public static void main(String args[]) {

        just('S','p','a','R','t','a')
                .map(Character::toLowerCase)
                .doOnNext(s -> System.out.println(s))
                .flatMap(Flatmap::toMorseCode)
                .subscribe(sound -> System.out.println("sound: "+ sound));
    }
}
