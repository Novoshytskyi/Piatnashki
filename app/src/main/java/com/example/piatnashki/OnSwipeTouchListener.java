package com.example.piatnashki;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnSwipeTouchListener implements View.OnTouchListener{
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
    //
    private final GestureDetector gestureDetector;
    //
    public OnSwipeTouchListener(Context ctx){
        gestureDetector = new GestureDetector(ctx, new GestureListener()); // аналог фокуса ввода,
        // который закускает слущателя(обработчик) GestureListener
        // this.gestureDetector = gestureDetector;
    }
    //
    private final class GestureListener extends GestureDetector.SimpleOnGestureListener{
        // константы:
        private static final int MIN_DELTA_X = 100; // поиграться...
        private static final int MIN_DELTA_V = 100; // поиграться...

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float dx = e2.getX() - e1.getX();
                float dy = e2.getY() - e1.getY();
                if(Math.abs(dx) > Math.abs(dy)){ // left <-> right при свайпе
                    if(Math.abs(dx) > Math.abs(dy) && Math.abs(velocityX) > MIN_DELTA_V){ // Допуски...
                        // проверяем: left <-> right
                        if(dx > 0){
                            onSwipeRight();
                        }
                        else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if(Math.abs(dy) > MIN_DELTA_X && Math.abs(velocityY) > MIN_DELTA_V){
                    if(dy < 0){
                        onSwipeTop();
                    }
                    else {
                        onSwipeBottom();
                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            return result;
        }
        //return super.onFling(e1, e2, velocityX, velocityY);
    }

    public void onSwipeLeft(){ // Свайп "Влево".

    }
    public void onSwipeRight(){ // Свайп "Вправо".

    }
    public void onSwipeTop(){ // Свайп "Вверх".

    }
    public void onSwipeBottom(){ // Свайп "Вниз".

    }

}
