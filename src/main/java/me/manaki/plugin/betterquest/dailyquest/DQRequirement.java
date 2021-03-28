package me.manaki.plugin.betterquest.dailyquest;

import java.util.List;

import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import me.manaki.plugin.betterquest.utils.QuestUtils;
import mk.plugin.playerdata.storage.PlayerData;
import mk.plugin.playerdata.storage.PlayerDataAPI;

public class DQRequirement {
	
	private int minLv;
	private int qP;
	private List<String> quests = Lists.newArrayList();
	private List<String> data = Lists.newArrayList();
	
	public DQRequirement(int minLv, int qP, List<String> quests, List<String> data) {
		this.minLv = minLv;
		this.qP = qP;
		this.quests = quests;
		this.data = data;
	}
	
	public int getMinLevel() {
		return this.minLv;
	}
	
	public int getQuestPoint() {
		return this.qP;
	}
	
	public List<String> getQuestsDone() {
		return this.quests;
	}
	
	public List<String> getPlayerData() {
		return this.data;
	}
	
	public boolean testRequirement(Player player) {
		PlayerData pd = PlayerDataAPI.getPlayerData(player);

		for (String d : this.data) {
			if (!pd.hasData(d)) return false;
		}
		for (String q : this.quests) { 
			if (QuestUtils.getQuestsPlugin().getQuester(player.getUniqueId()).getCompletedQuests().contains(q)) return false;
		}
		if (player.getLevel() < this.minLv) return false;
		if (QuestUtils.getQuestsPlugin().getQuester(player.getUniqueId()).getQuestPoints() < this.qP) return false;
		
		return true;
	}
	
}
