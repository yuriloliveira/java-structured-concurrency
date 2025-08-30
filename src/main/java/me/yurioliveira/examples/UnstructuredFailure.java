import me.yurioliveira.examples.social.Follower;
import me.yurioliveira.examples.social.Profile;
import me.yurioliveira.helpers.Result;
import me.yurioliveira.helpers.TimeCounter;

import static me.yurioliveira.helpers.ThreadAwareLogging.*;

void main() throws InterruptedException {
    Thread.Builder builder = Thread.ofVirtual().name("outer");

    var counter = TimeCounter.start();

    builder.start(() -> {
        Profile profile = new Profile(20);

        Result<Profile.Details> detailsResult = Result.notReady();
        Thread detailsThread = builder.start(() -> {
            log("Loading details...", ANSI_YELLOW);
            detailsResult.set(profile.loadWithError("Profile details"));
            log("Finished after %d ms", ANSI_YELLOW, counter.elapsed().toMillis());
        });

        Result<Integer> followersCountResult = Result.notReady();
        Thread followersCountThread = builder.start(() -> {
            log("Loading followers count...", ANSI_PURPLE);
            followersCountResult.set(profile.loadFollowerCount());
            log("Finished after %d ms", ANSI_PURPLE, counter.elapsed().toMillis());
        });

        Result<List<Follower>> followersResult = Result.notReady();
        Thread followersThread = builder.start(() -> {
            log("Loading followers...", ANSI_BLUE);
            followersResult.set(profile.loadFollowers());
            log("Finished after %d ms", ANSI_BLUE, counter.elapsed().toMillis());
        });

        try {
            detailsThread.join();
            followersCountThread.join();
            followersThread.join();

            Profile.CompleteProfile completeProfile = new Profile.CompleteProfile(
                detailsResult.get(),
                followersCountResult.get(),
                followersResult.get()
            );
            completeProfile.log();
        } catch (InterruptedException e) {
            log(e);
        }

    }).join();
}