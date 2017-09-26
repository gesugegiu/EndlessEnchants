package it.endless.enchants.api.currencyapi;

import it.endless.enchants.multisupport.Version;

import org.bukkit.entity.Player;

public class CurrencyAPI {
	
	/**
	 * Get the amount that a player has from a spesific currency.
	 * @param player The player you wish to get the amount from.
	 * @param currency The currency you wish to get from.
	 * @return The amount that the player has of that currency.
	 */
	public static int getCurrency(Player player, Currency currency) {
		try {
			switch(currency) {
				case VAULT:
					return (int) VaultSupport.getVault().getBalance(player);
				case XP_LEVEL:
					return player.getLevel();
				case XP_TOTAL:
					return getTotalExperience(player);
			}
		}catch(Exception e) {}catch(NoClassDefFoundError e) {}
		return 0;
	}
	
	/**
	 * Take an amount from a player's currency.
	 * @param player The player you wish to take from.
	 * @param currency The currency you wish to use.
	 * @param amount The amount you want to take.
	 */
	public static void takeCurrency(Player player, Currency currency, int amount) {
		try {
			switch(currency) {
				case VAULT:
					VaultSupport.getVault().withdrawPlayer(player, amount);
					break;
				case XP_LEVEL:
					player.setLevel(player.getLevel() - amount);
					break;
				case XP_TOTAL:
					takeTotalXP(player, amount);
					break;
			}
		}catch(Exception e) {}catch(NoClassDefFoundError e) {}
	}
	
	/**
	 * Give an amount to a player's currency.
	 * @param player The player you are giving to.
	 * @param currency The currency you want to use.
	 * @param amount The amount you are giving to the player.
	 */
	public static void giveCurrency(Player player, Currency currency, int amount) {
		try {
			switch(currency) {
				case VAULT:
					VaultSupport.getVault().depositPlayer(player, amount);
					break;
				case XP_LEVEL:
					player.setLevel(player.getLevel() + amount);
					break;
				case XP_TOTAL:
					takeTotalXP(player, -amount);
					break;
			}
		}catch(Exception e) {}catch(NoClassDefFoundError e) {}
	}
	
	/**
	 * Checks if the player has enought of a currency.
	 * @param player The player you are checking.
	 * @param currency The currencoy you wish to check.
	 * @param cost The cost of the item you are checking.
	 * @return True if they have enough to buy it or false if they don't.
	 */
	public static Boolean canBuy(Player player, Currency currency, int cost) {
		return getCurrency(player, currency) >= cost;
	}
	
	private static void takeTotalXP(Player player, int amount) {
		if(Version.getVersion().getVersionInteger() >= 181) {
			int total = getTotalExperience(player) - amount;
			player.setTotalExperience(0);
			player.setTotalExperience(total);
			player.setLevel(0);
			player.setExp(0);
			for(; total > player.getExpToLevel();) {
				total -= player.getExpToLevel();
				player.setLevel(player.getLevel() + 1);
			}
			float xp = (float) total / (float) player.getExpToLevel();
			player.setExp(xp);
		}else {
			player.giveExp(-amount);
		}
	}
	
	private static int getTotalExperience(Player player) {// https://www.spigotmc.org/threads/72804
		int experience = 0;
		int level = player.getLevel();
		if(level >= 0 && level <= 15) {
			experience = (int) Math.ceil(Math.pow(level, 2) + (6 * level));
			int requiredExperience = 2 * level + 7;
			double currentExp = Double.parseDouble(Float.toString(player.getExp()));
			experience += Math.ceil(currentExp * requiredExperience);
			return experience;
		}else if(level > 15 && level <= 30) {
			experience = (int) Math.ceil((2.5 * Math.pow(level, 2) - (40.5 * level) + 360));
			int requiredExperience = 5 * level - 38;
			double currentExp = Double.parseDouble(Float.toString(player.getExp()));
			experience += Math.ceil(currentExp * requiredExperience);
			return experience;
		}else {
			experience = (int) Math.ceil(((4.5 * Math.pow(level, 2) - (162.5 * level) + 2220)));
			int requiredExperience = 9 * level - 158;
			double currentExp = Double.parseDouble(Float.toString(player.getExp()));
			experience += Math.ceil(currentExp * requiredExperience);
			return experience;
		}
	}
	
	/**
	 * Loads the vault currency if it is on the server.
	 */
	public static void loadCurrency() {
		VaultSupport.loadVault();
	}
	
}