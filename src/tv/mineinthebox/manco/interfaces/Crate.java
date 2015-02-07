package tv.mineinthebox.manco.interfaces;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import tv.mineinthebox.manco.enums.CrateType;

public interface Crate {
	
	/**
	 * returns the minium slots required for the random items in the chest
	 * 
	 * @author xize
	 * @return int
	 */
	public int getMiniumSlots();

	/**
	 * returns the crate series name
	 * 
	 * @author xize
	 * @return String
	 */
	public String getCrateName();

	/**
	 * returns true if this crate is enabled, otherwise false
	 * 
	 * @author xize
	 * @return boolean
	 */
	public boolean isEnabled();


	/**
	 * returns true if this crate series needs a key otherwise false
	 * 
	 * @author xize
	 * @return boolean
	 */
	public boolean needsKey();

	/**
	 * returns the economy price of the key of this crate
	 * 
	 * @author xize
	 * @param returns the price of the key
	 * @return Double
	 */
	public double getKeyPrice();

	/**
	 * returns the key item of this crate serie
	 * 
	 * @author xize
	 * @param returns a ItemStack which is based to be a key!
	 * @return ItemStack
	 */
	public ItemStack getKeyItem();

	/**
	 * enables the crate to be used
	 * 
	 * @author xize
	 * @param enables or disables the crate type.
	 * @param bol - when enabled the crate will be used else it will not.
	 */
	public void setEnabled(boolean bol);

	/**
	 * returns the type of crate of this series
	 * 
	 * @author xize
	 * @param returns the crate type, this should be used only for in the super interface
	 * @return CrateType
	 */
	public CrateType getType();

	/**
	 * sets the crate type to rare or normal
	 * 
	 * @author xize
	 * @param sets the type of the crate.
	 * @param type - the CrateType
	 */
	public void setType(CrateType type);

	/**
	 * removes the crate from the configuration and memory
	 * 
	 * @author xize
	 * @param removes the crate
	 */
	public void remove();

	/**
	 * returns the random items from the crate series
	 * 
	 * @author xize
	 * @param returns the random items!
	 * @return ItemStack[]
	 */
	public List<ItemStack> getRandomItems();

	/**
	 * sets the random items from this crate series
	 * 
	 * @author xize
	 * @param sets the new random items.
	 */
	public void setRandomItems(ItemStack[] items);

}
