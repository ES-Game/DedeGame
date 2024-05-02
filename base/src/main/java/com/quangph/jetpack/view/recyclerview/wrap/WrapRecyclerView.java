package com.quangph.jetpack.view.recyclerview.wrap;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.quangph.base.R;
import com.quangph.base.view.recyclerview.adapter.BaseRclvAdapter;


/**
 * Created by Pham Hai Quang on 10/17/2019.
 */

public class WrapRecyclerView extends FrameLayout {

    public interface IOnLoadMoreDataListener {
        void onLoadData(int lastPageIndex, int lastPageSize, int totalCount);
    }

    private RecyclerView mRclvContent;
    private ProgressBar mPrgbLoading;
    private FrameLayout mRlNoItem;
    private SwipeRefreshLayout mSwpRefresh;

    private int mNoItemRes = -1;
    private boolean isLoading = false;
    private BaseRclvAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private IOnLoadMoreDataListener mLoadDataListener;
    private int mMaxHeight = -1;

    public WrapRecyclerView(Context context) {
        super(context);
    }

    public WrapRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        compound();
    }

    public WrapRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        compound();
    }

    public WrapRecyclerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
        compound();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRclvContent = findViewById(R.id.rclv_wrap_recyclerview_layout_content);
        mPrgbLoading = findViewById(R.id.prbr_wrap_recyclerview_layout_progress);
        mSwpRefresh = findViewById(R.id.swp_wrap_recyclerview_layout);

        mSwpRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mLoadDataListener == null) {
                    return;
                }
                mLoadDataListener.onLoadData(-1, 0, 0);
            }
        });

        if (mLayoutManager == null) {
            mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }
        mRclvContent.setLayoutManager(mLayoutManager);
        mRclvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!(mAdapter instanceof ILoadmoreAdapter)
                        || !((ILoadmoreAdapter) mAdapter).isLoadMoreSupport()
                        || !((ILoadmoreAdapter) mAdapter).isShowingLoadMore()) {
                    return;
                }

                if (mLoadDataListener == null) {
                    return;
                }

                if (dy > 0 && !isLoading) {
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                    if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                        isLoading = true;
                        mLoadDataListener.onLoadData(((ILoadmoreAdapter) mAdapter).getLatestPageIndex(),
                                ((ILoadmoreAdapter) mAdapter).getLatestPageSize(),
                                ((ILoadmoreAdapter) mAdapter).getTotalCount());
                    }
                }
            }
        });

        mRclvContent.setVisibility(INVISIBLE);

        mRlNoItem = findViewById(R.id.fl_wrap_recyclerview_layout_no_content_container);
        if (mNoItemRes != -1) {
            LayoutInflater.from(getContext()).inflate(mNoItemRes, mRlNoItem, true);
        }

        showLoading(true);
        showNoItem(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxHeight > 0){
            int hSize = MeasureSpec.getSize(heightMeasureSpec);
            int hMode = MeasureSpec.getMode(heightMeasureSpec);

            switch (hMode){
                case MeasureSpec.AT_MOST:
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(hSize, mMaxHeight), MeasureSpec.AT_MOST);
                    break;
                case MeasureSpec.UNSPECIFIED:
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
                    break;
                case MeasureSpec.EXACTLY:
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(hSize, mMaxHeight), MeasureSpec.EXACTLY);
                    break;
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        CoordinatorLayout parent = findParent();
        if (parent == null) return;

        int heightMeasured = MeasureSpec.getSize(heightMeasureSpec);
        int totalRange = findAppbarLayoutSize(parent);
        mRlNoItem.measure(widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(heightMeasured - totalRange, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        CoordinatorLayout parent = findParent();
        if (parent == null) return;

        int totalRange = findAppbarLayoutSize(parent);
        int toolbarHeight = getToolbarHeight();

        LayoutParams prgbParams = (LayoutParams) mPrgbLoading.getLayoutParams();
        if (isAppbarExpandFully(parent)) {
            prgbParams.topMargin = (totalRange + toolbarHeight) / 2 * (-1);
        } else {
            prgbParams.topMargin = toolbarHeight / 2 * (-1);
        }
        mPrgbLoading.setLayoutParams(prgbParams);
    }

    public void setLoadDataListener(IOnLoadMoreDataListener listener) {
        mLoadDataListener = listener;
    }

    public void setLayoutManager(LinearLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
        mRclvContent.setLayoutManager(mLayoutManager);
    }

    public void setAdapter(BaseRclvAdapter adapter) {
        if (adapter instanceof ILoadmoreAdapter) {
            mAdapter = adapter;
            ((ILoadmoreAdapter) mAdapter).setOnAddItemListener(new IOnAddItemListener() {
                @Override
                public void onAdded(int startIndex, int count, int totalCount) {
                    mSwpRefresh.setRefreshing(false);
                    isLoading = false;
                    showLoading(false);
                    if (totalCount == 0) {
                        showNoItem(true);
                    } else {
                        showNoItem(false);
                        if (mLoadDataListener != null
                                && ((KLoadMoreAdapter) adapter).isLoadMoreSupport()
                                && ((KLoadMoreAdapter) adapter).isShowingLoadMore()) {
                            mRclvContent.post(() -> {
                                if (!mRclvContent.canScrollVertically(1)) { //1 for down
                                    isLoading = true;
                                    mLoadDataListener.onLoadData(
                                            ((ILoadmoreAdapter) mAdapter).getLatestPageIndex(),
                                            ((ILoadmoreAdapter) mAdapter).getLatestPageSize(),
                                            ((ILoadmoreAdapter) mAdapter).getTotalCount());
                                }
                            });
                        }
                    }
                }
            });
            mRclvContent.setAdapter(adapter);
        } else {
            throw new IllegalArgumentException("Adapter must be ILoadmoreAdapter");
        }
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper){
        itemTouchHelper.attachToRecyclerView(mRclvContent);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        mRclvContent.addItemDecoration(decor);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration decor) {
        mRclvContent.removeItemDecoration(decor);
    }

    public void removeAllItemDecoration() {
        while (mRclvContent.getItemDecorationCount() > 0) {
            mRclvContent.removeItemDecorationAt(0);
        }
    }

    public void scrollToPosition(int position) {
        mRclvContent.scrollToPosition(position);
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        mRclvContent.addOnScrollListener(listener);
    }

    public void showLoading(boolean isShown) {
        mPrgbLoading.setVisibility(isShown ? VISIBLE : INVISIBLE);
    }

    public void showNoItem(boolean isShown) {
        mRlNoItem.setVisibility(isShown ? VISIBLE : INVISIBLE);
        mRclvContent.setVisibility(isShown ? INVISIBLE : VISIBLE);
        if (isShown) {
            mPrgbLoading.setVisibility(INVISIBLE);
        }
    }

    public void setMaxHeight(int maxHeight) {
        mMaxHeight = maxHeight;
    }

    private void compound() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_wrap_recyclerview, this, true);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.WrapRecyclerView);
        mNoItemRes = typedArray.getResourceId(R.styleable.WrapRecyclerView_wrap_rclv_no_item_res, -1);
        typedArray.recycle();
    }

    private CoordinatorLayout findParent() {
        View parent = this;
        CoordinatorLayout result = null;
        while (parent != null) {
            ViewParent v = parent.getParent();
            if (v instanceof CoordinatorLayout) {
                result = (CoordinatorLayout) v;
                break;
            } else {
                if (v instanceof View) {
                    parent = (View) v;
                } else {
                    parent = null;
                }
            }
        }
        return result;
    }

    private int findAppbarLayoutSize(CoordinatorLayout parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof AppBarLayout) {
                return ((AppBarLayout) child).getTotalScrollRange();
            }
        }
        return 0;
    }

    private int getToolbarHeight() {
        TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int h = (int) styledAttributes.getDimension(0, 0f);
        styledAttributes.recycle();
        return h;
    }

    private boolean isAppbarExpandFully(CoordinatorLayout parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof AppBarLayout) {
                return (child.getHeight() - child.getBottom()) == 0;
            }
        }
        return false;
    }
}
