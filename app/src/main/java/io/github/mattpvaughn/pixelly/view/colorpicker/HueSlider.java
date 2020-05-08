package io.github.mattpvaughn.pixelly.view.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;

import io.github.mattpvaughn.pixelly.R;

import static io.github.mattpvaughn.pixelly.view.colorpicker.ColorPickerDialog.OPAQUE;

/**
 * Created by Matt on 7/30/2016.
 */
public class HueSlider extends AppCompatSeekBar {
    private Paint paint;
    private int color;

    public HueSlider(Context context) {
        super(context);
    }

    public HueSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HueSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    // This is probably really bad practice! I couldn't figure out how to properly set
    // a thin lineargradient so I overrode onDraw and replaced the whole
    // damn ondraw stuff...
    // P.S.- please don't spend hours trying to figure out how to do the gradient thing
    // again... I swear this way works.
    @Override
    protected synchronized void onDraw(@NonNull Canvas canvas) {
        // Only create paint when we need it- so getWidth() will be ready...
        // --> don't need to override anything else this way (onmeasure or whatev)
        if (paint == null) {
            setPaint();
        }

        int height = this.getHeight();
        int width = this.getWidth();

        int sliderHeight = (int) getContext().getResources()
                                             .getDimension(
                                                     R.dimen.colorpicker_slider_height);
        int gutterHeight = (height - sliderHeight) / 2;

        canvas.drawRect(getPaddingLeft() / 2, height - gutterHeight,
                width - gutterHeight,
                gutterHeight, paint);

        getThumb().setColorFilter(
                Color.argb(OPAQUE, Color.red(color), Color.green(color),
                        Color.blue(color)),
                PorterDuff.Mode.SRC_IN);
        Rect bounds = getThumb().getBounds();
        getThumb().setBounds(bounds.left, height / 2, bounds.right, height / 2);
        getThumb().draw(canvas);
    }

    private void setPaint() {
        // For some reason the thumb doesn't align correctly, use this to hack it back in place
        setThumbOffset(getThumb().getIntrinsicWidth() / 2);
        setPadding(getThumb().getIntrinsicWidth() / 2, getPaddingTop(),
                getThumb().getIntrinsicWidth() / 2, getPaddingBottom());

        int width = getMeasuredWidth();
        LinearGradient hueGradient = new LinearGradient(0.f, 0.f, width, 0.0f,
                new int[]{0xFFFF0000, 0xFFFFFF00, 0xFF00FF00, 0xFF00FFFF,
                          0xFF0000FF, 0xFFFF00FF, 0xFFFF0000},
                null, Shader.TileMode.CLAMP);
        paint = new Paint();
        paint.setShader(hueGradient);
    }


}
