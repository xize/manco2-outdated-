package tv.mineinthebox.manco.hooks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class VaultHook {
	
	private static Economy econ;
	
	/**
	 * @author xize
	 * @param gives the player money
	 * @param s - the player
	 * @param money - amount
	 */
	@SuppressWarnings("deprecation")
	public void deposit(String s, double money) {
		if(!(econ instanceof Economy)) {
			RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			econ = rsp.getProvider();
		}
		econ.depositPlayer(s, money);
	}
	
	/**
	 * @author xize
	 * @param take the players money
	 * @param s - the player
	 * @param money - amount
	 */
	@SuppressWarnings("deprecation")
	public void withdraw(String s, double money) {
		if(!(econ instanceof Economy)) {
			RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			econ = rsp.getProvider();
		}
		econ.withdrawPlayer(s, money);
	}
	
	/**
	 * @author xize
	 * @param p - player name
	 * @param money - money
	 * @return Boolean
	 */
	@SuppressWarnings("deprecation")
	public boolean hasEnough(String p, double money) {
		if(!(econ instanceof Economy)) {
			RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			econ = rsp.getProvider();
		}
		return econ.has(p, money);
	}

}
