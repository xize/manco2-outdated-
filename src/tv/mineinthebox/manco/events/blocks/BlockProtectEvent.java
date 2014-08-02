package tv.mineinthebox.manco.events.blocks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockProtectEvent implements Listener {
	
	@EventHandler(priority = EventPriority.LOW)
	public void onBreak(BlockBreakEvent e) {
		if(e.isCancelled()) {
			return;
		}
		
		if(e.getBlock().getType() == Material.CHEST) {
			Chest chest = (Chest) e.getBlock().getState();
			if(chest.hasMetadata("crate_owner")) {
				e.getPlayer().sendMessage(ChatColor.RED + "you cannot break crates.");
				e.setCancelled(true);
			}
		}
	}
}
