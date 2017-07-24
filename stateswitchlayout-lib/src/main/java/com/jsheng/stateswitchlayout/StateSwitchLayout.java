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
    private static final int INVALID = -1;

    // 初始的状态
    public static final int STATE_INITAIL= 1;
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

        mLayoutLoading = a.getResourceId(R.styleable.StateSwitchLayout_layoutLoading, INVALID);
        mLayoutError= a.getResourceId(R.styleable.StateSwitchLayout_layoutError, INVALID);
        mLayoutEmpty = a.getResourceId(R.styleable.StateSwitchLayout_layoutEmpty, INVALID);

        a.recycle();

        mInflater = LayoutInflater.from(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mState = STATE_INITAIL;
        mSucceedView = getChildAt(0);
    }

    public int getState() {
        return mState;
    }

    public void setErrorClickListener(OnClickListener errorClickListener) {
        mErrorClickListener = errorClickListener;
    }

    public void switchToLoading() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                showLoadingView();
            }
        }, 600);
    }

    /**
     * @deprecated
     * pls use {@link #switchToLoading()}
     */
    public void showLoadingView() {
        goneAllChild();
        getLoadingView().setVisibility(VISIBLE);
        mState = STATE_LOADING;
    }

    public void switchToError() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                showErrorView();
            }
        }, 600);
    }

    /**
     * @deprecated
     * pls use {@link #switchToError()}
     */
    public void showErrorView() {
        goneAllChild();
        getErrorView().setVisibility(VISIBLE);
        mState = STATE_ERROR;
    }

    public void switchToEmpty() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                showEmptyView();
            }
        }, 600);
    }

    /**
     * @deprecated
     * pls use {@link #switchToEmpty()}
     */
    public void showEmptyView() {
        goneAllChild();
        getEmptyView().setVisibility(VISIBLE);
        mState = STATE_EMPTY;
    }

    public void switchToSucceed() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                showSucceedView();
            }
        }, 600);
    }

    /**
     * @deprecated
     * pls use {@link #switchToSucceed()}
     */
    public void showSucceedView() {
        goneAllChild();
        getSucceedView().setVisibility(VISIBLE);
        mState = STATE_SUCCEED;
    }

    private void goneAllChild() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setVisibility(GONE);
        }
    }

    private View createStateView(int layoutId) {
        if (layoutId == INVALID) {
            throw new IllegalArgumentException("createStateView with a invalid layoutId");
        }
        View view = mInflater.inflate(layoutId, this, false);
        addView(view, generateDefaultLayoutParams());
        return view;
    }

    private View getLoadingView() {
        if (mLoadingView == null) {
            mLoadingView = createStateView(mLayoutLoading);
        }
        return mLoadingView;
    }

    private View getErrorView() {
        if (mErrorView == null) {
            mErrorView = createStateView(mLayoutError);
        }
        if (mErrorClickListener != null) {
            mErrorView.setOnClickListener(mErrorClickListener);
        }
        return mErrorView;
    }

    private View getEmptyView() {
        if (mEmptyView == null) {
            mEmptyView = createStateView(mLayoutEmpty);
        }
        return mEmptyView;
    }

    private View getSucceedView() {
        if (mSucceedView == null) {
            throw new IllegalArgumentException("StateSwitchLayout need a child view at least");
        }
        return mSucceedView;
    }
}
