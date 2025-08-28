import me.yurioliveira.helpers.TimeCounter;

import static me.yurioliveira.helpers.Sleep.sleep;
import static me.yurioliveira.helpers.ThreadAwareLogging.*;

void main() throws InterruptedException {
    Thread.Builder builder = Thread.ofVirtual().name("outer");

    var counter = TimeCounter.start();

    builder.start(() -> {
        Thread.Builder innerBuilder = Thread.ofVirtual().name("inner", 1);

        Thread thread1 = innerBuilder.start(() -> {
           IntStream
               .range(0, 10)
               .forEach(el -> {
                   log("Processing element %d\n", ANSI_YELLOW, el);
                   sleep(500L);
               });
           log("Finished after %d ms\n", ANSI_YELLOW, counter.elapsed().toMillis());
        });

        Thread thread2 = innerBuilder.start(() -> {
           sleep(2000L);
           log("Failed after %d ms\n", ANSI_PURPLE, counter.elapsed().toMillis());
           throw new RuntimeException("Something went wrong");
        });


        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }).join();
}