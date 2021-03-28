package me.manaki.plugin.betterquest.utils;

import java.util.Map;

import org.bukkit.entity.Player;

import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;

public class QModuleUtils {
	
	public static Quests getQuests() {
		return Quests.getPlugin(Quests.class);
	}
	
	public static Quester getQuester(Player player) {
		return getQuests().getQuester(player.getUniqueId());
	}
	
	public static Quest getQuest(String name) {
		return getQuests().getQuest(name);
	}
	
	public static Map<Quest, Integer> getCurrentQuests(Player player) {
		return getQuester(player).getCurrentQuests();
	}
	
	public static String getData(Player player, String id, Quest quest, CustomObjective co) {
		return co.getDataForPlayer(player, co, quest).get(id).toString();
	}
	
}
