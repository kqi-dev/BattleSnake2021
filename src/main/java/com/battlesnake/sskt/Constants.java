package com.battlesnake.sskt;

public class Constants {
	static final int EMPTYSQUARE = 0;
	static final int SNAKEHEAD = 1;
	static final int SNAKEBODY = 2;
	static final int ENEMYHEAD = 3;
	static final int ENEMYBODY = 4;
	static final int FOOD = 5;
	
	static final String[] CARDINALMOVEMENTS = {"up", "right", "down", "left"};
	
	static int WIDTH;
	static int HEIGHT;
	static int HEALTH;

	static void setWidth(int width) {
		WIDTH = width;
	}
	static void setHeight(int height) {
		HEIGHT = height;
	}
	static void setHealth(int health) {
		HEALTH = health;
	}
}