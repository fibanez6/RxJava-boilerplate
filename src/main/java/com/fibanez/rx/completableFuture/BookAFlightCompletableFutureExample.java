package com.fibanez.rx.completableFuture;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class BookAFlightCompletableFutureExample {

    private class User {}
    private class GeoLocation {}
    private class Flight {}
    private class Ticket {}

    interface TravelAgency {
        CompletableFuture<Flight> searchAsync(User user, GeoLocation geoLocation);
    }
//    private class OnlineAgency implements TravelAgency {
//        @Override
//        public CompletableFuture<Flight> searchAsync(User user, GeoLocation geoLocation) {
//            return CompletableFuture.supplyAsync(Flight::new);
//        }
//    }
    private TravelAgency onlineAgency = (user,geoLocation) -> CompletableFuture.supplyAsync(Flight::new);
    private List<TravelAgency> agencies = Arrays.asList(onlineAgency);

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

    // Specific entities
    private CompletableFuture<User> user = findByIdAsync(1l);
    private CompletableFuture<GeoLocation> location = locateAsync();

    // the interesting one
    private CompletableFuture<Ticket> ticketFuture = user
            .thenCombine(location, (User us , GeoLocation loc) -> agencies
                .stream()
                .map(agency -> agency.searchAsync(us, loc))         // list<CompletableFuture<Flight>>
                .reduce((f1,f2) ->
                    f1.applyToEither(f2, Function.identity())       // it only cares about the first one to complete
                )
                .get()                                              // CompletableFuture<CompletableFuture<Flight>>
            )
            .thenCompose(Function.identity())                       // CompletableFuture<Flight>
            .thenCompose(this::bookAsync);                          // (flight -> bookAsync(flight)

}
