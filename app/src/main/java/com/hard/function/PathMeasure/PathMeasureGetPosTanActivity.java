package com.hard.function.PathMeasure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.hard.function.R;

/**
 * @author Jerry Lai on 2019/11/07
 * 通过PathMeasure的getPosTan获取坐标和余弦正弦值的数据画飞机绕着圆转
 */
public class PathMeasureGetPosTanActivity extends AppCompatActivity implements View.OnClickListener {

    private PathMeasureGetPosTanView mMeasureGetPosTanView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathmeasure_getpostan);
        findView();
    }

    private void findView() {
        mMeasureGetPosTanView = findViewById(R.id.get_pos_view);
        findViewById(R.id.btn_start).setOnClickListener(this::onClick);
        findViewById(R.id.btn_stop).setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                mMeasureGetPosTanView.startAnim();
                break;
            case R.id.btn_stop:
                mMeasureGetPosTanView.stopAnim();
                break;
        }
    }
}
