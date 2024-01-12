package xyz.raitaki.legendquests.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;

public class QuestUpdateEvent extends Event {

    private QuestBase questBase;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public QuestUpdateEvent(@NotNull QuestBase questBase) {
        this.questBase = questBase;
    }

    public QuestBase getQuestBase() {
        return questBase;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
