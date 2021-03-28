package me.manaki.plugin.betterquest.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.manaki.plugin.betterquest.help.QHelp;

public class StageUtils {
	
	public static void showObjective(Player player, Quest quest) {
		Quester qt = QModuleUtils.getQuester(player);
		if (qt.getCurrentStage(quest).getCustomObjectives().size() > 0 && qt.getCurrentStage(quest).getCustomObjectives().get(0).getName().equalsIgnoreCase("Run Command")) return;
		player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
		player.sendTitle("§2§l§oCÓ YÊU CẦU MỚI", "§aGhi /nhiemvuhientai để xem", 10, 100, 10);
		player.sendMessage("");
		player.sendMessage("§r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m ");
		player.sendMessage("");
		qt.getCurrentObjectives(quest, false).forEach(s -> {
			player.sendMessage("§6§l>> " + s);
			if (QHelp.hasHelp(player, quest)) {
				player.sendMessage("§7§o" + QHelp.getHelp(player, quest));
			}
		});
		player.sendMessage("");
		player.sendMessage("§r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m §r§m ");
		player.sendMessage("");
	}
	
}
