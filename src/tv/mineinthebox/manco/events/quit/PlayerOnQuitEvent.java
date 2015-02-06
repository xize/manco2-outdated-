package tv.mineinthebox.manco.events.quit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.instances.CratePlayer;

public class PlayerOnQuitEvent implements Listener {
	
	private final ManCo pl;
	
	public PlayerOnQuitEvent(ManCo pl) {
		this.pl = pl;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent e) {
		CratePlayer p = pl.getCratePlayer(e.getPlayer().getName());
		pl.removePlayer(p);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerKickEvent e) {
		CratePlayer p = pl.getCratePlayer(e.getPlayer().getName());
		pl.removePlayer(p);
	}

}
