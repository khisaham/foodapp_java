package com.foodapp.akka.actors;

import akka.actor.UntypedActor;
import com.foodapp.http.HttpServer;

/**
 * This actor is just responsible for staring the HTTP server
 *
 */
public class HttpServerActor extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        HttpServer.start();
    }
}
