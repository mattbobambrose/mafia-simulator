import PlayerType.*

class MafiaSimulator {
}

fun main() {
    var selectedForDeath: Player
    var lastPersonSaved: Player
    var detectiveCameOut = false
    var knownDetective: Player

    val config = GameConfig(1, 1, 1, 5)

    var players = config.create()

    val mafia = players.filter { it.playerType == MAFIA }
    val angel = players.filter { it.playerType == ANGEL }
    val detective = players.filter { it.playerType == DETECTIVE }

}

fun List<Player>.ensureNonZero(vararg types: PlayerType) {
    for (type in types) {
        if (count { it.playerType == type } == 0) error("No $type")
    }
}

fun wakeUp(selectedForDeath: Player, lastPersonSaved: Player) {

}

data class GameConfig(
    val numberOfMafia: Int,
    val numberOfAngel: Int,
    val numberOfDetective: Int,
    val numberOfVillager: Int,
) {

    fun create(): MutableList<Player> =
        buildList<Player> {
            addAll(List(numberOfMafia) { Mafia() })
            addAll(List(numberOfAngel) { Angel() })
            addAll(List(numberOfDetective) { Detective() })
            addAll(List(numberOfVillager) { Villager() })

            ensureNonZero(MAFIA, ANGEL, DETECTIVE, VILLAGER)
        }.toMutableList()
}