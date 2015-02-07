package tv.mineinthebox.manco;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import tv.mineinthebox.manco.enums.CrateType;
import tv.mineinthebox.manco.enums.LogType;
import tv.mineinthebox.manco.instances.NormalCrate;
import tv.mineinthebox.manco.instances.RareCrate;
import tv.mineinthebox.manco.interfaces.Crate;

public class Configuration {

	private final ManCo pl;
	private File f;
	private FileConfiguration con;

	public Configuration(ManCo pl) {
		this.pl = pl;
	}

	//private final EnumMap<CrateType, HashMap<String, Crate>> crateList = new EnumMap<CrateType, HashMap<String, Crate>>(CrateType.class);

	private final HashMap<String, Crate> crates = new HashMap<String, Crate>();

	/**
	 * @author xize
	 * @param creates the configuration, if the configuration already exists we skip this.
	 */
	@SuppressWarnings("deprecation")
	public void createConfiguration() {
		try {
			this.f = new File(pl.getDataFolder() + File.separator + "config.yml");
			if(f.exists()) {
				this.con = YamlConfiguration.loadConfiguration(f);
				ManCo.log(LogType.INFO, "Configuration file found!");
			} else {
				ManCo.log(LogType.INFO, "Configuration file is not found, generating a new one!");
				File dir = new File(pl.getDataFolder() + File.separator + "schematics");
				if(!dir.isDirectory()) {
					dir.mkdir();
				}
				con = YamlConfiguration.loadConfiguration(f);
				FileConfigurationOptions opt = con.options();
				opt.header("this plugin has been created by xize.");
				con.set("crate.dropsPerSchedule", 5);
				con.set("crate.rareCrateChance", 10);
				con.set("crate.schedule", 10);
				con.set("crate.protectChestAgainstOthers", true);
				con.set("crate.spawnRandom", false);
				con.set("crate.messages.enable", true);
				con.set("crate.messages.normalDropMessage", "&2an %crate crate has been dropped close to %p");
				con.set("crate.messages.rareDropMessage", "&5a rare crate %crate has been spawned near %p");
				ItemStack item1 = new ItemStack(Material.STONE_SWORD, 1);
				item1.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
				ItemStack item2 = new ItemStack(Material.BREAD, 4);
				ItemStack item3 = new ItemStack(Material.IRON_SPADE, 1);
				item3.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 3);
				List<ItemStack> items = new ArrayList<ItemStack>();
				items.add(item1);
				items.add(item2);
				items.add(item3);

				//a reminder for the crate system
				//the new chest inventory system will work with ItemMeta and enchants
				//however since its almost impossible to make it good configurable
				//we decided to mirror fake inventorys which will save and load on inventory open and close.

				con.set("crates.crate.serie51.isEnabled", true);
				con.set("crates.crate.serie51.isRare", false);
				con.set("crates.crate.serie51.rareEffects", false);
				con.set("crates.crate.serie51.keyEnable", true);
				con.set("crates.crate.serie51.keyItem", Material.BLAZE_ROD.name());
				con.set("crates.crate.serie51.keyPrice", 3.0);
				con.set("crates.crate.serie51.miniumSlotsFilled", 10);
				con.set("crates.crate.serie51.items", items.toArray());

				ItemStack item4 = new ItemStack(Material.GOLD_SWORD, 1);
				item2.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 1);
				ItemStack item5 = new ItemStack(Material.APPLE, 4);
				ItemStack item6 = new ItemStack(Material.IRON_AXE, 1);
				item3.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 10);
				List<ItemStack> items2 = new ArrayList<ItemStack>();
				items2.add(item4);
				items2.add(item5);
				items2.add(item6);

				con.set("crates.crate.serie61.isEnabled", true);
				con.set("crates.crate.serie61.isRare", true);
				con.set("crates.crate.serie61.rareEffects", true);
				con.set("crates.crate.serie61.keyEnable", true);
				con.set("crates.crate.serie61.keyItem", Material.STICK.getId());
				con.set("crates.crate.serie61.keyPrice", 1.0);
				con.set("crates.crate.serie61.miniumSlotsFilled", 10);
				con.set("crates.crate.serie61.items", items2.toArray());

				con.save(f);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		loadCrates();
	}

	/**
	 * @author xize
	 * @param loads all the crates once!
	 */
	private void loadCrates() {
		ManCo.log(LogType.INFO, "registering crates...");
		int num = 0;
		if(!crates.isEmpty()) {
			crates.clear();
		}
		for(String name : con.getConfigurationSection("crates.crate").getKeys(false)) {
			Crate crate = null;
			if(con.getBoolean("crates.crate."+name+".isRare")) {
				crate = new RareCrate(name, pl);
			} else {
				crate = new NormalCrate(name, pl);
			}
			if(!crates.containsKey(crate.getCrateName())) {
				crates.put(crate.getCrateName(), crate);
			} else {
				crates.get(crate.getCrateName());
			}
			num++;
		}
		ManCo.log(LogType.INFO, "in total " + num + " crates have been registered.");
	}

	/**
	 * @author xize
	 * @param returns the schedule time
	 * @return Integer
	 */
	public int getScheduleTime() {
		return con.getInt("crate.schedule");
	}

	/**
	 * @author xize
	 * @param returns the integer
	 * @return Integer
	 */
	public int getDropsPerSchedule() {
		return con.getInt("crate.dropsPerSchedule");
	}

	public int getRareCrateChance() {
		return con.getInt("crate.rareCrateChance");
	}

	public boolean isCrateMessagesEnabled() {
		return con.getBoolean("crate.messages.enable");
	}

	public String getNormalCrateDropMessage() {
		return ChatColor.translateAlternateColorCodes('&', con.getString("crate.messages.normalDropMessage"));
	}

	public String getRareCrateDropMessage() {
		return ChatColor.translateAlternateColorCodes('&', con.getString("crate.messages.rareDropMessage"));
	}

	public boolean isSpawnRandom() {
		return con.getBoolean("crate.spawnRandom");
	}

	public boolean hasProtection() {
		return con.getBoolean("crate.protectChestAgainstOthers");
	}

	public void reload() {
		HandlerList.getRegisteredListeners(pl);
		try {
			con.load(f);
			loadCrates();
			pl.getScheduler().restart();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		pl.getHandlers().start();
	}

	/**
	 * @author xize
	 * @param returns a shared FileConfiguration in a abstract way.
	 * @return FileConfiguration
	 */
	public FileConfiguration getConfig() {
		return con;
	}

	/**
	 * @author xize
	 * @param returns the file in a abstract way.
	 * @return File
	 */
	public File getFile() {
		return f;
	}

	/**
	 * returns the crate list
	 * 
	 * @author xize
	 * @return EnumMap<CrateType, HashMap<String, Crate>>()
	 */
	public HashMap<String, Crate> getCrateList() {
		return crates;
	}

	/**
	 * returns the crate list
	 * 
	 * @author xize
	 * @return EnumMap<CrateType, HashMap<String, Crate>>()
	 */
	public Crate[] getCrateList(CrateType type) {
		Collection<Crate> allcrates = getCrateList().values();
		Iterator<Crate> it = allcrates.iterator();
		List<Crate> crates = new ArrayList<Crate>();
		for(Crate crate = (it.hasNext() ? it.next() : null); it.hasNext(); crate = it.next()) {
			crates.add(crate);
		}
		return crates.toArray(new Crate[crates.size()]);
	}

}
