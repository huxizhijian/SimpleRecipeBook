package org.huxizhijian.simplerecipebook.ui.main.fragment;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.huxizhijian.sdk.util.adapter.recycleradapter.CommonAdapter;
import org.huxizhijian.sdk.util.adapter.recycleradapter.base.ViewHolder;
import org.huxizhijian.sdk.util.adapter.recycleradapter.wrapper.EmptyWrapper;
import org.huxizhijian.simplerecipebook.R;
import org.huxizhijian.simplerecipebook.base.BaseFragment;
import org.huxizhijian.simplerecipebook.greendao.db.DBHelper;
import org.huxizhijian.simplerecipebook.greendao.entity.RecipeEntity;
import org.huxizhijian.simplerecipebook.greendao.gen.RecipeEntityDao;
import org.huxizhijian.simplerecipebook.ui.detail.RecipeDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author huxizhijian 2017/5/6
 */
public class CollectionFragment extends BaseFragment {

    /**
     * UI
     */
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    /**
     * DATA
     */
    private List<RecipeEntity> mRecipeEntityList;
    private RecipeEntityDao mRecipeEntityDao;
    //adapter
    private EmptyWrapper mEmptyAdapter;

    public void updateView() {
        if (mEmptyAdapter == null) return;
        mRefreshLayout.setRefreshing(true);
        mRecipeEntityList.clear();
        List<RecipeEntity> recipeEntityList = mRecipeEntityDao.loadAll();
        if (recipeEntityList != null) {
            mRecipeEntityList.addAll(recipeEntityList);
        }
        mEmptyAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void initWidget(View root) {
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimaryDark));
        mRefreshLayout.setRefreshing(true);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateView();
            }
        });
    }

    @Override
    public void initData() {
        mRecipeEntityDao = DBHelper.getDaoSession(mContext).getRecipeEntityDao();
        mRecipeEntityList = mRecipeEntityDao.loadAll();
        if (mRecipeEntityList != null) {
            initRecyclerView();
        } else {
            mRecipeEntityList = new ArrayList<>();
            initRecyclerView();
        }
        mRefreshLayout.setRefreshing(false);
    }

    private void initRecyclerView() {
        CommonAdapter<RecipeEntity> adapter = new CommonAdapter<RecipeEntity>(mContext,
                R.layout.item_recipe_list, mRecipeEntityList) {
            @Override
            protected void convert(ViewHolder holder, final RecipeEntity recipeEntity, int position) {
                final ImageView imageView = holder.getView(R.id.image_view);
                Glide.with(mContext)
                        .load(recipeEntity.getImgUrl())
                        .error(R.mipmap.white_place_hold)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                        intent.putExtra("id", recipeEntity.getMenuId());
                        intent.putExtra("imgUrl", recipeEntity.getImgUrl());
                        intent.putExtra("title", recipeEntity.getName());
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                getActivity(),
                                new Pair(imageView, getResources().getString(R.string.transition_shot)),
                                new Pair(imageView, getResources().getString(R.string.transition_shot_background)));
                        ActivityCompat.startActivity(mContext, intent, options.toBundle());
                    }
                });
            }
        };
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mEmptyAdapter = new EmptyWrapper(adapter);
        //添加为空时的视图
        mEmptyAdapter.setEmptyView(R.layout.item_list_empty);
        mRecyclerView.setAdapter(mEmptyAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycler_view_with_refresh;
    }

}
