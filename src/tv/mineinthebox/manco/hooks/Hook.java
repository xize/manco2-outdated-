package tv.mineinthebox.manco.hooks;

import org.bukkit.Bukkit;

public class Hook {
	
	public boolean isVaultEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("Vault");
	}
	
	public boolean isWorldGuardEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("WorldGuard");
	}
	
	public boolean isWorldEditEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("WorldEdit");
	}
	
	public boolean isCoreProtectEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("CoreProtect");
	}
	
	public boolean isLogBlockEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("LogBlock");
	}
	
	public boolean isPrismEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("Prism");
	}
	
	public boolean isNCPEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("NoCheatPlus");
	}
}
