package tv.mineinthebox.manco.events.join;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.instances.CratePlayer;

public class PlayerOnJoinEvent implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		CratePlayer p = new CratePlayer(e.getPlayer());
		ManCo.getPlugin().addPlayer(p);
	}

}
