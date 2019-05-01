package reactor

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.test

class OnErrorContinueKtTest {

    companion object {
        private val logger = LoggerFactory.getLogger(OnErrorContinueKtTest::class.java)
    }

    @Test
    fun shouldHappyPass() {
        val source = Flux.just(1, 2, 3, 4, 5, 6)
                .map { if (it % 2 == 0) "Element $it" else throw IllegalArgumentException("Element is odd") }
                .doOnNext { logger.info("$it received") }
                .onErrorContinue(
                        { ex: Throwable -> ex is IllegalArgumentException },
                        { ex, elem -> logger.warn("Element $elem skipped, reason: ${ex.message}") })

        source.test()
                .expectNext("Element 2")
                .expectNext("Element 4")
                .expectNext("Element 6")
                .verifyComplete()
    }

    @Test
    fun shouldResultInEmptyFlux() {
        val source = Flux.just(1)
                .map { if (it % 2 == 0) "Element $it" else throw IllegalArgumentException("Element is odd") }
                .doOnNext { logger.info("$it received") }
                .onErrorContinue(
                        { ex: Throwable -> ex is IllegalArgumentException },
                        { ex, elem -> logger.warn("Element $elem skipped, reason: ${ex.message}") })

        source.test()
                .verifyComplete()
    }

    @Test
    fun shouldResultInEmptyMono() {
        val source = Mono.just(1)
                .map { if (it % 2 == 0) "Element $it" else throw IllegalArgumentException("Element is odd") }
                .doOnNext { logger.info("$it received") }
                .onErrorContinue(
                        { ex: Throwable -> ex is IllegalArgumentException },
                        { ex, elem -> logger.warn("Element $elem skipped, reason: ${ex.message}") })

        source.test()
                .verifyComplete()
    }
}