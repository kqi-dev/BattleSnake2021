package com.battlesnake.sskt;

import java.util.ArrayList;

public class Snake {
	CoordinatePair head;
	ArrayList<CoordinatePair> body;
	//int[][] distances;

	Snake(CoordinatePair headCo, ArrayList<CoordinatePair> bodyList) {
		head = headCo;
		body = bodyList;
		//distances = UtilFunctions.floodfill(headCo);
	}

}