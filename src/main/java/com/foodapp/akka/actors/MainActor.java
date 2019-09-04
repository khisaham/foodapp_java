package com.foodapp.akka.actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import com.foodapp.akka.messages.StartHttpServerActorMessage;

/**
 *
 */
public class MainActor extends UntypedActor {
    public static final String NAME = "MainActor";

    @Override
    public void onReceive(Object message) throws Exception {
       if (message instanceof StartHttpServerActorMessage) {
            getContext().actorOf(Props.create(HttpServerActor.class)).tell(message, getSelf());
        }
    }
}
