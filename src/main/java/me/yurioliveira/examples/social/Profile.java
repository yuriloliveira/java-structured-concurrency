package me.yurioliveira.examples.social;

import java.util.List;
import java.util.stream.IntStream;

import static me.yurioliveira.helpers.Sleep.sleep;

public class Profile {
    private final int followerCount;

    public Profile(int followerCount) {
        this.followerCount = followerCount;
    }

    public Details loadDetails() {
        sleep(1000);
        return new Details("johnd", "John Doe");
    }

    public Integer loadFollowerCount() {
        sleep(500);
        return followerCount;
    }

    public List<Follower> loadFollowers() {
        return IntStream
            .range(0, followerCount)
            .mapToObj(_ -> Follower.load())
            .toList();

    }

    public record Details(String username, String fullName) {}

    public record CompleteProfile(Details details, Integer followerCount, List<Follower> followers) {}
}
