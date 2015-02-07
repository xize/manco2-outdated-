package tv.mineinthebox.manco.interfaces;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import tv.mineinthebox.manco.enums.CrateType;
import tv.mineinthebox.manco.instances.CratePlayer;

public interface ManCoApi {
	
	/**
	 * returns the crate player
	 * 
	 * @author xize
	 * @param name - the players name
	 * @return CratePlayer
	 * @throws NullPointerException - when the player does not exist.
	 */
	public CratePlayer getCratePlayer(String name) throws NullPointerException;

	/**
	 * returns all crate players
	 * 
	 * @author xize
	 * @param returns all the crate player instances.
	 * @return CratePlayer[]
	 */
	public CratePlayer[] getCratePlayers();

	/**
	 * returns a normal crate
	 * 
	 * @author xize
	 * @param name - the crate name
	 * @return Crate
	 * @throws NullPointerException - when the crate is a invalid crate.
	 */
	public Crate getCrateSerie(String name);

	/**
	 * removes a crate serie from the configuration
	 * 
	 * @author xize
	 * @param name - remove a crate serie
	 * @throws NullPointerException - throws when the crate doesn't exist.
	 */
	public void removeCrateSerie(String name) throws NullPointerException;

	/**
	 * spawns a crate for a {@link CratePlayer}
	 * 
	 * @author xize
	 * @param p - the CratePlayer
	 * @param crate - the Crate serie
	 * @return returns true if the crate is spawned, else false a player can only have one crate per time.
	 */
	public Crate spawnCrate(CratePlayer p, Crate crate);

	/**
	 * spawns a crate for a {@link CratePlayer}
	 * 
	 * @author xize
	 * @param p - CratePlayer
	 * @param crate - the Crate serie
	 * @param loc - the location where the crate would drop.
	 * @return Boolean - when true the crate is spawned otherwise false.
	 */
	public Crate spawnCrate(CratePlayer p, Crate crate, Location loc);

	/**
	 * adds a new crate serie inside the configuration
	 * 
	 * @author xize
	 * @param serie - the crate serie.
	 * @param type - the CrateType whenever the crate should be rare or normal.
	 * @param items - the contents.
	 */
	public Crate addCrateSerie(String serie, CrateType type, ItemStack[] items);

	/**
	 * adds a new crate serie inside the configuration
	 * 
	 * @author xize
	 * @param serie - the serie type
	 * @param type - should the crate be rare or normal?
	 * @param items - the contents.
	 * @param enable - should the crate be enabled?
	 */
	public Crate addCrateSerie(String serie, CrateType type, ItemStack[] items, boolean enable);

	/**
	 * adds a new crate serie inside the configuration
	 * 
	 * @author xize
	 * @param serie - the serie type
	 * @param type - the CrateType
	 * @param items - the contents
	 * @param enable - should this crate be enabled?
	 * @param isRareEffects - should the crate have rare effects?
	 */
	public Crate addCrateSerie(String serie, CrateType type, ItemStack[] items, boolean enable, boolean isRareEffects);

	/**
	 * adds a new crate serie inside the configuration
	 * 
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
	public Crate addCrateSerie(String serie, CrateType type, ItemStack[] items, boolean enable, boolean isRareEffects, boolean keyEnable, Material key, Double price);

	/**
	 * adds a new crate serie inside the configuration
	 * 
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
	public Crate addCrateSerie(String serie, CrateType type, ItemStack[] items, boolean enable, boolean isRareEffects, boolean keyEnable, Material key, Double price, int miniumSlots);
	
	/**
	 * returns true if the given name is a crate name otherwise false
	 * 
	 * @author xize
	 * @param name - the name of the crate
	 * @return Boolean
	 */
	public boolean isCrate(String name);
	
	/**
	 * returns all crates
	 * 
	 * @author xize
	 * @return Crate[]
	 */
	public Crate[] getCrates();

}
