package tv.mineinthebox.manco.events.join;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.instances.CratePlayer;

public class PlayerOnJoinEvent implements Listener {
	
	private final ManCo pl;
	
	public PlayerOnJoinEvent(ManCo pl) {
		this.pl = pl;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent e) {
		CratePlayer p = new CratePlayer(e.getPlayer(), pl);
		pl.addPlayer(p);
	}

}
