package me.manaki.plugin.betterquest.gui;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

public class CustomGUI {
	
	public static Map<String, CustomGUI> guis = Maps.newHashMap();
	
	private GUIData guiData;
	private List<String> quests;
	
	public CustomGUI(GUIData guiData, List<String> quests) {
		this.guiData = guiData;
		this.quests = quests;
	}
	
	public GUIData getGUIData() {
		return this.guiData;
	}
	
	public List<String> getQuests() {
		return this.quests;
	}
	
}
