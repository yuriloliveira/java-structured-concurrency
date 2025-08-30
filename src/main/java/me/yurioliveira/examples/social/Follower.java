package me.yurioliveira.examples.social;

import static me.yurioliveira.examples.social.FakerInstance.FAKER;
import static me.yurioliveira.helpers.Sleep.sleep;
import static me.yurioliveira.helpers.ThreadAwareLogging.ANSI_BLUE;
import static me.yurioliveira.helpers.ThreadAwareLogging.log;

public record Follower(String username) {
    public static Follower load() {
        sleep(250);
        log("Loaded follower", ANSI_BLUE);
        return new Follower(
            FAKER.bojackHorseman().characters().replaceAll("[^a-zA-Z]", "").toLowerCase()
        );
    }

    @Override
    public String toString() {
        return username;
    }
}
