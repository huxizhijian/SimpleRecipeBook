package org.huxizhijian.simplerecipebook.ui.main.fragment.category;


import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import org.huxizhijian.sdk.util.LogUtils;
import org.huxizhijian.sdk.util.NetworkUtils;
import org.huxizhijian.sdk.util.adapter.recycleradapter.CommonAdapter;
import org.huxizhijian.sdk.util.adapter.recycleradapter.base.ViewHolder;
import org.huxizhijian.simplerecipebook.CookRecipeApp;
import org.huxizhijian.simplerecipebook.R;
import org.huxizhijian.simplerecipebook.base.BaseFragment;
import org.huxizhijian.simplerecipebook.bean.category.CategoryBean;
import org.huxizhijian.simplerecipebook.bean.category.Childs;
import org.huxizhijian.simplerecipebook.ui.showrecipelist.RecipeListActivity;
import org.huxizhijian.simplerecipebook.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.gujun.android.taggroup.TagGroup;

/**
 * 分类展示
 */
public class CategoryFragment extends BaseFragment implements CategoryContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private ImageView mNoConnection;

    private CategoryBean mCategoryBean;

    public CategoryFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    public void initData() {
        //申请分类数据
        CookRecipeApp.getApplication().getCategoryBean(this);
    }

    @Override
    public void onSuccess(CategoryBean categoryBean) {
        if (Integer.valueOf(categoryBean.getRetCode()) == Constant.API_SUCCESS) {
            LogUtils.i(categoryBean);
            mCategoryBean = categoryBean;
            initRecyclerView();
        }
    }

    private void initRecyclerView() {
        CommonAdapter<Childs> adapter = new CommonAdapter<Childs>(mContext, R.layout.item_category,
                mCategoryBean.getResult().getChilds()) {
            @Override
            protected void convert(ViewHolder holder, final Childs childs, int position) {
                holder.setText(R.id.text_category, childs.getCategoryInfo().getName());
                TagGroup tagGroup = holder.getView(R.id.tag_group);
                final List<String> tags = new ArrayList<>();
                final List<String> ctagIds = new ArrayList<>();
                for (int i = 0; i < childs.getChilds().size(); i++) {
                    tags.add(childs.getChilds().get(i).getCategoryInfo().getName());
                    ctagIds.add(childs.getChilds().get(i).getCategoryInfo().getCtgId());
                }
                tagGroup.setTags(tags);
                tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
                    @Override
                    public void onTagClick(String tag) {
                        Intent intent = new Intent(mContext, RecipeListActivity.class);
                        intent.putExtra("cid", ctagIds.get(tags.indexOf(tag)));
                        startActivity(intent);
                    }
                });
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onFailure(Throwable throwable) {
        throwable.printStackTrace();
        if (!NetworkUtils.isConnected()) {
            //显示没有网络提示
            if (mNoConnection == null) {
                ViewStub viewStub = (ViewStub) mRoot.findViewById(R.id.view_stub);
                mNoConnection = (ImageView) viewStub.inflate();
            }
            mNoConnection.setVisibility(View.VISIBLE);
            final AnimatedVectorDrawable avd =
                    (AnimatedVectorDrawable) mContext.getDrawable(R.drawable.avd_no_connection);
            if (mNoConnection != null && avd != null) {
                mNoConnection.setImageDrawable(avd);
                avd.start();
            }
            mNoConnection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //申请分类数据
                    initData();
                }
            });
        }
    }

}
