package tv.mineinthebox.manco.events.blocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.api.customevents.CrateFallEvent;
import tv.mineinthebox.manco.interfaces.Crate;

public class BlockFallEvent implements Listener {
	
	private final ManCo pl;
	
	public BlockFallEvent(ManCo pl) {
		this.pl = pl;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onCall(EntityChangeBlockEvent e) {
		if(e.getEntity() instanceof FallingBlock) {
			FallingBlock fall = (FallingBlock) e.getEntity();
			
			if(fall.hasMetadata("crate_serie") && fall.hasMetadata("crate_owner")) {
				
				String player = (String)((FixedMetadataValue)fall.getMetadata("crate_owner").get(0)).asString();
				String serie = (String)((FixedMetadataValue)fall.getMetadata("crate_serie").get(0)).asString();
				
				if(!pl.canFall(fall.getLocation())) {
					fall.removeMetadata("crate_owner", pl);
					fall.removeMetadata("crate_serie", pl);
					e.setCancelled(true);
					return;
				}
				
				Player p = Bukkit.getPlayer(player);
				
				if(p instanceof Player) {
					Block block = fall.getLocation().getBlock();
					block.setType(Material.CHEST);
					Chest chest = (Chest) block.getState();
					chest.setMetadata("crate_owner", new FixedMetadataValue(pl, player));
					chest.setMetadata("crate_serie", new FixedMetadataValue(pl, serie));
					Crate ncrate = pl.getCrateSerie(serie);
					
					List<ItemStack> items = new ArrayList<ItemStack>();
					Iterator<ItemStack> it = ncrate.getRandomItems().iterator();
					
					while(it.hasNext()) {
						items.add(it.next());
					}
					
					for(int i = 0; i < ncrate.getMiniumSlots(); i++) {
						Collections.shuffle(items);
						chest.getInventory().addItem(items.get(0));
					}
					
					p.setMetadata("crate", new FixedMetadataValue(pl, chest));
					
					Bukkit.getPluginManager().callEvent(new CrateFallEvent(pl.getApi().getCratePlayer(player), chest, ncrate));
					
					fall.removeMetadata("crate_owner", pl);
					fall.removeMetadata("crate_serie", pl);
					e.setCancelled(true);
				} else {
					fall.removeMetadata("crate_owner", pl);
					fall.removeMetadata("crate_serie", pl);
					e.setCancelled(true);
				}
			}
		}
	}

}
