package tv.mineinthebox.manco.hooks;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import net.coreprotect.CoreProtect;

public class CoreProtectHook {
	
	@SuppressWarnings("deprecation")
	public void log(String player, Block block) {
		CoreProtect core = (CoreProtect) Bukkit.getPluginManager().getPlugin("CoreProtect");
		core.getAPI().logPlacement(player, block.getLocation(), block.getTypeId(), block.getData());
	}


}
