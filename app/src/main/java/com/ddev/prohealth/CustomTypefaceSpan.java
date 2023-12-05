package com.ddev.prohealth;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class CustomTypefaceSpan extends MetricAffectingSpan {
    private final Typeface typeface;

    public CustomTypefaceSpan(Typeface typeface) {
        this.typeface = typeface;
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        applyCustomTypeface(p);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        applyCustomTypeface(tp);
    }

    private void applyCustomTypeface(Paint paint) {
        paint.setTypeface(typeface);
    }
}
