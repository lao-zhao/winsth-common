package com.winsth.android.libs.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

import com.winsth.android.libs.R;
import com.winsth.android.libs.adapters.LeftSlideRemoveAdapter;
import com.winsth.android.libs.interfs.OnItemRemoveListener;

/**
 * 作者 Aaron Zhao
 * 时间 2020/9/18 15:05
 * 名称 LeftSlideRemoveListView.java
 * 描述
 */
public class LeftSlideRemoveListView extends ListView {
    private final static int SNAP_VELOCITY = 600;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private int mTouchSlop;
    private boolean mIsSlide = false;
    private int mDelta = 0;
    private int mDownX;
    private int mDownY;
    private int mMaxDistence;
    private int mSlidePosition = INVALID_POSITION;
    private OnItemRemoveListener adapterListener;

    private OnItemRemoveListener mRemoveListener = new OnItemRemoveListener() {
        @Override
        public void onItemRemove(int position) {
            if (adapterListener != null) {
                adapterListener.onItemRemove(position);
            }
            clear();
            mSlidePosition = INVALID_POSITION;
        }
    };
    private LeftSlideRemoveAdapter mRemoveAdapter;

    private View mCurrentContentView, mCurrentRemoveView;

    public LeftSlideRemoveListView(Context context) {
        this(context, null);
    }

    public LeftSlideRemoveListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        mMaxDistence = context.getResources().getDimensionPixelSize(R.dimen.margin_60);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                addVelocityTracker(ev);

                if (!mScroller.isFinished()) {
                    return super.dispatchTouchEvent(ev);
                }
                // 起始位置，当前position
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                int position = pointToPosition(mDownX, mDownY);
                if (position == mSlidePosition)
                    break;

                mSlidePosition = position;

                if (mSlidePosition == INVALID_POSITION) {
                    return super.dispatchTouchEvent(ev);
                }

                // 恢复状态
                clear();

                // 获取当前界面
                View childView = getChildAt(mSlidePosition - getFirstVisiblePosition());
                mCurrentContentView = childView.findViewById(R.id.view_content);
                mCurrentRemoveView = childView.findViewById(R.id.tv_remove);

                break;
            case MotionEvent.ACTION_MOVE:
                if (mCurrentContentView == null)
                    break;

                if (Math.abs(getScrollVelocity()) > SNAP_VELOCITY || (Math.abs(ev.getX() - mDownX) > mTouchSlop && Math.abs(ev.getY() - mDownY) < mTouchSlop)) {
                    // 开始滑动
                    mIsSlide = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentContentView == null && mIsSlide)
                    break;

                // 如果左滑小于4/5，按钮不显示
                if (mDelta < mMaxDistence * 4 / 5) {
                    mCurrentRemoveView.setVisibility(View.GONE);
                    scrollRight();
                } else if (mDelta < mMaxDistence) {
                    scrollLeft();
                }
                recycleVelocityTracker();
                mIsSlide = false;
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mIsSlide && mSlidePosition != INVALID_POSITION) {
            final int action = ev.getAction();
            int x = (int) ev.getX();
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    addVelocityTracker(ev);

                    int deltaX = mDownX - x;
                    mDownX = x;

                    mDelta += deltaX;

                    if (mDelta < 0) {
                        mCurrentContentView.scrollTo(0, 0);

                        mDelta = 0;
                        mCurrentRemoveView.setVisibility(View.GONE);
                    } else if (mDelta >= mMaxDistence) {
                        mDelta = mMaxDistence;
                        mCurrentContentView.scrollTo(mMaxDistence, 0);
                        mCurrentRemoveView.setVisibility(View.VISIBLE);
                        mCurrentRemoveView.setTranslationX(0);
                    } else {
                        mCurrentContentView.scrollBy(deltaX, 0);
                        mCurrentRemoveView.setVisibility(View.VISIBLE);
                        mCurrentRemoveView.setTranslationX(mMaxDistence - mDelta);
                    }
                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    // 右滑
    private void scrollRight() {
        final int delta = mDelta;
        mScroller.startScroll(delta, 0, -delta, 0, Math.abs(delta));
        mDelta = 0;
        postInvalidate();
    }

    // 左滑
    private void scrollLeft() {
        final int delta = mMaxDistence - mDelta;
        mScroller.startScroll(mDelta, 0, delta, 0, Math.abs(delta));
        mDelta = mMaxDistence;
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mCurrentContentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            mCurrentRemoveView.setTranslationX(mMaxDistence - mScroller.getCurrX());

            postInvalidate();

            if (mScroller.isFinished()) {
                mCurrentContentView.scrollTo(mDelta, 0);
                mCurrentRemoveView.setTranslationX(0);
            }
        }
    }

    private void addVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(event);
    }

    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return velocity;
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void clear() {
        if (mCurrentContentView != null) {
            mDelta = 0;
            mCurrentContentView.scrollTo(0, 0);
            mCurrentContentView = null;

            mCurrentRemoveView.setVisibility(View.GONE);
            mCurrentRemoveView = null;
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof LeftSlideRemoveAdapter) {
            super.setAdapter(adapter);

            mRemoveAdapter = (LeftSlideRemoveAdapter) adapter;
            mRemoveAdapter.mListener = mRemoveListener;
        } else {
            throw new IllegalArgumentException("Must be LeftSlideRemoveAdapter");
        }
    }

    public void setOnItemRemoveListener(OnItemRemoveListener listener) {
        adapterListener = listener;
    }

}
