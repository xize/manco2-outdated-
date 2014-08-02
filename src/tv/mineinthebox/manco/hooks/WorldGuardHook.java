package tv.mineinthebox.manco.hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;

public class WorldGuardHook {
	
	private static WorldGuardPlugin wg;
	
	public boolean isInRegion(Location loc) {
		if(!(wg instanceof WorldGuardPlugin)) {
			wg = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
		}
		ApplicableRegionSet set = wg.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
		if(set.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isInRegion(Player p) {
		if(!(wg instanceof WorldGuardPlugin)) {
			wg = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
		}
		ApplicableRegionSet set = wg.getRegionManager(p.getLocation().getWorld()).getApplicableRegions(p.getLocation());
		if(set.size() > 0) {
			LocalPlayer lp = wg.wrapPlayer(p);
			return !set.canBuild(lp);
		} else {
			return false;
		}
	}

}
