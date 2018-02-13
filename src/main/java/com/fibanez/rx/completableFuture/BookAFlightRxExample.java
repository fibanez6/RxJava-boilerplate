package com.fibanez.rx.completableFuture;

import org.apache.commons.lang3.tuple.Pair;
import rx.Observable;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class BookAFlightRxExample {

    private class User {}
    private class GeoLocation {}
    private class Flight {}
    private class Ticket {}

    interface TravelAgency {
        Observable<Flight> rxSearch(User user, GeoLocation geoLocation);
    }
//    private class OnlineAgency implements TravelAgency {
//        @Override
//        public Observable<Flight> rxSearch(User user, GeoLocation geoLocation) {
//            return Observable.just(new Flight());
//        }
//    }
    private TravelAgency onlineAgency = (user,geoLocation) ->  Observable.just(new Flight());

    private User findById(long id) {
        return new User();
    }
    private Ticket book(Flight flight) {
        return new Ticket();
    }

    private CompletableFuture<User> findByIdAsync(long id) {
        return CompletableFuture.supplyAsync(() -> findById(id));
    }

    private CompletableFuture<GeoLocation> locateAsync() {
        return CompletableFuture.supplyAsync(GeoLocation::new);
    }

    private CompletableFuture<Ticket> bookAsync(Flight flight) {
        return CompletableFuture.supplyAsync(() -> book(flight));
    }

    // Observables layer
    private Observable<User> rxFindById(long id) {
        return Util.observe(findByIdAsync(id));
    }
    private Observable<GeoLocation> rxLocate() {
        return Util.observe(locateAsync());
    }
    private Observable<Ticket> rxBook(Flight flight) {
        return Util.observe(bookAsync(flight));
    }


    // Specific entities
    private Observable<User> user = rxFindById(1l);
    private Observable<GeoLocation> location = rxLocate();
    private Observable<TravelAgency> agencies = Observable.from(Arrays.asList(onlineAgency));

    // the interesting one
    private Observable<Ticket> ticket = user
            .zipWith(location, (User us , GeoLocation loc) ->
                agencies
                    .flatMap(agency -> agency.rxSearch(us, loc))        // Observable<Observable<Flight>>
                    .first()
            )
            .flatMap(x -> x)                                            // Observable<Flight>
            .flatMap(this::rxBook);                                     // (flight -> bookAsync(flight)

    // ALTERNATIVE
    private Observable<Ticket> ticket2 = user
            .zipWith(location, (us , loc) -> Pair.of(us,loc))           // Observable<Pair<User, GeoLocation>>
            .flatMap(pair -> agencies
                    .flatMap(agency -> {
                        User usr = pair.getLeft();
                        GeoLocation loc = pair.getRight();
                        return agency.rxSearch(usr, loc);               // Observable<Flight>
                    })
                    .first()
            )
            .flatMap(this::rxBook);
}
