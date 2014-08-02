package tv.mineinthebox.manco.api.customevents;

import org.bukkit.event.Event;

import tv.mineinthebox.manco.enums.CrateType;
import tv.mineinthebox.manco.instances.NormalCrate;

public abstract class CrateEvent extends Event {
	
	private final NormalCrate crate;
	
	public CrateEvent(NormalCrate crate) {
		this.crate = crate;
	}
	
	/**
	 * @author xize
	 * @param returns the crate name.
	 * @return String
	 */
	public String getCrateName() {
		return crate.getCrateName();
	}
	
	/**
	 * @author xize
	 * @param returns the crate type
	 * @return CrateType
	 */
	public CrateType getCrateType() {
		return crate.getType();
	}
	
	/**
	 * @author xize
	 * @param returns the crate
	 * @return Crate
	 */
	public NormalCrate getCrate() {
		return crate;
	}
	
	/**
	 * @author xize
	 * @param removes the crate type.
	 */
	public void remove() {
		crate.remove();
	}
	
	/**
	 * @author xize
	 * @param disables this type crate.
	 */
	public void disable() {
		crate.setEnabled(false);
	}
}
