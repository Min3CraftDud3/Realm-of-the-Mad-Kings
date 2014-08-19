package me.faris.rotmk.helpers;

public enum Tier {
	UT("UT"), TIER_0("0"), TIER_1("1"), TIER_2("2"), TIER_3("3"), TIER_4("4"), TIER_5("5"), TIER_6("6"), TIER_7("7"), TIER_8("8"), TIER_9("9"), TIER_10("10"), TIER_11("11"), TIER_12("12"), TIER_13("13");

	private String tierName = "";

	private Tier(String tierName) {
		this.tierName = tierName;
	}

	public String getTier() {
		return this.tierName;
	}

	public static Tier getTier(int tier) {
		if (tier == -1) return UT;
		else if (tier == 0) return TIER_0;
		else if (tier == 1) return TIER_1;
		else if (tier == 2) return TIER_2;
		else if (tier == 3) return TIER_3;
		else if (tier == 4) return TIER_4;
		else if (tier == 5) return TIER_5;
		else if (tier == 6) return TIER_6;
		else if (tier == 7) return TIER_7;
		else if (tier == 8) return TIER_8;
		else if (tier == 9) return TIER_9;
		else if (tier == 10) return TIER_10;
		else if (tier == 11) return TIER_11;
		else if (tier == 12) return TIER_12;
		else if (tier == 13) return TIER_13;
		else return TIER_0;
	}
}
