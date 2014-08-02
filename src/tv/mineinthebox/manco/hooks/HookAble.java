package tv.mineinthebox.manco.hooks;


public class HookAble {
	
	private CoreProtectHook core;
	private LogBlockHook logblock;
	private PrismHook prism;
	private VaultHook vault;
	private WorldGuardHook worldguard;
	private NCPHook ncp;
	
	public CoreProtectHook getCoreProtectHook() {
		if(!(this.core instanceof CoreProtectHook)) {
			this.core = new CoreProtectHook();
		}
		return this.core;
	}
	
	public LogBlockHook getLogBlockHook() {
		if(!(this.logblock instanceof LogBlockHook)) {
			this.logblock = new LogBlockHook();
		}
		return this.logblock;
	}
	
	public PrismHook getPrismHook() {
		if(!(this.prism instanceof PrismHook)) {
			this.prism = new PrismHook();
		}
		return this.prism;
	}
	
	public VaultHook getVaultHook() {
		if(!(this.vault instanceof VaultHook)) {
			this.vault = new VaultHook();
		}
		return this.vault;
	}
	
	public WorldGuardHook getWorldguardHook() {
		if(!(this.worldguard instanceof WorldGuardHook)) {
			this.worldguard = new WorldGuardHook();
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
