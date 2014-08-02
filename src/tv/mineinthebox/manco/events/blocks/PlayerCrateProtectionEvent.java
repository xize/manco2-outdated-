package tv.mineinthebox.manco.events.blocks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class PlayerCrateProtectionEvent implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.isCancelled()) {
			return;
		}

		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getClickedBlock().getType() == Material.CHEST) {
				Chest chest = (Chest) e.getClickedBlock().getState();
				if(chest.hasMetadata("crate_owner")) {
					String owner = ((FixedMetadataValue)chest.getMetadata("crate_owner").get(0)).asString();
					if(!e.getPlayer().getName().equalsIgnoreCase(owner)) {
						e.getPlayer().sendMessage(ChatColor.RED + "this crate does not belongs to you!.");
						e.setCancelled(true);
					}
				}
			}
		}
	}

}
