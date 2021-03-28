package me.manaki.plugin.betterquest.gui;

import java.util.List;

import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import me.blackvein.quests.Quester;
import me.manaki.plugin.betterquest.dailyquest.DQUtils;
import me.manaki.plugin.betterquest.utils.QuestUtils;

public enum SpecialGUI {
	
	CURRENTQUESTS {
		@Override
		public List<String> getQuests(Player player) {
			Quester quester = QuestUtils.getQuestsPlugin().getQuester(player.getUniqueId());
			List<String> list = Lists.newArrayList();
			quester.getCurrentQuests().keySet().forEach(quest -> {
				list.add(quest.getName());
			});
			return list;
		}
	},
	
	DAILYQUESTS {
		@Override
		public List<String> getQuests(Player player) {
			return DQUtils.getQuests(player);
		}
	};
	
	public abstract List<String> getQuests(Player player);
	
	private GUIData guiData;
	
	private SpecialGUI() {}
	
	public GUIData getGUIData() {
		return this.guiData;
	}
	
	public void setGUIData(GUIData guiData) {
		this.guiData = guiData;
	}
	
}
