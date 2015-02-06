package tv.mineinthebox.manco.events.money;

import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import tv.mineinthebox.manco.ManCo;

public class MoneyConsumeEvent implements Listener {
	
	private final ManCo pl;
	
	public MoneyConsumeEvent(ManCo pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getItem() instanceof ItemStack) {
				if(e.getItem().hasItemMeta()) {
					if(e.getItem().getItemMeta().hasDisplayName()) {
						if(isMoney(e.getItem().getItemMeta().getDisplayName())) {
							if(pl.getHooks().isVaultEnabled()) {
								pl.getHookManager().getVaultHook().deposit(e.getPlayer().getName(), getMoney(e.getItem().getItemMeta().getDisplayName()));
								e.getPlayer().sendMessage(ChatColor.GREEN + "successfully added " + getMoney(e.getItem().getItemMeta().getDisplayName()) + "$");
								if(e.getItem().getAmount() == 1) {
									e.getPlayer().setItemInHand(null);
								} else {
									e.getItem().setAmount(e.getItem().getAmount()-1);
								}
							} else {
								e.getPlayer().sendMessage(ChatColor.RED + "the server does not support Vault.");
							}
						}
					}
				}
			}
		}
	}
	
	private Double getMoney(String s) {
		String[] args = s.split(":");
		String mon = args[1].replace("$", "");
		return Double.parseDouble(mon);
	}
	
	private boolean isMoney(String name) {
		String reg = ChatColor.GOLD + "\\[money\\]"+ChatColor.GRAY+"\\:"+"(.*?)"+"\\$";
		return Pattern.matches(reg, name);
	}

}
