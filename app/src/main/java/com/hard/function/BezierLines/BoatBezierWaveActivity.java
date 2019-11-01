package com.hard.function.BezierLines;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hard.function.R;

/**
 * @author Jerry Lai on 2019/11/01
 * 乘风破浪的小船
 */
public class BoatBezierWaveActivity extends AppCompatActivity implements View.OnClickListener {

    private BoatWaveWithBezierView mBoatWaveWithBezierView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boat_wave_view);
        findView();
    }

    private void findView() {
        mBoatWaveWithBezierView = findViewById(R.id.boat_wave_view);
        findViewById(R.id.btn_start).setOnClickListener(this::onClick);
        findViewById(R.id.btn_stop).setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                mBoatWaveWithBezierView.startAnim();
                break;
            case R.id.btn_stop:
                mBoatWaveWithBezierView.stopAnim();
                break;
        }
    }
}
