package org.huxizhijian.simplerecipebook.ui.showrecipelist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import org.huxizhijian.sdk.util.LogUtils;
import org.huxizhijian.sdk.util.NetworkUtils;
import org.huxizhijian.sdk.util.adapter.recycleradapter.CommonAdapter;
import org.huxizhijian.sdk.util.adapter.recycleradapter.base.ViewHolder;
import org.huxizhijian.simplerecipebook.R;
import org.huxizhijian.simplerecipebook.base.BaseActivity;
import org.huxizhijian.simplerecipebook.bean.search.RecipeBean;
import org.huxizhijian.simplerecipebook.bean.search.SearchBean;
import org.huxizhijian.simplerecipebook.ui.detail.RecipeDetailActivity;
import org.huxizhijian.simplerecipebook.util.Constant;

import java.util.List;

import butterknife.BindView;

import static org.huxizhijian.simplerecipebook.R.id.loading;

public class RecipeListActivity extends BaseActivity implements RecipeListContract.View {

    /**
     * UI
     */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    LRecyclerView mRecyclerView;
    @BindView(R.id.fab_jump)
    FloatingActionButton mFabJump;
    @BindView(loading)
    ProgressBar mLoading;

    //无网络提示视图
    private ImageView mNoConnection;
    //跳转界面视图
    private AlertDialog mAlertDialog;
    private View mJumpView;
    private TextView mJumpText;
    private EditText mJumpEdit;

    /**
     * presenter
     */
    private RecipeListPresenter mRecipeListPresenter;
    private String cid;

    /**
     * DATA
     */
    private List<RecipeBean> mRecipeList;
    private CommonAdapter<RecipeBean> mAdapter;

    //当前页数
    private int mPage;
    //是否还有下一页
    private boolean noMore = false;
    //服务器返回的所有item数
    private int mItemSize;
    private int mTotalPage;
    //每页加载40个数据
    public static final int PAGE_SIZE = 40;

    @Override
    protected int getContentView() {
        return R.layout.activity_recipe_list;
    }

    @Override
    protected void initWidget() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mFabJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemSize == 0) return;
                if (mAlertDialog == null || mJumpView == null) {
                    mJumpView = LayoutInflater.from(RecipeListActivity.this)
                            .inflate(R.layout.jump_edit_text, null, false);
                    mJumpText = (TextView) mJumpView.findViewById(R.id.text_view);
                    mJumpEdit = (EditText) mJumpView.findViewById(R.id.edit_view);
                    mTotalPage = mItemSize / PAGE_SIZE;
                    if (mTotalPage % PAGE_SIZE != 0) {
                        mTotalPage++;
                    }
                    mAlertDialog = new AlertDialog.Builder(RecipeListActivity.this)
                            .setView(mJumpView)
                            .setTitle("页面跳转")
                            .setPositiveButton("跳转", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAlertDialog.dismiss();
                                    if (TextUtils.isEmpty(mJumpEdit.getText().toString().trim())) {
                                        Toast.makeText(RecipeListActivity.this, "输入为空", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    int page = Integer.valueOf(mJumpEdit.getText().toString());
                                    if (page - 1 > mTotalPage || page - 1 < 0) {
                                        Toast.makeText(RecipeListActivity.this, "不在页数范围内", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    //重新加载recyclerview
                                    mPage = page - 1;
                                    mAdapter = null;
                                    mRecipeList = null;
                                    getRecipeList(mPage);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAlertDialog.dismiss();
                                }
                            }).create();
                }
                mJumpText.setText("当前第" + (mPage + 1) + "页，" + "全" + (mTotalPage + 1) + "页");
                mAlertDialog.show();
            }
        });
    }

    @Override
    public void initData() {
        mRecipeListPresenter = new RecipeListPresenter(this);
        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");
        //取到第一页，每页20个食谱
        mPage = 0;
        getRecipeList(mPage);
    }

    private void getRecipeList(int page) {
        if (mNoConnection != null) {
            mNoConnection.setVisibility(View.GONE);
        }
        mLoading.setVisibility(View.VISIBLE);
        mRecipeListPresenter.getResult(cid, page, PAGE_SIZE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(SearchBean searchBean) {
        LogUtils.i(searchBean);
        mLoading.setVisibility(View.GONE);
        //如果请求成功
        if (Integer.valueOf(searchBean.getRetCode()) == Constant.API_SUCCESS) {
            if (searchBean.getResult().getList() == null &&
                    searchBean.getResult().getList().size() == 0) {
                noMore = true;
                Toast.makeText(this, "没有更多了", Toast.LENGTH_SHORT).show();
            } else {
                if (mRecipeList == null) {
                    mRecipeList = searchBean.getResult().getList();
                    mItemSize = searchBean.getResult().getTotal();
                } else {
                    mRecipeList.addAll(searchBean.getResult().getList());
                }
                //更新或者初始化recyclerview
                updateRecycleView();
            }
        }
    }

    private void updateRecycleView() {
        if (mAdapter == null) {
            initRecyclerView();
        } else {
            mRecyclerView.refreshComplete(PAGE_SIZE);
        }
    }

    private void initRecyclerView() {
        mAdapter = new CommonAdapter<RecipeBean>(this, R.layout.item_recipe_list, mRecipeList) {
            @Override
            protected void convert(ViewHolder holder, final RecipeBean recipeBean, int position) {
                final ImageView imageView = holder.getView(R.id.image_view);
                final String imgUrl;
                if (recipeBean.getRecipe() != null && recipeBean.getRecipe().getImg() != null) {
                    imgUrl = recipeBean.getRecipe().getImg();
                } else {
                    imgUrl = recipeBean.getThumbnail();
                }
                Glide.with(RecipeListActivity.this)
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
                        Intent intent = new Intent(RecipeListActivity.this, RecipeDetailActivity.class);
                        Resources resources = getResources();
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                RecipeListActivity.this,
                                new Pair(imageView, resources.getString(R.string.transition_shot)),
                                new Pair(imageView, resources.getString(R.string.transition_shot_background)));
                        intent.putExtra("id", recipeBean.getMenuId());
                        intent.putExtra("imgUrl", imgUrl);
                        intent.putExtra("title", title);
                        ActivityCompat.startActivity(RecipeListActivity.this, intent, options.toBundle());
                    }
                });
            }
        };
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //禁止上拉刷新
        mRecyclerView.setPullRefreshEnabled(false);
        //设置加载下一页监听
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (noMore) {
                    Toast.makeText(RecipeListActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    mRecyclerView.setNoMore(noMore);
                    return;
                }
                mPage++;
                getRecipeList(mPage);
            }
        });
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onFailure(Throwable throwable) {
        throwable.printStackTrace();
        mLoading.setVisibility(View.GONE);
        if (!NetworkUtils.isConnected()) {
            //显示没有网络提示
            if (mNoConnection == null) {
                ViewStub viewStub = (ViewStub) findViewById(R.id.view_stub);
                mNoConnection = (ImageView) viewStub.inflate();
            }
            mNoConnection.setVisibility(View.VISIBLE);
            final AnimatedVectorDrawable avd =
                    (AnimatedVectorDrawable) getDrawable(R.drawable.avd_no_connection);
            if (mNoConnection != null && avd != null) {
                mNoConnection.setImageDrawable(avd);
                avd.start();
            }
            mNoConnection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getRecipeList(mPage);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecipeListPresenter.clear();
    }

}
