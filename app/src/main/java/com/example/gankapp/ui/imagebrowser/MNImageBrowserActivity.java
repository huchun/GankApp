package com.example.gankapp.ui.imagebrowser;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.gankapp.R;
import com.example.gankapp.db.CollectDao;
import com.example.gankapp.ui.MyApplicaiton;
import com.example.gankapp.ui.bean.GankEntity;
import com.example.gankapp.ui.dialog.ListFragmentDialog;
import com.example.gankapp.ui.view.ProgressWheel;
import com.example.gankapp.util.BitmapUtils;
import com.example.gankapp.util.Constants;
import com.example.gankapp.util.IntentUtils;
import com.example.gankapp.util.MySnackbar;
import com.github.chrisbanes.photoview.PhotoView;
import com.maning.mndialoglibrary.MProgressDialog;
import com.maning.mndialoglibrary.MStatusDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunchun.hu on 2018/3/12.
 */

public class MNImageBrowserActivity extends AppCompatActivity {

    public final static String TAG = "MNImageBrowserActivity";

    public final static String IntentKey_ImageList = "IntentKey_ImageList";
    public final static String IntentKey_GankEntityList = "IntentKey_GankEntityList";
    public final static String IntentKey_CurrentPosition = "IntentKey_CurrentPosition";

    private Context context;
    private RelativeLayout rl_black_bg = null;
    private MNGestureView  mnGestureView = null;
    private ViewPager viewPagerBrowser = null;
    private TextView  tvNumShow = null;

    private ImageView currentImageView; //需要保存的图片
    private int clickPosition; //需要保存的图片
    private static int currentPosition;

    private ArrayList<String> imageUrlList = new ArrayList<>();
    private ArrayList<String> mListDialogDatas = new ArrayList<>();
    private ArrayList<GankEntity> welFareLists;

    private MStatusDialog mStatusDialog;
    private MProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setWindowFullScreen();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnimage_browser);

        context = this;

        imageUrlList = getIntent().getStringArrayListExtra(IntentKey_ImageList);
        welFareLists = (ArrayList<GankEntity>) getIntent().getSerializableExtra(IntentKey_GankEntityList);
        currentPosition = getIntent().getIntExtra(IntentKey_CurrentPosition, 1);

        initDialog();
        initViews();
        initData();
        initViewPager();
    }

    private void setWindowFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= 19){
            // 虚拟导航栏透明
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void initDialog() {
        //新建一个Dialog
        mStatusDialog = new MStatusDialog(this);
        mProgressDialog = new MProgressDialog.Builder(this).build();
    }

    private void initViews() {
         rl_black_bg = findViewById(R.id.rl_black_bg);
         mnGestureView = findViewById(R.id.mnGestureView);
         viewPagerBrowser = findViewById(R.id.viewPagerBrowser);
         tvNumShow = findViewById(R.id.tvNumShow);
    }

    private void initData() {
         tvNumShow.setText(String.valueOf((currentPosition + 1) + "/" + imageUrlList.size()));
    }

    private void initViewPager() {
         viewPagerBrowser.setAdapter(new MyViewPagerAdapter());
         viewPagerBrowser.setPageTransformer(true, new ZoomOutPageTransformer());
         viewPagerBrowser.setCurrentItem(currentPosition);
         viewPagerBrowser.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
             @Override
             public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

             @Override
             public void onPageSelected(int position) {
                 currentPosition = position;
                 tvNumShow.setText(String.valueOf((position + 1) + "/" + imageUrlList.size()));
             }

             @Override
             public void onPageScrollStateChanged(int state) {  }
         });

         mnGestureView.setOnSwipeListener(new MNGestureView.OnSwipeListener() {
             @Override
             public void downSwipe() {
                  finishBrowser();
             }

             @Override
             public void overSwipe() {
                  tvNumShow.setVisibility(View.VISIBLE);
                  rl_black_bg.setAlpha(1);
             }

             @Override
             public void onSwiping(float y) {
                 tvNumShow.setVisibility(View.GONE);

                 float mAlpha = 1 - y / 500;
                 if (mAlpha < 0.3){
                     mAlpha = 0.3f;
                 }
                 if (mAlpha > 1){
                     mAlpha = 1;
                 }
                 rl_black_bg.setAlpha(mAlpha);
             }
         });
    }

    private void saveImage() {
        showProgressDialog("正在保存图片...");
        currentImageView.setDrawingCacheEnabled(true);
        final Bitmap  bitmap = Bitmap.createBitmap(currentImageView.getDrawingCache());
        currentImageView.setDrawingCacheEnabled(false);
        if (null == bitmap){
                return;
        }

        // save Image
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean saveBitmapToSD = BitmapUtils.saveBitmapToSD(bitmap, Constants.BasePath, System.currentTimeMillis() + ".jpg", true);
                MyApplicaiton.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        dissmissProgressDialog();
                        if (saveBitmapToSD){
                            showProgressSuccess("保存成功");
                        }else{
                            showProgressError("保存失败");
                        }
                    }
                });
            }
        }).start();
    }

    public void showBottomSheet() {
         mListDialogDatas.clear();
         mListDialogDatas.add("保存");
         mListDialogDatas.add("分享");
         mListDialogDatas.add("设置壁纸");

         if (welFareLists != null && welFareLists.size() > 0){
             GankEntity gankEntity = welFareLists.get(currentPosition);
             //查询是否存在收藏
             boolean isCollect = new CollectDao().queryOneCollectByID(gankEntity.get_id());
             if (isCollect){
                 mListDialogDatas.add("取消收藏");
             }else{
                 mListDialogDatas.add("收藏");
             }
         }

         ListFragmentDialog.newInstance(MNImageBrowserActivity.this).showDialog(getSupportFragmentManager(), mListDialogDatas, new ListFragmentDialog.OnItemClickListener(){

             @Override
             public void onItemClick(View view, int position) {
                  if (position == 0){ //save photo
                      // 先判断是否有权限。
                      if (AndPermission.hasPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                           saveImage();  // 有权限，直接do anything.
                      }else {
                          // 申请权限。
                          AndPermission.with(MNImageBrowserActivity.this).requestCode(100).permission(Manifest.permission.WRITE_EXTERNAL_STORAGE).send();
                      }
                  }else if (position == 1){ //share photo
                      IntentUtils.startAppShareText(context, "GankApp图片分享", "分享图片:" + imageUrlList.get(clickPosition));
                  }else if (position == 2){//setting wallpaper
                      setWallpaper();
                  }else if (position == 3){//collect photo
                       if (welFareLists != null && welFareLists.size() > 0){
                              GankEntity gankEntity = welFareLists.get(currentPosition);
                              // query collect
                              boolean isCollect = new CollectDao().queryOneCollectByID(gankEntity.get_id());
                              if (!isCollect){
                                  // collect
                                  boolean insertResult = new CollectDao().insertOneCollect(gankEntity);
                                  if (insertResult){
                                      mStatusDialog.show("收藏成功", getResources().getDrawable(R.mipmap.mn_icon_dialog_success));
                                  }else{
                                      mStatusDialog.show("收藏失败", getResources().getDrawable(R.mipmap.mn_icon_dialog_fail));
                                  }
                              }else {
                                  // cancel collect
                                  boolean deleResult = new CollectDao().deleteOneCollect(gankEntity.get_id());
                                  if (deleResult){
                                      mStatusDialog.show("取消收藏成功", getResources().getDrawable(R.mipmap.mn_icon_dialog_success));
                                  }else{
                                      mStatusDialog.show("取消收藏失败", getResources().getDrawable(R.mipmap.mn_icon_dialog_fail));
                                  }
                              }
                       }
                  }
             }
         });
    }

    private void setWallpaper() {
        showProgressDialog("正在设置壁纸...");
        currentImageView.setDrawingCacheEnabled(true);
        final Bitmap bitmap = Bitmap.createBitmap(currentImageView.getDrawingCache());
        currentImageView.setDrawingCacheEnabled(false);
        if (bitmap == null){
            showProgressDialog("设置失败...");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
               boolean flag = false;
                WallpaperManager manager = WallpaperManager.getInstance(context);
                try {
                    manager.setBitmap(bitmap);
                    flag = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    flag = false;
                }finally {
                    if (flag){
                        MyApplicaiton.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                dissmissProgressDialog();
                                showProgressSuccess("设置成功!");
                            }
                        });
                    }else{
                        MyApplicaiton.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                dissmissProgressDialog();
                                showProgressSuccess("设置失败!");
                            }
                        });
                    }
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        finishBrowser();
    }

    private void finishBrowser() {
        tvNumShow.setVisibility(View.GONE);
        rl_black_bg.setAlpha(0);
        finish();
        this.overridePendingTransition(0, R.anim.browser_exit_anim);
    }

    private class MyViewPagerAdapter extends PagerAdapter{

        private LayoutInflater layoutInflater;

        MyViewPagerAdapter(){
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return imageUrlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View inflater = layoutInflater.inflate(R.layout.mn_image_browser_item_show_image, container, false);
            final PhotoView imageView = (PhotoView) inflater.findViewById(R.id.photoImageView);
            RelativeLayout rl_browser_root = inflater.findViewById(R.id.rl_browser_root);
            final RelativeLayout rl_image_placeholder_bg = inflater.findViewById(R.id.rl_image_placeholder_bg);
            final ImageView  iv_fail = inflater.findViewById(R.id.iv_fail);
            final ProgressWheel progressWheel = inflater.findViewById(R.id.progressWheel);

            iv_fail.setVisibility(View.GONE);

            String url = imageUrlList.get(position);
            Glide.with(context).load(url).thumbnail(0.2f).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressWheel.setVisibility(View.GONE);
                            iv_fail.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressWheel.setVisibility(View.GONE);
                            rl_image_placeholder_bg.setVisibility(View.GONE);
                            iv_fail.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(imageView);

            rl_browser_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                      finishBrowser();
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                       finishBrowser();
                }
            });

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clickPosition = position;
                    currentImageView = imageView;
                    showBottomSheet(); //显示隐藏下面的Dialog
                    return false;
                }
            });

            container.addView(inflater);
            return inflater;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantPermissions) {
            // 权限申请成功回调。
            if (requestCode == 100){
                MySnackbar.makeSnackBarBlack(viewPagerBrowser, "权限申请成功");
               // saveImage();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
             // 权限申请失败回调。
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(MNImageBrowserActivity.this,deniedPermissions)){
                // 第二种：用自定义的提示语。
                AndPermission.defaultSettingDialog(MNImageBrowserActivity.this,300)
                        .setTitle("权限申请失败")
                        .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                        .setPositiveButton("好，去设置")
                        .show();
            }
        }
    };

    private void showProgressDialog() {
           dissmissProgressDialog();
           mProgressDialog.show();
    }

    private void showProgressDialog(String message) {
        if (TextUtils.isEmpty(message)){
            showProgressDialog();
        }else{
            dissmissProgressDialog();
            mProgressDialog.show(message);
        }
    }

    private void dissmissProgressDialog() {
        if (mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    private void showProgressSuccess(String message) {
        mStatusDialog.show(message, getResources().getDrawable(R.mipmap.mn_icon_dialog_success));
    }

    private void showProgressError(String message) {
        mStatusDialog.show(message, getResources().getDrawable(R.mipmap.mn_icon_dialog_fail));
    }
}
