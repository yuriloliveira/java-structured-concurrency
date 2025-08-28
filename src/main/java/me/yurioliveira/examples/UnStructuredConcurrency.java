import me.yurioliveira.examples.social.Follower;
import me.yurioliveira.examples.social.Profile;
import me.yurioliveira.helpers.Result;
import me.yurioliveira.helpers.TimeCounter;

import static me.yurioliveira.helpers.ThreadAwareLogging.*;

void main() throws InterruptedException {
    Thread.Builder builder = Thread.ofVirtual().name("vthread", 1);

    var counter = TimeCounter.start();

    Profile profile = new Profile(10);

    Result<Profile.Details> detailsResult = Result.notReady();
    Thread detailsThread = builder.start(() -> {
//        sleep(1000);
        detailsResult.setValue(profile.loadDetails());
        log("Finished after %d ms", ANSI_YELLOW, counter.elapsed().toMillis());
    });

    Result<Integer> followersCountResult = Result.notReady();
    Thread followersCountThread = builder.start(() -> {
//        sleep(500);
        followersCountResult.setValue(profile.loadFollowerCount());
        log("Finished after %d ms", ANSI_PURPLE, counter.elapsed().toMillis());
    });

    Result<List<Follower>> followersResult = Result.notReady();
    Thread followersThread = builder.start(() -> {
        followersResult.setValue(profile.loadFollowers());
        log("Finished after %d ms", ANSI_BLUE, counter.elapsed().toMillis());
    });

    detailsThread.join();
    followersCountThread.join();
    followersThread.join();

    Result.logAll(detailsResult, followersCountResult, followersResult);
}