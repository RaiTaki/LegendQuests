package xyz.raitaki.legendquests.questhandlers.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint;
import xyz.raitaki.legendquests.questhandlers.QuestManager;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints.PlayerKillCheckpoint;

public class CheckpointPlaceholder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "legendquest";
    }

    @Override
    public @NotNull String getAuthor() {
        return "RaiTaki";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.1";
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, @NotNull String params){
        String st = "You don't have any quests";
        if(p == null) return st;
        QuestPlayer player = QuestManager.getQuestPlayerFromPlayer(p);
        PlayerQuest quest = player.getQuests().get(0);
        if(quest == null){
            return st;
        }

        if(params.equalsIgnoreCase("checkpoint")){
            if(quest.getCheckPoint() != null){
                QuestCheckpoint.CheckPointType type = quest.getCheckPoint().getType();
                if(type == QuestCheckpoint.CheckPointType.KILL){
                    PlayerKillCheckpoint checkpoint = (PlayerKillCheckpoint) quest.getCheckPoint();
                    st = "Kill " + checkpoint.getValue() + " " + checkpoint.getCounter() + "/" + checkpoint.getAmount();
                }
                if(type == QuestCheckpoint.CheckPointType.INTERECT){
                    st = "Interact with " + quest.getCheckPoint().getValue();
                }
            }
            else
                st = "";
        }
        return st;
    }

    public static void registerPlaceholder(){
        new CheckpointPlaceholder().register();
    }
}