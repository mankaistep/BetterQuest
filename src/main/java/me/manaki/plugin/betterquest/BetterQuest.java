package me.manaki.plugin.betterquest;

import java.io.File;
import java.io.IOException;
import java.util.List;

import me.manaki.plugin.betterquest.command.BNVCommand;
import me.manaki.plugin.betterquest.v3.BQPlaceholder;
import me.manaki.plugin.betterquest.v3.V3;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import me.manaki.plugin.betterquest.command.Commands;
import me.manaki.plugin.betterquest.listener.EventListener;
import me.manaki.plugin.betterquest.dailyquest.DQCategory;
import me.manaki.plugin.betterquest.dailyquest.DQRequirement;
import me.manaki.plugin.betterquest.dailyquest.DQUtils;
import me.manaki.plugin.betterquest.gui.CustomGUI;
import me.manaki.plugin.betterquest.gui.GUIData;
import me.manaki.plugin.betterquest.gui.SpecialGUI;
import me.manaki.plugin.betterquest.help.QHelp;

public class BetterQuest extends JavaPlugin {

	public static BetterQuest plugin;
	public static int GUI_SIZE = 0;
	
	private FileConfiguration config;
	
	@Override
	public void onEnable() {
		plugin = this;
		this.saveDefaultConfig();
		this.reloadConfig();
		
		// Command
		this.getCommand("eq").setExecutor(new Commands());
		this.getCommand("bangnhiemvu").setExecutor(new BNVCommand());
		
		// Listener
		Bukkit.getPluginManager().registerEvents(new EventListener(), this);

		// Task
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
			Bukkit.getOnlinePlayers().forEach(DQUtils::checkData);
		}, 0, 20);

		//
		new BQPlaceholder().register();
	}

	public FileConfiguration getConfig() {
		return this.config;
	}
	
	public void reloadConfig() {
		File file = new File(this.getDataFolder(), "config.yml");
		config = YamlConfiguration.loadConfiguration(file);

		// V3
		V3.reload(config);

		// Daily Quests
		DQUtils.DQCategories.clear();
		config.getConfigurationSection("dailyquest.category").getKeys(false).forEach(id -> {
			List<String> quests = Lists.newArrayList();
			config.getStringList("dailyquest.category." + id + ".quests").forEach(qn -> {
				quests.add(qn);
			});;
			
			int min = config.getInt("dailyquest.category." + id + ".min-quests");
			int max = config.getInt("dailyquest.category." + id + ".max-quests");
	
			int minLv = 0;
			if (config.contains("dailyquest.category." + id + ".requirement.min-level")) minLv = config.getInt("dailyquest.category." + id + ".requirement.min-level");
			int qP = 0;
			if (config.contains("dailyquest.category." + id + ".requirement.quest-point")) qP = config.getInt("dailyquest.category." + id + ".requirement.quest-point");
			List<String> qD = Lists.newArrayList();
			if (config.contains("dailyquest.category." + id + ".requirement.quest-done")) qD = config.getStringList("dailyquest.category." + id + ".requirement.quest-done");
			List<String> data = Lists.newArrayList();
			if (config.contains("dailyquest.category." + id + ".requirement.playerdata")) data = config.getStringList("dailyquest.category." + id + ".requirement.playerdata");
			
			DQRequirement r = new DQRequirement(minLv, qP, qD, data);
			
			DQUtils.DQCategories.add(new DQCategory(quests, min, max, r));
		});
		
		// Special 
		config.getConfigurationSection("special-gui").getKeys(false).forEach(id -> {
			String title = config.getString("special-gui." + id + ".title").replace("&", "§");
			int slot = config.getInt("special-gui." + id + ".slot");
			String name = config.getString("special-gui." + id + ".name").replace("&", "§");
			Material icon = Material.valueOf(config.getString("special-gui." + id + ".icon"));
			String desc = config.getString("special-gui." + id + ".desc").replace("&", "§");
			List<String> cmds = config.getStringList("special-gui." + id + ".commands");
			GUIData dg = new GUIData(title, slot, name, icon, desc, cmds);
			SpecialGUI.valueOf(id.toUpperCase()).setGUIData(dg);
		});
		
		// Custom GUI
		CustomGUI.guis.clear();
		config.getConfigurationSection("custom-gui").getKeys(false).forEach(id -> {
			String title = config.getString("custom-gui." + id + ".title").replace("&", "§");
			int slot = config.getInt("custom-gui." + id + ".slot");
			String name = config.getString("custom-gui." + id + ".name").replace("&", "§");
			Material icon = Material.valueOf(config.getString("custom-gui." + id + ".icon"));
			String desc = config.getString("custom-gui." + id + ".desc").replace("&", "§");
			List<String> cmds = config.getStringList("custom-gui." + id + ".commands");
			List<String> quests = config.getStringList("custom-gui." + id + ".quests");
			GUIData gd = new GUIData(title, slot, name, icon, desc, cmds);
			CustomGUI.guis.put(id, new CustomGUI(gd, quests));
		});
		
		// Quests' help
		QHelp.reload(config);
		
		// Gui's size
		if (config.contains("size")) GUI_SIZE = config.getInt("size");
		
	}
	
	public void saveConfig() {
		File file = new File(this.getDataFolder(), "config.yml");
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
