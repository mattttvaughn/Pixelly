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
public class SaturationSlider extends AppCompatSeekBar {
    private int color;
    private Paint paint;

    public SaturationSlider(Context context) {
        super(context);
    }

    public SaturationSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SaturationSlider(Context context, AttributeSet attrs, int color) {
        super(context, attrs);
        this.color = color;
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    @Override
    protected synchronized void onDraw(@NonNull Canvas canvas) {
        setPaint();

        int height = this.getHeight();
        int width = this.getWidth();

        int sliderHeight =
                (int) getContext().getResources()
                            .getDimension(R.dimen.colorpicker_slider_height);
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

        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);

        // Get the endpoints of the gradient
        hsv[1] = 0f;
        int fullyDesaturatedColor = Color.HSVToColor(OPAQUE, hsv);

        hsv[1] = 1f;
        int fullySaturatedColor = Color.HSVToColor(OPAQUE, hsv);

        int width = getMeasuredWidth();
        LinearGradient colorGradient = new LinearGradient(0f, 0f, width, 0f,
                new int[]{fullyDesaturatedColor, fullySaturatedColor},
                null, Shader.TileMode.CLAMP
        );
        paint = new Paint();
        paint.setShader(colorGradient);
    }

}
