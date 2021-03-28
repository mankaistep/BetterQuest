package me.manaki.plugin.betterquest.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

	public static int randomInt(int min, int max) {
		if (min == max) return min;
		return new Random().nextInt(max - min + 1) + min;
	}

	public static List<String> toList(String s, int length, String start) {
		List<String> result = new ArrayList<String>();
		if (s == null)
			return result;
		if (!s.contains(" ")) {
			result.add(s);
			return result;
		}

		String[] words = s.split(" ");
		int l = 0;
		String line = "";
		for (int i = 0; i < words.length; i++) {
			l += words[i].length();
			if (l > length) {
				result.add(line.substring(0, line.length() - 1));
				l = words[i].length();
				line = "";
				line += words[i] + " ";
			} else {
				line += words[i] + " ";
			}
		}

		if (!line.equalsIgnoreCase(" "))
			result.add(line);

		for (int i = 0; i < result.size(); i++) {
			result.set(i, start + result.get(i));
		}

		return result;
	}
	
}
