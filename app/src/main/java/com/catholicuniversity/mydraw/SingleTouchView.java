package com.catholicuniversity.mydraw;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import javax.annotation.Resource;

/**
 * Created by ccw on 2016-12-25.
 */

/*
to-do-list
0. 이미지와 캔버스를 겹치기. 캔버스와 이미지 view를 겹쳐놓아 드로우가 가능하게 만들기.( 제일 중요 ) - 해결
1. 왼쪽, 오른쪽에 드로우 띄우기 - 해결
2. 왼쪽 드로우 3칸으로 나누기(?) 아직 확정 안됨.
3. 카테고리를 만들어서 누르면 해당경로에 있는 이미지 파일을 띄우기
4. 0-0 드로우를 브러쉬 별, 형광펜 등등 펜을 선택 가능하게 만들기
5.
 */

public class SingleTouchView extends View {
    private Paint paint = new Paint();
    private Path path=new Path();
    private int paintColor = 0xFF000000;
    private Paint canvasPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private Uri mImageCaptureUri;
    private SingleTouchView userPhoto;
    ImageButton redBtn, blackBtn, yellowBtn, orangeBtn, whiteBtn;


    public SingleTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10f);
        paint.setColor(paintColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        //path = new Path();

        //canvasBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ribs);

        //Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.ribs);
        //canvasBitmap = bmp.copy(Bitmap.Config.ARGB_8888,true);
        //drawCanvas = new Canvas(canvasBitmap);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0,0, canvasPaint);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:

                drawCanvas.drawPath(path, paint);
                path.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void setColor(String newColor) {
        invalidate();
        paintColor = Color.parseColor(newColor);
        paint.setColor(paintColor);
    }

    public void doTakeAlbumAction(){

            Intent intent =new Intent(Intent.ACTION_PICK);
            intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
            mImageCaptureUri = intent.getData();

            Bitmap bmp = BitmapFactory.decodeFile(mImageCaptureUri.getPath());

        /*
            intent.putExtra("outputX",200);
            intent.putExtra("outputY",200);
            intent.putExtra("aspectX",1);
            intent.putExtra("aspectY",1);
            intent.putExtra("scale",true);
            intent.putExtra("return-data", true);

            final Bundle extras =intent.getExtras();
            Bitmap photo = extras.getParcelable("intent");
            userPhoto.setImageBitmap(photo);
        */
    }

}