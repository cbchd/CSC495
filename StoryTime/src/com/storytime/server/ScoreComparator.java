package com.storytime.server;

import java.util.Comparator;

public class ScoreComparator<E extends Comparable<E>> implements Comparator<E> {

	@Override
	public int compare(E a, E b) {
		if (a.compareTo(b) > 0) {
			return -1;
		} else if (a.compareTo(b) == 0) {
			return 0;
		} else if (a.compareTo(b) < 0) {
			return 1;
		}
		return 0;
	}

	

}
