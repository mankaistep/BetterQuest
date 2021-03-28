package me.manaki.plugin.betterquest.dailyquest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.manaki.plugin.betterquest.utils.QuestUtils;
import me.manaki.plugin.betterquest.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import me.blackvein.quests.Quests;
import me.manaki.plugin.betterquest.BetterQuest;
import mk.plugin.playerdata.storage.PlayerData;
import mk.plugin.playerdata.storage.PlayerDataAPI;

public class DQUtils {
	
	public static List<DQCategory> DQCategories = Lists.newArrayList();;
	private static Map<UUID, DQData> PlayerDQ = Maps.newHashMap();
	
	public static void generateRandom(Player player) {
		List<String> r = Lists.newArrayList();
		DQCategories.forEach(category -> {
			int min = category.getMinQuest();
			int max = category.getMaxQuest();
			int amount = Utils.randomInt(min, max);
			r.addAll(getRandom(category, player, getViaCategory(category), amount));
		});

		PlayerDQ.put(player.getUniqueId(), new DQData(r, Lists.newArrayList(), System.currentTimeMillis()));
		saveData(player);
	}
	
	public static List<String> getQuests(Player player) {
		DQData dqd = getData(player);
		if (dqd == null) return Lists.newArrayList();
		List<String> q = Lists.newArrayList();
		dqd.getQuests().forEach(dq -> q.add(dq));
		return q;
	}
	
	public static List<String> getRandom(DQCategory dQCategory, Player player, List<String> list, int amount) {
		List<String> li = Lists.newArrayList(list);
		list.forEach(id -> {
			if (!dQCategory.getRequirement().testRequirement(player)) li.remove(id);
		});
		amount = Math.min(amount, list.size());
		
		if (li.size() == 0) return Lists.newArrayList();
		
		// Get random
		List<String> r = Lists.newArrayList();
		while (r.size() < amount) {
			int i = Utils.randomInt(0, li.size() - 1);
			r.add(li.get(i));
			li.remove(i);
		}
		
		Collections.sort(r);
		
		return r;
	}
	
	public static List<String> getViaCategory(DQCategory dQCategory) {
		return dQCategory.getQuests();
	}
	
	public static DQData getData(Player player) {
		if (!PlayerDQ.containsKey(player.getUniqueId())) loadData(player);
		return PlayerDQ.getOrDefault(player.getUniqueId(), null);
	}
	
	public static boolean checkData(Player player) {
		DQData dqd = getData(player);
		if (dqd == null || dqd.needNewTime(System.currentTimeMillis())) {
			if (dqd != null) {
				Bukkit.getScheduler().runTask(BetterQuest.plugin, () -> {
					dqd.getQuests().forEach(quest -> {
						if (dqd.getDoneQuests().contains(quest)) return;
						QuestUtils.getQuestsPlugin().getQuester(player.getUniqueId()).quitQuest(QuestUtils.getQuestsPlugin().getQuest(quest), "");
					});
				});
			}
			generateRandom(player);
			player.sendMessage("§aNhiệm vụ hằng ngày được khởi tạo lại!");
			return true;
		}
		return false;
	}
	
	public static void clearData(Player player) {
		PlayerDQ.remove(player.getUniqueId());
	}
	
	public static void loadData(Player player) {
		PlayerData data = PlayerDataAPI.getPlayerData(player);
		if (!data.hasData("dq.time")) return;
		
		List<String> q = Lists.newArrayList();
		int i = 0;
		while (data.hasData("dq." + i)) {
			String qn = data.getValue("dq." + i);
			if (Quests.getPlugin(Quests.class).getQuest(qn) != null) q.add(qn);
			i++;
		}
		long time = Long.valueOf(data.getValue("dq.time"));
		
		List<String> dq = Lists.newArrayList();
		i = 0;
		while (data.hasData("dq.done." + i)) {
			String qn = data.getValue("dq.done." + i);
			if (Quests.getPlugin(Quests.class).getQuest(qn) != null) dq.add(data.getValue("dq.done." + i));
			i++;
		}
		
		DQData dqd = new DQData(q, dq, time);
		PlayerDQ.put(player.getUniqueId(), dqd);
	}
	
	public static void saveData(Player player) {
		PlayerData data = PlayerDataAPI.getPlayerData(player);
		synchronized (data) {
			if (PlayerDQ.containsKey(player.getUniqueId())) {
				DQData dqd = PlayerDQ.get(player.getUniqueId());
				
				// Remove old data
				for (String dK : Sets.newHashSet(data.getDataMap().keySet())) {
					if (dK.startsWith("dq-")) data.remove(dK);
				}
				
				// Save quests
				for (int i = 0 ; i < dqd.getQuests().size() ; i++) {
					data.set("dq-" + i, dqd.getQuests().get(i));
				}
				// Save done quests
				for (int i = 0 ; i < dqd.getDoneQuests().size() ; i++) {
					data.set("dq-done-" + i, dqd.getDoneQuests().get(i));
				}
				// Save time
				data.set("dq-time", System.currentTimeMillis() + "");
			}
		}	
	}

}
