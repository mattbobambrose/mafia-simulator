import DetectiveGuiltType.*
import PlayerType.*
import PublicGuiltType.*
import kotlin.random.Random.Default.nextInt

abstract class Player(val playerType: PlayerType) {
    var wasInvestigated = false
    var detectiveGuilt = D_UNKNOWN
    var publicGuilt = P_UNKNOWN

    open fun vote(players: List<Player>): Player {
        val notInnocents = players.filter { publicGuilt != P_INNOCENT }
        val mafiaMembers = notInnocents.filter { publicGuilt == P_MAFIA }
        val choice: Player
        if (mafiaMembers.isNotEmpty())
            choice = mafiaMembers.get(0)
        else
            choice = notInnocents.get(notInnocents.size)
        return choice
    }
}

enum class PlayerType {
    MAFIA, VILLAGER, ANGEL, DETECTIVE
}

enum class DetectiveGuiltType {
    D_UNKNOWN, D_INNOCENT, D_MAFIA
}

enum class PublicGuiltType {
    P_UNKNOWN, P_INNOCENT, P_MAFIA
}

class Mafia : Player(MAFIA) {
    fun kill(players: List<Player>): Player {
        val notMe = players.filter { it.playerType != MAFIA }
        return notMe[nextInt(notMe.size)]
    }
}

class Angel : Player(ANGEL) {
    fun save(players: List<Player>, justSaved: Player, detectiveCameOut: Boolean, knownDetective: Player): Player {
        val notChosen = players.filter { justSaved != it }
        val p: Player
        if (!detectiveCameOut)
            p = notChosen[nextInt(notChosen.size)]
        else
            p = knownDetective
        return p
    }
}

class Detective : Player(DETECTIVE) {
    init {
        this.detectiveGuilt = D_INNOCENT
        this.wasInvestigated = true
    }

    fun investigate(players: List<Player>) {
        val notChosen = players.filter { !it.wasInvestigated && it.playerType != DETECTIVE }
        val p = notChosen[nextInt(notChosen.size)]
        if (p.playerType == MAFIA)
            p.detectiveGuilt = D_MAFIA
        else
            p.detectiveGuilt = D_INNOCENT
    }

    fun comeOut(players: List<Player>, config: GameConfig) {
        val innocents = players.filter { it.detectiveGuilt == D_INNOCENT }
        val mafiaMembers = players.filter { it.detectiveGuilt == D_MAFIA }
        val unknowns = players.filter { it.detectiveGuilt == D_UNKNOWN }
        //s1 is if the detective found all the mafia
        val s1 = mafiaMembers.size == config.numberOfMafia
        //s2 is if all innocents have been found
        val s2 = innocents.size == config.numberOfVillager + config.numberOfDetective + config.numberOfAngel

        if (s1 || s2) {

        }
    }
}

class Villager : Player(VILLAGER) {

}