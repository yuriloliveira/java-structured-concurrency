import me.yurioliveira.examples.social.Follower;
import me.yurioliveira.examples.social.Profile;
import me.yurioliveira.helpers.TimeCounter;

import static me.yurioliveira.helpers.Sleep.sleep;
import static me.yurioliveira.helpers.ThreadAwareLogging.*;

@SuppressWarnings("preview")
void main() {
    var counter = TimeCounter.start();
    try (final var scope =  new StructuredTaskScope.ShutdownOnFailure()) {
        Profile profile = new Profile(20);

        scope.fork(() -> {
            log("Loading details...", ANSI_YELLOW);
            Profile.Details details = profile.loadDetails();
            log("Finished after %d ms", ANSI_YELLOW, counter.elapsed().toMillis());
            return details;
        });

        scope.fork(() -> {
            log("Loading follower count...", ANSI_PURPLE);
            Integer followerCount = profile.loadWithError("Follower count");
            log("Finished after %d ms", ANSI_PURPLE, counter.elapsed().toMillis());
            return followerCount;
        });

        scope.fork(() -> {
            log("Loading followers...", ANSI_BLUE);
            List<Follower> followers = profile.loadFollowers();
            log("Finished after %d ms", ANSI_BLUE, counter.elapsed().toMillis());
            return followers;
        });

        scope.join();
        log(scope.exception().orElseGet(() -> new RuntimeException("Not gonna happen")));
    } catch (InterruptedException e) {
        log(e);
    }

    sleep(2500);
}
