package jp.shsit.shsinfo2025.ui.uses;

import android.content.Context;
import android.graphics.Matrix;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.WindowManager;

import jp.shsit.shsinfo2025.R;


public class CustomImageView5 extends androidx.appcompat.widget.AppCompatImageView {
    private Matrix matrix = new Matrix();
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private final float SCALE_MAX = 2.0f;
    private final float PINCH_SENSITIVITY = 3.0f;
    private float width1,SCALE_MIN;

    public CustomImageView5(Context context) {
        super(context);
        init(context);

    }

    public CustomImageView5(Context context, AttributeSet attrs) {
        super(context, attrs);

        //画面サイズの取得
        WindowManager winMan = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display disp = winMan.getDefaultDisplay();
        DisplayMetrics dispMet = new DisplayMetrics();
        disp.getMetrics(dispMet);
        width1=dispMet.xdpi;
        Log.i("item",dispMet.xdpi+","+dispMet.ydpi);

        init(context);

    }

    public CustomImageView5(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }


    private void init(Context context) {
        String name= PreferenceManager.getDefaultSharedPreferences(getContext()).getString("lang","日本語");
        if(name.equals("English")){
            setImageResource(R.drawable.use_tenki_eng);
        }else{
            setImageResource(R.drawable.use_tenki_jap);
        }
        setScaleType(ScaleType.MATRIX);

        scaleGestureDetector = new ScaleGestureDetector(context, simpleOnScaleGestureListener);
        gestureDetector = new GestureDetector(context,simpleOnGestureListener);


        //画像の初期値

      /*  float width2 = (float) (width1/getImageWidth());
        if(width2>1)
            width2=1;*/
        float width2=0.6f;
        SCALE_MIN=width2;
        matrix.postScale(width2,width2);
        setImageMatrix(matrix);
        invalidate();
        Log.i("item",width1+","+width2+","+getImageWidth());


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        setImageMatrix(matrix);
        gestureDetector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private ScaleGestureDetector.SimpleOnScaleGestureListener simpleOnScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        float focusX;
        float focusY;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = 1.0f;
            float previousScale = getMatrixValue(Matrix.MSCALE_Y);

            if (detector.getScaleFactor() >= 1.0f) {
                scaleFactor = 1 + (detector.getScaleFactor() - 1) / (previousScale * PINCH_SENSITIVITY);
            } else {
                scaleFactor = 1 - (1 - detector.getScaleFactor()) / (previousScale * PINCH_SENSITIVITY);
            }

            float scale = scaleFactor * previousScale;

            if (scale < SCALE_MIN) {
                return false;
            }

            if (scale > SCALE_MAX) {
                return false;
            }

            matrix.postScale(scaleFactor, scaleFactor, focusX,focusY);

            invalidate();

            return super.onScale(detector);

        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            focusX = detector.getFocusX();
            focusY = detector.getFocusY();
            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
        }

    };

    private float getMatrixValue(int index) {
        if (matrix == null) {
            matrix = getImageMatrix();
        }

        float[] values = new float[9];
        matrix.getValues(values);
        Log.i("item",values[0]+","+values[1]+","+values[2]+","+values[3]+","+values[4]+
                ","+values[5]+","+values[6]+","+values[7]+","+values[8]);
        float value = values[index];
        return value;
    }

    private final GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener(){

        @Override
        public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
            //viewの縦横長
            float imageViewWidth = getWidth();
            float imageViewHeight = getHeight();
            //画像の縦横長
            float imageWidth = getImageWidth();
            float imageHeight = getImageHeight();
            //画像の左辺、右辺のx座標
            float leftSideX = getMatrixValue(Matrix.MTRANS_X);
            float rightSideX = leftSideX + imageWidth;
            //画像の上辺、底辺のy座標
            float topY = getMatrixValue(Matrix.MTRANS_Y);
            float bottomY = topY + imageHeight;

            if(imageViewWidth >= imageWidth && imageViewHeight >= imageHeight){
                return false;
            }
            //指の動きに追随してほしいため符号を反転
            float x = -distanceX;
            float y = -distanceY;

            if(imageViewWidth > imageWidth){
                x = 0;
            } else {
                if(leftSideX >  0 && x >0 ){
                    x = -leftSideX;
                } else if(rightSideX < imageViewWidth && x < 0) {
                    x = imageViewWidth - rightSideX;
                }
            }

            if(imageViewHeight > imageHeight){
                y = 0;
            } else {
                if(topY > 0 && y > 0 ){
                    y = -topY;

                } else if(bottomY < imageViewHeight && y < 0){
                    y = imageViewHeight - bottomY ;
                }
            }
            //Matrixを操作
            matrix.postTranslate(x,y);
            //再描画
            invalidate();

            return super.onScroll(event1, event2, distanceX, distanceY);
        }
    };

    private float getImageWidth(){
        return (getDrawable().getIntrinsicWidth())*getMatrixValue(Matrix.MSCALE_X);
    }

    private float getImageHeight(){
        return (getDrawable().getIntrinsicHeight())*getMatrixValue(Matrix.MSCALE_Y);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i("item","終了");
        matrix=null;
        scaleGestureDetector=null;
        gestureDetector=null;

    }

}
