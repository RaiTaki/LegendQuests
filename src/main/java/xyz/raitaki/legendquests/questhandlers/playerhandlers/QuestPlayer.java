package xyz.raitaki.legendquests.questhandlers.playerhandlers;

import org.bukkit.entity.Player;
import xyz.raitaki.legendquests.questhandlers.QuestBase;

import java.util.LinkedList;
import java.util.UUID;

public class QuestPlayer {

    private Player player;
    private UUID uuid;
    private LinkedList<PlayerQuest> quests;

    public QuestPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        quests = new LinkedList<>();
    }

    public void addQuest(PlayerQuest quest){
        quests.add(quest);
    }

    public Player getPlayer() {
        return player;
    }
}
