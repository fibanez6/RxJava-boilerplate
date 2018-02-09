package com.fibanez.rx.rxNetty;


import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import io.reactivex.netty.protocol.tcp.server.TcpServer;
import rx.Observable;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class EurUsdCurrencyTcpServer {

    private static final BigDecimal RATE = new BigDecimal("1.06448");
    private static final Random rnd = new Random();

    public static void main(final String[] args) {
        TcpServer
                .newServer(8080)
                .<String, String> pipelineConfigurator(pipeline -> {
                    pipeline.addLast(new LineBasedFrameDecoder(1024));
                    pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                })
                .start(connection -> {
                    Observable<String> output = connection
                            .getInput()
                            .filter( s -> s != null && !s.isEmpty())
                            .map(BigDecimal::new)
                            .flatMap(eur -> eurToUsd(eur));
                    return connection.writeAndFlushOnEach(output);
                })
                .awaitShutdown();
    }

    static Observable<String> eurToUsd(BigDecimal eur) {
        return Observable
                .just(eur.multiply(RATE))
                .map(amount -> eur + " EUR is " + amount + " USD\n")
                .delay(rnd.nextInt(3), TimeUnit.SECONDS);
    }
}
