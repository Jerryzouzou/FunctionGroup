package com.hard.function.BezierLines;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hard.function.R;

/**
 * @author Jerry Lai on 2019/07/20
 * 演示圆变心的心形图，通过spring公式改变
 * Math.pow(2, -10 * x) * Math.sin((x - factor / 4) * (2 * Math.PI) / factor) + 1;
 */
public class BezierHeartViewSpringActivity extends AppCompatActivity {

    private HeartViewByBezier mHeart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_heart);

        findViewAndInit();
    }

    private void findViewAndInit() {
        mHeart = findViewById(R.id.heart_view);
        //mHeart.start();
    }

    public void onStart(View v){
        mHeart.start();
    }

}
