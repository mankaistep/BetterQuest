package me.manaki.plugin.betterquest.dailyquest;

import java.util.List;

public class DQCategory {
	
	private List<String> quests;
	private int minQuests;
	private int maxQuests;
	private DQRequirement dQRequirement;
	
	public DQCategory(List<String> quests, int minQuests, int maxQuests, DQRequirement dQRequirement) {
		this.quests = quests;
		this.minQuests = minQuests;
		this.maxQuests = maxQuests;
		this.dQRequirement = dQRequirement;
	}
	
	public List<String> getQuests() {
		return this.quests;
	}
	
	public int getMinQuest() {
		return this.minQuests;
	}
	
	public DQRequirement getRequirement() {
		return this.dQRequirement;
	}
	
	public int getMaxQuest() {
		return this.maxQuests;
	}
	
}
