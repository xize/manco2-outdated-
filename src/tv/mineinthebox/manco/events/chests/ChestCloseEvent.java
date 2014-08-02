package tv.mineinthebox.manco.events.chests;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import tv.mineinthebox.manco.ManCo;

public class ChestCloseEvent implements Listener {
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if(e.getInventory().getHolder() instanceof Chest) {
			Chest chest = (Chest) e.getInventory().getHolder();
			if(chest.hasMetadata("crate_owner")) {
				chest.getBlock().setType(Material.AIR);
				ManCo.getPlugin().getCrateOwners().remove(e.getPlayer().getName());
				e.getPlayer().removeMetadata("crate", ManCo.getPlugin());
			}
		}
	}

}
