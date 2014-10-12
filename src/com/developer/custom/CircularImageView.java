package com.developer.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircularImageView extends ImageView {
	
	public CircularImageView(Context context) {
	    super(context);
	    // TODO Auto-generated constructor stub
	}
	
	public CircularImageView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}
	
	public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
	
	    Drawable drawable = getDrawable();
	
	    if (drawable == null) {
	        return;
	    }
	    
	    if (getWidth() == 0 || getHeight() == 0) {
	        return; 
	    }
	    Bitmap b =  ((BitmapDrawable)drawable).getBitmap() ;
	    Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
	
	    //int w = getWidth(), h = getHeight();
	
	
	    Bitmap roundBitmap =  getCircularBitmapFrom(bitmap);
	    canvas.drawBitmap(roundBitmap, 0,0, null);
	
	}
	
	public static Bitmap getCircularBitmapFrom(Bitmap bitmap) {
	    if (bitmap == null || bitmap.isRecycled()) {
	        return null;
	    }
	    float radius = bitmap.getWidth() > bitmap.getHeight() ? ((float) bitmap
	            .getHeight()) / 2f : ((float) bitmap.getWidth()) / 2f;
	    Bitmap canvasBitmap = Bitmap.createBitmap(bitmap.getWidth(),
	            bitmap.getHeight(), Bitmap.Config.ARGB_8888);
	    BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP,
	            TileMode.CLAMP);
	    Paint paint = new Paint();
	    paint.setAntiAlias(true);
	    paint.setShader(shader);

	    Canvas canvas = new Canvas(canvasBitmap);

	    canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
	            radius, paint);

	    return canvasBitmap;
	}
}
