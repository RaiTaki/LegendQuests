package xyz.raitaki.legendquests.events.listeners.custom;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.raitaki.legendquests.events.PlayerQuestEndEvent;

public class PlayerQuestEndListener implements Listener {

    @EventHandler
    public void onQuestEnd(PlayerQuestEndEvent event) {
        event.getQuestPlayer().getPlayer().sendMessage("You have completed the quest " + event.getPlayerQuest().getQuest().getName());
        event.getPlayerQuest().giveReward();
    }
}
