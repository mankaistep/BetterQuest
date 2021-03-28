package me.manaki.plugin.betterquest.command;

import me.manaki.plugin.betterquest.v3.V3;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BNVCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        Player player = (Player) sender;
        if (V3.isToggledOn(player)) {
            V3.closeBoard(player);
            return false;
        }

        String mainQ = V3.getMainQuest(player);
        if (mainQ == null) {
            player.sendMessage("§cBạn không có Nhiệm vụ Chính nào!");
            return false;
        }

        V3.openBoard(player, -1, false);

        return false;
    }

}
