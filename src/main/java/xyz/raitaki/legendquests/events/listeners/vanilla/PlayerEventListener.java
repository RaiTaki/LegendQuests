package xyz.raitaki.legendquests.events.listeners.vanilla;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.raitaki.legendquests.events.PlayerQuestInteractEvent;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint;
import xyz.raitaki.legendquests.questhandlers.QuestManager;
import xyz.raitaki.legendquests.questhandlers.gui.CheckpointGUI;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints.PlayerInteractionCheckpoint;

import java.awt.*;

public class PlayerEventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        QuestManager.addBaseQuestToPlayer(event.getPlayer(), QuestManager.getBaseQuests().get(0));
        Bukkit.broadcastMessage(QuestManager.getQuestPlayers().get(event.getPlayer()).getQuests().toString());
        QuestManager.getBaseQuests().getFirst().showGui(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event){
        Player player = event.getPlayer();
        TextComponent message = (TextComponent) event.message();
        String text = message.content();
        QuestBase quest = QuestManager.getQuestBaseFromEditor(player);
        if(quest == null) return;
        CheckpointGUI checkpointGUI = quest.getQuestGUI().getEditedCheckpoint();
        if(checkpointGUI == null) return;
        checkpointGUI.setChatMessage(text);
        event.setCancelled(true);

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        QuestPlayer questPlayer = QuestManager.getQuestPlayerFromPlayer(player);
        PlayerQuest playerQuest = questPlayer.getPlayerQuestByCheckpointType(QuestCheckpoint.CheckPointType.INTERECT);

        if(playerQuest == null) return;

        PlayerCheckpoint playerCheckpoint = playerQuest.getCheckPoint();
        String entityName = entity.getName();
        if(!(playerCheckpoint instanceof PlayerInteractionCheckpoint interactionCheckpoint)) return;

        String questEntityName = interactionCheckpoint.getNpcName();
        if(!entityName.equals(questEntityName)) return;

        PlayerQuestInteractEvent playerQuestInteractEvent = new PlayerQuestInteractEvent(playerQuest, questPlayer, entity);
        Bukkit.getPluginManager().callEvent(playerQuestInteractEvent);
    }
}
