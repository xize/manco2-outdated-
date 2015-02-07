package tv.mineinthebox.manco.events.crafting;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.enums.CrateType;
import tv.mineinthebox.manco.interfaces.Crate;

public class KeyCraftEvent implements Listener {
	
	private final ManCo pl;
	
	public KeyCraftEvent(ManCo pl) {
		this.pl = pl;
	}

	@EventHandler
	public void onPrepare(PrepareItemCraftEvent e) {
		CraftingInventory inv = e.getInventory();
		if(inv.getRecipe().getResult().hasItemMeta()) {
			if(inv.getRecipe().getResult().getItemMeta().hasDisplayName()) {
				if(inv.getRecipe().getResult().getItemMeta().getDisplayName().equalsIgnoreCase("crate")) {
					if(inv.getItem(5) != null) {
						if(inv.getItem(5).hasItemMeta()) {
							if(inv.getItem(5).getItemMeta().hasDisplayName()) {
								if(pl.isCrate(inv.getItem(5).getItemMeta().getDisplayName())) {
									Crate crate = pl.getCrate(inv.getItem(5).getItemMeta().getDisplayName());
									if(crate.needsKey()) {
										if(inv.getItem(5).getType() == (crate.getType() == CrateType.NORMAL ? Material.GOLD_INGOT : Material.DIAMOND)) {
											inv.setResult(crate.getKeyItem());
										} else {
											inv.setResult(null);
										}
									} else {
										inv.setResult(null);
									}
								} else {
									inv.setResult(null);
								}
							} else {
								inv.setResult(null);
							}
						} else {
							inv.setResult(null);
						}
					}
				}
			}
		}
	}

}