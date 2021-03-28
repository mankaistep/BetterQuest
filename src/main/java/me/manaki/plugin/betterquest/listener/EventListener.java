package me.manaki.plugin.betterquest.listener;

import me.manaki.plugin.betterquest.v3.V3;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.events.quester.QuesterPostChangeStageEvent;
import me.blackvein.quests.events.quester.QuesterPostStartQuestEvent;
import me.blackvein.quests.events.quester.QuesterPreCompleteQuestEvent;
import me.manaki.plugin.betterquest.BetterQuest;
import me.manaki.plugin.betterquest.dailyquest.DQData;
import me.manaki.plugin.betterquest.dailyquest.DQUtils;
import me.manaki.plugin.betterquest.utils.QuestUtils;
import me.manaki.plugin.betterquest.gui.CustomGUI;
import me.manaki.plugin.betterquest.gui.GUIOpener;
import me.manaki.plugin.betterquest.gui.SpecialGUI;
import me.manaki.plugin.betterquest.utils.StageUtils;

public class EventListener implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		GUIOpener.eventClick(e);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		Bukkit.getScheduler().runTaskLaterAsynchronously(BetterQuest.plugin, () -> {
			DQUtils.loadData(player);
			DQUtils.checkData(player);
		}, 20);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(BetterQuest.plugin, () -> {
			DQUtils.clearData(e.getPlayer());;
		}, 20);
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		String cmd = e.getMessage().replace("/", "");
		Player player = e.getPlayer();
		if (cmd.equalsIgnoreCase("quest") || cmd.equalsIgnoreCase("nhiemvu")) {
			e.setCancelled(true);
			GUIOpener.openMainGUI(player);
			return;
		}
		
		for (SpecialGUI sg : SpecialGUI.values()) {
			if (sg.getGUIData().getCommands().contains(cmd.toLowerCase())) {
				e.setCancelled(true);
				GUIOpener.openGUI(player, sg.getGUIData(), sg.getQuests(player), 1);
				return;
			}
		}
		
		for (CustomGUI cg : CustomGUI.guis.values()) {
			if (cg.getGUIData().getCommands().contains(cmd.toLowerCase())) {
				e.setCancelled(true);
				GUIOpener.openGUI(player, cg.getGUIData(), cg.getQuests(), 1);
				return;
			}
		}
		
	}

	@EventHandler
	public void onFinishQuest(QuesterPreCompleteQuestEvent e) {
		Quester quester = e.getQuester();
		Player player = Bukkit.getPlayer(quester.getUUID());
		String quest = e.getQuest().getName();
		if (QuestUtils.isDailyQuest(quest)) {
			DQData data = DQUtils.getData(player);
			data.addDoneQuests(quest);
			DQUtils.saveData(player);
		}

		//
		if (V3.isMainQuest(player, e.getQuest())) {
			player.sendMessage("");
			player.sendMessage("§aHoàn thành nhiệm vụ chính, tự động đóng Bảng Nhiệm vụ");
			player.sendMessage("");
			V3.closeBoard(player);
		}
	}
	
	@EventHandler
	public void onStageComplete(QuesterPostChangeStageEvent e) {
		if (e.getNextStage() != null) {
			Player player = e.getQuester().getPlayer();
			Quest quest = e.getQuest();
			V3.openBoard(player, 20, true);
			if (V3.isMainQuest(player, quest)) V3.openBoard(player, 20, false);
		}
	}
	
	@EventHandler
	public void onStartQuest(QuesterPostStartQuestEvent e) {
		Player player = e.getQuester().getPlayer();
		Quest quest = e.getQuest();
		if (V3.isMainQuest(player, quest)) V3.openBoard(player, 20, true);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		V3.quit(player);
	}
	
}
