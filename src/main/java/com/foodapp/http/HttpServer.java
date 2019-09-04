package com.foodapp.http;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.internal.SystemPropertyUtil;

import java.util.Locale;

public class HttpServer  {


    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", SSL? "8081" : "8080"));
    private final static int BOSS_THREADS = 1;
    private final static int MAX_WORKER_THREADS = 12;

    public static void start() throws Exception {
        System.out.println("Starting the HTTP(S) server");
        /// Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        // Check if we are running on Mac OS (especially if in the development environment)
        final boolean isMac = System.getProperty("os.name").toLowerCase(Locale.US).contains("mac");

        // Configure the server.
        // See https://netty.io/wiki/native-transports.html
        EventLoopGroup bossGroup;
        EventLoopGroup workerGroup;

        if (isMac) {
            bossGroup = new io.netty.channel.kqueue.KQueueEventLoopGroup();
            workerGroup = new io.netty.channel.kqueue.KQueueEventLoopGroup();
        } else {
            bossGroup =  new io.netty.channel.epoll.EpollEventLoopGroup();
            workerGroup = new io.netty.channel.epoll.EpollEventLoopGroup();
        }

        try {
            ServerBootstrap b = new ServerBootstrap();
            /** tune then PooledByteBufAllocator used in stead of creating and cleaning everytime*/
            b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.option(ChannelOption.SO_BACKLOG, 512 * 1024);
            b.childOption(ChannelOption.SO_KEEPALIVE, true);
            Class<KQueueServerSocketChannel> kqueueClass = KQueueServerSocketChannel.class;
            Class<EpollServerSocketChannel> epollClass = EpollServerSocketChannel.class;
            b.group(bossGroup, workerGroup)
                    .channel(isMac ? kqueueClass : epollClass)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpServerInitializer(sslCtx));

            Channel ch = b.bind(PORT).sync().channel();

            System.err.println("Open your web browser and navigate to " +
                    (SSL? "https" : "http") + "://127.0.0.1:" + PORT + '/');

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private static int calculateThreadCount() {
        int threadCount;
        if ((threadCount = SystemPropertyUtil.getInt("io.netty.eventLoopThreads", 0)) > 0) {
            return threadCount;
        } else {
            threadCount = Runtime.getRuntime().availableProcessors() * 2;
            return threadCount > MAX_WORKER_THREADS ? MAX_WORKER_THREADS : threadCount;
        }
    }
}