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
import tv.mineinthebox.manco.utils.SchematicUtils;


public class SchematicPasteEvent implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getItem() != null) {
				if(e.getItem().hasItemMeta()) {
					if(e.getItem().getItemMeta().hasDisplayName()) {
						SchematicUtils.initSchematics();
						if(isSchematic(e.getItem().getItemMeta().getDisplayName())) {
							Schematic schem = getSchematic(e.getItem().getItemMeta().getDisplayName());
							if(ManCo.getHooks().isWorldGuardEnabled()) {
								if(ManCo.getHookManager().getWorldguardHook().isInRegion(e.getClickedBlock().getLocation())) {
									e.getPlayer().sendMessage(ChatColor.RED + "you cannot use this inside a worldguard protected region!");
									e.setCancelled(true);
									return;
								}
							}
							SchematicBuilder build = new SchematicBuilder(schem, e.getClickedBlock().getRelative(BlockFace.UP).getLocation(), e.getPlayer());
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
		return SchematicUtils.getByName(args[1]);
	}
	
	private boolean isSchematic(String name) {
		String reg = ChatColor.GOLD + "\\[schematic\\]"+ChatColor.GRAY+"\\:"+"(.*?)";
		return Pattern.matches(reg, name);
	}

}
