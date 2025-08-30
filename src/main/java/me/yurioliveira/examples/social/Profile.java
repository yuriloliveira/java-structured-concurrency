package me.yurioliveira.examples.social;

import java.util.List;
import java.util.stream.IntStream;

import static me.yurioliveira.helpers.Sleep.sleep;
import static me.yurioliveira.helpers.ThreadAwareLogging.ANSI_RED;
import static me.yurioliveira.helpers.ThreadAwareLogging.log;

public record Profile(int followerCount) {

    public Details loadDetails() {
        sleep(2000);
        return new Details("johnd", "John Doe");
    }

    public Integer loadFollowerCount() {
        sleep(1000);
        return followerCount;
    }

    public List<Follower> loadFollowers() {
        return IntStream
                .range(0, followerCount)
                .mapToObj(_ -> Follower.load())
                .takeWhile(_ -> !Thread.interrupted())
                .toList();
    }

    public <T> T loadWithError(String resource) {
        sleep(1000);
        var error = new ProfileException(resource + " could not be loaded 😓");
        log("Error: %s", ANSI_RED, error.getMessage());
        throw error;
    }

    public record Details(String username, String fullName) {}

    public record CompleteProfile(Details details, Integer followerCount, List<Follower> followers) {}
}
