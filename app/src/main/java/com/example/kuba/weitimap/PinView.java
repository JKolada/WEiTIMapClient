package com.example.kuba.weitimap;

/**
 * Created by Kuba on 2016-04-14.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;


public class PinView extends SubsamplingScaleImageView {

    private PointF redPin, bluePin;
    private Bitmap pin_red_bitmap, pin_blue_bitmap;
    enum pinColor {PIN_RED, PIN_BLUE};
    private pinColor colorTemp;


    public PinView(Context context) {
        this(context, null);
    }

    public PinView(Context context, AttributeSet attr) {
        super(context, attr);
        initialise();
    }

    public void setPin(PointF sPin, pinColor color) {
        colorTemp = color;
        switch (color) {
            case PIN_RED:
                redPin = sPin;
                break;
            case PIN_BLUE:
                bluePin = sPin;
                break;
            default:
                return;
        }
        initialise();
        invalidate();
    }

    public void resetPins() {
        redPin = null;
        bluePin = null;

    }

    private void initialise() {
        float density = getResources().getDisplayMetrics().densityDpi;

        if (redPin != null) {
            pin_red_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.location_red);
            float w = (density/420f) * pin_red_bitmap.getWidth();
            float h = (density/420f) * pin_red_bitmap.getHeight();
            pin_red_bitmap = Bitmap.createScaledBitmap(pin_red_bitmap, (int)w, (int)h, true);
        }

        if (bluePin != null) {
            pin_blue_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.location_blue);
            float w = (density/420f) * pin_blue_bitmap.getWidth();
            float h = (density/420f) * pin_blue_bitmap.getHeight();
            pin_blue_bitmap = Bitmap.createScaledBitmap(pin_blue_bitmap, (int)w, (int)h, true);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isReady()) {
            return;
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        if (redPin != null) {
            PointF vPin = sourceToViewCoord(redPin);
            float vX = vPin.x - (pin_red_bitmap.getWidth()/2);
            float vY = vPin.y - pin_red_bitmap.getHeight();
            canvas.drawBitmap(pin_red_bitmap, vX, vY, paint);
        }

        if (bluePin != null) {
            PointF vPin = sourceToViewCoord(bluePin);
            float vX = vPin.x - (pin_blue_bitmap.getWidth()/2);
            float vY = vPin.y - pin_blue_bitmap.getHeight();
            canvas.drawBitmap(pin_blue_bitmap, vX, vY, paint);
        }
    }

}
