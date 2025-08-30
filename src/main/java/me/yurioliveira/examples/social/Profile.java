package me.yurioliveira.examples.social;

import me.yurioliveira.helpers.ThreadAwareLogging;

import java.util.List;
import java.util.stream.IntStream;

import static me.yurioliveira.examples.social.FakerInstance.FAKER;
import static me.yurioliveira.helpers.Sleep.sleep;
import static me.yurioliveira.helpers.ThreadAwareLogging.*;


public record Profile(int followerCount) {

    public Details loadDetails() {
        sleep(2000);

        var name = FAKER.rickAndMorty().character();
        return new Details(
            name.replaceAll("[^a-zA-Z]", "").toLowerCase(),
            name
        );
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
        log(error);
        throw error;
    }

    public record Details(String username, String fullName) {}

    public record CompleteProfile(Details details, Integer followerCount, List<Follower> followers) {
        public void log() {
            var detailsColor = details != null ? ANSI_GREEN : ANSI_RED;
            var followerCountColor = followerCount != null ? ANSI_GREEN : ANSI_RED;
            var followersColor = followers != null ? ANSI_GREEN : ANSI_RED;

            ThreadAwareLogging.log(
                "CompleteProfile[%sdetails=%s%s, %sfollowerCount=%s%s, %sfollowers=%s%s]",
                ANSI_GREEN,
                detailsColor,
                details,
                ANSI_GREEN,
                followerCountColor,
                followerCount,
                ANSI_GREEN,
                followersColor,
                followers,
                ANSI_GREEN
            );
        }
    }
}
