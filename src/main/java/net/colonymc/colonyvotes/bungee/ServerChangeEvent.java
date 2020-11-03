package net.colonymc.colonyvotes.bungee;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.colonymc.colonyapi.MainDatabase;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerChangeEvent implements Listener {
	
	final BungeeChannelManager bcm = new BungeeChannelManager();
	
	@EventHandler
	public void onChange(ServerSwitchEvent e) {
		try {
			ResultSet rs = MainDatabase.getResultSet("SELECT * FROM QueuedVotes WHERE uuid='" + e.getPlayer().getUniqueId().toString() + "' AND server='" + e.getPlayer().getServer().getInfo().getName() + "';");
			if(rs.next()) {
				int amount = rs.getInt("amount");
				bcm.handleVote(e.getPlayer().getName(), amount);
				MainDatabase.sendStatement("DELETE FROM QueuedVotes WHERE uuid='" + e.getPlayer().getUniqueId().toString() + "' AND server='" + e.getPlayer().getServer().getInfo().getName() + "';");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
