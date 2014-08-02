package tv.mineinthebox.manco.instances;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.enums.CrateType;
import tv.mineinthebox.manco.enums.LogType;
import tv.mineinthebox.manco.exceptions.InvalidCrateException;

public class NormalCrate {

	private final File f;
	private final FileConfiguration con;
	private final String crate;

	public NormalCrate(String crate) {
		this.f = ManCo.getConfiguration().getFile();
		this.con = ManCo.getConfiguration().getConfig();
		this.crate = crate;
		if(!this.con.contains("crates.crate."+crate.toLowerCase())) {
			try {
				throw new InvalidCrateException("crate name is invalid");
			} catch (InvalidCrateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @author xize
	 * @param returns the minium slots aquired to be randomized by items.
	 * @return Integer
	 */
	public int getMiniumSlots() {
		int slots = con.getInt("crates.crate."+ crate +".miniumSlotsFilled");
		if(!(slots > InventoryType.CHEST.getDefaultSize() || slots < 0)) {
			return con.getInt("crates.crate."+ crate +".miniumSlotsFilled");	
		} else {
			throw new IndexOutOfBoundsException(
					"an crate can only hold a maximun from 1 to "+InventoryType.CHEST.getDefaultSize()+" slots\n" +
							"the name of this crate is: " + getCrateName()+" and its size is: " + slots + "\n" +
							"detailed walktrough what happends:\n"
					);
		}
	}

	/**
	 * @author xize
	 * @param returns the crate name
	 * @return String
	 */
	public String getCrateName() {
		return crate;
	}

	/**
	 * @author xize
	 * @param returns true if the crate is enabled
	 * @return Boolean
	 */
	public boolean isEnabled() {
		return con.getBoolean("crates.crate."+crate+".isEnabled");
	}


	/**
	 * @author xize
	 * @param returns true if the crate needs a special key, else false
	 * @return Boolean
	 */
	public boolean needsKey() {
		return con.getBoolean("crates.crate."+ getCrateName() +".keyEnable");
	}

	/**
	 * @author xize
	 * @param returns the price of the key
	 * @return Double
	 */
	public double getKeyPrice() {
		return con.getDouble("crates.crate."+ getCrateName() +".keyPrice");
	}

	/**
	 * @author xize
	 * @param returns a ItemStack which is based to be a key!
	 * @return ItemStack
	 */
	@SuppressWarnings("deprecation")
	public ItemStack getKeyItem() {
		try {
			ItemStack stack = null;
			if(con.get("crates.crate."+getCrateName()+".keyItem") instanceof Integer) {
				stack = new ItemStack(Material.getMaterial(con.getInt("crates.crate."+getCrateName()+".keyItem")), 1);
			} else if(con.get("crates.crate."+getCrateName()+".keyItem") instanceof String) {
				stack = new ItemStack(Material.getMaterial(con.getString("crates.crate."+getCrateName()+".keyItem").toUpperCase()), 1);
			}
			ItemMeta meta = stack.getItemMeta();
			if(getType() == CrateType.NORMAL) {
				meta.setDisplayName(ChatColor.GOLD + "[ManCo key]" + ChatColor.GRAY + " type: " + getCrateName());
				meta.setLore(Arrays.asList(new String[] {
						ChatColor.GRAY + "for type: " + crate,
						"",
						ChatColor.GRAY + "description: " + "this key is hand crafted by one of our best engineers!",
						"",
						ChatColor.GRAY + "possible chance on contents: ",
						ChatColor.GREEN + getPossibleList()
						}));
			} else {
				meta.setDisplayName(ChatColor.GOLD + "[ManCo key]" + ChatColor.GRAY + " type: " + ChatColor.DARK_PURPLE + getCrateName());
				meta.setLore(Arrays.asList(new String[] {
						ChatColor.GRAY + "for type: " + ChatColor.DARK_PURPLE + crate,
						"",
						ChatColor.GRAY + "description: " + "this key is hand crafted by one of our finest engineers!",
						"",
						ChatColor.GRAY + "possible chance on contents: ",
						ChatColor.GREEN + getPossibleList()
						}));
			}
			stack.setItemMeta(meta);
			return stack;
		} catch(Exception e) {
			ManCo.log(LogType.SEVERE, "one of the items in crate " + getCrateName() + " has a invalid item id as key set.");
		}
		return null;
	}

	/**
	 * @author xize
	 * @param enables or disables the crate type.
	 * @param bol - when enabled the crate will be used else it will not.
	 */
	public void setEnabled(boolean bol) {
		con.set("crates.crate."+crate+".isEnabled", bol);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reload();
	}

	/**
	 * @author xize
	 * @param returns the crate type, this should be used only for in the super interface
	 * @return CrateType
	 */
	public CrateType getType() {
		if(con.getBoolean("crates.crate." + crate + ".isRare")) {
			return CrateType.RARE;
		}
		return CrateType.NORMAL;
	}

	/**
	 * @author xize
	 * @param sets the type of the crate.
	 * @param type - the CrateType
	 */
	public void setType(CrateType type) {
		con.set("crates.crate."+crate+".isRare", (type == CrateType.RARE ? true : false));
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reload();
	}

	/**
	 * @author xize
	 * @param removes the crate
	 */
	public void remove() {
		con.set("crates.crate."+crate, null);
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
	 * @param returns the random items!
	 * @return ItemStack[]
	 */
	@SuppressWarnings("unchecked")
	public List<ItemStack> getRandomItems() {
		List<ItemStack> stack = Arrays.asList(((List<ItemStack>)con.get("crates.crate."+crate+".items")).toArray(new ItemStack[0]));
		Collections.shuffle(stack);
		return stack;
	}

	/**
	 * @author xize
	 * @param sets the new random items.
	 */
	public void setRandomItems(ItemStack[] items) {

		List<ItemStack> stacks = new ArrayList<ItemStack>();

		for(ItemStack stack : items) {
			if(stack instanceof ItemStack) {
				stacks.add(stack);
			}
		}

		con.set("crates.crate."+getCrateName()+".items", stacks.toArray());
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reload();
	}
	
	private String getPossibleList() {
		StringBuilder build = new StringBuilder();
		build.append("[");
		for(int i = 0; i < (getRandomItems().size() < 3 ? getRandomItems().size() : 3); i++) {
			if(i == ((getRandomItems().size() < 3 ? getRandomItems().size() : 3)-1)) {
				build.append(getRandomItems().get(i).getType().name().toLowerCase() + "]");
			} else {
				build.append(getRandomItems().get(i).getType().name().toLowerCase() + ", ");
			}
		}
		return build.toString();
	}

	/**
	 * @author xize
	 * @param updates the crate.
	 */
	private void reload() {
		try {
			con.load(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
