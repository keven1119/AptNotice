package com.thejoyrun.secondmoduel;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.thejoyrun.noticefinder.NoticeFinder;
import com.thejoyrun.noticefinder.annotation.OnNotice;

import java.util.Date;

public class ThirdActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView;
    Button button;
    Button btn_updatamain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        textView = (TextView) findViewById(R.id.textview);
        button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(this);
        btn_updatamain = (Button) findViewById(R.id.btn_updatamain);
        btn_updatamain.setOnClickListener(this);

        NoticeFinder.inject(this);
    }

    public void updata(){
        NoticeFinder.toNoticeMethod(Uri.parse(NoticeFinder.getNoticeHost()+"/MainActivity/updata"));
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.thejoyrun.notice.aptnotice", "com.thejoyrun.notice.aptnotice.SecondActivity");
            intent.setComponent(cn);
            startActivity(intent);
        }else if(view.getId() == R.id.btn_updatamain){
            updata();
        }
    }

    @OnNotice()
    public void updataTime(){
        textView.setText("haha==>"+System.currentTimeMillis());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NoticeFinder.unInject(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
