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
    // 失败的状态
    public static final int STATE_ERROR = 2;
    // 空载的状态
    public static final int STATE_EMPTY = 3;
    // 成功的状态
    public static final int STATE_SUCCEED = 4;

    protected View mLoadingView;// 加载的view
    protected View mErrorView;// 错误的view
    protected View mEmptyView;// 空载的view
    protected View mSucceedView;// 成功的view

    protected int mLayoutLoading;
    protected int mLayoutError;
    protected int mLayoutEmpty;

    protected boolean mLoadingWithContent;
    protected boolean mErrorWithContent;
    protected boolean mEmptyWithContent;

    protected int mState;// 默认的状态
    protected LayoutInflater mInflater;

    protected int mErrorClickId;
    protected int mEmptyClickId;
    protected OnClickListener mErrorClickListener;
    protected OnClickListener mEmptyClickListener;

    protected long mTimeLoadingStart;
    protected long mTimeLoadingTotal;

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

        mLoadingWithContent = a.getBoolean(R.styleable.StateSwitchLayout_loadingWithCont, false);
        mErrorWithContent = a.getBoolean(R.styleable.StateSwitchLayout_errorWithCont, false);
        mEmptyWithContent = a.getBoolean(R.styleable.StateSwitchLayout_emptyWithCont, false);

        mTimeLoadingTotal = a.getInteger(R.styleable.StateSwitchLayout_loadingTime, LOADING_TIME);

        a.recycle();

        mInflater = LayoutInflater.from(getContext());

        mErrorClickId = INVALID_ID;
        mEmptyClickId = INVALID_ID;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 1) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        mState = STATE_INITIAL;
        getSucceedView();
    }

    /**
     * @deprecated pls use {isXxxxState()}
     */
    public int getState() {
        return mState;
    }

    public boolean isInitialState() {
        return mState == STATE_INITIAL;
    }

    public boolean isLoadingState() {
        return mState == STATE_LOADING;
    }

    public boolean isSucceedState() {
        return mState == STATE_SUCCEED;
    }

    public boolean isErrorState() {
        return mState == STATE_ERROR;
    }
    public boolean isEmptyState() {
        return mState == STATE_EMPTY;
    }

    public void setErrorClickListener(OnClickListener errorClickListener) {
        mErrorClickListener = errorClickListener;
    }

    public void setEmtpyClickListener(OnClickListener emptyClickListener) {
        mEmptyClickListener = emptyClickListener;
    }

    public void setErrorClickListener(int errorClickResId, OnClickListener errorClickListener) {
        mErrorClickId = errorClickResId;
        mErrorClickListener = errorClickListener;
    }

    public void setEmtpyClickListener(int emptyClickResId, OnClickListener emptyClickListener) {
        mEmptyClickId = emptyClickResId;
        mEmptyClickListener = emptyClickListener;
    }

    private void setStartTime() {
        mTimeLoadingStart = System.currentTimeMillis();
    }

    private long getDelayTime() {
        long timeLoadingEnd = System.currentTimeMillis();
        return mTimeLoadingTotal - (timeLoadingEnd - mTimeLoadingStart);
    }

    public void switchToLoading() {
        showLoadingView();
    }

    /**
     * @deprecated pls use {@link #switchToLoading()}
     */
    public void showLoadingView() {
        if (mLoadingWithContent) {
            hideAllChild(mLoadingView, mSucceedView);
        } else {
            hideAllChild(mLoadingView);
        }
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
        if (mErrorWithContent) {
            hideAllChild(mErrorView, mSucceedView);
        } else {
            hideAllChild(mErrorView);
        }
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
        if (mEmptyWithContent) {
            hideAllChild(mEmptyView, mSucceedView);
        } else {
            hideAllChild(mEmptyView);
        }
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

    private void hideAllChild(View ...exceptViews) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            boolean needHide = true;
            for (View exceptView : exceptViews) {
                if (child == exceptView) {
                    needHide = false;
                    break;
                }
            }
            child.setVisibility(needHide ? INVISIBLE : VISIBLE);
        }
    }

    /**
     * @param layoutId
     * @param addToLast true，则在内容界面之上；false，则在内容界面之下
     */
    private View createStateView(int layoutId, boolean addToLast) {
        if (layoutId == INVALID_ID) {
            throw new IllegalArgumentException("createStateView with a invalid layoutId");
        }
        View view = mInflater.inflate(layoutId, this, false);
        addView(view, addToLast ? -1 : 0);
        return view;
    }

    private View getLoadingView() {
        if (mLoadingView == null) {
            // 显示加载界面的同时显示内容界面，需要加载界面在内容界面之上，此处上下描述的是Z轴
            mLoadingView = createStateView(mLayoutLoading, mLoadingWithContent);
            mLoadingView.setClickable(true);
        }
        return mLoadingView;
    }

    private View getErrorView() {
        if (mErrorView == null) {
            // 显示错误界面的同时显示内容界面，需要错误界面在内容界面之下，此处上下描述的是Z轴
            mErrorView = createStateView(mLayoutError, !mErrorWithContent);
            mLoadingView.setClickable(true);
        }
        if (mErrorClickListener != null) {
            View errorClickView = mErrorClickId == INVALID_ID ?
                    mErrorView : mErrorView.findViewById(mErrorClickId);
            errorClickView.setOnClickListener(mErrorClickListener);
        }
        return mErrorView;
    }

    private View getEmptyView() {
        if (mEmptyView == null) {
            // 显示空白界面的同时显示内容界面，需要空白界面在内容界面之下，此处上下描述的是Z轴
            mEmptyView = createStateView(mLayoutEmpty, !mEmptyWithContent);
            mLoadingView.setClickable(true);
        }
        if (mEmptyClickListener != null) {
            View emptyClickView = mEmptyClickId == INVALID_ID ?
                    mEmptyView : mEmptyView.findViewById(mEmptyClickId);
            emptyClickView.setOnClickListener(mEmptyClickListener);
        }
        return mEmptyView;
    }

    private View getSucceedView() {
        if (mSucceedView == null) {
            mSucceedView = getChildAt(0);
        }
        return mSucceedView;
    }
}
