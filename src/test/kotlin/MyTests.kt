import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.startWith

class MyTests : StringSpec({
    "length should return size of string" {
        "hello".length shouldBe 5
    }
    "startsWith should test for a prefix" {
        "world" should startWith("wor")
    }

    "check config sizes" {
        val config = GameConfig(2, 1, 1, 5)
        val players = config.create()

        players.size shouldBe config.size
    }

    fun zeroTypeMsgs() = PlayerType.values().map { "No $it" }

    fun wrongNumber() = PlayerType.values().map { "Wrong number of $it" }

    "invalid mafia config sizes" {
        val exception =
            shouldThrow<IllegalStateException> {
                GameConfig(0, 1, 1, 5).create()
            }
        exception.message shouldBeIn zeroTypeMsgs()
    }

    "invalid villager config sizes" {
        val exception =
            shouldThrow<IllegalStateException> {
                GameConfig(2, 1, 1, 0).create()
            }
        exception.message shouldBeIn zeroTypeMsgs()
    }

    "invalid angel config sizes" {
        val exception =
            shouldThrow<IllegalStateException> {
                GameConfig(2, 0, 1, 5).create()
            }
        exception.message shouldBeIn wrongNumber()
    }

    "invalid detective config sizes" {
        val exception =
            shouldThrow<IllegalStateException> {
                GameConfig(2, 1, 0, 5).create()
            }
        exception.message shouldBeIn wrongNumber()
    }
})