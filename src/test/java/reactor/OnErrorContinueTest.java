package reactor;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class OnErrorContinueTest {

    private static final Logger logger = LoggerFactory.getLogger(OnErrorContinueTest.class);

    @Test
    void shouldHappyPass() {
        Flux<String> source = Flux.just(1, 2, 3, 4, 5, 6)
                .map(elem -> {
                    if (elem % 2 == 0) {
                        return "Element " + elem;
                    } else {
                        throw new IllegalArgumentException("Element is odd");
                    }
                })
                .doOnNext(elem -> logger.info(elem + " received"))
                .onErrorContinue(
                        ex -> ex instanceof IllegalArgumentException,
                        (ex, elem) -> logger.warn("Element " + elem + " skipped, reason: " + ex.getMessage()));

        StepVerifier.create(source)
                .expectNext("Element 2")
                .expectNext("Element 4")
                .expectNext("Element 6")
                .verifyComplete();
    }

    @Test
    void shouldResultInEmptyFlux() {
        Flux<String> source = Flux.just(1)
                .map(elem -> {
                    if (elem % 2 == 0) {
                        return "Element " + elem;
                    } else {
                        throw new IllegalArgumentException("Element is odd");
                    }
                })
                .doOnNext(elem -> logger.info(elem + " received"))
                .onErrorContinue(
                        ex -> ex instanceof IllegalArgumentException,
                        (ex, elem) -> logger.warn("Element " + elem + " skipped, reason: " + ex.getMessage()));

        StepVerifier.create(source)
                .verifyComplete();
    }

    @Test
    void shouldResultInEmptyMono() {
        Mono<String> source = Mono.just(1)
                .map(elem -> {
                    if (elem % 2 == 0) {
                        return "Element " + elem;
                    } else {
                        throw new IllegalArgumentException("Element is odd");
                    }
                })
                .doOnNext(elem -> logger.info(elem + " received"))
                .onErrorContinue(
                        ex -> ex instanceof IllegalArgumentException,
                        (ex, elem) -> logger.warn("Element " + elem + " skipped, reason: " + ex.getMessage()));

        StepVerifier.create(source)
                .verifyComplete();
    }
}
