import me.yurioliveira.examples.social.Follower;
import me.yurioliveira.examples.social.Profile;
import me.yurioliveira.helpers.TimeCounter;

import static me.yurioliveira.helpers.ThreadAwareLogging.*;

@SuppressWarnings("preview")
void main() {
    var counter = TimeCounter.start();
    try (final var scope =  new StructuredTaskScope.ShutdownOnFailure()) {
        Profile profile = new Profile(10);

        StructuredTaskScope.Subtask<Profile.Details> detailsTask = scope.fork(() -> {
            log("Loading details...", ANSI_YELLOW);
            Profile.Details details = profile.loadDetails();
            log("Finished after %d ms", ANSI_YELLOW, counter.elapsed().toMillis());
            return details;
        });

        StructuredTaskScope.Subtask<Integer> followersCountTask = scope.fork(() -> {
            log("Loading follower count...", ANSI_PURPLE);
            Integer followerCount = profile.loadFollowerCount();
            log("Finished after %d ms", ANSI_PURPLE, counter.elapsed().toMillis());
            return followerCount;
        });

        StructuredTaskScope.Subtask<List<Follower>> followersTask = scope.fork(() -> {
            log("Loading followers...", ANSI_BLUE);
            List<Follower> followers = profile.loadFollowers();
            log("Finished after %d ms", ANSI_BLUE, counter.elapsed().toMillis());
            return followers;
        });

        scope.join();
        Profile.CompleteProfile completeProfile = new Profile.CompleteProfile(
            detailsTask.get(),
            followersCountTask.get(),
            followersTask.get()
        );
        completeProfile.log();
    } catch (InterruptedException e) {
        log("Error: %s", ANSI_RED, e.getMessage());
    }
}
