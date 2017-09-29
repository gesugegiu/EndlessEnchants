package it.endless.endlessEnchants.api.currencyapi;

import it.endless.endlessEnchants.multisupport.Support.SupportedPlugins;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class VaultSupport {
	
	private static Economy vault = null;
	
	public static Economy getVault() {
		return vault;
	}
	
	public static void loadVault() {
		if(SupportedPlugins.VAULT.isPluginLoaded()) {
			RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
			if(rsp != null) {
				vault = rsp.getProvider();
			}
		}
	}
	
}