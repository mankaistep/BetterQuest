package me.manaki.plugin.betterquest.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.manaki.plugin.betterquest.BetterQuest;
import me.manaki.plugin.betterquest.dailyquest.DQUtils;
import me.manaki.plugin.betterquest.utils.GUIUtils;
import me.manaki.plugin.betterquest.utils.QuestStatus;
import me.manaki.plugin.betterquest.utils.QuestUtils;

public class GUIOpener {
	
	public static final int NEXT_ARROW = 10;
	public static final int PREVIOUS_ARROW = 37;
	public static final int INFO = 19;
	public static final int TUT = 28;
	
	public static final String MAIN_TITLE = "§2§lNHIỆM VỤ";
	
	public static void openMainGUI(Player player) {
		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
		int max = 0;
		for (SpecialGUI sg : SpecialGUI.values()) {
			if (sg.getGUIData().getSlot() > max) max = sg.getGUIData().getSlot();
		}
		for (CustomGUI cg : CustomGUI.guis.values()) {
			if (cg.getGUIData().getSlot() > max) max = cg.getGUIData().getSlot();
		}
		max++;
		int size = max % 9 == 0 ? max : (max / 9 + 1) * 9;
		if (BetterQuest.GUI_SIZE != 0) size = BetterQuest.GUI_SIZE;
		Inventory inv = Bukkit.createInventory(null, size, MAIN_TITLE);
		player.openInventory(inv);
		
		
		Bukkit.getScheduler().runTaskAsynchronously(BetterQuest.plugin, () -> {
			for (int i = 0 ; i < inv.getSize() ; i++) {
				inv.setItem(i, GUIUtils.getBlackSlot());
			}
			for (SpecialGUI sg : SpecialGUI.values()) {
				inv.setItem(sg.getGUIData().getSlot(), sg.getGUIData().createIcon());
			}
			for (CustomGUI cg : CustomGUI.guis.values()) {
				inv.setItem(cg.getGUIData().getSlot(), cg.getGUIData().createIcon());
			}
		});
	}
	
	public static void openGUI(Player player, GUIData guiData, List<String> quests, int page) {
		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
		int max = (quests.size() / 20 + 1);
		Inventory inv = Bukkit.createInventory(new CaiDitMeMay(page, guiData, quests), 54, guiData.getTitle() + " [" + page + "/" + max + "]");
		player.openInventory(inv);
		
		Bukkit.getScheduler().runTaskAsynchronously(BetterQuest.plugin, () -> {
			for (int i = 0 ; i < inv.getSize() ; i++) {
				inv.setItem(i, GUIUtils.getBlackSlot());
			}
			
			getQuestSlots().forEach(i -> {
				inv.setItem(i, GUIUtils.getBarrierSlot());
			});
			
			inv.setItem(NEXT_ARROW, getArrowNext());
			inv.setItem(PREVIOUS_ARROW, getPreviousArrow());
			inv.setItem(INFO, getInfoItem(player));
			inv.setItem(TUT, getTutItem());
			
			int start = 0 + (page - 1) * 20;
			int end = 19 + (page - 1) * 20;
			
			if (page > max) {
				player.sendMessage("§4Không tồn tại trang " + page);
				return;
			}
			
			for (int i = start ; i <= Math.min(end, quests.size() - 1) ; i++) {
				String qN = quests.get(i);
				Quest quest = QuestUtils.getQuestsPlugin().getQuest(qN);
				if (quest == null) continue;
				inv.setItem(getQuestSlots().get(i % 20), QuestUtils.getGUIIcon(player, quest));
			}
		});
	}
	
	public static void eventClick(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		if (e.getInventory().getHolder() instanceof CaiDitMeMay) {
			e.setCancelled(true);
			
			Inventory inv = e.getInventory();
			CaiDitMeMay holder = (CaiDitMeMay) e.getInventory().getHolder();
			List<String> quests = holder.getQuests();
			
			int slot = e.getSlot();
			int amount = quests.size();
			int max = (amount / 20 + 1);
			int page = ((CaiDitMeMay) e.getInventory().getHolder()).getPage();
			
			Quester qt = QuestUtils.getQuestsPlugin().getQuester(player.getUniqueId());
			
			if (e.getSlot() == NEXT_ARROW) {
				if (page >= max) return;
				openGUI(player, holder.getGUIData(), quests, page + 1);
			}
			else if (e.getSlot() == PREVIOUS_ARROW) {
				if (page == 1) return;
				openGUI(player, holder.getGUIData(), quests, page - 1);;
			}
			
			if (!getQuestSlots().contains(slot)) return;
			
			if (e.getClick() == ClickType.RIGHT || e.getClick() == ClickType.LEFT) {
				ItemStack item = e.getCurrentItem();
				if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return; 
				String qN = item.getItemMeta().getDisplayName().replace("§6§l", "");
				Quest quest = QuestUtils.getQuestsPlugin().getQuest(qN);
				if (quest == null) return;
				
				if (QuestUtils.getStatus(player, quest) == QuestStatus.CAN_DO) {
					qt.takeQuest(quest, false);
					inv.setItem(slot, QuestUtils.getGUIIcon(player, quest));
				}
			}
			
			else if (e.getClick() == ClickType.SHIFT_RIGHT || e.getClick() == ClickType.SHIFT_LEFT) {
				ItemStack item = e.getCurrentItem();
				if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return; 
				String qN = item.getItemMeta().getDisplayName().replace("§6§l", "");
				Quest quest = QuestUtils.getQuestsPlugin().getQuest(qN);
				if (quest == null) return;
				
				if (QuestUtils.getStatus(player, quest) == QuestStatus.STARTED) {
					if (DQUtils.getQuests(player).contains(qN)) {
						qt.quitQuest(quest, "§cĐã hủy quest " + qN);
					} else player.sendMessage("§cChỉ có thể hủy Nhiệm vụ hằng ngày");

				}
			}
			
		} else if (e.getView().getTitle().equals(MAIN_TITLE)) {
			e.setCancelled(true);
			int slot = e.getSlot();
			if (e.getClickedInventory() == e.getWhoClicked().getOpenInventory().getTopInventory()) {
				for (SpecialGUI sg : SpecialGUI.values()) {
					if (slot == sg.getGUIData().getSlot() ) {
						openGUI(player, sg.getGUIData(), sg.getQuests(player), 1);
					}
				}
				for (CustomGUI cg : CustomGUI.guis.values()) {
					if (slot == cg.getGUIData().getSlot() ) {
						openGUI(player, cg.getGUIData(), cg.getQuests(), 1);
					}
				}
			}
		}
	}
	
	public static List<Integer> getQuestSlots() {
		List<Integer> l = Lists.newArrayList();
		for (int j = 0 ; j < 4 ; j ++) {
			for (int i = 12 + j * 9 ; i <= 16 + j * 9 ; i++) l.add(i);	
		}
		return l;
	}
	
	public static ItemStack getArrowNext() {
		ItemStack other = new ItemStack(Material.ARROW, 1);
		ItemMeta meta = other.getItemMeta();
		meta.setDisplayName("§a§lTrang sau >>");
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		other.setItemMeta(meta);
		return other;
	}
	
	public static ItemStack getPreviousArrow() {
		ItemStack other = new ItemStack(Material.ARROW, 1);
		ItemMeta meta = other.getItemMeta();
		meta.setDisplayName("§a§l<< Trang trước");
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		other.setItemMeta(meta);
		return other;
	}
	
	public static ItemStack getInfoItem(Player player) {
		ItemStack other = new ItemStack(Material.PAPER, 1);
		ItemMeta meta = other.getItemMeta();
		meta.setDisplayName("§a§lThông tin");
		
		List<String> lore = Lists.newArrayList();
		lore.add("§fĐiểm quest: §7" + QuestUtils.getQuestsPlugin().getQuester(player.getUniqueId()).getQuestPoints());
		
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		other.setItemMeta(meta);
		return other;
	}
	
	public static ItemStack getTutItem() {
		ItemStack other = new ItemStack(Material.MAP, 1);
		ItemMeta meta = other.getItemMeta();
		meta.setDisplayName("§a§lHướng dẫn");
		
		List<String> lore = Lists.newArrayList();
		lore.add("§f>> §7Click để nhận Quest");
		
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		other.setItemMeta(meta);
		return other;
	}
	
}

class CaiDitMeMay implements InventoryHolder {

	private int page;
	private GUIData guiData;
	private List<String> quests;
	
	public CaiDitMeMay(int page, GUIData guiData, List<String> quests) {
		super();
		this.page = page;
		this.guiData = guiData;
		this.quests = quests;
	}
	
	public List<String> getQuests() {
		return this.quests;
	}
	
	public GUIData getGUIData() {
		return this.guiData;
	}
	
	public int getPage() {
		return this.page;
	}
	
	@Override
	public Inventory getInventory() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
