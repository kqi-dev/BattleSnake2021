package com.battlesnake.sskt;

import java.util.ArrayList;

public class UtilFunctions {
	static int[][] floodfill(CoordinatePair head) { //probably a useless function after realizing you can do this mathematically due to movement constraints
		int[][] ret = new int[Constants.WIDTH][Constants.HEIGHT];
		floodfillHelper(new CoordinatePair(head.x, head.y), 0, ret);

		return ret;
	}
  
	private static void floodfillHelper(CoordinatePair head, int dist, int[][] ret) {
		if(head.x < 0 || head.x >= Constants.WIDTH || head.y < 0 || head.y >= Constants.HEIGHT) {
			return;
		}
		if(ret[head.x][head.y] != 0) {
			return;
		}

		ret[head.x][head.y] = dist;

		floodfillHelper(new CoordinatePair(head.x + 1, head.y), dist + 1, ret);
		floodfillHelper(new CoordinatePair(head.x - 1, head.y), dist + 1, ret);
		floodfillHelper(new CoordinatePair(head.x, head.y + 1), dist + 1, ret);
		floodfillHelper(new CoordinatePair(head.x, head.y - 1), dist + 1, ret);
	}
	
	static boolean isClosestTo(ArrayList<ArrayList<CoordinatePair>> SnakeCoordinates, CoordinatePair food) {
		if(!(SnakeCoordinates.size() > 1)) {
			return true;
		}
		for(int i = 1; i < SnakeCoordinates.size(); i++) {
			if(SnakeCoordinates.get(0).get(0).cardinalDistanceTo(food) >= SnakeCoordinates.get(i).get(0).cardinalDistanceTo(food)) {
				return false;
			}
		}
		return true;
	}
	
	static String moveToTarget(int[][] gameBoard, CoordinatePair snakeHead, CoordinatePair target) {
		if(snakeHead.x == target.x && snakeHead.y == target.y) {
			return safeMove("random");
		}
		if(snakeHead.x == target.x && snakeHead.y < target.y) {
			return safeMove("up");
		}
		if(snakeHead.x == target.x && snakeHead.y > target.y) {
			return safeMove("down");
		}
		if(snakeHead.x < target.x && snakeHead.y == target.y) {
			return safeMove("right");
		}
		if(snakeHead.x > target.x && snakeHead.y == target.y) {
			return safeMove("left");
		}
		if(snakeHead.x < target.x && snakeHead.y < target.y) {
			if(0.5 < Math.random()) {
				return safeMove("right");
			}
			else {
				return safeMove("up");
			}
		}
		if(snakeHead.x < target.x && snakeHead.y > target.y) {
			if(0.5 < Math.random()) {
				return safeMove("right");
			}
			else {
				return safeMove("down");
			}
		}
		if(snakeHead.x > target.x && snakeHead.y < target.y) {
			if(0.5 < Math.random()) {
				return safeMove("left");
			}
			else {
				return safeMove("up");
			}
		}
		if(snakeHead.x > target.x && snakeHead.y > target.y) {
			if(0.5 < Math.random()) {
				return safeMove("left");
			}
			else {
				return safeMove("down");
			}
		}
		return "null";
	}
	
	static String safeMove(String direction) {
		switch(direction) {
		case "up":
			return "up";
		case "right":
			return "right";
		case "down":
			return "down";
		case "left":
			return "left";
		case "random":
			return Constants.CARDINALMOVEMENTS[(int)(Math.random() * 4)];
		}
		return "null";
	}
}