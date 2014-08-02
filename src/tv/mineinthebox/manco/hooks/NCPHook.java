package tv.mineinthebox.manco.hooks;

import org.bukkit.entity.Player;

import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;

public class NCPHook {

	public void exemptBlockPlaceHacks(Player p) {
		NCPExemptionManager.exemptPermanently(p, CheckType.BLOCKPLACE);
		//NCPExemptionManager.exemptPermanently(p, CheckType.FIGHT_NOSWING);
	}
	
	public void unExcemptBlockPlaceHacks(Player p) {
		NCPExemptionManager.unexempt(p, CheckType.BLOCKPLACE);
	}
	
	public boolean isBlockPlaceExempted(String p) {
		return NCPExemptionManager.isExempted(p, CheckType.BLOCKPLACE);
	}

}
