package me.manaki.plugin.betterquest.v3;

import be.maximvdw.featherboard.FeatherBoard;
import be.maximvdw.featherboard.api.FeatherBoardAPI;
import com.google.common.collect.Lists;
import me.blackvein.quests.Quest;
import me.manaki.plugin.betterquest.BetterQuest;
import me.manaki.plugin.betterquest.utils.QuestUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class V3 {

    private static List<String> mainQuests;
    private static String featherBoard = null;

    private static final List<String> toggledOn = Lists.newArrayList();

    public static void reload(FileConfiguration config) {
        if (config.getBoolean("featherboard.enable")) {
            featherBoard = config.getString("featherboard.board");
        } else featherBoard = null;
        mainQuests = config.getStringList("main-quests");
    }

    public static String getMainQuest(Player player) {
        var quester = QuestUtils.getQuestsPlugin().getQuester(player.getUniqueId());
        for (Quest q : quester.getCurrentQuests().keySet()) {
            if (mainQuests.contains(q.getName())) return q.getName();
        }
        return null;
    }

    public static boolean isMainQuest(Player player, Quest q) {
        return getMainQuest(player).equalsIgnoreCase(q.getName());
    }

    public static void openBoard(Player player, int seconds, boolean message) {
        if (featherBoard == null) return;
        toggledOn.add(player.getName());
        FeatherBoardAPI.showScoreboard(player, featherBoard);
        if (seconds != -1) {
            Bukkit.getScheduler().runTaskLater(BetterQuest.plugin, () -> {
                closeBoard(player);
            }, seconds * 20L);
        }
        if (message) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            player.sendMessage("");
            player.sendMessage("§aĐã chuyển sang chế độ Bảng Nhiệm vụ, tắt sau " + seconds + " giây");
            player.sendMessage("§aĐể chuyển về mặc định hoặc bật, ghi: §c/bangnhiemvu");
            player.sendMessage("");
        }
    }

    public static void closeBoard(Player player) {
        FeatherBoardAPI.removeScoreboardOverride(player, featherBoard);
        toggledOn.remove(player.getName());
    }

    public static boolean isToggledOn(Player player) {
        return toggledOn.contains(player.getName());
    }

    public static void quit(Player player) {
        toggledOn.remove(player.getName());
    }

}
