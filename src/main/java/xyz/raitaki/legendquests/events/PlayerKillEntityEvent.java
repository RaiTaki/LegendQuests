package xyz.raitaki.legendquests.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerKillEntityEvent extends Event {

    private Player player;
    private LivingEntity entity;

    private static final HandlerList handlerList = new HandlerList();

    public PlayerKillEntityEvent(@NotNull Player player, @NotNull LivingEntity entity) {
        this.player = player;
        this.entity = entity;
    }

    public Player getPlayer() {
        return player;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
