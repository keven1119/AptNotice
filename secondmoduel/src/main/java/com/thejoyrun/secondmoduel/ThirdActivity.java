package com.thejoyrun.secondmoduel;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thejoyrun.noticefinder.NoticeFinder;
import com.thejoyrun.noticefinder.annotation.OnNotice;

public class ThirdActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        NoticeFinder.inject(this);
        textView = (TextView) findViewById(R.id.textview);
        button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(this);

    }

    @OnNotice("updata_time")
    public void updata(){
        textView.setText("ThirdActivity ==》"+System.currentTimeMillis());
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName("com.thejoyrun.notice.aptnotice", "com.thejoyrun.notice.aptnotice.SecondActivity");
        intent.setComponent(cn);
        startActivity(intent);
    }
}
