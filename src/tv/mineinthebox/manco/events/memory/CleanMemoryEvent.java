package tv.mineinthebox.manco.events.memory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.instances.CratePlayer;

public class CleanMemoryEvent implements Listener {
	
	private final ManCo pl;
	
	public CleanMemoryEvent(ManCo pl) {
		this.pl = pl;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onQuit(PlayerQuitEvent e) {
		if(pl.containsPlayer(e.getPlayer().getName())) {
			CratePlayer p = pl.getCratePlayer(e.getPlayer().getName());
			p.remove();
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onQuit(PlayerKickEvent e) {
		if(pl.containsPlayer(e.getPlayer().getName())) {
			CratePlayer p = pl.getCratePlayer(e.getPlayer().getName());
			p.remove();
		}
	}
}
