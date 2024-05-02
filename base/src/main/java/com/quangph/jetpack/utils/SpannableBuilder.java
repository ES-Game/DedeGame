package com.quangph.jetpack.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;

public class SpannableBuilder {
    private SpannableStringBuilder mSpannableStringBuilder;
    private int mLastStartSpanIndex;

    public SpannableBuilder() {
        this(null);
    }

    public SpannableBuilder(String org) {
        if (org != null) {
            mSpannableStringBuilder = new SpannableStringBuilder(org);
        } else {
            mSpannableStringBuilder = new SpannableStringBuilder("");
        }
    }

    public SpannableBuilder appendText(String text) {
        mLastStartSpanIndex = mSpannableStringBuilder.length();
        mSpannableStringBuilder.append(text);
        return this;
    }

    public SpannableBuilder withSpan(Object span) {
        int endIndex = mSpannableStringBuilder.length();
        mSpannableStringBuilder.setSpan(span, mLastStartSpanIndex, endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public SpannableStringBuilder getSpannedText() {
        return mSpannableStringBuilder;
    }
}
