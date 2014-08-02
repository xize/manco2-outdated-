package tv.mineinthebox.manco.events.quit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.instances.CratePlayer;

public class PlayerOnQuitEvent implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent e) {
		CratePlayer p = ManCo.getPlugin().getCratePlayer(e.getPlayer().getName());
		ManCo.getPlugin().removePlayer(p);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerKickEvent e) {
		CratePlayer p = ManCo.getPlugin().getCratePlayer(e.getPlayer().getName());
		ManCo.getPlugin().removePlayer(p);
	}

}
