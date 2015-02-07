package tv.mineinthebox.manco.hooks;

import org.bukkit.Bukkit;

public class Hook {
	
	public boolean isVaultEnabled() {
		try {
			Class<?> vault = Class.forName("net.milkbowl.vault.economy.Economy");
			if(Bukkit.getServicesManager().getRegistration(vault) != null) {
				return true;
			} else {
				return false;
			}
		} catch(Exception e) {
			return false;
		}
	}
	
	public boolean isWorldGuardEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("WorldGuard");
	}
	
	public boolean isNCPEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("NoCheatPlus");
	}
}
