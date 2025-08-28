import me.yurioliveira.examples.social.Follower;
import me.yurioliveira.examples.social.Profile;
import me.yurioliveira.helpers.Result;
import me.yurioliveira.helpers.TimeCounter;

import static me.yurioliveira.helpers.ThreadAwareLogging.*;

void main() throws InterruptedException {
    Thread.Builder builder = Thread.ofVirtual().name("outer");

    var counter = TimeCounter.start();

    builder.start(() -> {
        Thread.Builder innerBuilder = Thread.ofVirtual().name("inner", 1);

        Profile profile = new Profile(20);

        Result<Profile.Details> detailsResult = Result.notReady();
        Thread detailsThread = innerBuilder.start(() -> {
            log("Loading details...", ANSI_YELLOW);
            detailsResult.setValue(profile.loadDetails());
            log("Finished after %d ms", ANSI_YELLOW, counter.elapsed().toMillis());
        });

        Result<Integer> followersCountResult = Result.notReady();
        Thread followersCountThread = innerBuilder.start(() -> {
            log("Loading followers count...", ANSI_PURPLE);
            followersCountResult.setValue(profile.loadFollowerCount());
            log("Finished after %d ms", ANSI_PURPLE, counter.elapsed().toMillis());
        });

        Result<List<Follower>> followersResult = Result.notReady();
        Thread followersThread = innerBuilder.start(() -> {
            log("Loading followers...", ANSI_BLUE);
            followersResult.setValue(profile.loadFollowers());
            log("Finished after %d ms", ANSI_BLUE, counter.elapsed().toMillis());
        });

        try {
            detailsThread.join(500);
            log("Gave up on details thread ☠️", ANSI_RED);
            followersCountThread.join(1000);
            log("Gave up on followers count thread ☠️", ANSI_RED);
            followersThread.join(1000);

            Profile.CompleteProfile completeProfile = new Profile.CompleteProfile(
                detailsResult.getValue(),
                followersCountResult.getValue(),
                followersResult.getValue()
            );

            log("%s", ANSI_GREEN, completeProfile);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }).join();
}