package xin.banghua.moyuan.AlphabeticalOrder;

import java.util.Comparator;

import xin.banghua.moyuan.Adapter.FriendList;

public class PinyinComparator implements Comparator<FriendList> {

	public int compare(FriendList o1, FriendList o2) {
		if (o1.getLetters().equals("@")
				|| o2.getLetters().equals("#")) {
			return 1;
		} else if (o1.getLetters().equals("#")
				|| o2.getLetters().equals("@")) {
			return -1;
		} else {
			return o1.getLetters().compareTo(o2.getLetters());
		}
	}
}
