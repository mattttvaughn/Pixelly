package io.github.mattpvaughn.pixelly.view.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;

import io.github.mattpvaughn.pixelly.R;

import static io.github.mattpvaughn.pixelly.view.colorpicker.ColorPickerDialog.OPAQUE;


/**
 * Created by Matt on 7/30/2016.
 *
 */
public class AlphaSlider extends AppCompatSeekBar {
    @ColorInt
    private int color;
    private Paint paint;
    private BitmapShader bitmapShader;

    public AlphaSlider(Context context) {
        super(context);
        init();
    }

    public AlphaSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
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


        int sliderHeight = (int) getContext().getResources().getDimension(R.dimen.colorpicker_slider_height);
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
        // Get the endpoints of the gradient
        int noAlphaColor = Color.argb(0, Color.red(color), Color.green(color),
                Color.blue(color));
        int fullAlphaColor = Color.argb(OPAQUE, Color.red(color),
                Color.green(color),
                Color.blue(color));

        LinearGradient colorGradient = new LinearGradient(0f, 0f,
                this.getMeasuredWidth() - this.getThumb().getIntrinsicWidth(), 0f,
                new int[]{noAlphaColor, fullAlphaColor},
                null, Shader.TileMode.CLAMP
        );

        Shader shader = new ComposeShader(colorGradient, bitmapShader,
                PorterDuff.Mode.ADD);

        paint.setShader(shader);
    }

    private void init() {
        // For some reason the thumb doesn't align correctly, use this to hack it back in place
        setThumbOffset(getThumb().getIntrinsicWidth() / 2);
        setPadding(getThumb().getIntrinsicWidth() / 2, getPaddingTop(),
                getThumb().getIntrinsicWidth() / 2, getPaddingBottom());

        Bitmap checkerboardTemplate = BitmapFactory.decodeResource(getResources(),
                R.drawable.checkerboard_tile);
        bitmapShader = new BitmapShader(checkerboardTemplate, Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT);
        paint = new Paint();
    }
}
