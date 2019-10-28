package com.pri.architecture_boilerplate.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by Priyanka on 10/5/17.
 */

public class CustomEditText extends AppCompatEditText {

    private ColorStateList mPrefixTextColor;
    private float mPrefixTextSize;
    private String prefix;

    public CustomEditText(Context context) {
        this(context, null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPrefixTextColor = getTextColors();
        mPrefixTextSize = getTextSize();
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        if (TextUtils.isEmpty(prefix)) {
            setCompoundDrawables(null, null, null, null);
        } else
            setCompoundDrawables(new TextDrawable(prefix), null, null, null);
    }

    public void setSuffix(String prefix) {
        setCompoundDrawables(null, null, new TextDrawable(prefix), null);
    }

    public void setPrefixAndSuffix(String prefix, Drawable drawable) {
        int h = drawable.getIntrinsicHeight();
        int w = drawable.getIntrinsicWidth();
        drawable.setBounds(0, 0, w, h);

        setCompoundDrawables(new TextDrawable(prefix), null, drawable, null);

    }


    public void setPrefixTextColor(int color) {
        mPrefixTextColor = ColorStateList.valueOf(color);
    }

    public void setPrefixTextColor(ColorStateList color) {
        mPrefixTextColor = color;
    }

    public void setPrefixTextSize(float size) {
        mPrefixTextSize = size;
    }

    public String getPrefix() {
        return prefix;
    }


    private class TextDrawable extends Drawable {
        private String mText = "";

        public TextDrawable(String text) {
            mText = text;
            Paint paint = new Paint(getPaint());
            paint.setTextSize(mPrefixTextSize);
            setBounds(0, 0, (int) paint.measureText(mText) + 2, (int) mPrefixTextSize);
        }

        @Override
        public void draw(Canvas canvas) {
            Paint paint = new Paint(getPaint());
            paint.setColor(mPrefixTextColor.getColorForState(getDrawableState(), 0));
            paint.setTextSize(mPrefixTextSize);
            int lineBaseline = getLineBounds(0, null);
            canvas.drawText(mText, 0, canvas.getClipBounds().top + lineBaseline, paint);
        }

        @Override
        public void setAlpha(int alpha) {/* Not supported */}

        @Override
        public void setColorFilter(ColorFilter colorFilter) {/* Not supported */}

        @Override
        public int getOpacity() {
            return 1;
        }
    }
}