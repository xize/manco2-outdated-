package tv.mineinthebox.manco;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import tv.mineinthebox.manco.enums.CrateType;
import tv.mineinthebox.manco.instances.NormalCrate;
import tv.mineinthebox.manco.instances.RareCrate;
import tv.mineinthebox.manco.instances.Schematic;
import tv.mineinthebox.manco.interfaces.Crate;

public class MancoCommand implements CommandExecutor {
	
	private final ManCo pl;
	
	public MancoCommand(ManCo pl) {
		this.pl = pl;
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(cmd.getName().equalsIgnoreCase("manco")) {
			if(sender.hasPermission("manco.command.manco")) {
				if(args.length == 0) {
					sender.sendMessage(ChatColor.GOLD + ".oO___[ManCo help]___Oo.");
					sender.sendMessage(ChatColor.GREEN + "version: "+pl.getDescription().getVersion()+" made by xize.");
					sender.sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/manco help " + ChatColor.WHITE + ": shows help");
					sender.sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/manco list " + ChatColor.WHITE + ": shows all the keys and prices");
					sender.sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/manco buy <crate> " + ChatColor.WHITE + ": allows to buy a key for a crate");
					if(sender.hasPermission("manco.isadmin")) {
						sender.sendMessage(ChatColor.GREEN + "notice!, for more options please look to the crates inside the config.yml");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco crates " + ChatColor.WHITE + ": shows all the names");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco edit <crate> " + ChatColor.WHITE + ": edit a crate through a inventory.");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco create <crate> " + ChatColor.WHITE + ": creates a new crate through a inventory.");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco delete <crate> " + ChatColor.WHITE + ": delete a crate.");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco disable <crate> " + ChatColor.WHITE + ": disables a crate.");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco enable <crate> " + ChatColor.WHITE + ": enables a crate.");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco setType <crate> <type> " + ChatColor.WHITE + ": here you can define the crate to \"rare\" or \"normal\".");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco spawn <crate> " + ChatColor.WHITE + ": spawn a crate.");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco spawn <player> <serie> " + ChatColor.WHITE + ": spawns a crate for a specific player");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco give money <price> <amount> " + ChatColor.WHITE + ": gives money paper");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco give schematic <name> <amount> " + ChatColor.WHITE + ": gives a schematic egg");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco reload " + ChatColor.WHITE + ": reloads the plugin");
					}
				} else if(args.length == 1) {
					if(args[0].equalsIgnoreCase("help")) {
						sender.sendMessage(ChatColor.GOLD + ".oO___[ManCo help]___Oo.");
						sender.sendMessage(ChatColor.GREEN + "version: "+pl.getDescription().getVersion()+" made by xize.");
						sender.sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/manco help " + ChatColor.WHITE + ": shows help");
						sender.sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/manco list " + ChatColor.WHITE + ": shows all the keys and prices");
						sender.sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/manco buy <crate> " + ChatColor.WHITE + ": allows to buy a key for a crate");
						if(sender.hasPermission("manco.isadmin")) {
							sender.sendMessage(ChatColor.GREEN + "notice!, for more options please look to the crates inside the config.yml");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco crates " + ChatColor.WHITE + ": shows all the names");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco edit <crate> " + ChatColor.WHITE + ": edit a crate through a inventory.");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco create <crate> " + ChatColor.WHITE + ": creates a new crate through a inventory.");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco delete <crate> " + ChatColor.WHITE + ": delete a crate.");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco disable <crate> " + ChatColor.WHITE + ": disables a crate.");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco enable <crate> " + ChatColor.WHITE + ": enables a crate.");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco setType <crate> <type> " + ChatColor.WHITE + ": here you can define the crate to \"rare\" or \"normal\".");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco spawn <crate> " + ChatColor.WHITE + ": spawn a crate.");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco spawn <player> <serie> " + ChatColor.WHITE + ": spawns a crate for a specific player");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco give money <price> <amount> " + ChatColor.WHITE + ": gives money paper");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco give schematic <name> <amount> " + ChatColor.WHITE + ": gives a schematic egg");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/manco reload " + ChatColor.WHITE + ": reloads the plugin");
						}
					} else if(args[0].equalsIgnoreCase("reload")) {
						if(sender.hasPermission("manco.isadmin")) {
							pl.getConfiguration().reload();
							sender.sendMessage(ChatColor.GREEN + "ManCo supply crates has been reloaded!");
						} else {
							sender.sendMessage(ChatColor.RED + "you cannot do that.");
						}
					} else if(args[0].equalsIgnoreCase("list")) {
						Collection<Crate> crates = pl.getConfiguration().getCrateList().values();
						String message = "";
						if(!crates.isEmpty()) {
							Iterator<Crate> it = crates.iterator();
							for(Crate crate = (it.hasNext() ? it.next() : null); it.hasNext(); crate = it.next()) {
								if(crate.getType() == CrateType.NORMAL) {
									NormalCrate nCrate = (NormalCrate) crate;
									if(nCrate.needsKey() && crate.isEnabled()) {
										if(!it.hasNext()) {
											message += ChatColor.DARK_GRAY + "[NormalCrate]" + ChatColor.GRAY + nCrate.getCrateName() + "(" + nCrate.getKeyPrice() + "$)";
										} else {
											message += ChatColor.DARK_GRAY + "[NormalCrate]" + ChatColor.GRAY + nCrate.getCrateName() + "(" + nCrate.getKeyPrice() + "$), ";
										}
									}
								} else if(crate.getType() == CrateType.RARE) {
									RareCrate rCrate = new RareCrate(crate.getCrateName(), pl);
									if(rCrate.needsKey() && crate.isEnabled()) {
										if(!it.hasNext()) {
											message += ChatColor.DARK_PURPLE + "[RareCrate]" + ChatColor.GRAY + rCrate.getCrateName() + "(" + rCrate.getKeyPrice() + "$)";
										} else {
											message += ChatColor.DARK_PURPLE + "[RareCrate]" + ChatColor.GRAY + rCrate.getCrateName() + "(" + rCrate.getKeyPrice() + "$), ";
										}
									}
								}
							}
							sender.sendMessage(ChatColor.GOLD + ".oO___[ManCo supply crate keys]___Oo.");
							sender.sendMessage(message);
						} else {
							sender.sendMessage(ChatColor.GOLD + ".oO___[ManCo supply crate keys]___Oo.");
							sender.sendMessage(ChatColor.RED + "no crate keys where found!");
						}
					}  else if(args[0].equalsIgnoreCase("crates")) {
						if(sender.hasPermission("manco.isadmin")) {
							Collection<Crate> crates = pl.getConfiguration().getCrateList().values();
							Iterator<Crate> it = crates.iterator();

							String message = "";

							for(Crate crate = (it.hasNext() ? it.next() : null); it.hasNext(); crate = it.next()) {
								if(!it.hasNext()) {
									message += (crate.isEnabled() ? ChatColor.GREEN + "[enabled]" : ChatColor.RED + "[disabled]")+crate.getType().getPrefix()+ChatColor.GRAY+crate.getCrateName();
								} else {
									message += (crate.isEnabled() ? ChatColor.GREEN + "[enabled]" : ChatColor.RED + "[disabled]")+crate.getType().getPrefix()+ChatColor.GRAY+crate.getCrateName() + ", ";
								}
							}

							sender.sendMessage(ChatColor.GOLD + ".oO___[ManCo crate list]___Oo.");
							sender.sendMessage(ChatColor.GRAY + message);
						}
					}
				} else if(args.length == 2) {
					if(args[0].equalsIgnoreCase("buy")) {
						if(sender instanceof Player) {
							if(pl.isCrate(args[1])) {
								Crate crate = pl.getCrate(args[1]);
								if(pl.getHooks().isVaultEnabled()) {
									if(pl.getHookManager().getVaultHook().hasEnough(sender.getName(), crate.getKeyPrice())) {
										Player p = (Player) sender;
										if(!(p.getInventory().firstEmpty() == -1)) {
											pl.getHookManager().getVaultHook().withdraw(sender.getName(), crate.getKeyPrice());
											p.getInventory().addItem(crate.getKeyItem());
										} else {
											sender.sendMessage(ChatColor.RED + "you inventory is full!");
										}
									} else {
										sender.sendMessage(ChatColor.RED + "you don't have enough money to afford this.");
									}
								} else {
									sender.sendMessage(ChatColor.RED + "failed to buy key, no economy plugin is installed please install Vault!");
								}
							} else {
								sender.sendMessage(ChatColor.RED + "this crate does not exist!.");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "a console cannot buy keys...");
						}
					} else if(args[0].equalsIgnoreCase("create")) {
						if(sender.hasPermission("manco.isadmin")) {
							if(sender instanceof Player) {
								if(!pl.isCrate(args[1])) {
									Player p = (Player) sender;
									Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST.getDefaultSize(), "mc:"+args[1].toLowerCase());
									sender.sendMessage(ChatColor.GREEN + "opening crate editor for player " + sender.getName());
									p.openInventory(inv);
								} else {
									sender.sendMessage(ChatColor.RED + "this crate already exists please use /manco edit <crate> to edit this crate.");
								}
							} else {
								sender.sendMessage(ChatColor.RED + "cannot open inventory from the console side.");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "you don't have permissions to do that.");
						}
					} else if(args[0].equalsIgnoreCase("edit")) {
						if(sender.hasPermission("manco.isadmin")) {
							if(sender instanceof Player) {
								if(pl.isCrate(args[1])) {
									Player p = (Player) sender;
									Crate crate = pl.getCrate(args[1]);
									Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST.getDefaultSize(), "me:"+args[1].toLowerCase());
									inv.setContents(crate.getRandomItems().toArray(new ItemStack[crate.getRandomItems().size()]));
									sender.sendMessage(ChatColor.GREEN + "opening crate editor for player " + sender.getName());
									p.openInventory(inv);
								} else {
									sender.sendMessage(ChatColor.RED + "crate does not exist.");
								}
							} else {
								sender.sendMessage(ChatColor.RED + "cannot open inventory from the console side.");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "you don't have permissions to do that.");
						}
					} else if(args[0].equalsIgnoreCase("delete")) {
						if(sender.hasPermission("manco.isadmin")) {
							if(pl.isCrate(args[1])) {
								Crate crate = pl.getCrate(args[1]);
								crate.remove();
								sender.sendMessage(ChatColor.GREEN + "you have successfully removed this crate.");
							} else {
								sender.sendMessage(ChatColor.RED + "this crate does not exist.");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "you don't have permission to do that.");
						}
					} else if(args[0].equalsIgnoreCase("disable")) {
						if(sender.hasPermission("manco.isadmin")) {
							if(pl.isCrate(args[1])) {
								Crate crate = pl.getCrate(args[1]);
								crate.setEnabled(false);
								pl.getConfiguration().reload();
							} else {
								sender.sendMessage(ChatColor.RED + "this crate does not exist.");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "you don't have permission to do that.");
						}
					} else if(args[0].equalsIgnoreCase("enable")) {
						if(sender.hasPermission("manco.isadmin")) {
							if(pl.isCrate(args[1])) {
								Crate crate = pl.getCrate(args[1]);
								crate.setEnabled(true);
								pl.getConfiguration().reload();
							} else {
								sender.sendMessage(ChatColor.RED + "this crate does not exist.");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "you don't have permission to do that.");
						}
					} else if(args[0].equalsIgnoreCase("spawn")) {
						if(sender instanceof Player) {
							Player p = (Player) sender;
							if(sender.hasPermission("manco.isadmin")) {
								if(pl.isCrate(args[1])) {
									
									if(pl.getCratePlayer(p.getName()).hasCrate()) {
										sender.sendMessage(ChatColor.RED + "this player has already a crate!");
										return false;
									}
									
									Random rand = new Random();
									Crate crate = pl.getCrate(args[1]);

									if(p.getLocation().getY() < 63) {
										if(pl.canFall(p.getLocation().getBlock().getLocation())) {
											if(pl.getConfiguration().isCrateMessagesEnabled()) {
												if(crate.getType() == CrateType.RARE) {
													Bukkit.broadcastMessage(pl.getConfiguration().getRareCrateDropMessage().replaceAll("%p", p.getName()).replaceAll("%crate", crate.getCrateName()));
												} else if(crate.getType() == CrateType.NORMAL) {
													Bukkit.broadcastMessage(pl.getConfiguration().getNormalCrateDropMessage().replaceAll("%p", p.getName()).replaceAll("%crate", crate.getCrateName()));
												}
											}

											if(pl.getConfiguration().isSpawnRandom()) {
												FallingBlock fall = p.getWorld().spawnFallingBlock(p.getWorld().getHighestBlockAt(p.getLocation()).getLocation().add(rand.nextInt(10), 30, rand.nextInt(10)), Material.CHEST, (byte)0);
												fall.setMetadata("crate_serie", new FixedMetadataValue(pl, crate.getCrateName()));
												fall.setMetadata("crate_owner", new FixedMetadataValue(pl, p.getName()));
												pl.getCrateOwners().add(p.getName());
											} else {
												FallingBlock fall = p.getWorld().spawnFallingBlock(p.getLocation().add(0, 1, 0), Material.CHEST, (byte)0);
												fall.setMetadata("crate_serie", new FixedMetadataValue(pl, crate.getCrateName()));
												fall.setMetadata("crate_owner", new FixedMetadataValue(pl, p.getName()));
												pl.getCrateOwners().add(p.getName());
											}
										} else {
											sender.sendMessage(ChatColor.RED + "could not spawn crate, because the player is either standing in a region or standing on a transparant block");
											return false;
										}
									} else {
										Location highest = p.getWorld().getHighestBlockAt(p.getLocation()).getLocation();
										if(pl.canFall(highest)) {
											if(pl.getConfiguration().isCrateMessagesEnabled()) {
												if(crate.getType() == CrateType.RARE) {
													Bukkit.broadcastMessage(pl.getConfiguration().getRareCrateDropMessage().replaceAll("%p", p.getName()).replaceAll("%crate", crate.getCrateName()));
												} else if(crate.getType() == CrateType.NORMAL) {
													Bukkit.broadcastMessage(pl.getConfiguration().getNormalCrateDropMessage().replaceAll("%p", p.getName()).replaceAll("%crate", crate.getCrateName()));
												}
											}

											if(pl.getConfiguration().isSpawnRandom()) {
												FallingBlock fall = p.getWorld().spawnFallingBlock(highest.add(rand.nextInt(10), 30, rand.nextInt(10)), Material.CHEST, (byte)0);
												fall.setMetadata("crate_serie", new FixedMetadataValue(pl, crate.getCrateName()));
												fall.setMetadata("crate_owner", new FixedMetadataValue(pl, p.getName()));
												pl.getCrateOwners().add(p.getName());
											} else {
												FallingBlock fall = p.getWorld().spawnFallingBlock(highest.add(0, 30, 0), Material.CHEST, (byte)0);
												fall.setMetadata("crate_serie", new FixedMetadataValue(pl, crate.getCrateName()));
												fall.setMetadata("crate_owner", new FixedMetadataValue(pl, p.getName()));
												pl.getCrateOwners().add(p.getName());
											}
										} else {
											sender.sendMessage(ChatColor.RED + "could not spawn crate, because the player is either standing in a region or standing on a transparant block");
											return false;
										}
									}
								} else {
									sender.sendMessage(ChatColor.RED + "this crate does not exist.");
								}
							}
						}
					} else {
						sender.sendMessage(ChatColor.RED + "unknown argument!");
					}
				} else if(args.length == 3) {
					if(args[0].equalsIgnoreCase("spawn")) {
						if(sender.hasPermission("manco.isadmin")) {
							Player p = Bukkit.getPlayer(args[1]);
							if(p instanceof Player) {
								
								if(pl.getCratePlayer(p.getName()).hasCrate()) {
									sender.sendMessage(ChatColor.RED + "this player has already a crate!");
									return false;
								}
								
								if(pl.isCrate(args[2])) {
									Random rand = new Random();
									Crate crate = pl.getCrate(args[2]);

									if(p.getLocation().getY() < 63) {
										if(pl.canFall(p.getLocation().getBlock().getLocation())) {
											if(pl.getConfiguration().isCrateMessagesEnabled()) {
												if(crate.getType() == CrateType.RARE) {
													Bukkit.broadcastMessage(pl.getConfiguration().getRareCrateDropMessage().replaceAll("%p", p.getName()).replaceAll("%crate", crate.getCrateName()));
												} else if(crate.getType() == CrateType.NORMAL) {
													Bukkit.broadcastMessage(pl.getConfiguration().getNormalCrateDropMessage().replaceAll("%p", p.getName()).replaceAll("%crate", crate.getCrateName()));
												}
											}

											if(pl.getConfiguration().isSpawnRandom()) {
												FallingBlock fall = p.getWorld().spawnFallingBlock(p.getWorld().getHighestBlockAt(p.getLocation()).getLocation().add(rand.nextInt(10), 30, rand.nextInt(10)), Material.CHEST, (byte)0);
												fall.setMetadata("crate_serie", new FixedMetadataValue(pl, crate.getCrateName()));
												fall.setMetadata("crate_owner", new FixedMetadataValue(pl, p.getName()));
												pl.getCrateOwners().add(p.getName());
											} else {
												FallingBlock fall = p.getWorld().spawnFallingBlock(p.getLocation().add(0, 1, 0), Material.CHEST, (byte)0);
												fall.setMetadata("crate_serie", new FixedMetadataValue(pl, crate.getCrateName()));
												fall.setMetadata("crate_owner", new FixedMetadataValue(pl, p.getName()));
												pl.getCrateOwners().add(p.getName());
											}
										} else {
											sender.sendMessage(ChatColor.RED + "could not spawn crate, because the player is either standing in a region or standing on a transparant block");
											return false;
										}
									} else {
										Location highest = p.getWorld().getHighestBlockAt(p.getLocation()).getLocation();
										if(pl.canFall(highest)) {
											if(pl.getConfiguration().isCrateMessagesEnabled()) {
												if(crate.getType() == CrateType.RARE) {
													Bukkit.broadcastMessage(pl.getConfiguration().getRareCrateDropMessage().replaceAll("%p", p.getName()).replaceAll("%crate", crate.getCrateName()));
												} else if(crate.getType() == CrateType.NORMAL) {
													Bukkit.broadcastMessage(pl.getConfiguration().getNormalCrateDropMessage().replaceAll("%p", p.getName()).replaceAll("%crate", crate.getCrateName()));
												}
											}

											if(pl.getConfiguration().isSpawnRandom()) {
												FallingBlock fall = p.getWorld().spawnFallingBlock(highest.add(rand.nextInt(10), 30, rand.nextInt(10)), Material.CHEST, (byte)0);
												fall.setMetadata("crate_serie", new FixedMetadataValue(pl, crate.getCrateName()));
												fall.setMetadata("crate_owner", new FixedMetadataValue(pl, p.getName()));
												pl.getCrateOwners().add(p.getName());
											} else {
												FallingBlock fall = p.getWorld().spawnFallingBlock(highest.add(0, 30, 0), Material.CHEST, (byte)0);
												fall.setMetadata("crate_serie", new FixedMetadataValue(pl, crate.getCrateName()));
												fall.setMetadata("crate_owner", new FixedMetadataValue(pl, p.getName()));
												pl.getCrateOwners().add(p.getName());
											}
										} else {
											sender.sendMessage(ChatColor.RED + "could not spawn crate, because the player is either standing in a region or standing on a transparant block");
											return false;
										}
									}
								} else {
									sender.sendMessage(ChatColor.RED + "this crate does not exist.");
								}
							} else {
								sender.sendMessage(ChatColor.RED + "no player online with this name.");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "you don't have permissions do to that.");
						}
					} else if(args[0].equalsIgnoreCase("setType")) {
						if(sender.hasPermission("manco.isadmin")) {
							if(pl.isCrate(args[1])) {
								Crate crate = pl.getCrate(args[1]);
								if(args[2].equalsIgnoreCase(CrateType.RARE.name())) {
									crate.setType(CrateType.RARE);
									pl.getConfiguration().reload();
									sender.sendMessage(ChatColor.GREEN + "you have successfully changed the type to rare");
								} else if(args[2].equalsIgnoreCase(CrateType.NORMAL.name())) {
									crate.setType(CrateType.NORMAL);
									pl.getConfiguration().reload();
									sender.sendMessage(ChatColor.GREEN + "you have successfully changed the type to normal");
								} else {
									sender.sendMessage(ChatColor.RED + "invalid crate type.");
								}
							} else {
								sender.sendMessage(ChatColor.RED + "this crate does not exist.");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "you don't have permissions do to that.");
						}
					}
				} else if(args.length == 4) {
					if(args[0].equalsIgnoreCase("give")) {
						if(args[1].equalsIgnoreCase("money")) {
							if(sender.hasPermission("manco.give.money")) {
								if(sender instanceof Player) {
									Player p = (Player) sender;
									try {
										Double money = Double.parseDouble(args[2]);
										int amount = Integer.parseInt(args[3]);
										ItemStack item = new ItemStack(Material.PAPER, amount);
										ItemMeta meta = item.getItemMeta();
										meta.setDisplayName(ChatColor.GOLD + "[money]"+ChatColor.GRAY+":"+money+"$");
										meta.setLore(Arrays.asList(new String[] {
												ChatColor.GREEN + "with this you can easily earn money!",
												ChatColor.GREEN + "when you right click it, you will get it automaticly in your bank.",
												"",
												ChatColor.GOLD + "part of the ManCo supply crates"
										}));
										item.setItemMeta(meta);
										if(p.getInventory().firstEmpty() > -1) {
											p.getInventory().addItem(item);
											sender.sendMessage(ChatColor.GREEN + "successfully added ManCo money into your inventory!");
										} else {
											sender.sendMessage(ChatColor.RED + "could not add the item in your inventory, its full.");
										}
									} catch(NumberFormatException e) {
										sender.sendMessage(ChatColor.RED + "the third and fourth arguments needs to be numbers!");
									}
								} else {
									sender.sendMessage(ChatColor.RED + "a console cannot obtain items!");
								}
							} else {
								sender.sendMessage(ChatColor.RED + "you don't have permission to do that.");
							}
						} else if(args[1].equalsIgnoreCase("schematic")) {
							if(sender.hasPermission("manco.give.schematic")) {
								if(sender instanceof Player) {
									Player p = (Player) sender;
									pl.getSchematicUtils().initSchematics();
									if(pl.getSchematicUtils().doesExist(args[2])) {
										Schematic schem = pl.getSchematicUtils().getByName(args[2]);

										try {
											int amount = Integer.parseInt(args[3]);
											ItemStack item = new ItemStack(383, amount);
											item.setDurability((short)120);
											ItemMeta meta = item.getItemMeta();
											meta.setDisplayName(ChatColor.GOLD + "[schematic]"+ChatColor.GRAY+":"+schem.getName());
											meta.setLore(Arrays.asList(new String[] {
													ChatColor.GREEN + "this special egg has its own magic powers!",
													ChatColor.GREEN + "it includes everything about architure!",
													"",
													ChatColor.GOLD + "part of the ManCo supply crates"
											}));
											item.setItemMeta(meta);
											if(p.getInventory().firstEmpty() > -1) {
												p.getInventory().addItem(item);
												sender.sendMessage(ChatColor.GREEN + "successfully added schematic items into your inventory!");
											} else {
												sender.sendMessage(ChatColor.RED + "could not add the item in your inventory, its full.");
											}
										} catch(NumberFormatException e) {
											sender.sendMessage(ChatColor.RED + "the fourth argument needs to be a number!");
										}

									} else {
										sender.sendMessage(ChatColor.RED + "this schematic does not exist!");
									}
								} else {
									sender.sendMessage(ChatColor.RED + "a console cannot use schematic eggs!");
								}
							} else {
								sender.sendMessage(ChatColor.RED + "you don't have permission to do that.");
							}
						}
					}
				}
			} else {
				sender.sendMessage(ChatColor.RED + "you dont have permission");
			}
		}
		return false;
	}

}
