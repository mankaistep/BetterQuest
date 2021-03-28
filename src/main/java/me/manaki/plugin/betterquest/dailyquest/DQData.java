package me.manaki.plugin.betterquest.dailyquest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

public class DQData {
	
	private List<String> quests = Lists.newArrayList();
	private List<String> doneQuests = Lists.newArrayList();
	private long time = 0;
	
	public DQData(List<String> quests, List<String> doneQuests, long time) {
		this.quests = quests;
		this.time = time;
		this.doneQuests = doneQuests;
	}
	
	public List<String> getQuests() {
		return this.quests;
	}
	
	public List<String> getDoneQuests() {
		return this.doneQuests;
	}
	
	public void addDoneQuests(String name) {
		this.doneQuests.add(name);
	}
	
	public long getTime() {
		return this.getTime();
	}
	
	public boolean needNewTime(long newTime) {
		Date oD = new Date(this.time);
		Date nD = new Date(newTime);
		LocalDateTime o = LocalDateTime.ofInstant(oD.toInstant(), ZoneId.systemDefault());
		LocalDateTime n = LocalDateTime.ofInstant(nD.toInstant(), ZoneId.systemDefault());
		if (o.getDayOfYear() != n.getDayOfYear()) return true;
		return false;
	}
	
}
