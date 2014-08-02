package tv.mineinthebox.manco.instances;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import tv.mineinthebox.manco.ManCo;

public class RareCrate extends NormalCrate {

	private boolean effects = false;
	private Effect effect;
	private Sound sound;
	private final FileConfiguration con;
	private final File f;
	private String crate;

	public RareCrate(String crate) {
		super(crate);
		this.f = ManCo.getConfiguration().getFile();
		this.con = ManCo.getConfiguration().getConfig();
		this.crate = crate;
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
