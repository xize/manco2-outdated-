package tv.mineinthebox.manco;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import tv.mineinthebox.manco.enums.CrateType;
import tv.mineinthebox.manco.enums.LogType;
import tv.mineinthebox.manco.events.Handler;
import tv.mineinthebox.manco.hooks.Hook;
import tv.mineinthebox.manco.hooks.HookAble;
import tv.mineinthebox.manco.instances.ChestSchedule;
import tv.mineinthebox.manco.instances.CratePlayer;
import tv.mineinthebox.manco.interfaces.Crate;
import tv.mineinthebox.manco.interfaces.ManCoApi;
import tv.mineinthebox.manco.utils.SchematicUtils;

public class ManCo extends JavaPlugin implements ManCoApi {

	private Configuration config;
	private ChestSchedule scheduler;
	private Handler handler;
	private HashSet<String> crateOwners;
	private Hook hooks;
	private HookAble hookss;
	private SchematicUtils schematic;

	private final HashMap<String, CratePlayer> players = new HashMap<String, CratePlayer>();

	public void onEnable() {
		config = new Configuration(this);
		config.createConfiguration();

		handler = new Handler(this);
		handler.start();

		if(getHooks().isWorldGuardEnabled()) {
			try {
				getHookManager().getWorldguardHook().registerFlag(getHookManager().getWorldguardHook().getManCoSpawnFlag());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		scheduler = new ChestSchedule(this);

		getCommand("manco").setExecutor(new MancoCommand(this));
		
		for(Player p : ManCo.getOnlinePlayers()) {
			CratePlayer crate = new CratePlayer(p, this);
			addPlayer(crate);
		}
		
		loadRecipes();
		
		
		
		log(LogType.INFO, "has been enabled!");
	}

	public void onDisable() {
		log(LogType.INFO, "has been disabled!");
		cleanup();

	}

	/**
	 * @author xize
	 * @param type - the LogType
	 * @param message - the message
	 */
	public static void log(LogType type, String message) {
		Bukkit.getConsoleSender().sendMessage(type.getPrefix() + message);
	}
	
	public SchematicUtils getSchematicUtils() {
		if(schematic == null) {
			this.schematic = new SchematicUtils(this);
		}
		return schematic;
	}

	/**
	 * @author xize
	 * @param returns the chest scheduler
	 * @return ChestSChedule
	 */
	public ChestSchedule getScheduler() {
		return scheduler;
	}

	/**
	 * @author xize
	 * @param returns the singleton factory instance of the config.
	 * @return Configuration
	 */
	public Configuration getConfiguration() {
		return config;
	}

	/**
	 * @author xize
	 * @param returns the Handler
	 * @return Handler
	 */
	public Handler getHandlers() {
		return handler;
	}

	/**
	 * returns the ManCo api, deprecated because you should now cast it like:
	 * ManCoApi api = (ManCoApi)Bukkit.getPluginManager().getPlugin("ManCo");
	 * 
	 * @author xize
	 * @deprecated
	 * @return ManCoApi
	 */
	public ManCoApi getApi() {
		return (ManCoApi) this;
	}

	public Crate getCrate(String name) {
		if(getConfiguration().getCrateList().containsKey(name)) {
			return getConfiguration().getCrateList().get(name);
		}
		throw new NullPointerException("crate does not exist");
	}

	/**
	 * @author xize
	 * @param loc - the location
	 * @return Boolean
	 */
	public boolean canFall(Location loc) {
		if(loc.getBlock().getType() != Material.STEP && loc.getBlock().getRelative(BlockFace.DOWN).getType() != Material.STEP) {
			if(!loc.getBlock().getType().isTransparent() && !loc.getBlock().getRelative(BlockFace.DOWN).getType().isTransparent()) {
				if((getHooks().isWorldGuardEnabled() ? getHookManager().getWorldguardHook().isFlagAllowed(getHookManager().getWorldguardHook().getManCoSpawnFlag(), loc) : true)) {
					return true;
				}
			} else if(loc.getBlock().getType() == Material.AIR || loc.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
				if((getHooks().isWorldGuardEnabled() ? getHookManager().getWorldguardHook().isFlagAllowed(getHookManager().getWorldguardHook().getManCoSpawnFlag(), loc) : true)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @author xize
	 * @param cleans up every chest or fallingblock, and every memory related object.
	 */
	private void cleanup() {
		for(CratePlayer player : getCratePlayers()) {
			player.remove();
		}
	}

	public void addPlayer(CratePlayer p) {
		players.put(p.getPlayer().getName().toLowerCase(), p);
	}

	public void removePlayer(CratePlayer p) {
		players.remove(p.getPlayer().getName().toLowerCase());
	}

	public boolean containsPlayer(String name) {
		return players.containsKey(name.toLowerCase());
	}

	public HashSet<String> getCrateOwners() {
		if(!(crateOwners instanceof HashSet)) {
			this.crateOwners = new HashSet<String>();
		}
		return crateOwners;
	}
	
	public Hook getHooks() {
		if(!(hooks instanceof Hook)) {
			hooks = new Hook();
		}
		return hooks;
	}
	
	public HookAble getHookManager() {
		if(!(hookss instanceof HookAble)) {
			this.hookss = new HookAble(this);
		}
		return hookss;
	}
	
	public void loadRecipes() {
		ItemStack stack = new ItemStack(Material.STONE, 1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("crate");
		stack.setItemMeta(meta);
		ShapedRecipe recipe1 = new ShapedRecipe(stack);
		recipe1.shape(new String[] {
				"AAA",
				"ABA",
				" A "
		});
		recipe1.setIngredient('A', Material.IRON_INGOT);
		recipe1.setIngredient('B', Material.GOLD_INGOT);
		
		ShapedRecipe recipe2 = new ShapedRecipe(stack);
		recipe2.shape(new String[] {
				"AAA",
				"ACA",
				" A "
		});
		recipe2.setIngredient('A', Material.IRON_INGOT);
		recipe2.setIngredient('C', Material.DIAMOND);
		
		Bukkit.addRecipe(recipe1);
		Bukkit.addRecipe(recipe2);
	}
	
	/**
	 * @author xize, shadypotato
	 * @param this is a modificated version from https://forums.bukkit.org/threads/code-snippet-workaround-for-the-new-bukkit-getonlineplayers-method.285072/ this version is wroted by myself to learn a bit about reflection, fall pits are Bukkit.getServer().getClass() ive learned to not use that.
	 * @param this will return a safe type compatibility array which could either be returned from a Collection, List, or the array it self.
	 * @return Player[]
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@SuppressWarnings("unchecked")
	public static Player[] getOnlinePlayers() {
		try {
			Method check = Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]);
			if(check.getReturnType() == Player[].class) {
				return (Player[])check.invoke(null, new Object[0]);	
			} else if(check.getReturnType() == List.class || check.getReturnType() == Collection.class) {
				Collection<Player> players = (Collection<Player>) check.invoke(null, new Object[0]);
				Player[] ps = new Player[(players.size())];
				int i = 0;
				for(Player p : players) {
					ps[i] = p;
					i++;
				}
				return ps;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		throw new NullPointerException("a fatal error has been occuried, please restart your server.");
	}
	
	//api start
	
	@Override
	public CratePlayer getCratePlayer(String name) throws NullPointerException {
		if(players.containsKey(name.toLowerCase())) {
			return players.get(name.toLowerCase());
		}
		throw new NullPointerException("player does not exist.");
	}

	@Override
	public CratePlayer[] getCratePlayers() {
		CratePlayer[] playerss = new CratePlayer[players.size()];
		int i = 0;
		for(CratePlayer cratep : players.values()) {
			playerss[i] = cratep;
			i++;
		}
		return playerss;
	}

	@Override
	public Crate getCrateSerie(String name) throws NullPointerException {
		return getCrate(name);
	}

	@Override
	public void removeCrateSerie(String name) throws NullPointerException {
		Crate crate = getCrateSerie(name);
		crate.remove();
	}

	@Override
	@SuppressWarnings("deprecation")
	public Crate spawnCrate(CratePlayer p, Crate crate) {
		if(p.hasCrate()) {
			return crate;
		}
		Random rand = new Random();
		if(p.getPlayer().getLocation().getY() < 63) {
			if(canFall(p.getPlayer().getLocation().getBlock().getLocation())) {
				if(getConfiguration().isCrateMessagesEnabled()) {
					if(crate.getType() == CrateType.RARE) {
						Bukkit.broadcastMessage(getConfiguration().getRareCrateDropMessage().replaceAll("%p", p.getPlayer().getName()).replaceAll("%crate", crate.getCrateName()));
					} else if(crate.getType() == CrateType.NORMAL) {
						Bukkit.broadcastMessage(getConfiguration().getNormalCrateDropMessage().replaceAll("%p", p.getPlayer().getName()).replaceAll("%crate", crate.getCrateName()));
					}
				}

				if(getConfiguration().isSpawnRandom()) {
					FallingBlock fall = p.getPlayer().getWorld().spawnFallingBlock(p.getPlayer().getWorld().getHighestBlockAt(p.getPlayer().getLocation()).getLocation().add(rand.nextInt(10), 30, rand.nextInt(10)), Material.CHEST, (byte)0);
					fall.setMetadata("crate_serie", new FixedMetadataValue(this, crate.getCrateName()));
					fall.setMetadata("crate_owner", new FixedMetadataValue(this, p.getPlayer().getName()));
					getCrateOwners().add(p.getPlayer().getName());
				} else {
					FallingBlock fall = p.getPlayer().getWorld().spawnFallingBlock(p.getPlayer().getLocation().add(0, 1, 0), Material.CHEST, (byte)0);
					fall.setMetadata("crate_serie", new FixedMetadataValue(this, crate.getCrateName()));
					fall.setMetadata("crate_owner", new FixedMetadataValue(this, p.getPlayer().getName()));
					getCrateOwners().add(p.getPlayer().getName());
				}
				return crate;
			}
		} else {
			Location highest = p.getPlayer().getWorld().getHighestBlockAt(p.getPlayer().getLocation()).getLocation();
			if(canFall(highest)) {
				if(getConfiguration().isCrateMessagesEnabled()) {
					if(crate.getType() == CrateType.RARE) {
						Bukkit.broadcastMessage(getConfiguration().getRareCrateDropMessage().replaceAll("%p", p.getPlayer().getName()).replaceAll("%crate", crate.getCrateName()));
					} else if(crate.getType() == CrateType.NORMAL) {
						Bukkit.broadcastMessage(getConfiguration().getNormalCrateDropMessage().replaceAll("%p", p.getPlayer().getName()).replaceAll("%crate", crate.getCrateName()));
					}
				}

				if(getConfiguration().isSpawnRandom()) {
					FallingBlock fall = p.getPlayer().getWorld().spawnFallingBlock(highest.add(rand.nextInt(10), 30, rand.nextInt(10)), Material.CHEST, (byte)0);
					fall.setMetadata("crate_serie", new FixedMetadataValue(this, crate.getCrateName()));
					fall.setMetadata("crate_owner", new FixedMetadataValue(this, p.getPlayer().getName()));
					getCrateOwners().add(p.getPlayer().getName());
				} else {
					FallingBlock fall = p.getPlayer().getWorld().spawnFallingBlock(highest.add(0, 30, 0), Material.CHEST, (byte)0);
					fall.setMetadata("crate_serie", new FixedMetadataValue(this, crate.getCrateName()));
					fall.setMetadata("crate_owner", new FixedMetadataValue(this, p.getPlayer().getName()));
					getCrateOwners().add(p.getPlayer().getName());	
				}
				return crate;
			}
		}
		return crate;
	}

	@Override
	@SuppressWarnings("deprecation")
	public Crate spawnCrate(CratePlayer p, Crate crate, Location loc) {
		if(p.hasCrate()) {
			return crate;
		}
		if(canFall(loc)) {
			if(getConfiguration().isCrateMessagesEnabled()) {
				if(crate.getType() == CrateType.RARE) {
					Bukkit.broadcastMessage(getConfiguration().getRareCrateDropMessage().replaceAll("%p", p.getPlayer().getName()).replaceAll("%crate", crate.getCrateName()));
				} else if(crate.getType() == CrateType.NORMAL) {
					Bukkit.broadcastMessage(getConfiguration().getNormalCrateDropMessage().replaceAll("%p", p.getPlayer().getName()).replaceAll("%crate", crate.getCrateName()));
				}
			}

				FallingBlock fall = p.getPlayer().getWorld().spawnFallingBlock(loc.add(0, 1, 0), Material.CHEST, (byte)0);
				fall.setMetadata("crate_serie", new FixedMetadataValue(this, crate.getCrateName()));
				fall.setMetadata("crate_owner", new FixedMetadataValue(this, p.getPlayer().getName()));
				getCrateOwners().add(p.getPlayer().getName());
		}
		return crate;
	}

	@Override
	public Crate addCrateSerie(String serie, CrateType type, ItemStack[] items) {
		File f = new File(getDataFolder() + File.separator + "config.yml");
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
			e.printStackTrace();
		}
		getConfiguration().reload();
		return getCrateSerie(serie);
	}

	@Override
	public Crate addCrateSerie(String serie, CrateType type, ItemStack[] items, boolean enable) {
		File f = new File(getDataFolder() + File.separator + "config.yml");
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
			e.printStackTrace();
		}
		getConfiguration().reload();
		return getCrateSerie(serie);
	}

	@Override
	public Crate addCrateSerie(String serie, CrateType type, ItemStack[] items, boolean enable, boolean isRareEffects) {
		File f = new File(getDataFolder() + File.separator + "config.yml");
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
			e.printStackTrace();
		}
		getConfiguration().reload();
		return getCrateSerie(serie);
	}

	@Override
	public Crate addCrateSerie(String serie, CrateType type, ItemStack[] items, boolean enable, boolean isRareEffects, boolean keyEnable, Material key, Double price) {
		File f = new File(getDataFolder() + File.separator + "config.yml");
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
			e.printStackTrace();
		}
		getConfiguration().reload();
		return getCrateSerie(serie);
	}
	
	@Override
	public Crate addCrateSerie(String serie, CrateType type, ItemStack[] items, boolean enable, boolean isRareEffects, boolean keyEnable, Material key, Double price, int miniumSlots) {
		File f = new File(getDataFolder() + File.separator + "config.yml");
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
			e.printStackTrace();
		}
		getConfiguration().reload();
		return getCrateSerie(serie);
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
	
	@Override
	public boolean isCrate(String name) {
		if(getConfiguration().getCrateList().containsKey(name)) {
			return true;
		}
		return false;
	}
	
	@Override
	public Crate[] getCrates() {
		return getConfiguration().getCrateList().values().toArray(new Crate[getConfiguration().getCrateList().size()]);
	}
	
	//api end
	
}
