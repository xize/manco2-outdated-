package tv.mineinthebox.manco.events.schematics;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class SchematicEntityDeath implements Listener {

	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		if(e.getEntity() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) e.getEntity();
			if(entity.isCustomNameVisible()) {
				if(entity.getCustomName().equalsIgnoreCase(ChatColor.GOLD + "[ManCo]"+ChatColor.WHITE + "builder!")) {
					e.getDrops().clear();
				}
			}
		}
	}

}
