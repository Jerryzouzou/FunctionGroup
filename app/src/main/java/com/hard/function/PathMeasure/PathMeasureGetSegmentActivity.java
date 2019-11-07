package com.hard.function.PathMeasure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hard.function.R;

/**
 * @author Jerry Lai on 2019/11/07
 * 通过PathMeasure的getSegment截取path部分，实现动态变长度的加载圈
 */
public class PathMeasureGetSegmentActivity extends AppCompatActivity implements View.OnClickListener {

    private PathMeasureGetSegmentView mGetSegmentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathmeasure_getsegment);
        findView();
    }

    private void findView() {
        mGetSegmentView = findViewById(R.id.get_seg_view);
        findViewById(R.id.btn_start).setOnClickListener(this::onClick);
        findViewById(R.id.btn_stop).setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                mGetSegmentView.startAnim();
                break;
            case R.id.btn_stop:
                mGetSegmentView.stopAnim();
                break;
        }
    }
}
