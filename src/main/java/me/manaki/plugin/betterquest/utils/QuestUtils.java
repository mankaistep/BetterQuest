package me.manaki.plugin.betterquest.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import me.blackvein.quests.CustomObjective;
import me.clip.placeholderapi.PlaceholderAPI;
import me.manaki.plugin.betterquest.dailyquest.DQUtils;
import mk.plugin.playerdata.storage.PlayerData;
import mk.plugin.playerdata.storage.PlayerDataAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;
import me.manaki.plugin.betterquest.dailyquest.DQCategory;

public class QuestUtils {
		
	public static Quests getQuestsPlugin() {
		return Quests.getPlugin(Quests.class);
	}
	
	public static String getDesc(Quest quest) {
		return quest.getDescription();
	}
	
	public static List<String> getRewards(Quest quest) {
		String[] a = quest.getFinished().split(";");
		return Lists.newArrayList(a);
	}
	
	public static List<String> getRequirements(Quest quest) {
		if (quest.getRequirements() == null) return Lists.newArrayList("Không rõ");
		if (quest.getRequirements().getDetailsOverride() == null) return Lists.newArrayList("Không rõ");
		if (quest.getRequirements().getDetailsOverride().size() == 0) return Lists.newArrayList("Không rõ");
		String[] a = quest.getRequirements().getDetailsOverride().get(0).split(";");
		
		return Lists.newArrayList(a);
	}
	
	public static boolean isDailyQuest(String name) {
		for (DQCategory c : DQUtils.DQCategories) {
			for (String q : c.getQuests()) {
				if (q.equals(name)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static QuestStatus getStatus(Player player, Quest quest) {
		if (quest == null) return QuestStatus.CANT_DO;
		Quester quester = getQuestsPlugin().getQuester(player.getUniqueId());
		boolean isDQ = false;
		for (DQCategory c : DQUtils.DQCategories) {
			for (String q : c.getQuests()) {
				if (q.equals(quest.getName())) {
					isDQ = true;
					break;
				}
			}
		}
		// Check Daily Quest
		if (isDQ) {
			if (quester.getCurrentQuests() != null && quester.getCurrentQuests().containsKey(quest)) {
				return QuestStatus.STARTED;
			}
			
			boolean isInDaily = false;
			for (String dq : DQUtils.getData(player).getQuests()) {
				if (dq.equals(quest.getName())) {
					isInDaily = true;
					break;
				}
			}
			if (!isInDaily) return QuestStatus.CANT_DO;
			
			if (quest.testRequirements(quester)) {
				if (DQUtils.getData(player).getDoneQuests().contains(quest.getName())) return QuestStatus.CANT_DO;
				return QuestStatus.CAN_DO;
			} else return QuestStatus.CANT_DO;
		}
		
		// Check Normal Quest
		if (quester.getCurrentQuests() != null && quester.getCurrentQuests().containsKey(quest)) {
			return QuestStatus.STARTED;
		}
		if (quester.getCompletedQuests() != null && quester.getCompletedQuests().contains(quest.getName())) {
			if (quest.getPlanner().cooldown < 0) {
				return QuestStatus.FINISHED;
			}
			else {
				if (quester.getCooldownDifference(quest) < 0) {
					return QuestStatus.CAN_DO;
				} else return QuestStatus.CANT_DO;
			}
		}
		if (quest.testRequirements(quester)) {
				return QuestStatus.CAN_DO;
		} else return QuestStatus.CANT_DO;
	}
	
	public static ItemStack getGUIIcon(Player player, Quest quest) {
		return getStatus(player, quest).getIcon(player, quest);
	}
	
	public static ItemStack getGUIIcon(Player player, String quest) {
		Quest q = getQuestsPlugin().getQuest(quest);
		return getStatus(player, q).getIcon(player, q);
	}
	

	public static ItemStack getCurrenIcon(Quest quest, Player player) {
		ItemStack item = new ItemStack(Material.BOOK);
		Quester qt = getQuestsPlugin().getQuester(player.getUniqueId());
		ItemStackUtils.setDisplayName(item, "§6§l" + quest.getName());
		
		List<String> lore = Lists.newArrayList();
		lore.add("§aTiến trình: §f" + getStageInfo(quest, qt.getCurrentQuests().get(quest)));
		lore.add("");
		qt.getObjectives(quest, false).forEach(s -> lore.add("§f>> §7" + s));
		lore.add("");
		lore.add("§cShift + Click để hủy Quest");
		ItemStackUtils.setLore(item, lore);
		
		return item;
	}
	
	public static String getStageInfo(Quest quest, int stage) {
		int max = quest.getStages().size();
		return stage + "/" + max;
	}

	public static String getObjective(Player player, Quest quest) {
		Quests quests = (Quests) Bukkit.getPluginManager().getPlugin("Quests");
		Quester quester = quests.getQuester(player.getUniqueId());

		String prefix = "";

		if (quester.getCurrentStage(quest) != null) {
			for (CustomObjective o : quester.getCurrentStage(quest).getCustomObjectives()) {
				if (o.getName().equalsIgnoreCase("Placeholder Change")) {
					double v = getReal(player, o, quest);
					double r = getRequirement(player, o, quest);

					LinkedList<Map.Entry<String, Object>> l = quester.getCurrentStage(quest).getCustomObjectiveData();
					for (Map.Entry<String, Object> e : l) {
						if (e.getKey().equalsIgnoreCase("Add")) {
							double add = Double.parseDouble(e.getValue().toString());
							double current = (add - (r - v));
							prefix = "[" + Double.valueOf(current).intValue() + "/" + Double.valueOf(add).intValue() + "] ";
						}
					}
				}
			}
		}

		return prefix + quester.getCurrentObjectives(quest, false).get(0).replace("§a", "");
	}

	public static final String KEY = "qs-qc-%placeholder%";

	private static double getRequirement(Player p, CustomObjective o, Quest quest) {
		PlayerData pd = PlayerDataAPI.get(p, "quests");
		for (Map.Entry<String, String> e : Maps.newHashMap(pd.getDataMap()).entrySet()) {
			String k = e.getKey();
			String v = e.getValue();
			if (k.contains(KEY.replace("%placeholder%", ""))) {
				String plh = k.replace(KEY.replace("%placeholder%", ""), "");
				String qplh = o.getDataForPlayer(p, o, quest).get("Placeholder").toString();
				if (!plh.equalsIgnoreCase(qplh)) continue;
				return Double.parseDouble(v);
			}
		}
		return -1;
	}

	private static double getReal(Player p, CustomObjective o, Quest quest) {
		PlayerData pd = PlayerDataAPI.get(p, "quests");
		for (Map.Entry<String, String> e : Maps.newHashMap(pd.getDataMap()).entrySet()) {
			String k = e.getKey();
			if (k.contains(KEY.replace("%placeholder%", ""))) {
				String plh = k.replace(KEY.replace("%placeholder%", ""), "");
				String qplh = o.getDataForPlayer(p, o, quest).get("Placeholder").toString();
				if (!plh.equalsIgnoreCase(qplh)) continue;
				return Double.parseDouble(PlaceholderAPI.setPlaceholders(p, plh));
			}
		}
		return -1;
	}
	
}
