package tv.mineinthebox.manco.events.inventory;

import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.enums.CrateType;
import tv.mineinthebox.manco.interfaces.Crate;

public class EditorEvent implements Listener {
	
	private final ManCo pl;
	
	public EditorEvent(ManCo pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onOpen(InventoryOpenEvent e) {
		if(e.getInventory().getTitle().startsWith("me:") || e.getInventory().getTitle().startsWith("mc:")) {
			e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.CHEST_OPEN, 1F, 1F);
		}
	}
	
	@EventHandler
	public void onEdit(InventoryCloseEvent e) {
		if(e.getInventory().getTitle().startsWith("me:")) {
			String name = e.getInventory().getTitle().substring("me:".length());
			Crate crate = pl.getCrate(name);
			crate.setRandomItems(e.getInventory().getContents());
		}
	}
	
	@EventHandler
	public void onCreate(InventoryCloseEvent e) {
		if(e.getInventory().getTitle().startsWith("mc:")) {
			String name = e.getInventory().getTitle().substring("mc:".length());
			pl.addCrateSerie(name, CrateType.NORMAL, e.getInventory().getContents());
		}
	}

}
