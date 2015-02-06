package tv.mineinthebox.manco.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.events.blocks.BlockFallEvent;
import tv.mineinthebox.manco.events.blocks.BlockProtectEvent;
import tv.mineinthebox.manco.events.blocks.PlayerCrateProtectionEvent;
import tv.mineinthebox.manco.events.chests.ChestCloseEvent;
import tv.mineinthebox.manco.events.chests.ChestOpenEvent;
import tv.mineinthebox.manco.events.chests.ChestPlaceEvent;
import tv.mineinthebox.manco.events.crafting.KeyCraftEvent;
import tv.mineinthebox.manco.events.inventory.EditorEvent;
import tv.mineinthebox.manco.events.join.PlayerOnJoinEvent;
import tv.mineinthebox.manco.events.memory.CleanMemoryEvent;
import tv.mineinthebox.manco.events.money.MoneyConsumeEvent;
import tv.mineinthebox.manco.events.quit.PlayerOnQuitEvent;
import tv.mineinthebox.manco.events.schematics.SchematicEntityDeath;
import tv.mineinthebox.manco.events.schematics.SchematicPasteEvent;

public class Handler {
	
	private final ManCo pl;
	
	public Handler(ManCo pl) {
		this.pl = pl;
	}
	
	public void start() {
		setListener(new BlockFallEvent(pl));
		setListener(new CleanMemoryEvent(pl));
		setListener(new BlockProtectEvent());
		setListener(new EditorEvent(pl));
		setListener(new ChestCloseEvent(pl));
		setListener(new ChestOpenEvent(pl));
		setListener(new MoneyConsumeEvent(pl));
		setListener(new SchematicPasteEvent(pl));
		setListener(new SchematicEntityDeath());
		setListener(new ChestPlaceEvent());
		setListener(new PlayerOnJoinEvent(pl));
		setListener(new PlayerOnQuitEvent(pl));
		setListener(new KeyCraftEvent(pl));
		if(pl.getConfiguration().hasProtection()) {
			setListener(new PlayerCrateProtectionEvent());
		}
	}
	
	public void setListener(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, pl);
	}

}
