package in.blogspot.androidoupsolving.animalsvoiceenglish;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public class GestureFlingDetector implements View.OnTouchListener {

    private final GestureDetector gestureDetector;

    public GestureFlingDetector(Context context){
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    //-----------------------CLASS -> TO LISTEN TO TOUCH THEN SWIPE
    private final class GestureListener extends GestureDetector.SimpleOnGestureListener{
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true; //always true, otherwise fling won't be detected.
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try{
                float diffY = e2.getY()  - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if(Math.abs(diffX) > Math.abs(diffY)){
                    if(Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD){
                        if(diffX > 0){
                            onSwipeRight();
                        }
                        else{
                            onSwipeLeft();
                        }
                    }
                }
                else if(Math.abs(diffY) > Math.abs(diffX)){
                    if(Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD){
                        if(diffY > 0){
                            onSwipeBottom();
                        }
                        else{
                            onSwipeTop();
                        }
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }


            return result;
        }

    }


    public void onSwipeRight(){

    }
    public void onSwipeLeft(){

    }
    public void onSwipeBottom(){

    }
    public void onSwipeTop(){

    }
}
