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
    private Bitmap pin;
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
//        colorTemp = color;
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

    private void initialise() {
        float density = getResources().getDisplayMetrics().densityDpi;

//        if (colorTemp != null) {
//            switch (colorTemp) {
//                case PIN_RED:
                    pin = BitmapFactory.decodeResource(this.getResources(), R.drawable.location_red);
//                    break;
//                case PIN_BLUE:
//                    pin = BitmapFactory.decodeResource(this.getResources(), R.drawable.location_blue);
//                    break;
//                default:
//                    return;
//            }
//        }

        float w = (density/420f) * pin.getWidth();
        float h = (density/420f) * pin.getHeight();
        pin = Bitmap.createScaledBitmap(pin, (int)w, (int)h, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady()) {
            return;
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        if (redPin != null && pin != null) {
            PointF vPin = sourceToViewCoord(redPin);
            float vX = vPin.x - (pin.getWidth()/2);
            float vY = vPin.y - pin.getHeight();
            canvas.drawBitmap(pin, vX, vY, paint);
        }

        if (bluePin != null && pin != null) {
            PointF vPin = sourceToViewCoord(bluePin);
            float vX = vPin.x - (pin.getWidth()/2);
            float vY = vPin.y - pin.getHeight();
            canvas.drawBitmap(pin, vX, vY, paint);
        }

    }

}
