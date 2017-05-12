package org.huxizhijian.simplerecipebook.ui.detail;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.huxizhijian.sdk.util.GlideUtils;
import org.huxizhijian.sdk.util.LogUtils;
import org.huxizhijian.sdk.util.NetworkUtils;
import org.huxizhijian.sdk.util.adapter.recycleradapter.CommonAdapter;
import org.huxizhijian.sdk.util.adapter.recycleradapter.base.ViewHolder;
import org.huxizhijian.sdk.util.adapter.recycleradapter.wrapper.HeaderAndFooterWrapper;
import org.huxizhijian.simplerecipebook.R;
import org.huxizhijian.simplerecipebook.base.BaseActivity;
import org.huxizhijian.simplerecipebook.bean.RecipeStep;
import org.huxizhijian.simplerecipebook.bean.query.QueryBean;
import org.huxizhijian.simplerecipebook.greendao.db.DBHelper;
import org.huxizhijian.simplerecipebook.greendao.entity.RecipeEntity;
import org.huxizhijian.simplerecipebook.greendao.gen.RecipeEntityDao;
import org.huxizhijian.simplerecipebook.ui.showrecipelist.RecipeListActivity;
import org.huxizhijian.simplerecipebook.util.AnimUtils;
import org.huxizhijian.simplerecipebook.util.ColorUtils;
import org.huxizhijian.simplerecipebook.util.ViewUtils;
import org.huxizhijian.simplerecipebook.widget.ElasticDragDismissFrameLayout;
import org.huxizhijian.simplerecipebook.widget.FABToggle;
import org.huxizhijian.simplerecipebook.widget.ParallaxScrimageView;
import org.huxizhijian.simplerecipebook.widget.recyclerview.InsetDividerDecoration;
import org.huxizhijian.simplerecipebook.widget.recyclerview.SlideInItemAnimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import me.gujun.android.taggroup.TagGroup;

import static org.huxizhijian.simplerecipebook.R.id.back;
import static org.huxizhijian.simplerecipebook.R.id.shot;


public class RecipeDetailActivity extends BaseActivity {

    /**
     * UI
     */
    @BindView(R.id.background)
    View mBackground;
    @BindView(shot)
    ParallaxScrimageView mShot;
    @BindView(back)
    ImageButton mBack;
    @BindView(R.id.back_wrapper)
    FrameLayout mBackWrapper;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.fab_heart)
    FABToggle mFabHeart;
    @BindView(R.id.frame_layout)
    ElasticDragDismissFrameLayout mFrameLayout;
    private ImageView mNoConnection;

    //header item in recyclerview
    private View mHeaderView;
    private View mShotSpacer;
    private TextView mShotTitle;
    private TextView mShotDescription;
    private TextView mShotIngredients;
    private TagGroup mTagGroup;

    /**
     * UI control
     */
    int fabOffset;

    private ElasticDragDismissFrameLayout.SystemChromeFader mChromeFader;

    private static final float SCRIM_ADJUSTMENT = 0.075f;
    //test id
    public static final String id = "00100010070000000001";
    //true id
    private String mMenuId;
    //is marked
    private boolean isMarked = false;
    private RecipeEntityDao mRecipeEntityDao;
    private RecipeEntity mRecipeEntity;

    /**
     * presenter
     */
    private RecipeIdPresenter mRecipeIdPresenter;

    /**
     * DATA
     */
    private QueryBean mQueryBean;
    private String mImgUrl;
    private String mTitle;
    @Override
    protected void initWidget() {
        //左上返回按钮事件绑定
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandImageAndFinish();
            }
        });
        //上下拖拽结束本activity
        mChromeFader = new ElasticDragDismissFrameLayout.SystemChromeFader(this) {
            @Override
            public void onDragDismissed() {
                expandImageAndFinish();
            }
        };
        //设置fab的事件
        mFabHeart.setOnClickListener(mFabClick);
    }

    private View.OnClickListener mFabClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mFabHeart.toggle();
            if (isMarked) {
                //解除收藏状态
                mRecipeEntityDao.delete(mRecipeEntity);
            } else {
                //增加收藏
                if (mRecipeEntity == null) {
                    mRecipeEntity = new RecipeEntity();
                    mRecipeEntity.setMenuId(mMenuId);
                    mRecipeEntity.setImgUrl(mImgUrl);
                    mRecipeEntity.setName(mTitle);
                    mRecipeEntity.setCTgTitles(mQueryBean.getResult().getCtgTitles());
                    StringBuilder ctgIds = new StringBuilder();
                    for (int i = 0; i < mQueryBean.getResult().getString().size(); i++) {
                        ctgIds.append(mQueryBean.getResult().getString().get(i));
                        if (i != mQueryBean.getResult().getString().size() - 1) {
                            ctgIds.append(",");
                        }
                    }
                    mRecipeEntity.setCTgIds(ctgIds.toString());
                    if (mQueryBean.getResult().getRecipe() != null) {
                        mRecipeEntity.setIngredients(mQueryBean.getResult().getRecipe().getIngredients());
                        mRecipeEntity.setMethod(mQueryBean.getResult().getRecipe().getMethod());
                        mRecipeEntity.setSumary(mQueryBean.getResult().getRecipe().getSumary());
                    }
                }
                mRecipeEntityDao.insert(mRecipeEntity);
            }
        }
    };


    @Override
    public void initData() {
        //获取传送来的数据
        Intent intent = getIntent();
        mMenuId = intent.getStringExtra("id");
        mImgUrl = intent.getStringExtra("imgUrl");
        mTitle = intent.getStringExtra("title");

        if (mImgUrl != null && mTitle != null) {
            mHeaderView = LayoutInflater.from(this).inflate(R.layout.item_header_detail, null);
            mShotTitle = (TextView) mHeaderView.findViewById(R.id.shot_title);
            mShotDescription = (TextView) mHeaderView.findViewById(R.id.shot_description);
            mShotSpacer = mHeaderView.findViewById(R.id.shot_spacer);
            mTagGroup = (TagGroup) mHeaderView.findViewById(R.id.tag_group);
            mShotIngredients = (TextView) mHeaderView.findViewById(R.id.shot_ingredients);

            mShotTitle.setText(mTitle);
            loadImage();
        }

        //查看是否收藏
        mRecipeEntityDao = DBHelper.getDaoSession(this).getRecipeEntityDao();
        List<RecipeEntity> entities = mRecipeEntityDao
                .queryBuilder()
                .where(RecipeEntityDao.Properties.MenuId.eq(mMenuId))
                .build().list();
        if (entities != null && entities.size() != 0) {
            mRecipeEntity = entities.get(0);
            mFabHeart.setChecked(true);
            isMarked = true;
            updateView();
        } else {
            //请求网络
            mRecipeIdPresenter = new RecipeIdPresenter(new RecipeIdContract.View() {
                @Override
                public void onSuccess(QueryBean queryBean) {
                    LogUtils.i(queryBean);
                    mQueryBean = queryBean;
                    updateView();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    throwable.printStackTrace();
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
                                if (TextUtils.isEmpty(mMenuId.trim())) {
                                    mRecipeIdPresenter.getDetails(id);
                                } else {
                                    mRecipeIdPresenter.getDetails(mMenuId);
                                }
                            }
                        });
                    }
                }
            });
            if (TextUtils.isEmpty(mMenuId.trim())) {
                mRecipeIdPresenter.getDetails(id);
            } else {
                mRecipeIdPresenter.getDetails(mMenuId);
            }
        }

    }

    private boolean updateView() {
        if (mQueryBean == null && mRecipeEntity == null) return false;

        if (mQueryBean != null) {
            if (mQueryBean.getResult().getRecipe() != null &&
                    mQueryBean.getResult().getRecipe().getImg() != null &&
                    !TextUtils.isEmpty(mQueryBean.getResult().getRecipe().getImg())) {
                mImgUrl = mQueryBean.getResult().getRecipe().getImg();
            } else {
                mImgUrl = mQueryBean.getResult().getThumbnail();
            }
            initRecyclerView();
            if (mShot.getDrawable() == null) {
                loadImage();
            }
        } else {
            initRecyclerView();
            mImgUrl = mRecipeEntity.getImgUrl();
            if (mShot.getDrawable() == null) {
                loadImage();
            }
        }
        return true;
    }

    private void loadImage() {
        //更新imageview
        Glide.with(this)
                .load(mImgUrl)
                .listener(glideLoadListener)
                .placeholder(R.mipmap.white_place_hold)
                .error(R.mipmap.white_place_hold)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mShot);

        mShot.getViewTreeObserver().addOnPreDrawListener(mShotPreDrawListener);
    }

    private void initRecyclerView() {
        //初始化headerview
        List<RecipeStep> recipeStepList;
        if (mQueryBean != null) {
            if (mHeaderView == null) {
                mHeaderView = LayoutInflater.from(this).inflate(R.layout.item_header_detail, null);
                mShotDescription = (TextView) mHeaderView.findViewById(R.id.shot_description);
                mShotSpacer = mHeaderView.findViewById(R.id.shot_spacer);
                mTagGroup = (TagGroup) mHeaderView.findViewById(R.id.tag_group);
                mShotIngredients = (TextView) mHeaderView.findViewById(R.id.shot_ingredients);
                mShotTitle = (TextView) mHeaderView.findViewById(R.id.shot_title);
                if (mQueryBean.getResult().getName() == null &&
                        TextUtils.isEmpty(mQueryBean.getResult().getName().trim())) {
                    mTitle = mQueryBean.getResult().getRecipe().getTitle();
                } else {
                    mTitle = mQueryBean.getResult().getName();
                }
                mShotTitle.setText(mTitle);
            }

            if (mQueryBean.getResult().getRecipe() != null &&
                    mQueryBean.getResult().getRecipe().getSumary() != null &&
                    !TextUtils.isEmpty(mQueryBean.getResult().getRecipe().getSumary().trim())) {
                mShotDescription.setText(mQueryBean.getResult().getRecipe().getSumary());
            }
            if (mQueryBean.getResult().getCtgTitles() != null &&
                    !TextUtils.isEmpty(mQueryBean.getResult().getCtgTitles().trim())) {
                final String[] tags = mQueryBean.getResult().getCtgTitles().split(",");
                mTagGroup.setTags(tags);
                mTagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
                    @Override
                    public void onTagClick(String tag) {
                        List<String> tagList = Arrays.asList(tags);
                        Intent intent = new Intent(RecipeDetailActivity.this, RecipeListActivity.class);
                        intent.putExtra("cid", mQueryBean.getResult().getString().get(tagList.indexOf(tag)));
                        startActivity(intent);
                    }
                });
            }
            if (mQueryBean.getResult().getRecipe() != null &&
                    mQueryBean.getResult().getRecipe().getIngredients() != null &&
                    !TextUtils.isEmpty(mQueryBean.getResult().getRecipe().getIngredients().trim())) {
                String ingredients = mQueryBean.getResult().getRecipe().getIngredients();
                mShotIngredients.setText("所需材料：" + ingredients.substring(2, ingredients.length() - 2));
            }
            if (mQueryBean.getResult().getRecipe().getMethod() != null) {
                //将json格式转换成bean
                recipeStepList = new Gson()
                        .fromJson(mQueryBean.getResult().getRecipe().getMethod(),
                                new TypeToken<ArrayList<RecipeStep>>() {
                                }.getType());
                LogUtils.i(recipeStepList);
            } else {
                //如果不存在制作步骤
                recipeStepList = new ArrayList<>();
                recipeStepList.add(new RecipeStep(null, "制作步骤无"));
                LogUtils.i(recipeStepList);
            }
        } else {
            if (mHeaderView == null) {
                mHeaderView = LayoutInflater.from(this).inflate(R.layout.item_header_detail, null);
                mShotDescription = (TextView) mHeaderView.findViewById(R.id.shot_description);
                mShotSpacer = mHeaderView.findViewById(R.id.shot_spacer);
                mTagGroup = (TagGroup) mHeaderView.findViewById(R.id.tag_group);
                mShotIngredients = (TextView) mHeaderView.findViewById(R.id.shot_ingredients);
                mShotTitle = (TextView) mHeaderView.findViewById(R.id.shot_title);
                mShotTitle.setText(mRecipeEntity.getName());
            }

            if (mRecipeEntity.getSumary() != null &&
                    !TextUtils.isEmpty(mRecipeEntity.getSumary().trim())) {
                mShotDescription.setText(mRecipeEntity.getSumary());
            }
            if (mRecipeEntity.getCTgTitles() != null &&
                    !TextUtils.isEmpty(mRecipeEntity.getCTgTitles().trim())) {
                final String[] tags = mRecipeEntity.getCTgTitles().split(",");
                final String[] tagIds = mRecipeEntity.getCTgIds().split(",");
                mTagGroup.setTags(tags);
                mTagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
                    @Override
                    public void onTagClick(String tag) {
                        List<String> tagList = Arrays.asList(tags);
                        List<String> tagIdList = Arrays.asList(tagIds);
                        Intent intent = new Intent(RecipeDetailActivity.this, RecipeListActivity.class);
                        intent.putExtra("cid", tagIdList.get(tagList.indexOf(tag)));
                        startActivity(intent);
                    }
                });
            }
            if (mRecipeEntity.getIngredients() != null &&
                    !TextUtils.isEmpty(mRecipeEntity.getIngredients().trim())) {
                String ingredients = mRecipeEntity.getIngredients();
                mShotIngredients.setText("所需材料：" + ingredients.substring(2, ingredients.length() - 2));
            }
            if (mRecipeEntity.getMethod() != null) {
                //将json格式转换成bean
                recipeStepList = new Gson()
                        .fromJson(mRecipeEntity.getMethod(),
                                new TypeToken<ArrayList<RecipeStep>>() {
                                }.getType());
                LogUtils.i(recipeStepList);
            } else {
                //如果不存在制作步骤
                recipeStepList = new ArrayList<>();
                recipeStepList.add(new RecipeStep(null, "制作步骤无"));
                LogUtils.i(recipeStepList);
            }
        }

        //初始化recyclerview的adapter
        CommonAdapter<RecipeStep> adapter = new CommonAdapter<RecipeStep>(this, R.layout.item_recipe_step,
                recipeStepList) {
            @Override
            protected void convert(ViewHolder holder, final RecipeStep recipeStep, final int position) {
                holder.setText(R.id.step_description, recipeStep.getStep().substring(2));
                final ImageView imageView = holder.getView(R.id.step_img);
                Glide.with(RecipeDetailActivity.this)
                        .load(recipeStep.getImg())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .priority(Priority.IMMEDIATE)
                        .placeholder(R.mipmap.white_place_hold)
                        .error(R.mipmap.white_place_hold)
                        .into(imageView);
            }
        };
        //包装一个带headerview的adapter
        HeaderAndFooterWrapper headerAdapter = new HeaderAndFooterWrapper(adapter);
        //添加headerview
        headerAdapter.addHeaderView(mHeaderView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        CommentAnimator commentAnimator = new CommentAnimator();
        commentAnimator.setAnimateMoves(true);
        mRecyclerView.setItemAnimator(commentAnimator);
        mRecyclerView.setAdapter(headerAdapter);
        mRecyclerView.addItemDecoration(new InsetDividerDecoration(
                ViewHolder.class,
                getResources().getDimensionPixelSize(R.dimen.divider_height),
                getResources().getDimensionPixelSize(R.dimen.keyline_1),
                ContextCompat.getColor(this, R.color.divider)));
        mRecyclerView.addOnScrollListener(scrollListener);
        mRecyclerView.setOnFlingListener(flingListener);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_recipe_detail;
    }

    private void expandImageAndFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFrameLayout.addListener(mChromeFader);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFrameLayout.removeListener(mChromeFader);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRecipeIdPresenter != null) {
            mRecipeIdPresenter.clear();
            mRecipeIdPresenter = null;
        }
    }

    private RequestListener glideLoadListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onResourceReady(GlideDrawable resource, String model,
                                       Target<GlideDrawable> target, boolean isFromMemoryCache,
                                       boolean isFirstResource) {
            final Bitmap bitmap = GlideUtils.getBitmap(resource);
            final int twentyFourDip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    24, RecipeDetailActivity.this.getResources().getDisplayMetrics());
            Palette.from(bitmap)
                    .maximumColorCount(3)
                    .clearFilters() /* by default palette ignore certain hues
                        (e.g. pure black/white) but we don't want this. */
                    .setRegion(0, 0, bitmap.getWidth() - 1, twentyFourDip) /* - 1 to work around
                        https://code.google.com/p/android/issues/detail?id=191013 */
                    .generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            boolean isDark;
                            @ColorUtils.Lightness int lightness = ColorUtils.isDark(palette);
                            if (lightness == ColorUtils.LIGHTNESS_UNKNOWN) {
                                isDark = ColorUtils.isDark(bitmap, bitmap.getWidth() / 2, 0);
                            } else {
                                isDark = lightness == ColorUtils.IS_DARK;
                            }

                            if (!isDark) { // make back icon dark on light images
                                mBack.setColorFilter(ContextCompat.getColor(
                                        RecipeDetailActivity.this, R.color.dark_icon));
                            }

                            // color the status bar. Set a complementary dark color on L,
                            // light or dark color on M (with matching status bar icons)
                            int statusBarColor = getWindow().getStatusBarColor();
                            final Palette.Swatch topColor =
                                    ColorUtils.getMostPopulousSwatch(palette);
                            if (topColor != null &&
                                    (isDark || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                                statusBarColor = ColorUtils.scrimify(topColor.getRgb(),
                                        isDark, SCRIM_ADJUSTMENT);
                                // set a light status bar on M+
                                if (!isDark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    ViewUtils.setLightStatusBar(mShot);
                                }
                            }

                            if (statusBarColor != getWindow().getStatusBarColor()) {
                                mShot.setScrimColor(statusBarColor);
                                ValueAnimator statusBarColorAnim = ValueAnimator.ofArgb(
                                        getWindow().getStatusBarColor(), statusBarColor);
                                statusBarColorAnim.addUpdateListener(new ValueAnimator
                                        .AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        getWindow().setStatusBarColor(
                                                (int) animation.getAnimatedValue());
                                    }
                                });
                                statusBarColorAnim.setDuration(1000L);
                                statusBarColorAnim.setInterpolator(
                                        AnimUtils.getFastOutSlowInInterpolator(RecipeDetailActivity.this));
                                statusBarColorAnim.start();
                            }
                        }
                    });

            Palette.from(bitmap)
                    .clearFilters()
                    .generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            // slightly more opaque ripple on the pinned image to compensate
                            mShotSpacer.setBackground(
                                    ViewUtils.createRipple(palette, 0.25f, 0.5f,
                                            ContextCompat.getColor(RecipeDetailActivity.this,
                                                    R.color.mid_grey),
                                            true));
                            // for the scrim
                            mShot.setForeground(
                                    ViewUtils.createRipple(palette, 0.3f, 0.6f,
                                            ContextCompat.getColor(RecipeDetailActivity.this,
                                                    R.color.mid_grey),
                                            true));
                        }
                    });

            mShot.setBackground(null);
            return false;
        }

        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                   boolean isFirstResource) {
            return false;
        }
    };

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            final int scrollY = mHeaderView.getTop();
            mShot.setOffset(scrollY);
            mFabHeart.setOffset(fabOffset + scrollY);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            // as we animate the main image's elevation change when it 'pins' at it's min height
            // a fling can cause the title to go over the image before the animation has a chance to
            // run. In this case we short circuit the animation and just jump to state.
            mShot.setImmediatePin(newState == RecyclerView.SCROLL_STATE_SETTLING);
        }
    };

    private RecyclerView.OnFlingListener flingListener = new RecyclerView.OnFlingListener() {
        @Override
        public boolean onFling(int velocityX, int velocityY) {
            mShot.setImmediatePin(true);
            return false;
        }
    };

    private ViewTreeObserver.OnPreDrawListener mShotPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            mShot.getViewTreeObserver().removeOnPreDrawListener(this);
            calculateFabPosition();
            startPostponedEnterTransition();
            return true;
        }
    };

    //計算fab的位置
    void calculateFabPosition() {
        // calculate 'natural' position i.e. with full height image. Store it for use when scrolling
        fabOffset = mShot.getHeight() + mShotTitle.getHeight() - (mFabHeart.getHeight() / 2);
        mFabHeart.setOffset(fabOffset);

        // calculate min position i.e. pinned to the collapsed image when scrolled
        mFabHeart.setMinOffset(mShot.getMinimumHeight() - (mFabHeart.getHeight() / 2));
    }

    static class CommentAnimator extends SlideInItemAnimator {

        private boolean animateMoves = false;

        CommentAnimator() {
            super();
        }

        void setAnimateMoves(boolean animateMoves) {
            this.animateMoves = animateMoves;
        }

        @Override
        public boolean animateMove(
                RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
            if (!animateMoves) {
                dispatchMoveFinished(holder);
                return false;
            }
            return super.animateMove(holder, fromX, fromY, toX, toY);
        }
    }

}
