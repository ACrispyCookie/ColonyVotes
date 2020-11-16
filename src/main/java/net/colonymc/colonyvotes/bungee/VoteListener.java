package net.colonymc.colonyvotes.bungee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.vexsoftware.votifier.model.VotifierEvent;

import net.colonymc.colonyapi.database.MainDatabase;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class VoteListener implements Listener {
	
	final BungeeChannelManager bcm = new BungeeChannelManager();
	
	@EventHandler
	public void onNewVote(VotifierEvent e) {
		try {
			String username = e.getVote().getUsername();
			String uuid = MainDatabase.getUuid(username);
			if(!uuid.equals("Not Found")) {
				String timeStamp = e.getVote().getTimeStamp();
				int timesVoted = 0;
				ResultSet rs = MainDatabase.getResultSet("SELECT timesVoted FROM PlayerVotes WHERE uuid='" + uuid + "';");
				if(rs.next()) {
					timesVoted = rs.getInt("timesVoted");
				}
                Set<String> queued = new HashSet<>(ProxyServer.getInstance().getServers().keySet());
				if(ProxyServer.getInstance().getPlayer(username) != null) {
					queued.remove(ProxyServer.getInstance().getPlayer(username).getServer().getInfo().getName());
				}
				addToPlayerVotes(username, uuid, timeStamp, timesVoted);
				addToQueuedVotes(username, uuid, queued);
				sendVoteMessage(username);
				bcm.handleVote(username, 1);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private void addToQueuedVotes(String username, String uuid, Set<String> queued) {
		for(String s : queued) {
			try {
				ResultSet rs = MainDatabase.getResultSet("SELECT * FROM QueuedVotes WHERE uuid='" + uuid + "' AND server='" + s + "';");
				if(rs.next()) {
					int newAmount = rs.getInt("amount") + 1;
					MainDatabase.sendStatement("UPDATE QueuedVotes SET amount=" + newAmount + " WHERE uuid='" + uuid + "' AND server='" + s + "';");
				}
				else {
					MainDatabase.sendStatement("INSERT INTO QueuedVotes (uuid, server, amount) VALUES ('" + uuid + "', '" + s + "', " + 1 + ");");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	private void addToPlayerVotes(String username, String uuid, String timeStamp, int timesVoted) {
		try {
			if(!MainDatabase.getResultSet("SELECT * FROM PlayerVotes WHERE uuid='" + uuid + "';").next()) {
				MainDatabase.sendStatement("INSERT INTO PlayerVotes (uuid, timesVoted, lastVote) VALUES ('" + uuid + "', " + (timesVoted + 1) + ", " + Long.parseLong(timeStamp) + ");");
			}
			else {
				MainDatabase.sendStatement("UPDATE PlayerVotes SET timesVoted=" + (timesVoted  + 1) + ", lastVote=" + Long.parseLong(timeStamp) + " WHERE uuid='" + uuid +"';");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private void sendVoteMessage(String name) {
		for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
			p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &d" + name + " &fhas voted for the server and received many rewards on every gamemode!")));
		}
	}

}
