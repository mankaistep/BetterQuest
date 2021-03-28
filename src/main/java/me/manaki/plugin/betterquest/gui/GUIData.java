package me.manaki.plugin.betterquest.gui;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.manaki.plugin.betterquest.utils.Utils;

public class GUIData {
	
	private String title;
	private int slot;
	private Material icon;
	private String name;
	private String desc;
	private List<String> commands;
	
	public GUIData(String title, int slot, String name, Material icon, String desc, List<String> commands) {
		this.title = title;
		this.slot = slot;
		this.name = name;
		this.icon = icon;
		this.desc = desc;
		this.commands = commands;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public int getSlot() {
		return this.slot;
	}
	
	public Material getIcon() {
		return this.icon;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDesc() {
		return this.desc;
	}
	
	public List<String> getCommands() {
		return this.commands;
	}
	
	public ItemStack createIcon() {
		ItemStack is = new ItemStack(this.icon);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(this.name.replace("&", "§"));
		meta.setLore(Utils.toList(this.desc, 20, "§7§o"));
		is.setItemMeta(meta);
		
		return is;
	}
	
}
