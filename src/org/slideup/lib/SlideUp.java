package org.slideup.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class SlideUp extends RelativeLayout {

    public static final int TOUCHMODE_MARGIN = 0;

    public static final int TOUCHMODE_FULLSCREEN = 1;

    private ContentView mViewAbove;

    private HeaderView mViewBehind;
    
    int offsetContent;

    public SlideUp(Context context) {
        this(context, null);
    }

    public SlideUp(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideUp(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutParams behindParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViewBehind = new HeaderView(context);
        addView(mViewBehind, behindParams);
        LayoutParams aboveParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViewAbove = new ContentView(context);
        addView(mViewAbove, aboveParams);
        // register the CustomViewBehind2 with the CustomViewAbove
        mViewAbove.setCustomViewBehind2(mViewBehind);

        // now style everything!
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.slideUp);
        // set the above and behind views if defined in xml
        int viewAbove = ta.getResourceId(R.styleable.slideUp_contentView, -1);
        if (viewAbove != -1) {
            View v = LayoutInflater.from(context).inflate(viewAbove, null);
            setViewAbove(v);
        }
        int viewBehind = ta.getResourceId(R.styleable.slideUp_headerView, -1);
        if (viewBehind != -1) {
            View v = LayoutInflater.from(context).inflate(viewBehind, null);
            setViewBehind(v);
        }
        int touchModeAbove = ta.getInt(R.styleable.slideUp_contentTouchMode, TOUCHMODE_MARGIN);
        setTouchModeAbove(touchModeAbove);
        int touchModeBehind = ta.getInt(R.styleable.slideUp_headerTouchMode, TOUCHMODE_MARGIN);
        setTouchModeBehind(touchModeBehind);

        int offsetBehind = (int) ta.getDimension(R.styleable.slideUp_headerOffset, 0);
        setBehindOffset(offsetBehind);
        
        int offsetAbove = (int) ta.getDimension(R.styleable.slideUp_contentOffset, 0);
//        setAboveOffset(offsetAbove);
        mViewAbove.setContentOffset(offsetAbove);
        
        float scrollOffsetBehind = ta.getFloat(R.styleable.slideUp_headerScrollScale, 0.50f);
        setBehindScrollScale(scrollOffsetBehind);
    }

    public void setViewAbove(int res) {
        setViewAbove(LayoutInflater.from(getContext()).inflate(res, null));
    }

    public void setViewAbove(View v) {
        mViewAbove.setContent(v);
    }

    public void setViewBehind(int res) {
        setViewBehind(LayoutInflater.from(getContext()).inflate(res, null));
    }

    public void setViewBehind(View v) {
        mViewBehind.setContent(v);
    }

    public void setSlidingEnabled(boolean b) {
        mViewAbove.setSlidingEnabled(b);
    }

    public boolean isSlidingEnabled() {
        return mViewAbove.isSlidingEnabled();
    }

    /**
     * Shows the behind view
     */
    public void showBehind() {
        mViewAbove.setCurrentItem(0);
    }

    /**
     * Shows the above view
     */
    public void showAbove() {
        mViewAbove.setCurrentItem(1);
    }

    /**
     * 
     * @return Whether or not the behind view is showing
     */
    public boolean isBehindShowing() {
        return mViewAbove.getCurrentItem() == 0;
    }

    /**
     * 
     * @return The margin on the bottom of the screen that the behind view scrolls to
     */
    public int getBehindOffset() {
        return ((RelativeLayout.LayoutParams) mViewBehind.getLayoutParams()).bottomMargin;
    }

    /**
     * 
     * @param i The margin on the bottom of the screen that the behind view scrolls to
     */
    public void setBehindOffset(int i) {
        RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams) mViewBehind.getLayoutParams());
        int top = params.topMargin;
        int left = params.leftMargin;
        int right = params.rightMargin;
        int bottom = params.bottomMargin;
        ((RelativeLayout.LayoutParams) mViewBehind.getLayoutParams()).setMargins(left, top, right, i);
    }

    /**
     * 
     * @param res The dimension resource to be set as the behind offset
     */
    public void setBehindOffsetRes(int res) {
        int i = (int) getContext().getResources().getDimension(res);
        setBehindOffset(i);
    }

    /**
     * 
     * @return The scale of the parallax scroll
     */
    public float getBehindScrollScale() {
        return mViewAbove.getScrollScale();
    }

//    /**
//     * 
//     * @param offset
//     */
//    public void setAboveOffset(int offset){
//    	offsetContent = offset;
//    	mViewAbove.setOf
////        RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams) mViewAbove.getLayoutParams());
////        int left = params.leftMargin;
////        int right = params.rightMargin;
//////        int bottom = params.bottomMargin + offset;
////        int bottom = params.bottomMargin;
////        int top = params.topMargin + offset;
//////        ((RelativeLayout.LayoutParams) mViewAbove.getLayoutParams()).setMargins(left, offset, right, bottom);
////        ((RelativeLayout.LayoutParams) mViewAbove.getLayoutParams()).setMargins(left, top, right, bottom);
//        
//    }
    /**
     * 
     * @param f The scale of the parallax scroll (i.e. 1.0f scrolls 1 pixel for every
     * 1 pixel that the above view scrolls and 0.0f scrolls 0 pixels)
     */
    public void setBehindScrollScale(float f) {
        mViewAbove.setScrollScale(f);
    }

    public int getTouchModeAbove() {
        return mViewAbove.getTouchModeAbove();
    }

    public void setTouchModeAbove(int i) {
        if (i != TOUCHMODE_FULLSCREEN && i != TOUCHMODE_MARGIN) {
            throw new IllegalStateException("TouchMode must be set to either"
                    + "TOUCHMODE_FULLSCREEN or TOUCHMODE_MARGIN.");
        }
        mViewAbove.setTouchModeAbove(i);
    }

    public int getTouchModeBehind() {
        return mViewAbove.getTouchModeBehind();
    }

    public void setTouchModeBehind(int i) {
        if (i != TOUCHMODE_FULLSCREEN && i != TOUCHMODE_MARGIN) {
            throw new IllegalStateException("TouchMode must be set to either"
                    + "TOUCHMODE_FULLSCREEN or TOUCHMODE_MARGIN.");
        }
        mViewAbove.setTouchModeBehind(i);
    }

    public void setFadeEnabled(boolean b) {
        mViewAbove.setBehindFadeEnabled(b);
    }

    public void setFadeDegree(float f) {
        mViewAbove.setBehindFadeDegree(f);
    }

    public static class SavedState extends BaseSavedState {
        boolean mBehindShowing;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBooleanArray(new boolean[] { mBehindShowing });
        }

        public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat
                .newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {

                    public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                });

        SavedState(Parcel in) {
            super(in);
            boolean[] showing = new boolean[1];
            in.readBooleanArray(showing);
            mBehindShowing = showing[0];
        }
    }

    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.mBehindShowing = isBehindShowing();
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        if (ss.mBehindShowing) {
            showBehind();
        } else {
            showAbove();
        }
    }

}