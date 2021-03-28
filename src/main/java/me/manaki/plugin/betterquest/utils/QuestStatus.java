package me.manaki.plugin.betterquest.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;
import me.clip.placeholderapi.PlaceholderAPI;
import me.manaki.plugin.betterquest.help.QHelp;
import mk.plugin.playerdata.storage.PlayerData;
import mk.plugin.playerdata.storage.PlayerDataAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public enum QuestStatus {
	
	CANT_DO("Chưa thể làm", Material.BOOK, "§c") {
		@Override
		public ItemStack getIcon(Player player, Quest quest) {
			ItemStack item = new ItemStack(this.getMaterial());
			ItemStackUtils.setDisplayName(item, "§6§l" + quest.getName());
			List<String> lore = Lists.newArrayList();
//			lore.add(this.getColor() + this.getName());
//			lore.add("");
//			lore.add("§aYêu cầu: ");
			QuestUtils.getRequirements(quest).forEach(s -> {
				lore.add("§c§o" + s.replace("&", "§"));
			});
			ItemStackUtils.setLore(item, lore);
			ItemStackUtils.addFlag(item, ItemFlag.HIDE_ATTRIBUTES);
			
			return item;
		}
	},
	CAN_DO("Có thể làm", Material.ENCHANTED_BOOK, "§a") {
		@Override
		public ItemStack getIcon(Player player, Quest quest) {
			ItemStack item = new ItemStack(this.getMaterial());
			ItemStackUtils.setDisplayName(item, "§6§l" + quest.getName());
			
			List<String> lore = Lists.newArrayList();
			String desc = QuestUtils.getDesc(quest);
//			lore.add(this.getColor() + this.getName());
			lore.addAll(Utils.toList(desc, 25, "§7§o"));
			lore.add("");
			lore.add("§aCó thể làm, phần thưởng:");
			List<String> rewards = QuestUtils.getRewards(quest);
			rewards.forEach(s -> {
				lore.add(" §f" + s.replace("&", "§"));
			});
			ItemStackUtils.setLore(item, lore);
			ItemStackUtils.addFlag(item, ItemFlag.HIDE_ATTRIBUTES);
			
			return item;
		}
	},
	STARTED("Đang làm", Material.BOOK, "§e") {
		@SuppressWarnings("deprecation")
		@Override
		public ItemStack getIcon(Player player, Quest quest) {
			ItemStack item = new ItemStack(Material.BOOK);
			Quester qt = QuestUtils.getQuestsPlugin().getQuester(player.getUniqueId());
			ItemStackUtils.setDisplayName(item, "§6§l" + quest.getName());
			
			List<String> lore = Lists.newArrayList();
			lore.add("§aTiến trình: §f" + QuestUtils.getStageInfo(quest, qt.getCurrentQuests().get(quest)));
			lore.add("§aYêu cầu:");
			lore.add(QuestUtils.getObjective(player, quest));
			qt.getObjectives(quest, false).forEach(s -> lore.addAll(Utils.toList(ChatColor.stripColor(s), 25, "§e§o ")));
			if (QHelp.hasHelp(player, quest)) {
				lore.add("§aGợi ý:");
				lore.addAll(Utils.toList(QHelp.getHelp(player, quest), 25, "§7§o "));
			}
			lore.add("");
			lore.add("§cShift + Click để hủy Quest");
			ItemStackUtils.setLore(item, lore);
			
			return item;
		}
	},
	FINISHED("Đã hoàn thành", Material.KNOWLEDGE_BOOK, "§6") {
		@Override
		public ItemStack getIcon(Player player, Quest quest) {
			ItemStack item = new ItemStack(this.getMaterial());
			ItemStackUtils.setDisplayName(item, "§6§l" + quest.getName());
			ItemStackUtils.addLoreLine(item, this.getColor() + this.getName());
			ItemStackUtils.addFlag(item, ItemFlag.HIDE_ATTRIBUTES);
			return item;
		}
	};
	
	private String name;
	private Material material;
	private String color;
	
	public abstract ItemStack getIcon(Player player, Quest quest);
	
	private QuestStatus(String name, Material material, String color) {
		this.name = name;
		this.material = material;
		this.color = color;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Material getMaterial() {
		return this.material;
	}
	
	public String getColor() {
		return this.color;
	}
	
}
