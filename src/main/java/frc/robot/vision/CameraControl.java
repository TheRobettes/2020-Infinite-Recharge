/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.vision;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Timer;

/**
 * Do NOT add any static variables to this class, or any initialization at all. Unless you know what
 * you are doing, do not modify this file except to change the parameter class to the startRobot
 * call.
 */
public final class CameraControl {
  private static final int FPS = 15 ; // NOTE: slower than 'execute()' rate!
  public static final int IMG_WIDTH = 160;
  public static final int IMG_HEIGHT = 120;
  private static final int TELEOP_EXPOSURE = 36;
  private static final int VISION_EXPOSURE = 10; //init test: 4
  private static UsbCamera camera1 = null;
  private static CvSink cvSink1 = null;
  //private static ArrayList<Rect> rectangles = new ArrayList<Rect>();
  public static boolean isImageReady = false;
  static final Scalar WHITE = new Scalar(255, 255, 255);
  static final Scalar RED = new Scalar(0, 0, 255);
  static final Scalar GREEN = new Scalar(0, 255,0);
  private static Mat image = null;
  public static int lastCount = 999;
  private static final GripPipeline pipeline = new GripPipeline();
  private static boolean isPipelineActive = false;

  
  public static void cameraInit() {
    camera1 = CameraServer.getInstance()
    .startAutomaticCapture(0); // aka“cam0”
    camera1.setResolution(IMG_WIDTH, IMG_HEIGHT);
    camera1.setFPS(FPS);
    camera1.setExposureManual(VISION_EXPOSURE);
    cvSink1 = CameraServer.getInstance().getVideo(camera1);
    cvSink1.setEnabled(true);
    image = new Mat();
    visionFilterStreamer();
    }

    public static void setPipelineState(boolean pipelineState){
      isPipelineActive = pipelineState;
      System.out.println("pipeline status is " + isPipelineActive);
      int exposureState = (isPipelineActive)
      ? VISION_EXPOSURE
      : TELEOP_EXPOSURE;
      camera1.setExposureManual(exposureState);
      /*if(isPipelineActive){
        camera1.setExposureManual(VISION_EXPOSURE);
      }
      else{
        camera1.setExposureManual(TELEOP_EXPOSURE);
      }*/
    }

    private static void visionFilterStreamer() {
      // the following defines a run(){}, required for any Thread-subclass.
      Thread switcherThread = new Thread(() -> {
        CvSource outputStream = CameraServer.getInstance()
        .putVideo("Contours", IMG_WIDTH, IMG_HEIGHT);
        while(!Thread.interrupted()) {
          if ( !isPipelineActive ) {
            Timer.delay(.1);
            continue;
            }   
  
          // This next synchronized statement does nothing for now,
          // however it becomes relevant if cam2/cvSync2 feature
          // is created, the omitted-synchronized statement is
          // needed during camera-flips, if they are ever added.
          synchronized(image){
            long framestatus = cvSink1.grabFrame(image);
            // later, ...with a second camera...
            // =?: cvSink2.grabFrame(image);

              if (framestatus == 0) continue; 
          }
          if (isPipelineActive) //TODO: Vision command to make a button in OI for a vision test button 
            processImage(); // do GRIP stuff...
              // finally, send image-data to driver-console...
          outputStream.putFrame(image);
          System.gc();
        } // end of while(){}.
      }); // end of "lambda", ->, thread.run().
      
      switcherThread.start();
    } // end of visionFilterStreamer(){}.










  private static void processImage(){
  /* Call to a standard public method of any VisionPipeline.
  */
  pipeline.process(image);
  /* Here one of the generated pipeline-methods is called,
  * because a get-countours() step is needed in order to draw
  * pipeline results to a drivestation-display.
  * (your pipeline class will have this defined,
  * it just gets changed from private to public)
  * (This type of call is equivalent to adding a network-table
  * publishing step to the pipeline itself. )
  */
  ArrayList<Rect> contours = TargetAnalysis.filter(pipeline.findContoursOutput());
  /* Read the current contours, draw each as a rectangle,
  * also add each into a set that can be used by TargetAnalysis.
  */
  
  //see commented rectangles above

  //synchronized(rectangles)
  {
  // keep the same set-instance, because its synchronized...,
    //rectangles.clear(); // but reset it to empty.
    int c = contours.size();
    for (int x = 0; x < c; x++) {
      Rect r = contours.get(x);
        //rectangles.add(r);
        //draw r onto camera stream
        boolean isLast = (x== c-1); //last rect is red line, combined contours
        boolean isFirst = (x == 0); //first rect is left edge (how much we're filtering)

        Scalar color = (isFirst)
          ? GREEN //color sorcery: the programming wizards are here!
            :(isLast) //nested ? : operator to get 3 values
            ? RED
            : WHITE;
        int lineThickness = (isFirst || isLast) 
          ? 3 
          : 2;
          Imgproc.rectangle(image,
          new Point(r.x, r.y),
          new Point(r.x+r.width, r.y+r.height),
          color, lineThickness
        );
    } //end for loop
   
    if (c != lastCount) {
    System.out.println(" pipeline contains " + lastCount );
    lastCount = c;
    }
    isImageReady = true;
  } // now Vision-Commands can get latest distances
  // from newest TargetAnalysis-set
  }


  /*
  see commented rectangles
  public static ArrayList<Rect> getContours() {
    synchronized(rectangles) {
    isImageReady = false;
    return (ArrayList<Rect>) rectangles.clone();
    }
    } */
}