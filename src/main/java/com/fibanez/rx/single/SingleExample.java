package com.fibanez.rx.single;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import rx.Single;
import rx.schedulers.Schedulers;


/**
 * Return only one value.
 * It is lazy and async.
 * WHEN USE SINGLE
 *      - An operation must complete with some particular value or an exception. For example,
 *      calling a web service always result with either a response from an external server or
 *      some sort of exception.
 *      - There is not such thing as a stream in your problem domain.
 *      - Observable is  too  heavyweight and  you  measured  that  Single  is faster in your
 *      particular problem.
 *
 * WHEN USE OBSERVABLE
 *      - Your  model  some sort of events (messages,  GUI events)  which are  by  definition
 *      occurring several times, possibly infinite.
 *      - Or entirely the opposite, you expect  the value  to occur  or not before completion.
 */
public class SingleExample {

    interface Document{}

    private JdbcTemplate getJdbcTemplate() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        return new JdbcTemplate(dataSource);
    }

    private JdbcTemplate jbdcTemplate = getJdbcTemplate();

    private Single<String> content(int id) {
        return Single.fromCallable( () -> jbdcTemplate
                .queryForObject(
                    "SELECT content FROM articles WHERE id - ?" ,
                        String.class, id))
                .subscribeOn(Schedulers.io());
    }

    private Single<Integer> likes(int id) {
        // async HTTP request to social media website
        return Single.just(0);
    }

    private Single<Void> updateReadCount() {
        // only side efect, no return value in Single
        return Single.just(null);
    }

    // Interesting one
    private Single<Document> doc = Single.zip(
            content(123),
            likes(123),
            updateReadCount(),
            (con, lks, vod) -> builtHtml(con, lks)
    );


    private Document builtHtml(String content, int likes) {
        return new Document(){};
    }
}
