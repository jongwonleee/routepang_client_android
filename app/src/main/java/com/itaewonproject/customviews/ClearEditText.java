package com.itaewonproject.customviews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.itaewonproject.R;

import java.util.Objects;


/**
 * Created by TedPark on 16. 4. 11..
 */

public class ClearEditText extends AppCompatEditText implements TextWatcher, View.OnTouchListener, View.OnFocusChangeListener {

    private Drawable clearDrawable;
    private View.OnFocusChangeListener onFocusChangeListener;
    private View.OnTouchListener onTouchListener;

    public ClearEditText(final Context context) {
        super(context);
        init();
    }

    public ClearEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearEditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setOnFocusChangeListener(View.OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void setOnTouchListener(View.OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }


    private void init() {

        setPadding(25,25,0,25);
        Drawable tempDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_ico_close_circle_fill_s);
        clearDrawable = DrawableCompat.wrap(Objects.requireNonNull(tempDrawable));
        DrawableCompat.setTintList(clearDrawable,getHintTextColors());
        clearDrawable.setBounds(-40, 0, clearDrawable.getIntrinsicWidth()-40, clearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);


        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }


    @Override
    public void onFocusChange(final View view, final boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(Objects.requireNonNull(getText()).length() > 0);
        } else {
            setClearIconVisible(false);
        }

        if (onFocusChangeListener != null) {
            onFocusChangeListener.onFocusChange(view, hasFocus);
        }
    }


    @Override
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final int x = (int) motionEvent.getX();
        if (clearDrawable.isVisible() && x > getWidth() - getPaddingRight() - (clearDrawable.getIntrinsicWidth()-40)-100) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                setError(null);
                setText(null);
            }
            return true;
        }

        if (onTouchListener != null) {
            return onTouchListener.onTouch(view, motionEvent);
        } else {
            return false;
        }

    }

    @Override
    public final void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        if (isFocused()) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }


    private void setClearIconVisible(boolean visible) {
        clearDrawable.setVisible(visible, false);

        setCompoundDrawables(null, null, visible ? clearDrawable : null, null);
    }


}