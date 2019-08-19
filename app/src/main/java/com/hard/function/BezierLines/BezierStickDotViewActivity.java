package com.hard.function.BezierLines;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.hard.function.R;
import com.hard.function.tool.Utils;

import androidx.annotation.Nullable;

/**
 * @author Jerry Lai on 2019/08/19
 */
public class BezierStickDotViewActivity extends Activity {

    private StickDotViewWithBezier mStickDotView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_stick_dot);

        findViewAndInit();
    }

    private void findViewAndInit() {
        mStickDotView = findViewById(R.id.stick_dot_view);
        findViewById(R.id.btn_reset).setOnClickListener((View v)->{
            mStickDotView.setVisibility(View.VISIBLE);
        });

        mStickDotView.setText("99+");
        mStickDotView.setDragStatusListener(new StickDotViewWithBezier.onDragStatusListener() {
            @Override
            public void onDrag() {
                Utils.logiJerry("mStickDotView on drag");
            }

            @Override
            public void onMove() {
                Utils.logiJerry("mStickDotView on move");
            }

            @Override
            public void onRestore() {
                Utils.logiJerry("mStickDotView on restore");
                mStickDotView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDismiss() {
                Utils.logiJerry("mStickDotView on dismiss");
                mStickDotView.setVisibility(View.GONE);
            }
        });
    }
}
