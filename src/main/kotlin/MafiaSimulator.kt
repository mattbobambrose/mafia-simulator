import PlayerType.*

fun main() {
    runSim()
}

fun runSim() {
    var selectedForDeath: Player
    var lastPersonSaved: Player? = null
    var knownDetective: Player? = null

    val config = GameConfig(2, 1, 1, 5)

    var players = config.create()

    val mafia = players.filter { it.playerType == MAFIA }.toMutableList()
    val angel = players.find { it.playerType == ANGEL }
    val detective = players.find { it.playerType == DETECTIVE }

    while (mafia.isNotEmpty() && mafia.size < players.size - mafia.size) {
        selectedForDeath = mafia[0].kill(players)
        //if (angel)
        lastPersonSaved = angel?.save(players, lastPersonSaved, knownDetective)
        detective?.investigate(players)
        wakeUp(selectedForDeath, lastPersonSaved)
        detective?.comeOut(players, config)
        if (checkIfVotingIsAGoodIdea(players, config))
            players.removeAt(initiateVoting(players))
    }
}

fun List<Player>.ensureNonZero(vararg types: PlayerType) {
    types.forEach { type ->
        if (count { it.playerType == type } == 0) error("No $type")
    }
}

fun List<Player>.ensureSingle(vararg types: PlayerType) {
    types.forEach { type ->
        if (count { it.playerType == type } != 1) error("Wrong number of $type")
    }
}

fun wakeUp(selectedForDeath: Player, lastPersonSaved: Player?) {
    if (selectedForDeath == lastPersonSaved) {
        selectedForDeath.publicGuilt = PublicGuiltType.P_INNOCENT
        selectedForDeath.detectiveGuilt = DetectiveGuiltType.D_INNOCENT
    }
}


fun checkIfVotingIsAGoodIdea(players: MutableList<Player>, config: GameConfig): Boolean {
    val innocents = players.filter { it.publicGuilt == PublicGuiltType.P_INNOCENT }
    val mafiaMembers = players.filter { it.publicGuilt == PublicGuiltType.P_MAFIA }
    val unknowns = players.filter { it.publicGuilt == PublicGuiltType.P_UNKNOWN }

    //s1 is if we have found any mafia
    val s1 = mafiaMembers.isNotEmpty()
    //s2 is the  number of unknowns is the same as the number of mafia a.k.a. all innocents have been identified
    var s2 = unknowns.size == players.size - innocents.size
    //s3 is where we don't know any mafia and we know the number of surviving mafia is one less than half the remaining village a.k.a. the villagers have to vote
    var s3 = players.size == config.numberOfMafia * 2 + 1
    //I'll add more probabilities as numbers clarify
    return s1 || s2 || s3
}

fun initiateVoting(players: MutableList<Player>): Int {
    val candidates = players.onEach { it.vote(players) }
    //return candidates.groupingBy { it }.eachCount().maxOf { it.value }.
    return 0
}

data class GameConfig(
    val numberOfMafia: Int,
    val numberOfAngel: Int,
    val numberOfDetective: Int,
    val numberOfVillager: Int,
) {

    val size = numberOfMafia + numberOfAngel + numberOfDetective + numberOfVillager

    fun create(): MutableList<Player> =
        buildList<Player> {
            addAll(List(numberOfMafia) { Mafia() })
            addAll(List(numberOfAngel) { Angel() })
            addAll(List(numberOfDetective) { Detective() })
            addAll(List(numberOfVillager) { Villager() })

            ensureNonZero(MAFIA, VILLAGER)
            ensureSingle(ANGEL, DETECTIVE)
        }.toMutableList()
}