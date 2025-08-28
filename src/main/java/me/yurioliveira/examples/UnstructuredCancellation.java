import me.yurioliveira.helpers.Result;
import me.yurioliveira.helpers.TimeCounter;

import static me.yurioliveira.helpers.Sleep.sleep;
import static me.yurioliveira.helpers.ThreadAwareLogging.log;

void main() throws InterruptedException {
    Thread.Builder builder = Thread.ofVirtual().name("outer");

    var counter = TimeCounter.start();

    builder.start(() -> {
        Thread.Builder innerBuilder = Thread.ofVirtual().name("inner", 1);

        final Result<String> result1 = Result.notReady();
        Thread thread1 = innerBuilder.start(() -> {
           IntStream
               .range(0, 10)
               .forEach(el -> {
                   log("Processing %d%% done\n", el*10);
                   sleep(500L);
               });
           result1.setValue("Result 1");
           log("Finished after %d ms\n", counter.elapsed().toMillis());
        });


        Result<String> result2 = Result.notReady();
        Thread thread2 = innerBuilder.start(() -> {
           sleep(2000L);
           log("Finished after %d ms\n", counter.elapsed().toMillis());
           result2.setValue("Result 2");
        });


        try {
            thread1.join(500);
            log("Gave up on thread 1...\n");
            thread2.join(2500);

            Result.logAll(result1, result2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }).join();
}