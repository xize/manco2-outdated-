package tv.mineinthebox.manco.events.memory;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

import tv.mineinthebox.manco.ManCo;

public class CleanMemoryEvent implements Listener {
	
	private final ManCo pl;
	
	public CleanMemoryEvent(ManCo pl) {
		this.pl = pl;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(pl.getCrateOwners().contains(e.getPlayer().getName())) {
			pl.getCrateOwners().remove(e.getPlayer().getName());
		}
		if(e.getPlayer().hasMetadata("crate")) {
			Chest chest = (Chest) ((FixedMetadataValue)e.getPlayer().getMetadata("crate").get(0)).value();
			chest.getInventory().clear();
			chest.getBlock().setType(Material.AIR);
			e.getPlayer().removeMetadata("crate", pl);
		}
	}

	@EventHandler
	public void onQuit(PlayerKickEvent e) {
		if(pl.getCrateOwners().contains(e.getPlayer().getName())) {
			pl.getCrateOwners().remove(e.getPlayer().getName());
		}
		if(e.getPlayer().hasMetadata("crate")) {
			Chest chest = (Chest) ((FixedMetadataValue)e.getPlayer().getMetadata("crate").get(0)).value();
			chest.getInventory().clear();
			chest.getBlock().setType(Material.AIR);
			e.getPlayer().removeMetadata("crate", pl);
		}
	}
}
