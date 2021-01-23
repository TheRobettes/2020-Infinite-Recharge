package frc.robot.vision;

import java.util.ArrayList;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class TargetAnalysis {
    
    public static boolean isImageGood = false;
    public static double distance;
    public static double targetXLeft; //a percentage of the image size
    public static double targetXCenter;
    public static double targetXRight;

    //distance calc: based on converting contour width in pixels to distance in feet
    private static final double DISTX1 = 15; //"closer"
    private static final double DISTX2 = 17.5; //"farther"
    private static final double WIDTHY1 = 33; //pixels
    private static final double WIDTHY2 = 28;
    private static final double DISTANCESLOPE = (WIDTHY2-WIDTHY1)/(DISTX2-DISTX1);
    private static final double DISTYINT = WIDTHY1-(DISTANCESLOPE*DISTX1);
    //used the (x1,y1) but could use (x2,y2)

    public static final double FIELD_OF_VIEW = 67;
    public static final double DEG_PER_PIXEL = FIELD_OF_VIEW/CameraControl.IMG_WIDTH;

    //filter limits
    private static final int MAX_CONTOUR_HEIGHT = (int)(0.4 * CameraControl.IMG_HEIGHT);
    
    //will change this for real robot (victoria was short)
    //private static final int TOP_VERTICAL_POSITION = (int)(0.5 * CameraControl.IMG_HEIGHT);

    //changed for the real robot which is taller 
    private static final int FILTER_TOP = (int)(0.5 * CameraControl.IMG_HEIGHT);
    private static final int FILTER_BOTTOM = (int) (0.8 * CameraControl.IMG_HEIGHT);


    public static ArrayList<Rect> filter(ArrayList<MatOfPoint> unfilteredList){
        ArrayList<Rect> filteredList = new ArrayList<Rect>();
        int c = unfilteredList.size();
        isImageGood = c != 0;
         if(!isImageGood){
            return filteredList; //don't jump into calculated values without contours
        }
        //c = count

        for (int x = 0; x < c; x++) {
            Rect r = Imgproc.boundingRect(unfilteredList.get(x));
            //screen size = 120*160, rect size = about 5% of screen
            //TODO: revisit width filtering (if needed)- yes it is needed 
           
            if(r.y > FILTER_TOP && r.y + r.height < FILTER_BOTTOM) { 
                //r.y < image top and r.y > image bottom means we wat ti to be in the middle part of the screen
                filteredList.add(r);
            }

            //victoria filtering cuz it was short
            /* if(r.height < MAX_CONTOUR_HEIGHT && r.y < TOP_VERTICAL_POSITION) //the good stuff
            {
                filteredList.add(r);
            }     */   
        }

        Rect calculatedRect =  calculateValues(filteredList);
        Rect filterRect = new Rect(0, FILTER_TOP, 0, FILTER_BOTTOM-FILTER_TOP);
        filteredList.add(calculatedRect);
        filteredList.add(0, filterRect); //0 makes this the 1st rectangle in isFIRST logic
        return filteredList;

    }
    public static Rect calculateValues(ArrayList<Rect> filteredList){
        int c = filteredList.size();
        //c = count

        int contourX = CameraControl.IMG_WIDTH;
        int rightPixel = 0;
        for (int x = 0; x < c; x++) {
            Rect thisContour = filteredList.get(x);
            if (thisContour.x < contourX){
                contourX = thisContour.x;  
            }
                
            int thisRightPixel = thisContour.x + thisContour.width;
            if (thisRightPixel > rightPixel){ 
                rightPixel = thisRightPixel;
            }
        
        }
        
        Rect bigContour = new Rect (contourX,CameraControl.IMG_HEIGHT/2,rightPixel-contourX, 0);
        processRect(bigContour);
        return bigContour;


    
        
       //the old way
        /*int c = filteredList.size();
        //c = count
        isImageGood = c==1 || c==2;
        if(!isImageGood){
            return;
        }
        if(c==1){
            processRect(filteredList.get(0));
            return;
        }
        //TODO: combine rectangle 0 and rectangle 1 and process
        Rect contour1 = filteredList.get(0);
        Rect contour2 = filteredList.get(1);
        if(contour1.x < contour2.x){
            processRect(new Rect(contour1.x,0,contour1.width+contour2.width, 0));
        }
        else{
            processRect(new Rect(contour2.x,0,contour1.width+contour2.width, 0));
        }*/
    }

    private static void processRect(Rect visionTargetRect){
        distance = (DISTANCESLOPE*visionTargetRect.width + DISTYINT);
        SmartDashboard.putNumber("Target Distance:", distance);
        SmartDashboard.putNumber("Target Width:", visionTargetRect.width);
    
        //Left group analysis 
        double LeftEdgeToContour = .15*visionTargetRect.width+visionTargetRect.x;
        double ContourLeftOffset = LeftEdgeToContour - (CameraControl.IMG_WIDTH/2);
        targetXLeft = (ContourLeftOffset / CameraControl.IMG_WIDTH)*100;
        SmartDashboard.putNumber("Target Left Position % :", targetXLeft);

        //center group analysis
        double CenterEdgeToContour = .35*visionTargetRect.width+visionTargetRect.x;
        double ContourCenterOffset = CenterEdgeToContour - (CameraControl.IMG_WIDTH/2);
        targetXCenter = (ContourCenterOffset / CameraControl.IMG_WIDTH)*100;
        SmartDashboard.putNumber("Target Left Position % :", targetXCenter);

        //right group analysis 
        double RightEdgeToContour = .55*visionTargetRect.width+visionTargetRect.x;
        double ContourRightOffset = RightEdgeToContour - (CameraControl.IMG_WIDTH/2);
        targetXRight = (ContourRightOffset / CameraControl.IMG_WIDTH)*100;
        SmartDashboard.putNumber("Target Left Position % :", targetXRight);

        SmartDashboard.putNumber("Pixel Width: ", visionTargetRect.width);
    }
}
