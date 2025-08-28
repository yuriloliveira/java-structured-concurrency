import me.yurioliveira.helpers.Result;
import me.yurioliveira.helpers.TimeCounter;

import static me.yurioliveira.helpers.Sleep.sleep;
import static me.yurioliveira.helpers.ThreadAwareLogging.log;

void main() throws InterruptedException {
    Thread.Builder builder = Thread.ofVirtual().name("vthread", 1);

    var counter = TimeCounter.start();

    Result<String> result1 = Result.notReady();
    Thread thread1 = builder.start(() -> {
        sleep(1000);
        log("Finished after %d ms\n", counter.elapsed().toMillis());
        result1.setValue("Result 1");
    });

    Result<String> result2 = Result.notReady();
    Thread thread2 = builder.start(() -> {
        sleep(500);
        log("Finished after %d ms\n", counter.elapsed().toMillis());
        result2.setValue("Result 2");
    });


    thread1.join();
    thread2.join();

    Result.logAll(result1, result2);
}