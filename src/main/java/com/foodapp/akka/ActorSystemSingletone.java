package com.foodapp.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.foodapp.akka.actors.MainActor;
import com.foodapp.akka.messages.StartHttpServerActorMessage;
import com.typesafe.config.ConfigFactory;

/**
 * @author khisahamphrey
 */
public class ActorSystemSingletone {
    public static final String SYSTEM_NAME = "foodapp_server";
    public static ActorRef mainActorRef = null;

    public static void init() {
        final ActorSystem system =ActorSystem.create(SYSTEM_NAME, ConfigFactory.load(("foodapp_server")));
        mainActorRef = system.actorOf(Props.create(MainActor.class), MainActor.NAME);

        sendMessage(new StartHttpServerActorMessage(), null);
        //MainActor.startScheduledTasks(system, mainActorRef);
    }

    public static boolean sendMessage(Object message, ActorRef sender) {
        if(mainActorRef != null) {
            mainActorRef.tell(message, sender);
            return true;
        }
        return false;
    }
}
