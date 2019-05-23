package com.hard.function.BezierLines;

import android.app.Activity;
import android.os.Bundle;

import com.hard.function.R;

import androidx.annotation.Nullable;

/**
 * @author Jerry Lai on 2019/05/09
 * 演示贝塞尔曲线动画
 */
public class BezierBeginDerivationActivity extends Activity {

    private BezierRunView bezierView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_begin_derivation);

        findView();

    }

    private void findView() {
        bezierView = findViewById(R.id.bezier_view);
        bezierView.start();
    }
}
