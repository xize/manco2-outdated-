package tv.mineinthebox.manco.instances;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.interfaces.Crate;

public class CratePlayer implements Comparable<String> {
	
	private final Player p;
	private final ManCo pl;
	
	public CratePlayer(Player p, ManCo pl) {
		this.p = p;
		this.pl = pl;
	}
	
	/**
	 * returns the player instance
	 * 
	 * @author xize
	 * @return Player
	 */
	public Player getPlayer() {
		return p;
	}
	
	/**
	 * returns true whenever the player has a crate otherwise false
	 * 
	 * @author xize
	 * @return Boolean
	 */
	public boolean hasCrate() {
		return (pl.getCrateOwners().contains(p.getName()) && p.hasMetadata("crate"));
	}
	
	public Crate getCrate() {
		if(hasCrate()) {
			return pl.getCrate(getCrateChest().getMetadata("crate_serie").get(0).asString());
		}
		return null;
	}
	
	/**
	 * returns the chest
	 * 
	 * @author xize
	 * @return Chest
	 * @throws NullPointerException - when the chest is not fallen yet or just doesn't exist.
	 */
	public Chest getCrateChest() {
		if(p.hasMetadata("crate")) {
			return (Chest)((FixedMetadataValue)p.getMetadata("crate").get(0)).value();
		}
		throw new NullPointerException("player has no crate chest.");
	}
	
	/**
	 * removes all memory allocated objects of the crate the player owns
	 * 
	 * @author xize
	 */
	public void remove() {
		if(pl.getCrateOwners().contains(p.getName())) {
			pl.getCrateOwners().remove(p.getName());
		}
		if(p.hasMetadata("crate")) {
			Chest chest = getCrateChest();
			chest.getInventory().clear();
			chest.removeMetadata("crate_owner", pl);
			chest.removeMetadata("crate_serie", pl);
			if(chest.hasMetadata("crate")) {
				System.out.println("chest has metadata key \"crate\"");
			}
			//chest.removeMetadata("crate", pl);
			chest.getBlock().setType(Material.AIR);
			p.removeMetadata("crate", pl);
		}
	}

	@Override
	public int compareTo(String o) {
		return o.compareTo(p.getName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p == null) ? 0 : p.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CratePlayer other = (CratePlayer) obj;
		if (p == null) {
			if (other.p != null)
				return false;
		} else if (!p.equals(other.p))
			return false;
		return true;
	}
}
