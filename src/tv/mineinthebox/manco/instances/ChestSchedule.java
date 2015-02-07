package tv.mineinthebox.manco.instances;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.enums.CrateType;
import tv.mineinthebox.manco.enums.LogType;
import tv.mineinthebox.manco.interfaces.Crate;


public class ChestSchedule implements Runnable {

	private final Random rand = new Random();
	private final ManCo pl;

	private BukkitTask task;

	public ChestSchedule(ManCo pl) {
		this.pl = pl;
		this.task = Bukkit.getScheduler().runTaskTimer(pl, this, 100, pl.getConfiguration().getScheduleTime()*60*60*20);
	}

	public void restart() {
		stop();
		Bukkit.getScheduler().runTaskTimer(pl, this, 0, pl.getConfiguration().getScheduleTime());
	}

	public void stop() {
		if(task instanceof BukkitTask) {
			task.cancel();
			task = null;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if(pl.getCratePlayers().length >= pl.getConfiguration().getDropsPerSchedule()) {
			for(int i = 0; i < pl.getConfiguration().getDropsPerSchedule(); i++) {
				Collection<Crate> crateslist = pl.getConfiguration().getCrateList().values();

				if(crateslist.isEmpty()) {
					ManCo.log(LogType.SEVERE, "please ensure that minimal one normal crate is listed in the config!");
					return;
				}

				Crate crate = null;
				
				if(isRare()) {
					Crate[] crates = pl.getConfiguration().getCrateList(CrateType.RARE);
					crate = crates[rand.nextInt(crates.length)];
				} else {
					Crate[] crates = pl.getConfiguration().getCrateList(CrateType.NORMAL);
					crate = crates[rand.nextInt(crates.length)];
				}

				Player p = getRandomPlayer();

				if(p == null) {
					return;
				}
				
				if(p.getLocation().getY() < 63) {
					if(pl.canFall(p.getLocation().getBlock().getLocation())) {
						if(pl.getConfiguration().isCrateMessagesEnabled()) {
							if(crate.getType() == CrateType.RARE) {
								Bukkit.broadcastMessage(pl.getConfiguration().getRareCrateDropMessage().replace("%p", p.getName()).replace("%crate", crate.getCrateName()));
							} else if(crate.getType() == CrateType.NORMAL) {
								Bukkit.broadcastMessage(pl.getConfiguration().getNormalCrateDropMessage().replace("%p", p.getName()).replace("%crate", crate.getCrateName()));
							}
						}
					}
				} else {
					if(pl.canFall(p.getLocation().getBlock().getLocation())) {
						if(pl.getConfiguration().isCrateMessagesEnabled()) {
							if(crate.getType() == CrateType.RARE) {
								Bukkit.broadcastMessage(pl.getConfiguration().getRareCrateDropMessage().replace("%p", p.getName()).replace("%crate", crate.getCrateName()));
							} else if(crate.getType() == CrateType.NORMAL) {
								Bukkit.broadcastMessage(pl.getConfiguration().getNormalCrateDropMessage().replace("%p", p.getName()).replace("%crate", crate.getCrateName()));
							}
						}
					}
				}
				
				if(p.getLocation().getY() < 63) {
					if(pl.canFall(p.getLocation().getBlock().getLocation())) {
						if(pl.getConfiguration().isCrateMessagesEnabled()) {
							if(crate.getType() == CrateType.RARE) {
								Bukkit.broadcastMessage(pl.getConfiguration().getRareCrateDropMessage().replaceAll("%p", p.getName()).replaceAll("%crate", crate.getCrateName()));
							} else if(crate.getType() == CrateType.NORMAL) {
								Bukkit.broadcastMessage(pl.getConfiguration().getNormalCrateDropMessage().replaceAll("%p", p.getName()).replaceAll("%crate", crate.getCrateName()));
							}
						}

						if(pl.getConfiguration().isSpawnRandom()) {
							FallingBlock fall = p.getWorld().spawnFallingBlock(p.getWorld().getHighestBlockAt(p.getLocation()).getLocation().add(rand.nextInt(10), 30, rand.nextInt(10)), Material.CHEST, (byte)0);
							fall.setMetadata("crate_serie", new FixedMetadataValue(pl, crate.getCrateName()));
							fall.setMetadata("crate_owner", new FixedMetadataValue(pl, p.getName()));
							pl.getCrateOwners().add(p.getName());
						} else {
							FallingBlock fall = p.getWorld().spawnFallingBlock(p.getLocation().add(0, 1, 0), Material.CHEST, (byte)0);
							fall.setMetadata("crate_serie", new FixedMetadataValue(pl, crate.getCrateName()));
							fall.setMetadata("crate_owner", new FixedMetadataValue(pl, p.getName()));
							pl.getCrateOwners().add(p.getName());
						}
					}
				} else {
					Location highest = p.getWorld().getHighestBlockAt(p.getLocation()).getLocation();
					if(pl.canFall(highest)) {
						if(pl.getConfiguration().isCrateMessagesEnabled()) {
							if(crate.getType() == CrateType.RARE) {
								Bukkit.broadcastMessage(pl.getConfiguration().getRareCrateDropMessage().replaceAll("%p", p.getName()).replaceAll("%crate", crate.getCrateName()));
							} else if(crate.getType() == CrateType.NORMAL) {
								Bukkit.broadcastMessage(pl.getConfiguration().getNormalCrateDropMessage().replaceAll("%p", p.getName()).replaceAll("%crate", crate.getCrateName()));
							}
						}

						if(pl.getConfiguration().isSpawnRandom()) {
							FallingBlock fall = p.getWorld().spawnFallingBlock(highest.add(rand.nextInt(10), 30, rand.nextInt(10)), Material.CHEST, (byte)0);
							fall.setMetadata("crate_serie", new FixedMetadataValue(pl, crate.getCrateName()));
							fall.setMetadata("crate_owner", new FixedMetadataValue(pl, p.getName()));
							pl.getCrateOwners().add(p.getName());
						} else {
							FallingBlock fall = p.getWorld().spawnFallingBlock(highest.add(0, 30, 0), Material.CHEST, (byte)0);
							fall.setMetadata("crate_serie", new FixedMetadataValue(pl, crate.getCrateName()));
							fall.setMetadata("crate_owner", new FixedMetadataValue(pl, p.getName()));
							pl.getCrateOwners().add(p.getName());
						}
					}
				}
			}
		}
	}

	/**
	 * @author xize
	 * @param returns the player
	 * @return Player
	 */
	private Player getRandomPlayer() {
		LinkedList<Player> players = new LinkedList<Player>();
		for(Player p : ManCo.getOnlinePlayers()) {
			players.add(p);
		}
		Collections.shuffle(players);
		for(Player p : players) {
			if(!pl.getCrateOwners().contains(p.getName()) && !p.hasPermission("manco.bypass")) {
				return p;
			}
		}
		return null;
	}


	/**
	 * @author xize
	 * @param returns true if its rare crate time
	 * @return Boolean
	 */
	private boolean isRare() {
		return (rand.nextInt(100) >= (100-pl.getConfiguration().getRareCrateChance()));
	}
}
