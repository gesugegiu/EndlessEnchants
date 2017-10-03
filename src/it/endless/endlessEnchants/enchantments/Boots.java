package it.endless.endlessEnchants.enchantments;

import it.endless.endlessEnchants.Main;
import it.endless.endlessEnchants.ParticleEffect;
import it.endless.endlessEnchants.api.CEnchantments;
import it.endless.endlessEnchants.api.events.ArmorEquipEvent;
import it.endless.endlessEnchants.multisupport.Support;
import it.endless.endlessEnchants.multisupport.Version;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Boots implements Listener {
	
	private static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("EndlessEnchants");
	private int id = 0;
	public static ArrayList<Player> Flying = new ArrayList<Player>();
	
	@EventHandler
	public void onEquip(ArmorEquipEvent e) {
		Player player = e.getPlayer();
		ItemStack NewItem = e.getNewArmorPiece();
		ItemStack OldItem = e.getOldArmorPiece();
		if(Main.CE.hasEnchantments(NewItem)) {
			if(Main.CE.hasEnchantment(NewItem, CEnchantments.WINGS)) {
				if(CEnchantments.WINGS.isEnabled()) {
					if(Support.inTerritory(player) || Support.inWingsRegion(player)) {
						if(player.getGameMode() != GameMode.CREATIVE) {
							if(Version.getVersion().comparedTo(Version.v1_8_R1) >= 1) {
								if(player.getGameMode() != GameMode.ADVENTURE) {
									player.setAllowFlight(true);
								}
							}else {
								player.setAllowFlight(true);
							}
						}
					}
				}
			}
		}
		if(Main.CE.hasEnchantments(OldItem)) {
			if(Main.CE.hasEnchantment(OldItem, CEnchantments.WINGS)) {
				if(CEnchantments.WINGS.isEnabled()) {
					if(player.getGameMode() != GameMode.CREATIVE) {
						if(Version.getVersion().comparedTo(Version.v1_8_R1) >= 1) {
							if(player.getGameMode() != GameMode.ADVENTURE) {
								player.setAllowFlight(false);
							}
						}else {
							player.setAllowFlight(false);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onFly(PlayerToggleFlightEvent e) {
		Player player = e.getPlayer();
		ItemStack boots = player.getEquipment().getBoots();
		if(Main.CE.hasEnchantments(boots)) {
			if(Main.CE.hasEnchantment(boots, CEnchantments.WINGS)) {
				if(CEnchantments.WINGS.isEnabled()) {
					if(Support.inTerritory(player) || Support.inWingsRegion(player)) {
						if(!areEnemiesNearBy(player)) {
							
							if(e.isFlying()) {
								if(player.getAllowFlight()) {
									e.setCancelled(true);
									player.setFlying(true);
									Flying.add(player);
								}
							}else {
								Flying.remove(player);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		ItemStack boots = player.getEquipment().getBoots();
		if(Main.CE.hasEnchantments(boots)) {
			if(Main.CE.hasEnchantment(boots, CEnchantments.WINGS)) {
				if(CEnchantments.WINGS.isEnabled()) {
					if(Support.inTerritory(player) || Support.inWingsRegion(player)) {
						if(!areEnemiesNearBy(player)) {
							if(!player.getAllowFlight()) {
								player.setAllowFlight(true);
							}
						}else {
							if(player.isFlying()) {
								if(player.getGameMode() != GameMode.CREATIVE) {
									if(Version.getVersion().comparedTo(Version.v1_8_R1) >= 1) {
										if(player.getGameMode() != GameMode.ADVENTURE) {
											player.setFlying(false);
											player.setAllowFlight(false);
											Flying.remove(player);
										}
									}else {
										player.setFlying(false);
										player.setAllowFlight(false);
										Flying.remove(player);
									}
								}
							}
						}
					}else {
						if(player.isFlying()) {
							if(player.getGameMode() != GameMode.CREATIVE) {
								if(Version.getVersion().comparedTo(Version.v1_8_R1) >= 1) {
									if(player.getGameMode() != GameMode.ADVENTURE) {
										player.setFlying(false);
										player.setAllowFlight(false);
										Flying.remove(player);
									}
								}else {
									player.setFlying(false);
									player.setAllowFlight(false);
									Flying.remove(player);
								}
							}
						}
					}
					if(player.isFlying()) {
						Flying.add(player);
					}
				}
			}
			
			if(Main.CE.hasEnchantment(boots, CEnchantments.FIREMAN)) {
				if(CEnchantments.FIREMAN.isEnabled()) {
						for (Player other : Bukkit.getOnlinePlayers()) {
							if(other.getName().equalsIgnoreCase(player.getName())) continue;
							  if (other.getLocation().distance(player.getLocation()) <= 3) {
							    other.setFireTicks(40);
							  }
						} 
				}
			}
			
			if(Main.CE.hasEnchantment(boots, CEnchantments.THORNS)) {
				if(CEnchantments.THORNS.isEnabled()) {
					if(player.getHealth() <= 2) {
						player.getInventory().getBoots().addEnchantment(Enchantment.THORNS, 3);
					}else{
						player.getInventory().getBoots().removeEnchantment(Enchantment.THORNS);
					}
				}
			}
			
			if(Main.CE.hasEnchantment(boots, CEnchantments.HORSE)) {
				if(CEnchantments.HORSE.isEnabled()) {
					if(player.getHealth() <= 2 && !Main.horsePl.contains(player.getName())) {
						Main.horsePl.add(player.getName());
						Horse h = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
						h.setTamed(true);
						h.getInventory().setSaddle(new ItemStack(Material.SADDLE, 1));
						
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		ItemStack boots = player.getEquipment().getBoots();
		if(Main.CE.hasEnchantments(boots)) {
			if(Main.CE.hasEnchantment(boots, CEnchantments.WINGS)) {
				if(CEnchantments.WINGS.isEnabled()) {
					if(Support.inTerritory(player) || Support.inWingsRegion(player)) {
						if(!areEnemiesNearBy(player)) {
							
							player.setAllowFlight(true);
							Flying.add(player);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		ItemStack boots = player.getEquipment().getBoots();
		if(Main.CE.hasEnchantments(boots)) {
			if(Main.CE.hasEnchantment(boots, CEnchantments.WINGS)) {
				if(CEnchantments.WINGS.isEnabled()) {
					player.setFlying(false);
					player.setAllowFlight(false);
					Flying.remove(player);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void onStart() {
		if(Main.settings.getConfig().contains("Settings.EnchantmentOptions.Wings.Clouds")) {
			if(Main.settings.getConfig().getBoolean("Settings.EnchantmentOptions.Wings.Clouds")) {
			Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
				
				@Override
				public void run() {
					for(Player player : Bukkit.getServer().getOnlinePlayers()) {
						if(Flying.contains(player)) {
							Location l = player.getLocation().subtract(0, .25, 0);
							if(player.isFlying()) {
								ItemStack boots = player.getEquipment().getBoots();
								if(boots != null) {
									if(Main.CE.hasEnchantment(boots, CEnchantments.WINGS)) {
										if(CEnchantments.WINGS.isEnabled()) {
											ParticleEffect.CLOUD.display((float) .25, (float) 0, (float) .25, 0, 10, l, 100);
										}
									}
								}
							}
						}
					}
				}
			}, 1,1);
			}
		}
	}
	
	private Boolean areEnemiesNearBy(Player player) {
		if(Main.settings.getConfig().getBoolean("Settings.EnchantmentOptions.Wings.Enemy-Toggle")) {
			for(Player p : getNearByPlayers(player, Main.settings.getConfig().getInt("Settings.EnchantmentOptions.Wings.Distance"))) {
				if(!Support.isFriendly(player, p)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private ArrayList<Player> getNearByPlayers(Player player, int radius) {
		ArrayList<Player> players = new ArrayList<Player>();
		for(Entity en : player.getNearbyEntities(radius, radius, radius)) {
			if(en instanceof Player) {
				if((Player) en != player) {
					players.add((Player) en);
				}
			}
		}
		return players;
	}
	
}