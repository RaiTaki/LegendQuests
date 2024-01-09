package xyz.raitaki.legendquests.listeners;

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
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (event.getEntity() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) event.getEntity();
                PlayerKillEntityEvent killEvent = new PlayerKillEntityEvent(player, entity);
                Bukkit.getPluginManager().callEvent(killEvent);
            }
        }
    }
}
