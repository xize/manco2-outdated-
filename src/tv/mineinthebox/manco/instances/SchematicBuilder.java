package tv.mineinthebox.manco.instances;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.enums.LogType;

public class SchematicBuilder {

	private final Schematic schematic;
	private final Location loc;
	private final Location playerLoc;
	private Player player;
	private boolean warning = false;
	private final ManCo pl;
	
	private final LinkedHashMap<Block, Integer> data = new LinkedHashMap<Block, Integer>();

	public SchematicBuilder(Schematic schematic, Location base, Player player, ManCo pl) {
		this.schematic = schematic;
		this.loc = base;
		this.playerLoc = loc.clone();
		this.loc.setX(loc.getX()-(schematic.getWidth()/2));
		this.loc.setZ(loc.getZ()-(schematic.getLength()/2));
		this.player = player;
		this.pl = pl;
	}

	/**
	 * @author xize
	 * @param returns the schematic
	 * @return Schematic
	 */
	public Schematic getSchematic() {
		return schematic;
	}

	/**
	 * @author xize
	 * @param slowly generates the schematic from bottom to highest.
	 */
	public void startGeneration(final EntityType type) {
		new BukkitRunnable() {

			private Iterator<Entry<Block, Integer>> it;
			private LivingEntity entity;

			public void setData() {
				for (int y = 0; y < schematic.getHeight(); y++){
					for(int x = 0; x < schematic.getWidth(); x++){
						for (int z = 0; z < schematic.getLength(); ++z){

							Location temp = loc.clone().add(x, y, z);

							//Location temp = loc.clone().add(x/2, y, z/2); <- pastes the schematic but 1/² smaller this is per accident but pretty cool.

							//Location loc = new Location(temp.getWorld(), temp.getBlockX()/2, temp.getBlockY(), temp.getBlockZ()/2);

							Block block = temp.getBlock();
							int index = y * schematic.getWidth() * schematic.getLength() + z * schematic.getWidth() + x;
							if(getMaterial(schematic.getBlocks()[index]) != Material.AIR) {
								data.put(block, index);	
							}
						}
					}
				}
			}

			public void setupBuilderEntity() {
				if(this.entity instanceof LivingEntity) {
					if(this.entity.isDead()) {
						this.entity = null;
						this.entity = (LivingEntity) playerLoc.getWorld().spawnEntity(playerLoc, type);
						this.entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1));
						this.entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
						this.entity.setCustomName(ChatColor.GOLD + "[ManCo]"+ChatColor.WHITE + "builder!");
						this.entity.setCustomNameVisible(true);
					} else {
						return;
					}
				} else {
					this.entity = (LivingEntity) playerLoc.getWorld().spawnEntity(playerLoc, type);
					this.entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1));
					this.entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
					this.entity.setCustomName(ChatColor.GOLD + "[ManCo]"+ChatColor.WHITE + "builder!");
					this.entity.setCustomNameVisible(true);
				}
			}

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if(data.isEmpty()) {
					setData();
				}

				setupBuilderEntity();

				if(!(it instanceof Iterator)) {
					this.it = data.entrySet().iterator();
				}

				if(it instanceof Iterator) {
					if(it.hasNext()) {
						Entry<Block, Integer> entry = it.next();
						Block block = entry.getKey();
						int index = entry.getValue();
						Material dataValue = getMaterial(schematic.getBlocks()[index]);
						byte subValue = schematic.getData()[index];

						if(entity instanceof Enderman) {
							Enderman enderman = (Enderman) entity;
							if(subValue == 0) {
								enderman.setCarriedMaterial(new MaterialData(dataValue));
							} else {
								enderman.setCarriedMaterial(new MaterialData(dataValue.getId(), subValue));	
							}
						}

						if(block.getType() == Material.AIR) {
							
							try {
								if(!block.getChunk().isLoaded()) {
									block.getChunk().load();
								}

								if(subValue == 0) {
									block.setType(dataValue);
								} else {
									block.setTypeIdAndData(dataValue.getId(), subValue, true);	
								}
								saveRollback(block, player);
								block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, dataValue);
								entity.teleport(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getLocation());
								entity.setHealth(entity.getMaxHealth());
							}catch(NullPointerException e) {e.printStackTrace();}
						}
						it.remove();
					} else {
						entity.remove();
						data.clear();
						cancel();
					}
				}

			}

		}.runTaskTimer(pl, 0L, 1L);
	}

	@SuppressWarnings("deprecation")
	public Material getMaterial(int id) {
		try {
			Material mat = Material.getMaterial(id);
			mat.getId();
			return mat;
		} catch(NullPointerException e) {
			if(!warning) {
				ManCo.log(LogType.SEVERE, "Warning we don't know anything about data value " + id + " at schematic " + schematic.getName() + " perhaps this schematic is made in a newer minecraft version or mod?");
				ManCo.log(LogType.SEVERE, "however this block will be changed to air, and the generation could have unpredictal results.");	
			}
			return Material.AIR;
		}
	}

	private void saveRollback(Block block, Player p) {
		if(p instanceof Player) {
			if(pl.getHooks().isNCPEnabled()) {
				if(!pl.getHookManager().getNcpHook().isBlockPlaceExempted(p.getName())) {
					pl.getHookManager().getNcpHook().exemptBlockPlaceHacks(p);
				}
			}
			Bukkit.getPluginManager().callEvent(new BlockPlaceEvent(block, null, block.getRelative(BlockFace.DOWN), (p.getItemInHand() != null ? p.getItemInHand() : new ItemStack(block.getType())), p, true));
			if(pl.getHooks().isNCPEnabled()) {
				if(!pl.getHookManager().getNcpHook().isBlockPlaceExempted(p.getName())) {
					pl.getHookManager().getNcpHook().unExcemptBlockPlaceHacks(p);
				}
			}
		}
	}
}