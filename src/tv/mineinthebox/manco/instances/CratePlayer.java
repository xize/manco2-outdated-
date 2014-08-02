package tv.mineinthebox.manco.instances;

import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import tv.mineinthebox.manco.ManCo;

public class CratePlayer implements Comparable<String> {
	
	Player p;
	
	public CratePlayer(Player p) {
		this.p = p;
	}
	
	/**
	 * @author xize
	 * @param returns the player instance.
	 * @return Player
	 */
	public Player getPlayer() {
		return p;
	}
	
	/**
	 * @author xize
	 * @param returns true whenever the player has a crate.
	 * @return Boolean
	 */
	public boolean hasCrate() {
		return (ManCo.getPlugin().getCrateOwners().contains(p.getName()) && p.hasMetadata("crate"));
	}
	
	/**
	 * @author xize
	 * @param returns the Chest this player owns.
	 * @return Chest
	 * @throws NullPointerException - when the chest is not fallen yet or just doesn't exist.
	 */
	public Chest getCrateChest() {
		if(p.hasMetadata("crate")) {
			return (Chest)((FixedMetadataValue)p.getMetadata("crate").get(0)).value();
		}
		throw new NullPointerException("player has no crate chest.");
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
