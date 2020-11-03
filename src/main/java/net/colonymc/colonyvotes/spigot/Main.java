package net.colonymc.colonyvotes.spigot;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	static Main instance = null;

	@Override
	public void onEnable() {
		instance = this;
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "VoteChannel", new SpigotChannelManager());
		this.saveDefaultConfig();
		System.out.println(" » ColonyVotes has been successfully enabled!");
	}
	
	@Override
	public void onDisable() {
		System.out.println(" » ColonyVotes has been successfully disabled!");
	}
	
	public static Main getInstance() {
		return instance;
	}
	
}
