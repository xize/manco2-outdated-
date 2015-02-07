package tv.mineinthebox.manco.events.chests;

import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.instances.CratePlayer;

public class ChestCloseEvent implements Listener {
	
	private final ManCo pl;
	
	public ChestCloseEvent(ManCo pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if(e.getInventory().getHolder() instanceof Chest) {
			Chest chest = (Chest) e.getInventory().getHolder();
			if(chest.hasMetadata("crate_owner")) {
				CratePlayer player = pl.getCratePlayer(e.getPlayer().getName());
				player.remove();
			}
		}
	}

}
