package org.huxizhijian.simplerecipebook.ui.search;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import org.huxizhijian.sdk.util.LogUtils;
import org.huxizhijian.sdk.util.adapter.recycleradapter.CommonAdapter;
import org.huxizhijian.sdk.util.adapter.recycleradapter.base.ViewHolder;
import org.huxizhijian.sdk.util.adapter.recycleradapter.wrapper.EmptyWrapper;
import org.huxizhijian.simplerecipebook.R;
import org.huxizhijian.simplerecipebook.base.BaseActivity;
import org.huxizhijian.simplerecipebook.bean.search.RecipeBean;
import org.huxizhijian.simplerecipebook.bean.search.SearchBean;
import org.huxizhijian.simplerecipebook.greendao.db.DBHelper;
import org.huxizhijian.simplerecipebook.greendao.entity.SearchHistory;
import org.huxizhijian.simplerecipebook.greendao.gen.SearchHistoryDao;
import org.huxizhijian.simplerecipebook.ui.detail.RecipeDetailActivity;
import org.huxizhijian.simplerecipebook.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchActivity extends BaseActivity implements SearchContract.View {

    /**
     * UI
     */
    @BindView(R.id.search_results_list)
    LRecyclerView mSearchResultsList;
    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.parent_view)
    RelativeLayout mRelativeLayout;

    /**
     * 上次搜索key
     */
    private String mLastQuery;
    //搜索历史数据库查询辅助
    private SearchHistoryDao mSearchHistoryDao;

    /**
     * DATA
     */
    private List<RecipeBean> mSearchBeanList;
    private int mPage = 0;
    private static final int PAGE_SIZE = 40;
    /**
     * 获取模式
     */
    private int mMode;
    private static final int MODE_NEXT_PAGE = 0x01; //加载下一页
    private static final int MODE_NEW_SEARCH = 0x02; //加载新搜索
    // presenter
    private SearchPresenter mSearchPresenter;

    /**
     * adapter
     */
    private LRecyclerViewAdapter mAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchPresenter.clear();
    }

    protected int getContentView() {
        return R.layout.activity_sliding_search;
    }

    protected void initWidget() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.parent_view);
        setupEnterAnimations();
        if (mSearchHistoryDao == null) {
            mSearchHistoryDao = DBHelper.getDaoSession(SearchActivity.this)
                    .getSearchHistoryDao();
        }
        setupSearchView();
    }

    private void setupSearchView() {
        mSearchView.setOnHomeActionClickListener(
                new FloatingSearchView.OnHomeActionClickListener() {
                    @Override
                    public void onHomeClicked() {
                        finishAfterTransition();
                    }
                });
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {
                    mSearchView.showProgress();

                    //提取相关的搜索历史，按照保存时间排列
                    List<SearchHistory> results = mSearchHistoryDao.queryBuilder()
                            .where(SearchHistoryDao.Properties.SearchKey.like(newQuery))
                            .orderDesc(SearchHistoryDao.Properties.SaveTime)
                            .limit(5)
                            .build().list();

                    mSearchView.swapSuggestions(results);

                    mSearchView.hideProgress();
                }
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                mLastQuery = searchSuggestion.getBody();
                mSearchView.setSearchText(searchSuggestion.getBody());
                doSearch(searchSuggestion.getBody());
            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
                doSearch(query);
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                //根据保存时间提取最新保存的记录
                List<SearchHistory> results = mSearchHistoryDao.queryBuilder()
                        .limit(5)
                        .build().list();

                //show suggestions when search bar gains focus (typically history suggestions)
                mSearchView.swapSuggestions(results);
            }

            @Override
            public void onFocusCleared() {

            }
        });

        mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                mSearchResultsList.setTranslationY(newHeight);
            }
        });

        /*
         * 当单击clear按钮时
         */
        mSearchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {
                LogUtils.i("clear click");
            }
        });

    }

    /**
     * 进行搜索操作
     *
     * @param searchKey
     */
    private void doSearch(String searchKey) {
        mProgressBar.setVisibility(View.VISIBLE);
        mSearchResultsList.setVisibility(View.GONE);
        mMode = MODE_NEW_SEARCH;
        mPage = 0;
        mSearchPresenter.getResult(searchKey, mPage, PAGE_SIZE);
    }

    /**
     * 保存searchkey
     *
     * @param searchKey
     */
    private void saveSearchKey(String searchKey) {
        SearchHistory searchHistory;
        List<SearchHistory> results = mSearchHistoryDao.queryBuilder()
                .where(SearchHistoryDao.Properties.SearchKey.eq(searchKey))
                .build().list();
        if (results != null && results.size() != 0) {
            searchHistory = results.get(0);
            searchHistory.setSaveTime(System.currentTimeMillis());
            mSearchHistoryDao.update(searchHistory);
        } else {
            searchHistory = new SearchHistory();
            searchHistory.setSearchKey(searchKey);
            searchHistory.setSaveTime(System.currentTimeMillis());
            mSearchHistoryDao.insert(searchHistory);
        }
    }

    public void initData() {
        mSearchPresenter = new SearchPresenter(this);
    }

    private void initRecyclerView() {
        if (mSearchBeanList == null) mSearchBeanList = new ArrayList<>();
        CommonAdapter<RecipeBean> adapter = new CommonAdapter<RecipeBean>(this, R.layout.item_recipe_list,
                mSearchBeanList) {
            @Override
            protected void convert(ViewHolder holder, final RecipeBean recipeBean, int position) {
                final ImageView imageView = holder.getView(R.id.image_view);
                final String imgUrl;
                if (recipeBean.getRecipe() != null && recipeBean.getRecipe().getImg() != null) {
                    imgUrl = recipeBean.getRecipe().getImg();
                } else {
                    imgUrl = recipeBean.getThumbnail();
                }
                Glide.with(SearchActivity.this)
                        .load(imgUrl)
                        .error(R.mipmap.white_place_hold)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .fitCenter()
                        .into(imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到详情页
                        String title;
                        if (recipeBean.getName() == null &&
                                TextUtils.isEmpty(recipeBean.getName().trim())) {
                            title = recipeBean.getRecipe().getTitle();
                        } else {
                            title = recipeBean.getName();
                        }
                        Intent intent = new Intent(SearchActivity.this, RecipeDetailActivity.class);
                        Resources resources = getResources();
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                SearchActivity.this,
                                new Pair(imageView, resources.getString(R.string.transition_shot)),
                                new Pair(imageView, resources.getString(R.string.transition_shot_background)));
                        intent.putExtra("id", recipeBean.getMenuId());
                        intent.putExtra("imgUrl", imgUrl);
                        intent.putExtra("title", title);
                        ActivityCompat.startActivity(SearchActivity.this, intent, options.toBundle());
                    }
                });
            }
        };
        if (mAdapter == null) {
            if (mSearchResultsList == null) {
                mSearchResultsList = (LRecyclerView) findViewById(R.id.search_results_list);
            }
            mSearchResultsList.setItemAnimator(new DefaultItemAnimator());
            mSearchResultsList.setLayoutManager(new GridLayoutManager(this, 2));
            mSearchResultsList.setPullRefreshEnabled(false);
            mSearchResultsList.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    mMode = MODE_NEXT_PAGE;
                    mPage++;
                    mSearchPresenter.getResult(mLastQuery, mPage, PAGE_SIZE);
                }
            });
        }
        EmptyWrapper emptyWrapper = new EmptyWrapper(adapter);
        emptyWrapper.setEmptyView(R.layout.item_list_empty);
        mAdapter = new LRecyclerViewAdapter(emptyWrapper);
        if (mSearchResultsList == null) {
            mSearchResultsList = (LRecyclerView) findViewById(R.id.search_results_list);
        }
        mSearchResultsList.setAdapter(mAdapter);
    }

    private void setupEnterAnimations() {
        Fade enterTransition = new Fade(); //淡入淡出动画
        getWindow().setEnterTransition(enterTransition);
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_short));
        enterTransition.setStartDelay(getResources().getInteger(R.integer.anim_duration_short));
        enterTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                //移除监听
                transition.removeListener(this);
                //Reveal动画
                animateRevealShow(mRelativeLayout);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }
        });
    }

    private void animateRevealShow(View viewRoot) {
        //获取控件中心点坐标
        //int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cx = viewRoot.getLeft() + viewRoot.getRight();
        //int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        int cy = 0;
        //获取宽高中的最大值
        int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());
        /*
        * 快速实现圆形缩放动画 第一个参数是你要进行圆形缩放的view 第二个参数和第三个参数是开始缩放点的x y坐标
        * 第四第五个参数是开始的半径和结束的半径
        */
        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
        viewRoot.setVisibility(View.VISIBLE); //设为可见
        anim.setDuration(getResources().getInteger(R.integer.anim_duration_short));
        anim.setInterpolator(new AccelerateInterpolator()); //加速度插值器
        anim.start();
    }

    @Override
    public void onSuccess(SearchBean searchBean) {
        LogUtils.i(searchBean);
        if (Integer.valueOf(searchBean.getRetCode()) == Constant.API_SUCCESS) {
            //请求成功
            //保存请求数据
            saveSearchKey(mLastQuery);
            if (mMode == MODE_NEXT_PAGE) {
                mSearchBeanList.addAll(searchBean.getResult().getList());
                mAdapter.notifyDataSetChanged();
            } else if (mMode == MODE_NEW_SEARCH) {
                mSearchBeanList = searchBean.getResult().getList();
                initRecyclerView();
                mProgressBar.setVisibility(View.GONE);
                mSearchResultsList.setVisibility(View.VISIBLE);
            }
        } else {
            saveSearchKey(mLastQuery);
            mSearchResultsList = null;
            initRecyclerView();
            mProgressBar.setVisibility(View.GONE);
            mSearchResultsList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        throwable.printStackTrace();
        mSearchBeanList = new ArrayList<>();
        initRecyclerView();
    }

}
