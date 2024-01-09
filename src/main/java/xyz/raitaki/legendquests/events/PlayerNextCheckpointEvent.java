package xyz.raitaki.legendquests.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;

public class PlayerNextCheckpointEvent extends Event implements Cancellable {

    private QuestPlayer questPlayer;
    private PlayerQuest playerQuest;
    private PlayerCheckpoint currentCheckpoint;
    private PlayerCheckpoint nextCheckpoint;
    private boolean cancelled;
    private static final HandlerList handlerList = new HandlerList();

    public PlayerNextCheckpointEvent(@NotNull QuestPlayer questPlayer, @NotNull PlayerQuest playerQuest, @NotNull PlayerCheckpoint currentCheckpoint, @Nullable PlayerCheckpoint nextCheckpoint) {
        this.questPlayer = questPlayer;
        this.playerQuest = playerQuest;
        this.currentCheckpoint = currentCheckpoint;
        this.nextCheckpoint = nextCheckpoint;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return null;
    }

    public QuestPlayer getQuestPlayer() {
        return questPlayer;
    }

    public PlayerQuest getPlayerQuest() {
        return playerQuest;
    }

    public PlayerCheckpoint getCurrentCheckpoint() {
        return currentCheckpoint;
    }

    public PlayerCheckpoint getNextCheckpoint() {
        return nextCheckpoint;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
