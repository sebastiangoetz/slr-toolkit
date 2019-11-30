package de.tudresden.slr.questionnaire.util;

import java.util.List;

public class CollectionUtils {
	public static <T> boolean moveUp(List<T> list, T element) {
		int index = list.indexOf(element);
		if (index > 0) {
			list.remove(index);
			list.add(index - 1, element);
			return true;
		}
		return false;
	}
	
	public static <T> boolean moveDown(List<T> list, T element) {
		int index = list.indexOf(element);
		if (index + 1 < list.size()) {
			list.remove(index);
			list.add(index + 1, element);
			return true;
		}
		return false;
	}
}
