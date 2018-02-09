package com.fibanez.rx.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


public class HttpInitializer extends ChannelInitializer<SocketChannel>{

    private final HttpHandler httpHandler = new HttpHandler();

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new HttpServerCodec())
                .addLast(httpHandler);
    }
}
