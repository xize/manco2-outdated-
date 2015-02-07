package tv.mineinthebox.manco.events.chests;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.enums.CrateType;
import tv.mineinthebox.manco.instances.RareCrate;
import tv.mineinthebox.manco.interfaces.Crate;

public class ChestOpenEvent implements Listener {
	
	private final HashSet<String> unlocked = new HashSet<String>();
	private final HashMap<String, Chest> timer = new HashMap<String, Chest>();
    private final ManCo pl;
    
    public ChestOpenEvent(ManCo pl) {
    	this.pl = pl;
    }
	
	@EventHandler
	public void onOpen(final InventoryOpenEvent e) {
		if(e.isCancelled()) {
			return;
		}

		if(e.getInventory().getHolder() instanceof Chest) {
			final Chest chest = (Chest) e.getInventory().getHolder();
			if(chest.hasMetadata("crate_owner")) {
				final Player p = (Player)e.getPlayer();
				
				if(unlocked.contains(p.getName())) {
					unlocked.remove(p.getName());
					return;
				}
				
				if(timer.containsKey(e.getPlayer().getName())) {
					p.sendMessage(ChatColor.RED + "please wait till the timer has finished!");
					e.setCancelled(true);
					return;
				}
				final Crate crate = pl.getCrate(((FixedMetadataValue)chest.getMetadata("crate_serie").get(0)).asString());
				
				
				if(crate.needsKey()) {
					if(e.getPlayer().getItemInHand().getType() != Material.AIR) {
						if(e.getPlayer().getItemInHand().getItemMeta().hasDisplayName()) {
							if(!e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(crate.getKeyItem().getItemMeta().getDisplayName())) {
								p.sendMessage(ChatColor.RED + "this crate requires a key in hand, please look in /manco list for crate " + crate.getCrateName());
								e.setCancelled(true);
								return;
							} else {
								p.sendMessage(ChatColor.GREEN + "key matched!, please do not walk away or you have to buy a new key.");
								if(e.getPlayer().getItemInHand().getAmount() > 1) {
									ItemStack item = crate.getKeyItem().clone();
									item.setAmount(e.getPlayer().getItemInHand().getAmount()-1);
									e.getPlayer().setItemInHand(item);
								} else {
									e.getPlayer().setItemInHand(null);
								}
							}	
						} else {
							p.sendMessage(ChatColor.RED + "this crate requires a key in hand, please look in /manco list for crate " + crate.getCrateName());
							e.setCancelled(true);
							return;
						}	
					} else {
						p.sendMessage(ChatColor.RED + "this crate requires a key in hand, please look in /manco list for crate " + crate.getCrateName());
						e.setCancelled(true);
						return;
					}
				}
				
				
				timer.put(e.getPlayer().getName(), chest);
				e.setCancelled(true);

				new BukkitRunnable() {

					int i = 10;
					private int soundTime = 0;

					public void playSound(final Player p, final Chest chest, final Sound sound) {
						soundTime += 1;
						Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {


							@Override
							public void run() {
								p.getWorld().playSound(chest.getLocation(), sound, 1F, 1F);
							}


						}, soundTime);
					}

					@Override
					public void run() {
						if(i <= 0) {
							if(chest.getLocation().distanceSquared(p.getLocation()) < 10) {
								if(e.isCancelled()) {
									e.setCancelled(false);
								}
								soundTime = 0;
								timer.remove(p.getName());
								unlocked.add(p.getName());
								p.openInventory(chest.getInventory());
								this.cancel();
								return;
							} else {
								if(e.isCancelled()) {
									e.setCancelled(false);
								}
								timer.remove(p.getName());
								p.sendMessage(ChatColor.RED + "failed you where to far away of the chest!");
								soundTime = 0;
								this.cancel();
								return;
							}
						} else {
							if(i == 10) {
								if(crate.getType() == CrateType.RARE) {
									RareCrate rcrate = (RareCrate)crate;
									if(rcrate.hasEffects()) {
										Effect effect = rcrate.getEffect();
										Sound sound = rcrate.getEffectSound();
										p.sendMessage(ChatColor.GOLD + "opening crate in 10 seconds");
										playSound(p, chest, Sound.HORSE_ARMOR);
										playSound(p, chest, Sound.CHEST_OPEN);
										if(effect instanceof Effect) {
											p.getWorld().playEffect(chest.getLocation(), effect, 10);
										}
										if(sound instanceof Sound) {
											p.getWorld().playSound(chest.getLocation(), sound, 1F, 1F);
										}
									} else {
										p.sendMessage(ChatColor.GOLD + "opening crate in 10 seconds");
										playSound(p, chest, Sound.HORSE_ARMOR);
										playSound(p, chest, Sound.CHEST_OPEN);
									}
								} else {
									p.sendMessage(ChatColor.GOLD + "opening crate in 10 seconds");
									playSound(p, chest, Sound.HORSE_ARMOR);
									playSound(p, chest, Sound.CHEST_OPEN);
								}
							} else {
								if(crate.getType() == CrateType.RARE) {
									RareCrate rcrate = (RareCrate)crate;
									if(rcrate.hasEffects()) {
										p.sendMessage(ChatColor.GOLD + "opening crate in " + i + " seconds");
										playSound(p, chest, Sound.HORSE_ARMOR);
										playSound(p, chest, Sound.CHEST_OPEN);
										if(rcrate.getEffect() instanceof Effect) {
											p.getWorld().playEffect(chest.getLocation(), rcrate.getEffect(), 10);
										}
										if(rcrate.getEffectSound() instanceof Sound) {
											p.getWorld().playSound(chest.getLocation(), rcrate.getEffectSound(), 1F, 1F);
										}
									} else {
										p.sendMessage(ChatColor.GOLD + "opening crate in " + i + " seconds");
										playSound(p, chest, Sound.HORSE_ARMOR);
										playSound(p, chest, Sound.CHEST_OPEN);
									}
								} else {
									p.sendMessage(ChatColor.GOLD + "opening crate in " + i + " seconds");
									playSound(p, chest, Sound.HORSE_ARMOR);
									playSound(p, chest, Sound.CHEST_OPEN);
								}
							}
						}
						i--;
					}

				}.runTaskTimer(pl, 0L, 10L);
			}
		}
	}
}