package com.qun.lib.pullrefreshlistview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class PullRefreshListView extends ListView {

    private long mLastRefreshTime;
    private Context mContext;
    private int mHeaderViewHeight;
    private View mHeaderView;
    private RefreshState mState = RefreshState.IDLE;
    private OnFetchDataListener mOnFetchDataListener;
    private TextView mTvState;
    private ImageView mIvState;
    private ProgressBar mPb;
    private TextView mTvLastRefreshTime;
    private View mFooterView;
    private TextView mTvLoadMoreState;
    private boolean mNoMoreData;
    private boolean mLoadingMoreData;
    private boolean mLoadMoreSwitch = true;
    private boolean mPullRefreshSwitch = true;
    private boolean intercept;
    private int mLastY;
    private RotateAnimation mRefreshAnimation;
    private RotateAnimation mIDLEAnimation;

    private enum RefreshState {
        LOADING, READY, IDLE
    }

    public PullRefreshListView(Context context) {
        this(context, null);
    }

    public PullRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PullRefreshListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initHeaderView();
        initFooterView();
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (getLastVisiblePosition() == getCount() - 1 && scrollState == SCROLL_STATE_IDLE
                        && !mLoadingMoreData && mLoadMoreSwitch) {
                    if (!mNoMoreData) {
                        mFooterView.setPadding(0, 0, 0, 0);
                        setSelection(getCount() - 1);
                        mTvLoadMoreState.setText(R.string.load_more_data);
                        if (mOnFetchDataListener != null) {
                            mOnFetchDataListener.onLoadMore();
                        }
                    }

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(parent, view, position - getHeaderViewsCount(), id);
                }
            }
        });
        setSelector(android.R.color.transparent);
        initAnimations();
    }

    private void initAnimations() {
        mRefreshAnimation = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mRefreshAnimation.setDuration(200);
        mRefreshAnimation.setFillAfter(true);

        mIDLEAnimation = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mIDLEAnimation.setDuration(200);
        mIDLEAnimation.setFillAfter(true);
    }

    public void setOnFetchDataListener(OnFetchDataListener onFetchDataListener) {
        mOnFetchDataListener = onFetchDataListener;
    }

    public void setLoadMoreSwitch(boolean loadMoreSwitch) {
        mLoadMoreSwitch = loadMoreSwitch;
    }

    public void setPullRefreshSwitch(boolean pullRefreshSwitch) {
        mPullRefreshSwitch = pullRefreshSwitch;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private OnItemClickListener mOnItemClickListener;

    private void initHeaderView() {
        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.listview_header, null, false);
        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
        addHeaderView(mHeaderView, null, true);
        mTvState = mHeaderView.findViewById(R.id.tv_state);
        mTvState.setText(R.string.pull_to_refresh);
        mIvState = findViewById(R.id.iv_state);
        mPb = mHeaderView.findViewById(R.id.pb);
        mTvLastRefreshTime = findViewById(R.id.tv_last_refresh_time);
    }

    private void initFooterView() {
        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.listview_footer, null, false);
        mFooterView.measure(0, 0);
        mTvLoadMoreState = mFooterView.findViewById(R.id.tv_load_more_state);
        mTvLoadMoreState.setText(R.string.load_more_data);
        addFooterView(mFooterView, null, false);
//        mFooterView.setVisibility(View.GONE);
        mFooterView.setPadding(0, -mFooterView.getMeasuredHeight(), 0, 0);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mState == RefreshState.LOADING || mLoadingMoreData || !mPullRefreshSwitch) {
            return super.onTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int curY = (int) ev.getY();
                int dy = curY - mLastY;
                mLastY = curY;
                if (getFirstVisiblePosition() == 0) {
                    if (mHeaderView.getPaddingTop() <= -mHeaderViewHeight && dy < 0) {
                        return super.onTouchEvent(ev);
                    } else {
                        mHeaderView.setPadding(0, mHeaderView.getPaddingTop() + dy / 2, 0, 0);
                        if (mHeaderView.getPaddingTop() >= 0) {
                            mState = RefreshState.READY;
                            mTvState.setText(R.string.release_to_refresh);
                            mIvState.startAnimation(mRefreshAnimation);
                        } else {
                            mState = RefreshState.IDLE;
                            mTvState.setText(R.string.pull_to_refresh);
                            mIvState.startAnimation(mIDLEAnimation);
                        }
                        intercept = true;
                        return true;
                    }
                }
                break;


            case MotionEvent.ACTION_UP:
                if (intercept) {
                    confirmFinalState();
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    private void confirmFinalState() {
        intercept = false;
        switch (mState) {
            case IDLE:
                foldHeaderView(mHeaderView.getPaddingTop(), -mHeaderViewHeight);
                break;
            case READY:
                foldHeaderView(mHeaderView.getPaddingTop(), 0);
                mState = RefreshState.LOADING;
                mTvState.setText(R.string.refreshing);
                mIvState.clearAnimation();
                mIvState.setVisibility(View.GONE);
                mPb.setVisibility(View.VISIBLE);
                if (mOnFetchDataListener != null) {
                    mOnFetchDataListener.onRefresh();
                }
                break;
        }
    }

    private void foldHeaderView(int fromY, int toY) {
        ValueAnimator animator = ValueAnimator.ofInt(fromY, toY);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mHeaderView.setPadding(0, (int) animation.getAnimatedValue(), 0, 0);
            }
        });
        animator.start();
    }

    public void onRefreshCompleted() {
        foldHeaderView(0, -mHeaderViewHeight);
        mTvState.setText(R.string.refresh_finish);
        mState = RefreshState.IDLE;
        mPb.setVisibility(View.GONE);
        mIvState.setVisibility(View.VISIBLE);
        mNoMoreData = false;
        mLastRefreshTime = System.currentTimeMillis();
        if (mLastRefreshTime == 0) {
            mTvLastRefreshTime.setText(String.format(mContext.getString(R.string.last_refresh_time), "unKnow"));
        } else {
            mTvLastRefreshTime.setText(String.format(mContext.getString(R.string.last_refresh_time), formatTime(mLastRefreshTime)));
        }
    }

    public void onLoadMoreCompleted(boolean noMoreData) {
        mNoMoreData = noMoreData;
        mLoadingMoreData = false;
        if (mNoMoreData) {
            mTvLoadMoreState.setText(R.string.no_more_data);
            mFooterView.setPadding(0, 0, 0, 0);
        } else {
            mFooterView.setPadding(0, -mFooterView.getMeasuredHeight(), 0, 0);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    public interface OnFetchDataListener {
        void onRefresh();

        void onLoadMore();
    }

    private String formatTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return sdf.format(calendar.getTime());
    }
}
