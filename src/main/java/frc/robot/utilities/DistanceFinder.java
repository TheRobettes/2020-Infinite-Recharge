/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.utilities;

//TODO distance equation, pixels to feet calculation could be moved here from Target analysis

public class DistanceFinder {
  private static int c = 0;
  public static final int MAX_DISTANCE = 25;
  private static double closestDistance = MAX_DISTANCE; //from the widest contour, 18 is max shooting distance
  public static double filterDistance(double currentDistance){
    c++;
    System.out.println("DistanceFinder - currentDistance: " + currentDistance);
    if(currentDistance < closestDistance){
      closestDistance = currentDistance;
    }
    if(c >= 3){
      return closestDistance;
    }
    return 0;
  }
  public static void resetFinder(){
    c = 0;
    closestDistance = MAX_DISTANCE;
  }
}
  

