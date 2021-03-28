package me.manaki.plugin.betterquest.help;

import java.util.List;

import me.manaki.plugin.betterquest.utils.QModuleUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;

public class QHelp {
	
	public static List<QHelp> helps = Lists.newArrayList();
	
	public static void reload(FileConfiguration config) {
		helps.clear();
		config.getStringList("quest-help").forEach(s -> {
			helps.add(new QHelp(s));
		});
	}
	
	public static boolean hasHelp(Player player, Quest quest) {
		Quester quester = QModuleUtils.getQuester(player);
		int stage = quester.getCurrentQuests().getOrDefault(quest, -1);
		if (stage == -1) return false;
		for (QHelp help : helps) {
			if (help.getQuest().equalsIgnoreCase(quest.getName())) {
				if (help.getStage() == stage) return true;
			}
		}
		return false;
	}
	
	public static String getHelp(Player player, Quest quest) {
		Quester quester = QModuleUtils.getQuester(player);
		int stage = quester.getCurrentQuests().getOrDefault(quest, -1);
		if (stage == -1) return null;
		for (QHelp help : helps) {
			if (help.getQuest().equalsIgnoreCase(quest.getName())) {
				if (help.getStage() == stage) return help.getDesc();
			}
		}
		return null;
	}
	
	private String quest;
	private int stage;
	private String desc;
	
	public QHelp(String quest, int stage, String desc) {
		this.quest = quest;
		this.stage = stage;
		this.desc = desc;
	}
	
	public QHelp(String s) {
		this.quest = s.split(";")[0];
		this.stage = Integer.parseInt(s.split(";")[1]);
		this.desc = s.split(";")[2];
	}
	
	public String getQuest() {
		return this.quest;
	}
	
	public int getStage() {
		return this.stage;
	}
	
	public String getDesc() {
		return this.desc;
	}
	
	
}
