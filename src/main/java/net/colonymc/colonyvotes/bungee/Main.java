package net.colonymc.colonyvotes.bungee;

import java.util.concurrent.TimeUnit;

import net.colonymc.colonyapi.MainDatabase;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class Main extends Plugin {
	
	private static Main instance;
	private ScheduledTask checkForDatabase;
	
	@Override
	public void onEnable() {
		Runnable run = () -> {
			try {
				MainDatabase.isConnected();
				checkForDatabase.cancel();
				if(!MainDatabase.isConnecting()) {
					if(MainDatabase.isConnected()) {
						setupListeners();
						setupCommands();
						ProxyServer.getInstance().registerChannel("VoteChannel");
						System.out.println("[ColonyVotes] has been successfully enabled!");
					}
					else {
						System.out.println("[ColonyVotes] Couldn't connect to the main database!");
					}
				}
			} catch(NoSuchMethodError ignored) {

			}
		};
		checkForDatabase = ProxyServer.getInstance().getScheduler().schedule(this, run, 0, 2, TimeUnit.SECONDS);
	}
	
	private void setupCommands() {
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new VoteCommand());
	}

	private void setupListeners() {
		ProxyServer.getInstance().getPluginManager().registerListener(this, new VoteListener());
		ProxyServer.getInstance().getPluginManager().registerListener(this, new ServerChangeEvent());
	}

	@Override
	public void onDisable() {
		System.out.println(" » ColonyVotes has been successfully disabled!");
	}
	
	public static Main getInstance() {
		return instance;
	}
	
}
