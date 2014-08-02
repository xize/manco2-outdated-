package tv.mineinthebox.manco.events.chests;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class ChestPlaceEvent implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onChestPlace(BlockPlaceEvent e) {
		
		ItemStack item = e.getItemInHand();
		if(item.getType() == Material.CHEST) {
			for(BlockFace face : BlockFace.values()) {
				Block block = e.getBlock().getRelative(face);
				if(block.getType() == Material.CHEST) {
					Chest chest = (Chest) block.getState();
					if(isCrate(chest)) {
						e.getPlayer().sendMessage(ChatColor.RED + "you are not allowed to place a chest near a ManCo crate!");
						e.setCancelled(true);
						block.setData((byte) 3);
						e.setCancelled(true);
						break;
					}
				}
			}
		}
	}
	
	private boolean isCrate(Chest chest) {
		if(chest.hasMetadata("crate_owner")) {
			return true;
		}
		return false;
	}
}
