package tv.mineinthebox.manco.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.FallingBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.enums.CrateType;
import tv.mineinthebox.manco.instances.CratePlayer;
import tv.mineinthebox.manco.instances.NormalCrate;

public class ManCoApi {

	/**
	 * @author xize
	 * @param name - the players name
	 * @return CratePlayer
	 * @throws NullPointerException - when the player does not exist.
	 */
	public CratePlayer getCratePlayer(String name) throws NullPointerException {
		if(ManCo.getPlugin().containsPlayer(name)) {
			return ManCo.getPlugin().getCratePlayer(name.toLowerCase());
		}
		throw new NullPointerException("player does not exist.");
	}

	/**
	 * @author xize
	 * @param returns all the crate player instances.
	 * @return CratePlayer[]
	 */
	public CratePlayer[] getCratePlayers() {
		return ManCo.getPlugin().getCratePlayers();
	}

	/**
	 * @author xize
	 * @param name - the crate name
	 * @return NormalCrate
	 * @throws NullPointerException - when the crate is a invalid crate.
	 */
	public NormalCrate getCrateSerie(String name) throws NullPointerException {
		return ManCo.getPlugin().getCrate(name);
	}

	/**
	 * @author xize
	 * @param name - remove a crate serie
	 * @throws NullPointerException - throws when the crate doesn't exist.
	 */
	public void removeCrateSerie(String name) throws NullPointerException {
		NormalCrate crate = getCrateSerie(name);
		crate.remove();
	}

	/**
	 * @author xize
	 * @param p - the CratePlayer
	 * @param crate - the Crate serie
	 * @return returns true if the crate is spawned, else false a player can only have one crate per time.
	 */
	@SuppressWarnings("deprecation")
	public boolean spawnCrate(CratePlayer p, NormalCrate crate) {
		if(p.hasCrate()) {
			return false;
		}
		Random rand = new Random();
		if(p.getPlayer().getLocation().getY() < 63) {
			if(ManCo.getPlugin().canFall(p.getPlayer().getLocation().getBlock().getLocation())) {
				if(ManCo.getConfiguration().isCrateMessagesEnabled()) {
					if(crate.getType() == CrateType.RARE) {
						Bukkit.broadcastMessage(ManCo.getConfiguration().getRareCrateDropMessage().replaceAll("%p", p.getPlayer().getName()).replaceAll("%crate", crate.getCrateName()));
					} else if(crate.getType() == CrateType.NORMAL) {
						Bukkit.broadcastMessage(ManCo.getConfiguration().getNormalCrateDropMessage().replaceAll("%p", p.getPlayer().getName()).replaceAll("%crate", crate.getCrateName()));
					}
				}

				if(ManCo.getConfiguration().isSpawnRandom()) {
					FallingBlock fall = p.getPlayer().getWorld().spawnFallingBlock(p.getPlayer().getWorld().getHighestBlockAt(p.getPlayer().getLocation()).getLocation().add(rand.nextInt(10), 30, rand.nextInt(10)), Material.CHEST, (byte)0);
					fall.setMetadata("crate_serie", new FixedMetadataValue(ManCo.getPlugin(), crate.getCrateName()));
					fall.setMetadata("crate_owner", new FixedMetadataValue(ManCo.getPlugin(), p.getPlayer().getName()));
					ManCo.getPlugin().getCrateOwners().add(p.getPlayer().getName());
				} else {
					FallingBlock fall = p.getPlayer().getWorld().spawnFallingBlock(p.getPlayer().getLocation().add(0, 1, 0), Material.CHEST, (byte)0);
					fall.setMetadata("crate_serie", new FixedMetadataValue(ManCo.getPlugin(), crate.getCrateName()));
					fall.setMetadata("crate_owner", new FixedMetadataValue(ManCo.getPlugin(), p.getPlayer().getName()));
					ManCo.getPlugin().getCrateOwners().add(p.getPlayer().getName());
				}
				return true;
			}
		} else {
			Location highest = p.getPlayer().getWorld().getHighestBlockAt(p.getPlayer().getLocation()).getLocation();
			if(ManCo.getPlugin().canFall(highest)) {
				if(ManCo.getConfiguration().isCrateMessagesEnabled()) {
					if(crate.getType() == CrateType.RARE) {
						Bukkit.broadcastMessage(ManCo.getConfiguration().getRareCrateDropMessage().replaceAll("%p", p.getPlayer().getName()).replaceAll("%crate", crate.getCrateName()));
					} else if(crate.getType() == CrateType.NORMAL) {
						Bukkit.broadcastMessage(ManCo.getConfiguration().getNormalCrateDropMessage().replaceAll("%p", p.getPlayer().getName()).replaceAll("%crate", crate.getCrateName()));
					}
				}

				if(ManCo.getConfiguration().isSpawnRandom()) {
					FallingBlock fall = p.getPlayer().getWorld().spawnFallingBlock(highest.add(rand.nextInt(10), 30, rand.nextInt(10)), Material.CHEST, (byte)0);
					fall.setMetadata("crate_serie", new FixedMetadataValue(ManCo.getPlugin(), crate.getCrateName()));
					fall.setMetadata("crate_owner", new FixedMetadataValue(ManCo.getPlugin(), p.getPlayer().getName()));
					ManCo.getPlugin().getCrateOwners().add(p.getPlayer().getName());
				} else {
					FallingBlock fall = p.getPlayer().getWorld().spawnFallingBlock(highest.add(0, 30, 0), Material.CHEST, (byte)0);
					fall.setMetadata("crate_serie", new FixedMetadataValue(ManCo.getPlugin(), crate.getCrateName()));
					fall.setMetadata("crate_owner", new FixedMetadataValue(ManCo.getPlugin(), p.getPlayer().getName()));
					ManCo.getPlugin().getCrateOwners().add(p.getPlayer().getName());	
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * @author xize
	 * @param p - CratePlayer
	 * @param crate - the Crate serie
	 * @param loc - the location where the crate would drop.
	 * @return Boolean - when true the crate is spawned otherwise false.
	 */
	@SuppressWarnings("deprecation")
	public boolean spawnCrate(CratePlayer p, NormalCrate crate, Location loc) {
		if(p.hasCrate()) {
			return false;
		}
		if(ManCo.getPlugin().canFall(loc)) {
			if(ManCo.getConfiguration().isCrateMessagesEnabled()) {
				if(crate.getType() == CrateType.RARE) {
					Bukkit.broadcastMessage(ManCo.getConfiguration().getRareCrateDropMessage().replaceAll("%p", p.getPlayer().getName()).replaceAll("%crate", crate.getCrateName()));
				} else if(crate.getType() == CrateType.NORMAL) {
					Bukkit.broadcastMessage(ManCo.getConfiguration().getNormalCrateDropMessage().replaceAll("%p", p.getPlayer().getName()).replaceAll("%crate", crate.getCrateName()));
				}
			}

				FallingBlock fall = p.getPlayer().getWorld().spawnFallingBlock(loc.add(0, 1, 0), Material.CHEST, (byte)0);
				fall.setMetadata("crate_serie", new FixedMetadataValue(ManCo.getPlugin(), crate.getCrateName()));
				fall.setMetadata("crate_owner", new FixedMetadataValue(ManCo.getPlugin(), p.getPlayer().getName()));
				ManCo.getPlugin().getCrateOwners().add(p.getPlayer().getName());
		}
		return false;
	}

	/**
	 * @author xize
	 * @param serie - the crate serie.
	 * @param type - the CrateType whenever the crate should be rare or normal.
	 * @param items - the contents.
	 */
	public void addCrateSerie(String serie, CrateType type, ItemStack[] items) {
		File f = new File(ManCo.getPlugin().getDataFolder() + File.separator + "config.yml");
		YamlConfiguration con = YamlConfiguration.loadConfiguration(f);
		con.set("crates.crate."+serie.toLowerCase()+".isEnabled", true);
		con.set("crates.crate."+serie.toLowerCase()+".isRare", (type == CrateType.RARE));
		con.set("crates.crate."+serie.toLowerCase()+".rareEffects", false);
		con.set("crates.crate."+serie.toLowerCase()+".keyEnable", false);
		con.set("crates.crate."+serie.toLowerCase()+".keyItem", Material.BLAZE_ROD.name());
		con.set("crates.crate."+serie.toLowerCase()+".keyPrice", 0.0);
		con.set("crates.crate."+serie.toLowerCase()+".miniumSlotsFilled", 10);
		con.set("crates.crate."+serie.toLowerCase()+".items", getCleanItems(items));
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ManCo.getConfiguration().reload();
	}

	/**
	 * @author xize
	 * @param serie - the serie type
	 * @param type - should the crate be rare or normal?
	 * @param items - the contents.
	 * @param enable - should the crate be enabled?
	 */
	public void addCrateSerie(String serie, CrateType type, ItemStack[] items, boolean enable) {
		File f = new File(ManCo.getPlugin().getDataFolder() + File.separator + "config.yml");
		YamlConfiguration con = YamlConfiguration.loadConfiguration(f);
		con.set("crates.crate."+serie.toLowerCase()+".isEnabled", enable);
		con.set("crates.crate."+serie.toLowerCase()+".isRare", (type == CrateType.RARE));
		con.set("crates.crate."+serie.toLowerCase()+".rareEffects", false);
		con.set("crates.crate."+serie.toLowerCase()+".keyEnable", false);
		con.set("crates.crate."+serie.toLowerCase()+".keyItem", Material.BLAZE_ROD.name());
		con.set("crates.crate."+serie.toLowerCase()+".keyPrice", 0.0);
		con.set("crates.crate."+serie.toLowerCase()+".miniumSlotsFilled", 10);
		con.set("crates.crate."+serie.toLowerCase()+".items", getCleanItems(items));
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ManCo.getConfiguration().reload();
	}

	/**
	 * @author xize
	 * @param serie - the serie type
	 * @param type - the CrateType
	 * @param items - the contents
	 * @param enable - should this crate be enabled?
	 * @param isRareEffects - should the crate have rare effects?
	 */
	public void addCrateSerie(String serie, CrateType type, ItemStack[] items, boolean enable, boolean isRareEffects) {
		File f = new File(ManCo.getPlugin().getDataFolder() + File.separator + "config.yml");
		YamlConfiguration con = YamlConfiguration.loadConfiguration(f);
		con.set("crates.crate."+serie.toLowerCase()+".isEnabled", true);
		con.set("crates.crate."+serie.toLowerCase()+".isRare", (type == CrateType.RARE));
		con.set("crates.crate."+serie.toLowerCase()+".rareEffects", isRareEffects);
		con.set("crates.crate."+serie.toLowerCase()+".keyEnable", false);
		con.set("crates.crate."+serie.toLowerCase()+".keyItem", Material.BLAZE_ROD.name());
		con.set("crates.crate."+serie.toLowerCase()+".keyPrice", 0.0);
		con.set("crates.crate."+serie.toLowerCase()+".miniumSlotsFilled", 10);
		con.set("crates.crate."+serie.toLowerCase()+".items", getCleanItems(items));
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ManCo.getConfiguration().reload();
	}

	/**
	 * @author xize
	 * @param serie - the type serie
	 * @param type - the CrateType
	 * @param items - the contents
	 * @param enable - should it be enabled?
	 * @param isRareEffects - should it have rare effects?
	 * @param keyEnable - should keys be enabled?
	 * @param key - the material type key
	 * @param price - the price.
	 */
	public void addCrateSerie(String serie, CrateType type, ItemStack[] items, boolean enable, boolean isRareEffects, boolean keyEnable, Material key, Double price) {
		File f = new File(ManCo.getPlugin().getDataFolder() + File.separator + "config.yml");
		YamlConfiguration con = YamlConfiguration.loadConfiguration(f);
		con.set("crates.crate."+serie.toLowerCase()+".isEnabled", true);
		con.set("crates.crate."+serie.toLowerCase()+".isRare", (type == CrateType.RARE));
		con.set("crates.crate."+serie.toLowerCase()+".rareEffects", isRareEffects);
		con.set("crates.crate."+serie.toLowerCase()+".keyEnable", keyEnable);
		con.set("crates.crate."+serie.toLowerCase()+".keyItem", key);
		con.set("crates.crate."+serie.toLowerCase()+".keyPrice", price);
		con.set("crates.crate."+serie.toLowerCase()+".miniumSlotsFilled", 10);
		con.set("crates.crate."+serie.toLowerCase()+".items", getCleanItems(items));
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ManCo.getConfiguration().reload();
	}

	/**
	 * @author xize
	 * @param serie - the crate serie
	 * @param type - the type of crate
	 * @param items - the possible contents of a crate
	 * @param enable - should it be enabled?
	 * @param isRareEffects - should it have effects?
	 * @param keyEnable - should we use keys?
	 * @param key - key type (Material enum).
	 * @param price - the cost of the key
	 * @param miniumSlots - the minium slots
	 */
	public void addCrateSerie(String serie, CrateType type, ItemStack[] items, boolean enable, boolean isRareEffects, boolean keyEnable, Material key, Double price, int miniumSlots) {
		File f = new File(ManCo.getPlugin().getDataFolder() + File.separator + "config.yml");
		YamlConfiguration con = YamlConfiguration.loadConfiguration(f);
		con.set("crates.crate."+serie.toLowerCase()+".isEnabled", true);
		con.set("crates.crate."+serie.toLowerCase()+".isRare", (type == CrateType.RARE));
		con.set("crates.crate."+serie.toLowerCase()+".rareEffects", isRareEffects);
		con.set("crates.crate."+serie.toLowerCase()+".keyEnable", keyEnable);
		con.set("crates.crate."+serie.toLowerCase()+".keyItem", key);
		con.set("crates.crate."+serie.toLowerCase()+".keyPrice", price);
		con.set("crates.crate."+serie.toLowerCase()+".miniumSlotsFilled", miniumSlots);
		con.set("crates.crate."+serie.toLowerCase()+".items", getCleanItems(items));
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ManCo.getConfiguration().reload();
	}
	
	private ItemStack[] getCleanItems(ItemStack[] items) {
		List<ItemStack> items2 = new ArrayList<ItemStack>();
		for(ItemStack item : items) {
			if(item != null) {
				items2.add(item);
			}
		}
		return items2.toArray(new ItemStack[items2.size()]);
	}
	
	/**
	 * @author xize
	 * @param name - the name of the crate
	 * @return Boolean
	 */
	public boolean isCrate(String name) {
		return ManCo.getPlugin().isCrate(name);
	}
}
