package com.dede.dedegame.presentation.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.dede.dedegame.R;

public class InputEditText extends ConstraintLayout {
    private View container;
    private ImageView imvIcon;
    private EditText edtInput;
    private TextView txtError;
    private int text_error_color, input_color, hint_color;
    private String hint_content, error_content, input_content;
    private Drawable imageIcon;

    public InputEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        container = LayoutInflater.from(context).inflate(R.layout.customize_edt_input, this, true);
        imvIcon = container.findViewById(R.id.imvIcon);
        edtInput = container.findViewById(R.id.edtInput);
        txtError = container.findViewById(R.id.txtError);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputEditText);
        text_error_color = typedArray.getColor(R.styleable.InputEditText_text_error_color, ContextCompat.getColor(context, R.color.errorColor));
        input_color = typedArray.getColor(R.styleable.InputEditText_input_color, ContextCompat.getColor(context, R.color.black));
        hint_color = typedArray.getColor(R.styleable.InputEditText_hint_color, ContextCompat.getColor(context, R.color.gray_300));
        hint_content = typedArray.getString(R.styleable.InputEditText_hint_content);
        error_content = typedArray.getString(R.styleable.InputEditText_error_content);
        input_content = typedArray.getString(R.styleable.InputEditText_input_content);
        imageIcon = typedArray.getDrawable(R.styleable.InputEditText_image_icon);

        edtInput.setTextColor(input_color);
        edtInput.setHintTextColor(hint_color);
        txtError.setTextColor(text_error_color);
        edtInput.setHint(hint_content);
        imvIcon.setBackground(imageIcon);
        txtError.setText(error_content);
        edtInput.setText(input_content);
        typedArray.recycle();

        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mListener != null) {
                    mListener.onTextChanged(String.valueOf(s), start, before, count);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtError.setVisibility(View.GONE);
    }


    public void setErrorTextColor(int color) {
        text_error_color = color;
        txtError.setTextColor(ContextCompat.getColor(txtError.getContext(), text_error_color));
    }

    public void setErrorText(String text) {
        error_content = text;
        txtError.setText(error_content);
    }

    public void setSecureTextEntry() {
        edtInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public void showError(boolean isShown) {
        if (isShown) {
            txtError.setVisibility(View.VISIBLE);
        } else {
            txtError.setVisibility(View.GONE);
        }
    }

    public void setInputTextColor(int color) {
        input_color = color;
        edtInput.setTextColor(ContextCompat.getColor(txtError.getContext(), input_color));
    }

    public void setInputText(String text) {
        input_content = text;
        edtInput.setText(input_content);
    }

    public void setHintTextColor(int color) {
        hint_color = color;
        edtInput.setHintTextColor(ContextCompat.getColor(txtError.getContext(), hint_color));
    }

    public void setHintText(String text) {
        hint_content = text;
        edtInput.setHint(hint_content);
    }

    public String getInputText() {
        return edtInput.getText().toString();
    }

    public void setOnEventViewListener(OnEventViewListener listener) {
        this.mListener = listener;
    }

    private OnEventViewListener mListener;

    public interface OnEventViewListener {
        void onTextChanged(String s, int start, int before, int count);
    }
}
