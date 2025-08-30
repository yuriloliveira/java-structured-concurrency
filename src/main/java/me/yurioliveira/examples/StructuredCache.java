import me.yurioliveira.examples.social.Follower;
import me.yurioliveira.examples.social.Profile;
import me.yurioliveira.examples.social.ProfileException;
import me.yurioliveira.helpers.Result;
import me.yurioliveira.helpers.TimeCounter;

import static me.yurioliveira.helpers.Sleep.sleep;
import static me.yurioliveira.helpers.ThreadAwareLogging.*;


@SuppressWarnings("preview")
void main() {
    var counter = TimeCounter.start();

    Result<Profile.CompleteProfile> cachedProfile = Result.notReady();

    try (var scan = new Scanner(System.in)) {

        do {
            try (final var scope = new StructuredTaskScope.ShutdownOnSuccess<Profile.CompleteProfile>()) {

                scope.fork(() -> getCompleteProfileFromCahe(cachedProfile));

                scope.fork(() -> {
                    cachedProfile.setIfNotNull(loadCompleteProfile(counter));
                    return cachedProfile.get();
                });

                scope.join();
                scope.result().log();
            } catch (ExecutionException | InterruptedException e) {
                log(e);
            }

        } while (shouldContinue(scan));
    }

}

private static Profile.CompleteProfile getCompleteProfileFromCahe(Result<Profile.CompleteProfile> cachedProfile) {
    sleep(500);
    if (cachedProfile.get() != null) {
        log("Profile was cached 🤙", ANSI_WHITE);
    }
    return Optional
            .ofNullable(cachedProfile.get())
            .orElseThrow(() -> new ProfileException("Cached profile not found"));
}

private boolean shouldContinue(Scanner scan) {
    System.out.println("Run again? (Yes = 'y' ; No = anything else)");
    return scan.next().equalsIgnoreCase("y");
}

@SuppressWarnings("preview")
private static Profile.CompleteProfile loadCompleteProfile(TimeCounter counter) {
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
        return new Profile.CompleteProfile(
            detailsTask.get(),
            followersCountTask.get(),
            followersTask.get()
        );
    } catch (InterruptedException e) {
        log("Loading complete profile was interrupted", ANSI_WHITE);
    }

    return null;
}
