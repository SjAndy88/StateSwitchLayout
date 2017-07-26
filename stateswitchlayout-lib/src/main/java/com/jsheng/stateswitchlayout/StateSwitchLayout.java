package com.jsheng.stateswitchlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by shengjun on 2017/7/23.
 */

public class StateSwitchLayout extends FrameLayout {
    private static final int LOADING_TIME = 600;

    private static final int INVALID_ID = -1;

    // 初始的状态
    public static final int STATE_INITIAL = 0;
    // 加载的状态
    public static final int STATE_LOADING = 1;
    // 加载失败的状态
    public static final int STATE_ERROR = 2;
    // 加载空的状态
    public static final int STATE_EMPTY = 3;
    // 加载成功的状态
    public static final int STATE_SUCCEED = 4;

    private View mLoadingView;// 转圈的view
    private View mErrorView;// 错误的view
    private View mEmptyView;// 空的view
    private View mSucceedView;// 成功的view

    private int mLayoutLoading;
    private int mLayoutError;
    private int mLayoutEmpty;


    private int mState;// 默认的状态
    private LayoutInflater mInflater;
    private OnClickListener mErrorClickListener;

    private long mTimeLoadingStart;

    public StateSwitchLayout(@NonNull Context context) {
        super(context);
    }

    public StateSwitchLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateSwitchLayout(@NonNull Context context, @Nullable AttributeSet attrs,
                             @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.StateSwitchLayout);

        mLayoutLoading = a.getResourceId(R.styleable.StateSwitchLayout_layoutLoading, INVALID_ID);
        mLayoutError = a.getResourceId(R.styleable.StateSwitchLayout_layoutError, INVALID_ID);
        mLayoutEmpty = a.getResourceId(R.styleable.StateSwitchLayout_layoutEmpty, INVALID_ID);

        a.recycle();

        mInflater = LayoutInflater.from(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mState = STATE_INITIAL;
        getSucceedView();
    }

    public int getState() {
        return mState;
    }

    public void setErrorClickListener(OnClickListener errorClickListener) {
        mErrorClickListener = errorClickListener;
    }

    private void setStartTime() {
        mTimeLoadingStart = System.currentTimeMillis();
    }

    private long getDelayTime() {
        long timeLoadingEnd = System.currentTimeMillis();
        return LOADING_TIME - (timeLoadingEnd - mTimeLoadingStart);
    }

    public void switchToLoading() {
        showLoadingView();
    }

    /**
     * @deprecated pls use {@link #switchToLoading()}
     */
    public void showLoadingView() {
        hideAllChild(mLoadingView);
        getLoadingView().setVisibility(VISIBLE);
        mState = STATE_LOADING;
        setStartTime();
    }

    public void switchToError() {
        long delayTime = getDelayTime();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                showErrorView();
            }
        }, delayTime > 0 ? delayTime : 0);
    }

    /**
     * @deprecated pls use {@link #switchToError()}
     */
    public void showErrorView() {
        hideAllChild(mErrorView);
        getErrorView().setVisibility(VISIBLE);
        mState = STATE_ERROR;
    }

    public void switchToEmpty() {
        long delayTime = getDelayTime();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                showEmptyView();
            }
        }, delayTime > 0 ? delayTime : 0);
    }

    /**
     * @deprecated pls use {@link #switchToEmpty()}
     */
    public void showEmptyView() {
        hideAllChild(mEmptyView);
        getEmptyView().setVisibility(VISIBLE);
        mState = STATE_EMPTY;
    }

    public void switchToSucceed() {
        long delayTime = getDelayTime();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                showSucceedView();
            }
        }, delayTime > 0 ? delayTime : 0);
    }

    /**
     * @deprecated pls use {@link #switchToSucceed()}
     */
    public void showSucceedView() {
        hideAllChild(mSucceedView);
        mState = STATE_SUCCEED;
        getSucceedView().setVisibility(VISIBLE);
    }

    private void hideAllChild(View exceptView) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child != exceptView) {
                child.setVisibility(INVISIBLE);
            }
        }
    }

    private View createStateView(int layoutId) {
        if (layoutId == INVALID_ID) {
            throw new IllegalArgumentException("createStateView with a invalid layoutId");
        }
        View view = mInflater.inflate(layoutId, this, false);
        addView(view);
        return view;
    }

    private View getLoadingView() {
        if (mLoadingView == null) {
            mLoadingView = createStateView(mLayoutLoading);
            mLoadingView.setClickable(true);
        }
        return mLoadingView;
    }

    private View getErrorView() {
        if (mErrorView == null) {
            mErrorView = createStateView(mLayoutError);
            mLoadingView.setClickable(true);
        }
        if (mErrorClickListener != null) {
            mErrorView.setOnClickListener(mErrorClickListener);
        }
        return mErrorView;
    }

    private View getEmptyView() {
        if (mEmptyView == null) {
            mEmptyView = createStateView(mLayoutEmpty);
            mLoadingView.setClickable(true);
        }
        return mEmptyView;
    }

    private View getSucceedView() {
        if (mSucceedView == null) {
            mSucceedView = getChildAt(0);
            if (mSucceedView == null) {
                throw new IllegalArgumentException("StateSwitchLayout need a child view at least");
            }
        }
        return mSucceedView;
    }
}
