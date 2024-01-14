package xyz.raitaki.legendquests.events.listeners.vanilla;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.raitaki.legendquests.events.PlayerKillEntityEvent;

public class EntityDamageEventListener implements Listener {

  @EventHandler
  public void onEntityDamage(EntityDamageByEntityEvent event) {
    if (!(event.getDamager() instanceof Player player)) {
      return;
    }
    if (!(event.getEntity() instanceof LivingEntity entity)) {
      return;
    }
    if (entity.getHealth() - event.getFinalDamage() > 0) {
      return;
    }

    PlayerKillEntityEvent killEvent = new PlayerKillEntityEvent(player, entity);
    Bukkit.getPluginManager().callEvent(killEvent);
  }
}
