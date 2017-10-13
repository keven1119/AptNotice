package com.thejoyrun.notice.aptnotice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.thejoyrun.noticefinder.annotation.OnNotice;

/**
 * Created by keven-liang on 2017/10/10.
 */

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        btn = (Button) findViewById(R.id.btn_second);
        btn.setOnClickListener(this);

    }

    public void updataFirst(){
    }

    @Override
    public void onClick(View view) {
        updataFirst();
    }
}
