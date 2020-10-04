package net.colonymc.colonyvotes.bungee;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

public class BungeeChannelManager {

	public void handleVote(String username, int amount) {
		if(ProxyServer.getInstance().getPlayer(username) != null) {
			ProxiedPlayer p = ProxyServer.getInstance().getPlayer(username);
			Server s = p.getServer();
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(bytes);
			try {
				out.writeUTF(p.getName());
				out.writeInt(amount);
			} catch (IOException e) {
				e.printStackTrace();
			}
			s.sendData("VoteChannel", bytes.toByteArray());
		}
	}
	
}
