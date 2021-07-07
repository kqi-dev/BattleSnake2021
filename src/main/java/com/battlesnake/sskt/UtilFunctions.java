package com.battlesnake.sskt;

public class UtilFunctions {
	static int[][] floodfill(CoordinatePair head) {
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

		floodfillHelper(new CoordinatePair(head.x + 1, head.y), dist + 1, ret);floodfillHelper(new CoordinatePair(head.x - 1, head.y), dist + 1, ret);
		floodfillHelper(new CoordinatePair(head.x, head.y + 1), dist + 1, ret);floodfillHelper(new CoordinatePair(head.x, head.y - 1), dist + 1, ret);
	}
}