package tv.mineinthebox.manco.events.schematics;

import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.instances.Schematic;
import tv.mineinthebox.manco.instances.SchematicBuilder;


public class SchematicPasteEvent implements Listener {
	
	private final ManCo pl;
	
	public SchematicPasteEvent(ManCo pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getItem() != null) {
				if(e.getItem().hasItemMeta()) {
					if(e.getItem().getItemMeta().hasDisplayName()) {
						pl.getSchematicUtils().initSchematics();
						if(isSchematic(e.getItem().getItemMeta().getDisplayName())) {
							Schematic schem = getSchematic(e.getItem().getItemMeta().getDisplayName());
							if(pl.getHooks().isWorldGuardEnabled()) {
								if(pl.getHookManager().getWorldguardHook().isInRegion(e.getClickedBlock().getLocation())) {
									e.getPlayer().sendMessage(ChatColor.RED + "you cannot use this inside a worldguard protected region!");
									e.setCancelled(true);
									return;
								}
							}
							SchematicBuilder build = new SchematicBuilder(schem, e.getClickedBlock().getRelative(BlockFace.UP).getLocation(), e.getPlayer(), pl);
							e.getPlayer().sendMessage(ChatColor.GREEN + "you are successfully building the " + build.getSchematic().getName() + " house!");
							if(e.getItem().getAmount() == 1) {
								e.getPlayer().setItemInHand(null);
							} else {
								e.getItem().setAmount(e.getItem().getAmount()-1);
							}
							build.startGeneration(EntityType.ENDERMAN);
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
	private Schematic getSchematic(String s) {
		String[] args = s.split(":");
		return pl.getSchematicUtils().getByName(args[1]);
	}
	
	private boolean isSchematic(String name) {
		String reg = ChatColor.GOLD + "\\[schematic\\]"+ChatColor.GRAY+"\\:"+"(.*?)";
		return Pattern.matches(reg, name);
	}

}
