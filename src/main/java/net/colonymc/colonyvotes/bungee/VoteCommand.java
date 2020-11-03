package net.colonymc.colonyvotes.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class VoteCommand extends Command {

	public VoteCommand() {
		super("vote");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		TextComponent text = new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou can vote for our network here: &dhttps://colonymc.net/vote"));
		text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://colonymc.net/vote"));
		sender.sendMessage(text);
	}

}
