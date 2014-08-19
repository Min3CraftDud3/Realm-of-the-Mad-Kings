package me.faris.rotmk.helpers;

import java.util.Random;

import me.faris.rotmk.helpers.utils.Utilities;

public class ClassStatistic {
	private int max = 1, min = 0;

	public ClassStatistic(int max, int min) {
		this.max = Math.max(max, min);
		this.min = Math.min(max, min);
	}

	public int getMaximum() {
		return this.max;
	}

	public int getMinimum() {
		return this.min;
	}

	public int getRandom() {
		if (this.max == this.min) return this.max;
		Random random = Utilities.getRandom();
		int randomNumber = random.nextInt(this.max + 1);
		while (randomNumber < this.min)
			randomNumber = random.nextInt(this.max + 1);
		return randomNumber;
	}

	public void setMaximum(int max) {
		this.max = max;
	}

	public void setMinimum(int min) {
		this.min = min;
	}

	public String toString() {
		return this.max + "-" + this.min;
	}

}
