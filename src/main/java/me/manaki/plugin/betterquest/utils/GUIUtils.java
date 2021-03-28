package me.manaki.plugin.betterquest.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIUtils {

	public static ItemStack getBlackSlot() {
		ItemStack other = new ItemStack(Material.GLASS_PANE);
		ItemMeta meta = other.getItemMeta();
		meta.setCustomModelData(1);
		meta.displayName(Component.text(""));
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		other.setItemMeta(meta);
		return other;
	}
	
	public static ItemStack getBarrierSlot() {
		ItemStack other = new ItemStack(Material.BARRIER, 1);
		ItemMeta meta = other.getItemMeta();
		meta.displayName(Component.text(""));
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		other.setItemMeta(meta);
		return other;
	}
	
}