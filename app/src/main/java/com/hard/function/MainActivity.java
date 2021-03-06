package com.hard.function;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.hard.function.BezierLines.BezierBeginDerivationActivity;
import com.hard.function.BezierLines.BezierDIYActivity;
import com.hard.function.BezierLines.BezierHeartViewSpringActivity;
import com.hard.function.BezierLines.BezierRun2CircleActivity;
import com.hard.function.BezierLines.BezierRun2CircleView;
import com.hard.function.BezierLines.BezierStickDotViewActivity;
import com.hard.function.BezierLines.BoatBezierWaveActivity;
import com.hard.function.PathMeasure.PathMeasureGetPosTanActivity;
import com.hard.function.PathMeasure.PathMeasureGetPosTanView;
import com.hard.function.PathMeasure.PathMeasureGetSegmentActivity;
import com.hard.function.PathMeasure.PathMeasureGetSegmentView;
import com.hard.function.tool.UIUtils;
import com.hard.function.tool.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private int btnId[] = {R.id.btn_f_01, R.id.btn_f_02, R.id.btn_f_03, R.id.btn_f_04, R.id.btn_f_05,
        R.id.btn_f_06, R.id.btn_f_07, R.id.btn_f_08};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        findView();
        Log.i("Jerry", "onCreate: ");
    }

    private void findView() {
        for (int i=0; i<btnId.length; i++){
            findViewById(btnId[i]).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_f_01:
                Utils.startActivity(mContext, BezierBeginDerivationActivity.class);
                break;
            case R.id.btn_f_02:
                Utils.startActivity(mContext, BezierRun2CircleActivity.class);
                break;
            case R.id.btn_f_03:
                Utils.startActivity(mContext, BezierDIYActivity.class);
                break;
            case R.id.btn_f_04:
                Utils.startActivity(mContext, BezierHeartViewSpringActivity.class);
                break;
            case R.id.btn_f_05:
                Utils.startActivity(mContext, BezierStickDotViewActivity.class);
                break;
            case R.id.btn_f_06:
                Utils.startActivity(mContext, BoatBezierWaveActivity.class);
                break;
            case R.id.btn_f_07:
                Utils.startActivity(mContext, PathMeasureGetPosTanActivity.class);
                break;
            case R.id.btn_f_08:
                Utils.startActivity(mContext, PathMeasureGetSegmentActivity.class);
                break;
        }
    }


}
