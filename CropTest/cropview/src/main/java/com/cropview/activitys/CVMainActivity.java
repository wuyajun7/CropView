package com.cropview.activitys;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.cropview.utils.CVImageUtil;
import com.cropview.R;
import com.cropview.controller.CVAppController;

import java.io.File;
import java.util.Date;

/**
 * Created by wuyajun on 15/11/17.
 * Detail:
 */
public class CVMainActivity extends Activity {

    private final int minification = 3;//图片压缩倍数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cv_main);

        findViewById(R.id.select_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoDialog();
            }
        });
    }

    private File mCaptureFile = null;
    private static final int REQUEST_CAPTURE_IMAGE = 0;

    /**
     * 显示图片选择 Dialog
     */
    private void showPhotoDialog() {

        new AlertDialog.Builder(this)
                .setTitle("头像上传")
                .setItems(new String[]{"拍照", "从相册中选择"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            mCaptureFile = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM),
                                    "" + new Date().getTime() + ".jpg");
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCaptureFile));
                            startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
                        } else if (which == 1) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_PICK);//Pick an item from the data
                            intent.setType("image/*");//从所有图片中进行选择
                            startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
                        }
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CAPTURE_IMAGE) {
            try {
                Uri uri = data.getData();
                if (uri != null) {
                    Cursor cr = getContentResolver().query(uri,
                            new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                    if (cr.moveToFirst()) {
//                        Log.i("resulteeeee", "1");
                        File file = new File(cr.getString(cr.getColumnIndex(MediaStore.Images.Media.DATA)));
//                        Bitmap tBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                        Log.i("resulteeeee", "1- " + FileSizeUtil.getBitmapsize(tBitmap, FileSizeUtil.SIZETYPE_KB));
//                        Log.i("resulteeeee", "1 " + FileSizeUtil.getFileOrFilesSize(file.getAbsolutePath(), FileSizeUtil.SIZETYPE_KB));
                        Bitmap bitmap = CVImageUtil.loadBitmap(file, minification);
//                        Log.i("resulteeeee", "1- " + FileSizeUtil.getBitmapsize(bitmap, FileSizeUtil.SIZETYPE_KB));
                        setBitmapJumpActivity(bitmap);
                    }
                    cr.close();
                } else {
//                    Log.i("resulteeeee", "2");
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
//                    Log.i("resulteeeee", "2 " + FileSizeUtil.getBitmapsize(bitmap, FileSizeUtil.SIZETYPE_KB));
                    setBitmapJumpActivity(CVImageUtil.changeBitmap(bitmap,
                            bitmap.getWidth() / minification, bitmap.getHeight() / minification));
                }

            } catch (Exception e) {
                if (mCaptureFile != null && mCaptureFile.exists()) {
//                    Log.i("resulteeeee", "3");
//                    Bitmap tBitmap = BitmapFactory.decodeFile(mCaptureFile.getAbsolutePath());
//                    Log.i("resulteeeee", "3- " + FileSizeUtil.getBitmapsize(tBitmap, FileSizeUtil.SIZETYPE_KB));
//                    Log.i("resulteeeee", "3 " + FileSizeUtil.getFileOrFilesSize(mCaptureFile.getAbsolutePath(), FileSizeUtil.SIZETYPE_KB));
                    Bitmap bitmap = CVImageUtil.loadBitmap(mCaptureFile, minification);
//                    Log.i("resulteeeee", "3 " + FileSizeUtil.getBitmapsize(bitmap, FileSizeUtil.SIZETYPE_KB));
                    setBitmapJumpActivity(bitmap);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setBitmapJumpActivity(Bitmap bitmap) {
        CVAppController.cropped = bitmap;
        startActivity(new Intent(CVMainActivity.this, CVCorpActivity.class));
    }

}
