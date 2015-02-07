package tv.mineinthebox.manco.api.customevents;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import tv.mineinthebox.manco.instances.CratePlayer;
import tv.mineinthebox.manco.interfaces.Crate;

public class CrateFallEvent extends CrateEvent implements Chest {

	private final Chest chest;
	private static final HandlerList handlers = new HandlerList();
	private final CratePlayer p;
	
	public CrateFallEvent(CratePlayer p, Chest chest, Crate crate) {
		super(crate);
		this.chest = chest;
		this.p = p;
	}
	
	public CratePlayer getCratePlayer() {
		return p;
	}

	public Block getBlock() {
		return chest.getBlock();
	}

	public Chunk getChunk() {
		return chest.getChunk();
	}

	public MaterialData getData() {
		return chest.getData();
	}

	public byte getLightLevel() {
		return chest.getLightLevel();
	}

	public Location getLocation() {
		return chest.getLocation();
	}

	public Location getLocation(Location arg0) {
		return chest.getLocation(arg0);
	}

	@Deprecated
	public byte getRawData() {
		return chest.getRawData();
	}

	@Deprecated
	public int getTypeId() {
		return chest.getTypeId();
	}

	public World getWorld() {
		return chest.getWorld();
	}

	public int getX() {
		return chest.getX();
	}

	public int getY() {
		return chest.getY();
	}

	public int getZ() {
		return chest.getZ();
	}

	public void setData(MaterialData arg0) {
		chest.setData(arg0);
	}

	@Deprecated
	public void setRawData(byte arg0) {
		chest.setRawData(arg0);
	}

	public void setType(Material arg0) {
		chest.setType(arg0);
	}

	@Deprecated
	public boolean setTypeId(int arg0) {
		return chest.setTypeId(arg0);
	}

	public boolean update() {
		return chest.update();
	}

	public boolean update(boolean arg0) {
		return chest.update(arg0);
	}
	
	public boolean update(boolean arg0, boolean arg1) {
		return chest.update(arg0, arg1);
	}

	public List<MetadataValue> getMetadata(String arg0) {
		return chest.getMetadata(arg0);
	}

	public boolean hasMetadata(String arg0) {
		return chest.hasMetadata(arg0);
	}

	public void removeMetadata(String arg0, Plugin arg1) {
		chest.removeMetadata(arg0, arg1);
	}

	public void setMetadata(String arg0, MetadataValue arg1) {
		chest.setMetadata(arg0, arg1);
	}

	public Inventory getInventory() {
		return chest.getInventory();
	}

	public Inventory getBlockInventory() {
		return chest.getBlockInventory();
	}

	public Material getType() {
		return chest.getType();
	}

	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
