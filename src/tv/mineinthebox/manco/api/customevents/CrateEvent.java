package tv.mineinthebox.manco.api.customevents;

import org.bukkit.event.Event;

import tv.mineinthebox.manco.enums.CrateType;
import tv.mineinthebox.manco.interfaces.Crate;

public abstract class CrateEvent extends Event {
	
	private final Crate crate;
	
	public CrateEvent(Crate crate) {
		this.crate = crate;
	}
	
	/**
	 * returns the crate name
	 * 
	 * @author xize
	 * @return String
	 */
	public String getCrateName() {
		return crate.getCrateName();
	}
	
	/**
	 * returns the crate type of the crate in this event
	 * 
	 * @author xize
	 * @return CrateType
	 */
	public CrateType getCrateType() {
		return crate.getType();
	}
	
	/**
	 * returns the crate being associated in this event
	 * 
	 * @author xize
	 * @return Crate
	 */
	public Crate getCrate() {
		return crate;
	}
	
	/**
	 * removes the crate from the configuration
	 * 
	 * @author xize
	 */
	public void remove() {
		crate.remove();
	}
	
	/**
	 * disables the crate serie associated in this event
	 * 
	 * @author xize
	 */
	public void disable() {
		crate.setEnabled(false);
	}
}
