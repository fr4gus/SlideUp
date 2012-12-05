package org.slideup.lib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class HeaderView extends ContentView {

    public HeaderView(Context context) {
        this(context, null);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs, false);
    }

    public int getDestScrollY() {
        if (isMenuOpen()) {
            return getBehindHeight();
        } else {
            return 0;
        }
    }

    public int getChildTop(int i) {
        return 0;
    }

    public int getChildBottom(int i) {
        return getChildTop(i) + getChildHeight(i);
    }

    public boolean isMenuOpen() {
        return getScrollX() == 0;
    }

    public int getCustomHeight() {
        int i = isMenuOpen() ? 0 : 1;
        return getChildHeight(i);
    }

    public int getChildHeight(int i) {
        if (i <= 0) {
            return getBehindHeight();
        } else {
            return getChildAt(i).getMeasuredHeight();
        }
    }

    public int getBehindHeight() {
        ViewGroup.LayoutParams params = getLayoutParams();
        return params.width;
    }

    @Override
    public void setContent(View v) {
        super.setMenu(v);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }

}
