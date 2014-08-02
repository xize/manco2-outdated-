package tv.mineinthebox.manco;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import tv.mineinthebox.manco.api.ManCoApi;
import tv.mineinthebox.manco.enums.CrateType;
import tv.mineinthebox.manco.enums.LogType;
import tv.mineinthebox.manco.events.Handler;
import tv.mineinthebox.manco.hooks.Hook;
import tv.mineinthebox.manco.hooks.HookAble;
import tv.mineinthebox.manco.instances.ChestSchedule;
import tv.mineinthebox.manco.instances.CratePlayer;
import tv.mineinthebox.manco.instances.NormalCrate;

public class ManCo extends JavaPlugin {

	private static ManCo manco;
	private Configuration config;
	private ChestSchedule scheduler;
	private Handler handler;
	private ManCoApi api;
	private HashSet<String> crateOwners;
	private static Hook hooks;
	private static HookAble hookss;

	private HashMap<String, CratePlayer> players = new HashMap<String, CratePlayer>();

	public void onEnable() {
		manco = this;
		log(LogType.INFO, "has been enabled!");

		config = new Configuration(this);
		config.createConfiguration();

		handler = new Handler(this);
		handler.start();

		scheduler = new ChestSchedule();

		getCommand("manco").setExecutor(new command());
		
		this.api = new ManCoApi();
		
		for(Player p : ManCo.getOnlinePlayers()) {
			CratePlayer crate = new CratePlayer(p);
			addPlayer(crate);
		}
		
		loadRecipes();
		
		try {
			MetricsLite m = new MetricsLite(this);
			m.start();
		} catch(Exception e) {

		}
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

	/**
	 * @author xize
	 * @param returns the ManCo plugin
	 * @return ManCo
	 */
	public static ManCo getPlugin() {
		return manco;
	}

	/**
	 * @author xize
	 * @param returns the chest scheduler
	 * @return ChestSChedule
	 */
	public static ChestSchedule getScheduler() {
		return ManCo.getPlugin().scheduler;
	}

	/**
	 * @author xize
	 * @param returns the singleton factory instance of the config.
	 * @return Configuration
	 */
	public static Configuration getConfiguration() {
		return ManCo.getPlugin().config;
	}

	/**
	 * @author xize
	 * @param returns the Handler
	 * @return Handler
	 */
	public static Handler getHandlers() {
		return ManCo.getPlugin().handler;
	}

	public static ManCoApi getApi() {
		return ManCo.getPlugin().api;
	}

	/**
	 * @author xize
	 * @param name - the possible crate name
	 * @return Boolean
	 */
	public boolean isCrate(String name) {
		List<NormalCrate> crates = new ArrayList<NormalCrate>();
		if(ManCo.getConfiguration().crateList.containsKey(CrateType.NORMAL)) {
			crates.addAll(ManCo.getConfiguration().crateList.get(CrateType.NORMAL).values());
		}
		if(ManCo.getConfiguration().crateList.containsKey(CrateType.RARE)) {
			crates.addAll(ManCo.getConfiguration().crateList.get(CrateType.RARE).values());
		}
		for(NormalCrate crate : crates) {
			if(crate.getCrateName().equalsIgnoreCase(name.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public NormalCrate getCrate(String name) {
		List<NormalCrate> crates = new ArrayList<NormalCrate>();
		if(ManCo.getConfiguration().crateList.containsKey(CrateType.NORMAL)) {
			crates.addAll(ManCo.getConfiguration().crateList.get(CrateType.NORMAL).values());
		}
		if(ManCo.getConfiguration().crateList.containsKey(CrateType.RARE)) {
			crates.addAll(ManCo.getConfiguration().crateList.get(CrateType.RARE).values());
		}
		for(NormalCrate crate : crates) {
			if(crate.getCrateName().equalsIgnoreCase(name.toLowerCase())) {
				return crate;
			}
		}
		throw new NullPointerException("crate does not exist");
	}

	/**
	 * @author xize
	 * @param loc - the location
	 * @return Boolean
	 */
	public boolean canFall(Location loc) {
		if(!loc.getBlock().getRelative(BlockFace.DOWN).getType().isTransparent() && loc.getBlock().getRelative(BlockFace.DOWN).getType().isSolid()) {
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param cleans up every chest or fallingblock, and every memory related object.
	 */
	private void cleanup() {
		for(Player p : ManCo.getOnlinePlayers()) {
			if(ManCo.getPlugin().getCrateOwners().contains(p.getPlayer().getName())) {
				ManCo.getPlugin().getCrateOwners().remove(p.getPlayer().getName());
			}
			if(p.getPlayer().hasMetadata("crate")) {
				Chest chest = (Chest) ((FixedMetadataValue)p.getPlayer().getMetadata("crate").get(0)).value();
				chest.getInventory().clear();
				chest.getBlock().setType(Material.AIR);
				p.removeMetadata("crate", this);
			}
		}
	}

	public void addPlayer(CratePlayer p) {
		players.put(p.getPlayer().getName().toLowerCase(), p);
	}
	
	public NormalCrate[] getCrates() {
		int fullSize =  ((getConfiguration().crateList.containsKey(CrateType.NORMAL) ? getConfiguration().crateList.get(CrateType.NORMAL).size() : 0) + (getConfiguration().crateList.containsKey(CrateType.RARE) ? getConfiguration().crateList.get(CrateType.RARE).size() : 0));
		NormalCrate[] crates = new NormalCrate[fullSize];
		//List<NormalCrate> crates = new ArrayList<NormalCrate>();
		int i = 0;
		for(CrateType type : CrateType.values()) {
			if(getConfiguration().crateList.containsKey(type)) {
				for(NormalCrate crate : getConfiguration().crateList.get(type).values()) {
					crates[i] = crate;
					i++;
				}
			}
		}
		return crates;
	}

	public void removePlayer(CratePlayer p) {
		players.remove(p.getPlayer().getName().toLowerCase());
	}

	public CratePlayer getCratePlayer(String name) {
		return players.get(name.toLowerCase());
	}

	public CratePlayer[] getCratePlayers() {
		CratePlayer[] playerss = new CratePlayer[players.size()];
		int i = 0;
		for(CratePlayer cratep : players.values()) {
			playerss[i] = cratep;
			i++;
		}
		return playerss;
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
	
	public static Hook getHooks() {
		if(!(hooks instanceof Hook)) {
			hooks = new Hook();
		}
		return hooks;
	}
	
	public static HookAble getHookManager() {
		if(!(hookss instanceof HookAble)) {
			hookss = new HookAble();
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
}
