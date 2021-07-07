package com.battlesnake.sskt;

public class CoordinatePair {
  int x;
  int y;

  CoordinatePair(int xCo, int yCo) {
    x = xCo;
    y = yCo;
  }

  public int[] getArray(){
    int[] ret = new int[2];
    ret[0] = x;
    ret[1] = y;
    return ret;
  }
}