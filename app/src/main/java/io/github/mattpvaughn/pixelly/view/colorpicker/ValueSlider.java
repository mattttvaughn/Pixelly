package io.github.mattpvaughn.pixelly.view.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;

import static io.github.mattpvaughn.pixelly.view.colorpicker.ColorPickerDialog.OPAQUE;

/**
 * Created by Matt on 7/30/2016.
 */
public class ValueSlider extends AppCompatSeekBar {
    private int color;
    private Paint paint;

    public ValueSlider(Context context, AttributeSet attrs, int color) {
        super(context, attrs);
        init(color);
        this.color = color;
    }

    private void init(int color) {
        this.color = color;
        setPadding(getThumb().getIntrinsicWidth() / 2, getPaddingTop(),
                getThumb().getIntrinsicWidth() / 2, getPaddingBottom());
    }


    public ValueSlider(Context context) {
        super(context);
        init(Color.BLUE);
    }

    public ValueSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(Color.BLUE);
    }

    @Override
    protected synchronized void onDraw(@NonNull Canvas canvas) {
        setPaint();

        int height = this.getHeight();
        int width = this.getWidth();

        int sliderHeight = Utils.dpToPixels(8, getResources());
        int gutterHeight = (height - sliderHeight) / 2;

        canvas.drawRect(getPaddingLeft(), height - gutterHeight,
                width - getPaddingRight(),
                gutterHeight, paint);

        Drawable thumb = getThumb();

        thumb.setColorFilter(
                Color.argb(OPAQUE, Color.red(color), Color.green(color),
                        Color.blue(color)),
                PorterDuff.Mode.SRC);
        Rect bounds = getThumb().getBounds();
        thumb.setBounds(bounds.left, height / 2, bounds.right, height / 2);
        thumb.draw(canvas);
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    private void setPaint() {
        // For some reason the thumb doesn't align correctly, use this to hack it back in place
        setThumbOffset(getThumb().getIntrinsicWidth() / 2);

        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);

        // Get the endpoints of the gradient
        hsv[2] = 0f;
        int minValueColor = Color.HSVToColor(OPAQUE, hsv);

        hsv[2] = 1f;

        int maxValueColor = Color.HSVToColor(OPAQUE, hsv);

        LinearGradient valueGradient = new LinearGradient(0f, 0f,
                this.getMeasuredWidth() - this.getThumb().getIntrinsicWidth(), 0f,
                new int[]{minValueColor, maxValueColor},
                null, Shader.TileMode.CLAMP
        );
        paint = new Paint();
        paint.setShader(valueGradient);
    }


}
