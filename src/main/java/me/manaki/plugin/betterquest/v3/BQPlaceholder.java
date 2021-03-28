package me.manaki.plugin.betterquest.v3;

import me.blackvein.quests.Quest;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.manaki.plugin.betterquest.utils.QuestUtils;
import me.manaki.plugin.betterquest.utils.Utils;
import org.bukkit.entity.Player;

import java.util.List;

public class BQPlaceholder extends PlaceholderExpansion  {

    @Override
    public String getIdentifier() {
        return "bquest";
    }

    @Override
    public String getAuthor() {
        return "MankaiStep";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    // %mainquest_name%, %mainquest_stage%, %mainquest_objective_1%, %mainquest_objective_2%
    @Override
    public String onPlaceholderRequest(Player player, String s){

        String mainQuest = V3.getMainQuest(player);
        if (mainQuest == null) return "Không có";

        var quester = QuestUtils.getQuestsPlugin().getQuester(player.getUniqueId());
        Quest q = QuestUtils.getQuestsPlugin().getQuest(mainQuest);

        if (s.equalsIgnoreCase("main_quest_name")) {
            return q.getName() + "";
        }

        else if (s.equalsIgnoreCase("main_quest_stage")) {
            return QuestUtils.getStageInfo(q, quester.getCurrentQuests().get(q));
        }

        else if (s.equalsIgnoreCase("main_quest_objective_1")) {
            String o = QuestUtils.getObjective(player, q);
            List<String> os = Utils.toList(o, 30, "");
            return os.get(0);
        }

        else if (s.equalsIgnoreCase("main_quest_objective_2")) {
            String o = QuestUtils.getObjective(player, q);
            List<String> os = Utils.toList(o, 23, "");
            return os.size() > 1 ? os.get(1) : "";
        }

        return "Wrong placeholder";
    }

}
