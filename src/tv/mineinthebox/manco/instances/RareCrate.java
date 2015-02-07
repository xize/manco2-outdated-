package tv.mineinthebox.manco.instances;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.enums.CrateType;
import tv.mineinthebox.manco.enums.LogType;
import tv.mineinthebox.manco.interfaces.Crate;

public class RareCrate implements Crate {

	private final ManCo pl;
	private final FileConfiguration con;
	private final File f;
	
	private boolean effects = false;
	private Effect effect;
	private Sound sound;
	private String crate;

	public RareCrate(String crate, ManCo pl) {
		this.pl = pl;
		this.f = pl.getConfiguration().getFile();
		this.con = pl.getConfiguration().getConfig();
		this.crate = crate;
	}
	
	@Override
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

	@Override
	public String getCrateName() {
		return crate;
	}

	@Override
	public boolean isEnabled() {
		return con.getBoolean("crates.crate."+crate+".isEnabled");
	}


	@Override
	public boolean needsKey() {
		return con.getBoolean("crates.crate."+ getCrateName() +".keyEnable");
	}

	@Override
	public double getKeyPrice() {
		return con.getDouble("crates.crate."+ getCrateName() +".keyPrice");
	}

	@Override
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

	@Override
	public void setEnabled(boolean bol) {
		con.set("crates.crate."+crate+".isEnabled", bol);
		try {
			con.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		reload();
	}

	@Override
	public CrateType getType() {
		if(con.getBoolean("crates.crate." + crate + ".isRare")) {
			return CrateType.RARE;
		}
		return CrateType.NORMAL;
	}

	@Override
	public void setType(CrateType type) {
		con.set("crates.crate."+crate+".isRare", (type == CrateType.RARE ? true : false));
		try {
			con.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		reload();
	}

	@Override
	public void remove() {
		con.set("crates.crate."+crate, null);
		try {
			con.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		pl.getConfiguration().reload();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ItemStack> getRandomItems() {
		List<ItemStack> stack = Arrays.asList(((List<ItemStack>)con.get("crates.crate."+crate+".items")).toArray(new ItemStack[0]));
		Collections.shuffle(stack);
		return stack;
	}

	@Override
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
	 * @param returns true if the crate has effects
	 * @return Boolean
	 */
	public boolean hasEffects() {
		reload();
		if(con.contains("crates.crate."+crate+".rareEffects")) {
			this.effects = con.getBoolean("crates.crate."+crate+".rareEffects");
		}
		return effects;
	}

	/**
	 * @author xize
	 * @param sets the effects, sound and effect, however effects or sounds can be null.
	 * @param effect - the effect what will play on the schedule.
	 * @param sound - the sound that will play per tick.
	 */
	public void setEffects(Effect effect, Sound sound) {
		this.effects = true;
		this.effect = effect;
		this.sound = sound;
	}

	/**
	 * @author xize
	 * @param removes the effects, this include with sounds aswell.
	 */
	public void removeEffects() {
		this.effects = false;
		this.effect = null;
		this.sound = null;
	}

	/**
	 * @author xize
	 * @param returns the effect
	 * @return Effect
	 */
	public Effect getEffect() {
		if(!(effect instanceof Effect)) {
			if(hasEffects()) {
				return Effect.MOBSPAWNER_FLAMES;
			}
		}
		return (effect instanceof Effect ? effect : null);
	}

	/**
	 * @author xize
	 * @param returns the sound
	 * @return Sound
	 */
	public Sound getEffectSound() {
		if(!(sound instanceof Sound)) {
			if(hasEffects()) {
				return Sound.ZOMBIE_WOODBREAK;
			}
		}
		return (sound instanceof Sound ? sound : null);
	}

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
