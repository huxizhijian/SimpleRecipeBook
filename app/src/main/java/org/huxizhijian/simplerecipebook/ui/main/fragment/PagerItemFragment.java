package org.huxizhijian.simplerecipebook.ui.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.huxizhijian.sdk.util.LogUtils;
import org.huxizhijian.simplerecipebook.R;
import org.huxizhijian.simplerecipebook.bean.query.QueryBean;
import org.huxizhijian.simplerecipebook.ui.detail.RecipeDetailActivity;
import org.huxizhijian.simplerecipebook.widget.DragLayout;


public class PagerItemFragment extends Fragment implements DragLayout.GotoDetailListener {

    private TextView mRecipeName;
    private TextView mRecipeSummary;
    private ImageView mImage;
    private DragLayout mDragLayout;
    private View mBackGround;
    private View mRoot;

    private QueryBean mQueryBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(getLayoutId(), container, false);
        checkAndInitView();
        return mRoot;
    }

    @Override
    public void gotoDetail() {
        Activity activity = (Activity) getContext();
        Resources resources = getActivity().getResources();
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                new Pair(mImage, resources.getString(R.string.transition_shot)),
                new Pair(mBackGround, resources.getString(R.string.transition_shot_background)));
        Intent intent = new Intent(activity, RecipeDetailActivity.class);
        intent.putExtra("id", mQueryBean.getResult().getMenuId());
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private int getLayoutId() {
        return R.layout.fragment_viewpager_item;
    }

    public void bindView(QueryBean queryBean) {
        LogUtils.i(queryBean);
        if (queryBean != null) {
            mQueryBean = queryBean;
        }
    }

    private void updateView() {
        if (mQueryBean.getResult().getName() == null &&
                TextUtils.isEmpty(mQueryBean.getResult().getName().trim())) {
            mRecipeName.setText(mQueryBean.getResult().getRecipe().getTitle());
        } else {
            mRecipeName.setText(mQueryBean.getResult().getName());
        }
        if (mQueryBean.getResult().getRecipe().getSumary() != null &&
                !TextUtils.isEmpty(mQueryBean.getResult().getRecipe().getSumary().trim())) {
            mRecipeSummary.setText(mQueryBean.getResult().getRecipe().getSumary());
        }

        String imgUrl;
        if (mQueryBean.getResult().getRecipe().getImg() != null &&
                !TextUtils.isEmpty(mQueryBean.getResult().getRecipe().getImg())) {
            imgUrl = mQueryBean.getResult().getRecipe().getImg();
        } else {
            imgUrl = mQueryBean.getResult().getThumbnail();
        }

        if (mImage.getDrawable() != null) {
            Glide.clear(mImage);
        }
        Glide.with(this)
                .load(imgUrl)
                .centerCrop()
                .into(mImage);
    }

    private void checkAndInitView() {
        mRecipeName = findView(R.id.recipe_name);
        mRecipeSummary = findView(R.id.recipe_summary);
        mImage = findView(R.id.image_view);
        mDragLayout = findView(R.id.drag_layout);
        mBackGround = findView(R.id.back_ground);

        mDragLayout.setGotoDetailListener(this);
        if (mQueryBean != null) {
            updateView();
        }
    }

    private <T extends View> T findView(int viewId) {
        return (T) mRoot.findViewById(viewId);
    }

}
