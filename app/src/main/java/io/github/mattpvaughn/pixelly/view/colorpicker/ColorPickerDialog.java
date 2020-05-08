package io.github.mattpvaughn.pixelly.view.colorpicker;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SeekBar;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import io.github.mattpvaughn.pixelly.databinding.ColorPickerViewBinding;

/**
 * Created by Matt on 7/30/2016.
 */
public class ColorPickerDialog extends DialogFragment {
    static final int OPAQUE = 0xFF;
    @NonNull
    private static final String COLOR_STRING_TOKEN = "COLOR_STRING_TOKEN";
    public static final float MAX_HUE = 360.0f;
    public static final float SEEKBAR_DEFAULT_MAX = 100.0f;
    public static final float MAX_ALPHA = 255.0f;
    private static final String DEFAULT_COLOR = "#00AFF0";
    private float hue;
    private float saturation;
    private float value;
    private int alpha;

    private ColorPickerListener listener = null;


    private ColorPickerViewBinding binding;

    @NonNull
    public static ColorPickerDialog newInstance(@ColorInt int color) {
        ColorPickerDialog dialogFragment = new ColorPickerDialog();
        Bundle args = new Bundle(2);
        args.putInt(COLOR_STRING_TOKEN, color);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    public ColorPickerDialog setOnPositiveButtonClickListener(final ColorPickerListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() == null) {
            throw new AssertionError();
        }
        int color = getArguments().getInt(COLOR_STRING_TOKEN);

        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);

        this.hue = Math.round(hsv[0]);
        this.saturation = Math.round(hsv[1]);
        this.value = Math.round(hsv[2]);
        this.alpha = Math.round(Color.alpha(color));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Remove space for title
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        binding = ColorPickerViewBinding.inflate(inflater, container, false);

        setViewsForColorType();

        final int color = getColor();

        binding.hueSlider.setColor(color);
        binding.hueSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hue = getHueFromHueProgress(binding.hueSlider.getProgress());
                updateColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        binding.valueSlider.setColor(color);
        binding.valueSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value = binding.valueSlider.getProgress() / SEEKBAR_DEFAULT_MAX; // [0 ... 1]
                updateColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        binding.saturationSlider.setColor(color);
        binding.saturationSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                saturation = binding.saturationSlider.getProgress() / SEEKBAR_DEFAULT_MAX; // [0 ... 1]
                updateColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Initialize alpha slider same regardless of useappcolor
        binding.alphaSlider.setColor(color);
        binding.alphaSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                alpha = Math.round(binding.alphaSlider.getProgress() * MAX_ALPHA / SEEKBAR_DEFAULT_MAX);
                updateColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        binding.colorpickerInputColor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(@NonNull CharSequence s, int start, int before,
                                      int count) {
                String hexCode = s.toString();
                // Check if text is a valid color before setting it
                if (Utils.isValidColor(hexCode)) {
                    // Color is FF (fully opaque) unless user passes in an ARGB string
                    int color = Utils.parseColor(hexCode);

                    float[] hsv = new float[3];
                    Color.colorToHSV(color, hsv);

                    hue = hsv[0];
                    saturation = hsv[1];
                    value = hsv[2];
                    alpha = Color.alpha(color);

                    updateColor();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        binding.colorpickerFinishButton.setOnClickListener(
                v -> {
                    listener.onPositiveButtonClick(getColor());
                    ColorPickerDialog.this.dismiss();
                }
        );

        updateColor();

        return binding.getRoot();
    }

    private void setViewsForColorType() {// Initialize the demo colors
        binding.colorpickerInputColor.setVisibility(View.VISIBLE);
        binding.colorpickerInputColorLabel.setVisibility(View.VISIBLE);
    }

    // Refresh the slider backgrounds based on color taken from sliders
    private void updateColor() {
        // Get the color
        int color = getColor();

        binding.hueSlider.setProgress(Math.round(hue / MAX_HUE * SEEKBAR_DEFAULT_MAX));
        binding.saturationSlider.setProgress(Math.round(saturation * SEEKBAR_DEFAULT_MAX));
        binding.valueSlider.setProgress(Math.round(value * SEEKBAR_DEFAULT_MAX));
        binding.alphaSlider.setProgress(Math.round(alpha * SEEKBAR_DEFAULT_MAX / MAX_ALPHA));

        // Update the color of the sliders
        binding.alphaSlider.setColor(color);
        binding.valueSlider.setColor(color);
        binding.saturationSlider.setColor(color);
        binding.hueSlider.setColor(color);

    }

    private float getHueFromHueProgress(int hueProgress) {
        return Math.round(MAX_HUE * hueProgress / 100);
    }

    private int getColor() {
        return Color.HSVToColor(this.alpha,
                new float[]{this.hue, this.saturation, this.value});
    }

    public interface ColorPickerListener {
        void onPositiveButtonClick(int color);
    }
}
