package tv.mineinthebox.manco.enums;

import org.bukkit.ChatColor;

public enum CrateType {
	
	RARE(ChatColor.DARK_PURPLE + "[RARE]"+ChatColor.RESET),
	NORMAL(ChatColor.GRAY + "[NORMAL]"+ChatColor.RESET);
	
	private final String prefix;
	
	private CrateType(String s) {
		this.prefix = s;
	}
	
	public String getPrefix() {
		return prefix;
	}

}
