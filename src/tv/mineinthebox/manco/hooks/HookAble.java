package tv.mineinthebox.manco.hooks;

import tv.mineinthebox.manco.ManCo;


public class HookAble {
	
	private VaultHook vault;
	private WorldGuardHook worldguard;
	private NCPHook ncp;
	
	private final ManCo pl;
	
	public HookAble(ManCo pl) {
		this.pl = pl;
	}
	
	public VaultHook getVaultHook() {
		if(!(this.vault instanceof VaultHook)) {
			this.vault = new VaultHook();
		}
		return this.vault;
	}
	
	public WorldGuardHook getWorldguardHook() {
		if(!(this.worldguard instanceof WorldGuardHook)) {
			this.worldguard = new WorldGuardHook(pl);
		}
		return this.worldguard;
	}
	
	public NCPHook getNcpHook() {
		if(!(this.ncp instanceof NCPHook)) {
			this.ncp = new NCPHook();
		}
		return this.ncp;
	}

}
