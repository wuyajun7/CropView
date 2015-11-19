package com.cropview.activitys;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.cropview.R;
import com.cropview.controller.CVAppController;


public class CVResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cv_result);

        // get cropped bitmap from Application
        Bitmap cropped = CVAppController.cropped;
        // set cropped bitmap to ImageView
        ((ImageView) findViewById(R.id.result_image)).setImageBitmap(cropped);
    }
}
