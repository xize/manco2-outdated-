package tv.mineinthebox.manco.hooks;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.enums.LogType;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardHook {

	private final WorldGuardPlugin wg;
	
	private final StateFlag ManCoSpawn = new ManCoFlag();
	
	public WorldGuardHook(ManCo pl) {
		this.wg = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
	}

	/**
	 * registers the mob-protect flag
	 * 
	 * @author xize
	 * @param flag - the flag or StateFlag to be injected/added in worldguard
	 * @throws Exception when something failed
	 */
	public void registerFlag(Flag<?> flag) throws Exception {
		Field f1 = DefaultFlag.class.getDeclaredField("flagsList");
		Field f2 = Field.class.getDeclaredField("modifiers");
		f2.setAccessible(true);
		f2.setInt(f1, f1.getModifiers() &~Modifier.FINAL);
		f1.setAccessible(true);

		Flag<?>[] flags = DefaultFlag.getFlags();

		List<Flag<?>> flaglist = Arrays.asList(flags);
		if(!flaglist.contains(flag)) {
			Flag<?>[] newflags = Arrays.copyOf(flags, flags.length+1);
			newflags[newflags.length-1] = flag;
			f1.set(null, newflags);	
		}

		f1.setAccessible(false);
		f2.setAccessible(false);
		reloadWG();
		ManCo.log(LogType.INFO, "flag registered for worldguard: " + flag.getName());
	}

	/**
	 * unregisters the mob-protect flag
	 * 
	 * @author xize
	 * @param flag - the flag or StateFlag to be injected/removed in worldguard
	 * @throws Exception when something failed
	 */
	public void unregisterFlag(Flag<?> flag) throws Exception {
		Field f1 = DefaultFlag.class.getDeclaredField("flagsList");
		Field f2 = Field.class.getDeclaredField("modifiers");
		f2.setAccessible(true);
		f2.setInt(f1, f1.getModifiers() &~Modifier.FINAL);
		f1.setAccessible(true);

		Flag<?>[] flags = DefaultFlag.getFlags();

		List<Flag<?>> flaglist = Arrays.asList(flags);
		if(flaglist.contains(flag)) {
			flaglist.remove(flag);
			Flag<?>[] newflags = flaglist.toArray(new Flag<?>[flaglist.size()]);
			f1.set(null, newflags);	
		}

		f1.setAccessible(false);
		f2.setAccessible(false);
		reloadWG();
		ManCo.log(LogType.INFO, "flag unregistered for worldguard: " + flag.getName());
	}

	/**
	 * returns true whenever the location is inside a region otherwise false
	 * 
	 * @author xize
	 * @param loc - the location to be checked on
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean isInRegion(Location loc) {
		try {
			Iterable<ProtectedRegion> regions = (Iterable<ProtectedRegion>) wg.getRegionManager(loc.getWorld()).getClass().getMethod("getApplicableRegions", Location.class).invoke(wg.getRegionManager(loc.getWorld()), loc);
			return regions.iterator().hasNext();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * returns the ProtectedRegion from the location however its better to use this after {@link #isInRegion(Location loc)} check is used
	 * 
	 * @author xize
	 * @param loc - the location where the region is in
	 * @return ProtectedRegion
	 */
	@SuppressWarnings("unchecked")
	public Iterator<ProtectedRegion> getRegion(Location loc) {
		try {
			Iterable<ProtectedRegion> regions = (Iterable<ProtectedRegion>) wg.getRegionManager(loc.getWorld()).getClass().getMethod("getApplicableRegions", Location.class).invoke(wg.getRegionManager(loc.getWorld()), loc);
			return regions.iterator();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * returns true if the flag is set to allow or deny in a specific location presenting the region
	 * 
	 * @author xize
	 * @param flag - the flag state which we will check
	 * @param loc - the location presenting the region
	 * @return boolean
	 */
	public boolean isFlagAllowed(StateFlag flag, Location loc) {
		try {
			Object obj = wg.getRegionManager(loc.getWorld()).getClass().getMethod("getApplicableRegions", Location.class).invoke(wg.getRegionManager(loc.getWorld()), loc);
			Method m1 = obj.getClass().getMethod("allows", StateFlag.class);
			boolean bol = (Boolean)m1.invoke(obj, flag);
			return bol;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * returns true if the flag is set to allow or deny in a specific location presenting the region
	 * 
	 * @author xize
	 * @param flag - the flag state which we will check
	 * @param loc - the location presenting the region
	 * @return boolean
	 */
	public boolean isMember(Player p, Location loc) {
		try {
			LocalPlayer lp = wg.wrapPlayer(p);
			Object obj = wg.getRegionManager(loc.getWorld()).getClass().getMethod("getApplicableRegions", Location.class).invoke(wg.getRegionManager(loc.getWorld()), loc);
			Method m1 = obj.getClass().getMethod("isMemberOfAll", LocalPlayer.class);
			boolean bol = (p.isOp() ? true : (Boolean)m1.invoke(obj, lp));
			return bol;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * returns the manco's spawn flag for worldguard regions
	 * 
	 * @author xize
	 * @return StateFlag
	 */
	public StateFlag getManCoSpawnFlag() {
		return ManCoSpawn;
	}

	private void reloadWG() {
		WorldGuardPlugin wg = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		try {
			Method m1 = wg.getClass().getMethod("getRegionContainer");
			Object regionmgr = m1.invoke(wg);
			Method m2 = regionmgr.getClass().getMethod("reload");
			m2.invoke(regionmgr);
			//wg.getRegionContainer().reload();
		} catch(NoSuchMethodException e) {
			ManCo.log(LogType.SEVERE, "it seems you are using a old version of worldguard we will recommend to use 6.0 or higher!, reloading worldguard by command.");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wg reload");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private class ManCoFlag extends StateFlag {

		public ManCoFlag() {
			super("crate-spawn", true);
		}
		
	}

}
