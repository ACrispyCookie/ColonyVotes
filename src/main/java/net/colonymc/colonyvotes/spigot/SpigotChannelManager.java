package net.colonymc.colonyvotes.spigot;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.colonymc.api.player.ColonyPlayer;

public class SpigotChannelManager implements PluginMessageListener {

	@SuppressWarnings("unchecked")
	@Override
	public void onPluginMessageReceived(String channel, Player p, byte[] message) {
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
	    if(channel.equals("VoteChannel")) {
	    	String name = in.readUTF();
	    	int amount = in.readInt();
	    	List<String> commands = (List<String>) Main.getInstance().getConfig().getList("command_rewards");
	    	for(String cmd : commands) {
	    		for(int i = 0; i < amount; i++) {
			    	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%name%", name));
	    		}
	    	}
	    	if(Bukkit.getPlayer(name) != null) {
	    		ColonyPlayer.getByPlayer(Bukkit.getPlayer(name)).addVotes(1);
	    	}
	    }
	}

}
