package org.huxizhijian.simplerecipebook.ui.main.fragment.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.huxizhijian.simplerecipebook.R;
import org.huxizhijian.simplerecipebook.bean.query.QueryBean;
import org.huxizhijian.simplerecipebook.ui.detail.RecipeDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<QueryBean> mData;
    private float mBaseElevation;

    private Activity mContext;
    private LayoutInflater mInflater;

    public CardPagerAdapter(Activity context, List<QueryBean> queryBeen) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mViews = new ArrayList<>();
        mData = queryBeen;
    }

    public void addCardItem(QueryBean queryBean) {
        mViews.add(null);
        mData.add(queryBean);
    }

    public void updateData(List<QueryBean> queryBeen) {
        mData = queryBeen;
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.item_viewpager_random, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.add(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(final QueryBean queryBean, final View view) {
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        final ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        //设置标题
        final String title;
        if (queryBean.getResult().getName() == null &&
                TextUtils.isEmpty(queryBean.getResult().getName().trim())) {
            title = queryBean.getResult().getRecipe().getTitle();
        } else {
            title = queryBean.getResult().getName();
        }
        titleTextView.setText(title);
        //读取图片
        final String imgUrl;
        if (queryBean.getResult().getRecipe() != null &&
                queryBean.getResult().getRecipe().getImg() != null &&
                !TextUtils.isEmpty(queryBean.getResult().getRecipe().getImg())) {
            imgUrl = queryBean.getResult().getRecipe().getImg();
        } else {
            imgUrl = queryBean.getResult().getThumbnail();
        }
        Glide.with(mContext)
                .load(imgUrl)
                .fitCenter()
                .error(R.mipmap.white_place_hold)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
        //设置onclick事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                Resources resources = mContext.getResources();
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        mContext,
                        new Pair(imageView, resources.getString(R.string.transition_shot)),
                        new Pair(view, resources.getString(R.string.transition_shot_background)));
                intent.putExtra("id", queryBean.getResult().getMenuId());
                intent.putExtra("imgUrl", imgUrl);
                intent.putExtra("title", title);
                ActivityCompat.startActivity(mContext, intent, options.toBundle());
            }
        });
    }

}
