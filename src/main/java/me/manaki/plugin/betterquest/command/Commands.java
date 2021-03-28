package me.manaki.plugin.betterquest.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.manaki.plugin.betterquest.BetterQuest;
import me.manaki.plugin.betterquest.dailyquest.DQUtils;
import me.manaki.plugin.betterquest.utils.QuestUtils;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		if (!sender.hasPermission("eq.*")) return false;
		
		if (args.length == 0) {
			sendTutorial(sender);
			return false;
		}
		
		if (args[0].equalsIgnoreCase("reload")) {
			BetterQuest.plugin.reloadConfig();
			sender.sendMessage("Ok");
		}
		
		else if (args[0].equalsIgnoreCase("newdq")) {
			Player target = Bukkit.getPlayer(args[1]);
			DQUtils.checkData(target);
			DQUtils.generateRandom(target);
			target.sendMessage("§aKhởi tạo lại quest hằng ngày!");
		}
		
		else if (args[0].equalsIgnoreCase("giveundonequest")) {
			Player player = Bukkit.getPlayer(args[1]);
			String q = "";
			for (int i = 2 ; i < args.length ; i++) {
				q += args[i] + " ";
			}
			if (q.length() > 0) q = q.substring(0, q.length() - 1);
			Quester quester = QuestUtils.getQuestsPlugin().getQuester(player.getUniqueId());
			Quest quest = QuestUtils.getQuestsPlugin().getQuest(q);
			if (!quester.getCompletedQuests().contains(q) && !quester.getCurrentQuests().containsKey(quest)) {
				quester.takeQuest(QuestUtils.getQuestsPlugin().getQuest(q), false);
			} else player.sendMessage("§aKhông thể nhận nữa");
		}
		
		return false;
	}
	
	public void sendTutorial(CommandSender sender) {
		sender.sendMessage("/eq reload");
		sender.sendMessage("/eq current <player>");
		sender.sendMessage("/eq quests <player> <page>");
		sender.sendMessage("/eq dailyquests|dq <player> <page>");
		sender.sendMessage("/eq newdq <player>");
		sender.sendMessage("/eq newdqall");
		sender.sendMessage("/eq giveundonequest <player> <name>");
	}

}
