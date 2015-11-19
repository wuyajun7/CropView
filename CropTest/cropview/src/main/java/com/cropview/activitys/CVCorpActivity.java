package com.cropview.activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import com.cropview.R;
import com.cropview.controller.CVAppController;
import com.cropview.controller.CVCropImageView;
import com.cropview.interfaces.ISaveImgListener;
import com.cropview.utils.CVAsyncTaskSaveImage;


public class CVCorpActivity extends Activity {

    private CVCropImageView mCropView;
    private LinearLayout mRootLayout;
    private LinearLayout mTabBar;

    private DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cv_crop);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        initViews();

        mCropView.setImageBitmap(CVAppController.cropped);
    }

    private void initViews() {
        mCropView = (CVCropImageView) findViewById(R.id.cropImageView);

        //自定义充满宽度 - 比例7：5
//        mCropView.setInitialFrameScale(1.0f);
        mCropView.setCustomRatio(7, 5);

        findViewById(R.id.buttonDone).setOnClickListener(btnListener);
        findViewById(R.id.buttonFitImage).setOnClickListener(btnListener);
        findViewById(R.id.button1_1).setOnClickListener(btnListener);
        findViewById(R.id.button3_4).setOnClickListener(btnListener);
        findViewById(R.id.button4_3).setOnClickListener(btnListener);
        findViewById(R.id.button9_16).setOnClickListener(btnListener);
        findViewById(R.id.button16_9).setOnClickListener(btnListener);
        findViewById(R.id.buttonFree).setOnClickListener(btnListener);
        findViewById(R.id.buttonRotateImage).setOnClickListener(btnListener);
        findViewById(R.id.buttonSelectScale).setOnClickListener(btnListener);
        findViewById(R.id.buttonCustom).setOnClickListener(btnListener);
        findViewById(R.id.buttonCircle).setOnClickListener(btnListener);
        mRootLayout = (LinearLayout) findViewById(R.id.layout_root);
        mTabBar = (LinearLayout) findViewById(R.id.tab_bar);
        findViewById(R.id.close_tabbar).setOnClickListener(btnListener);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCropView.setmCropMode(CVCropImageView.CropMode.RATIO_FREE);
            }
        },3000);
    }

    private final View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCropView.setInitialFrameScale(0.75f);

            if (v.getId() == R.id.buttonDone){
                CVAppController.cropped = mCropView.getCroppedBitmap();

                CVAsyncTaskSaveImage asyncTaskSaveImage = new CVAsyncTaskSaveImage(
                        mCropView.getCroppedBitmap(),
                        "niuniu" + System.currentTimeMillis(), 40,
                        new ISaveImgListener() {
                            @Override
                            public void onSuccess(int statusCode) {
                                Intent intent = new Intent(CVCorpActivity.this, CVResultActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(int statusCode, String msg) {

                            }

                            @Override
                            public void progress(int pro) {
                                Log.i("propropro", "" + pro);
                            }
                        }
                );
                asyncTaskSaveImage.execute();
            }else if(v.getId() == R.id.buttonFitImage){
                mCropView.setCropMode(CVCropImageView.CropMode.RATIO_FIT_IMAGE);
            }else if(v.getId() == R.id.button1_1){
                mCropView.setCropMode(CVCropImageView.CropMode.RATIO_1_1);
            }else if(v.getId() == R.id.button3_4){
                mCropView.setCropMode(CVCropImageView.CropMode.RATIO_3_4);
            }else if(v.getId() == R.id.button4_3){
                mCropView.setCropMode(CVCropImageView.CropMode.RATIO_4_3);
            }else if(v.getId() == R.id.button9_16){
                mCropView.setCropMode(CVCropImageView.CropMode.RATIO_9_16);
            }else if(v.getId() == R.id.button16_9){
                mCropView.setCropMode(CVCropImageView.CropMode.RATIO_16_9);
            }else if(v.getId() == R.id.buttonCustom){
                mCropView.setCustomRatio(7, 5);
            }else if(v.getId() == R.id.buttonFree){
                mCropView.setCropMode(CVCropImageView.CropMode.RATIO_FREE);
            }else if(v.getId() == R.id.buttonCircle){
                mCropView.setCropMode(CVCropImageView.CropMode.CIRCLE);
            }else if(v.getId() == R.id.buttonRotateImage){
                mCropView.rotateImage(CVCropImageView.RotateDegrees.ROTATE_90D);
            }else if(v.getId() == R.id.buttonSelectScale){
                showOrHidLayout(mTabBar);
            }else if(v.getId() == R.id.close_tabbar){
                showOrHidLayout(mTabBar);
            }
        }
    };

    //动画 时间
    private final int duration = 400;

    /**
     * 显示或隐藏layout
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void showOrHidLayout(final View view) {
        if (View.GONE == view.getVisibility()) {
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationX", metrics.widthPixels * 3 / 2, 0F);
            animator1.setDuration(duration);
            animator1.setInterpolator(new AccelerateInterpolator());
            animator1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    view.setVisibility(View.VISIBLE);
                }
            });
            animator1.start();
        } else {
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationX", 0F, metrics.widthPixels * 3 / 2);
            animator1.setDuration(duration);
            animator1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.GONE);
                }
            });
            animator1.start();
        }
    }

}
