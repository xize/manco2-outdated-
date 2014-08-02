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
	
	private final ManCo manco;
	
	public Handler(ManCo manco) {
		this.manco = manco;
	}
	
	public void start() {
		setListener(new BlockFallEvent());
		setListener(new CleanMemoryEvent());
		setListener(new BlockProtectEvent());
		setListener(new EditorEvent());
		setListener(new ChestCloseEvent());
		setListener(new ChestOpenEvent());
		setListener(new MoneyConsumeEvent());
		setListener(new SchematicPasteEvent());
		setListener(new SchematicEntityDeath());
		setListener(new ChestPlaceEvent());
		setListener(new PlayerOnJoinEvent());
		setListener(new PlayerOnQuitEvent());
		setListener(new KeyCraftEvent());
		if(ManCo.getConfiguration().hasProtection()) {
			setListener(new PlayerCrateProtectionEvent());
		}
	}
	
	public void setListener(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, manco);
	}

}
