package com.thejoyrun.notice.aptnotice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thejoyrun.notice.aptnotice.R;
import com.thejoyrun.notice.aptnotice.SecondActivity;
import com.thejoyrun.noticefinder.NoticeFinder;
import com.thejoyrun.secondmoduel.ThirdActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mTextView;
    Button mButton;

    public void onButtonClick() {
        Toast.makeText(this, "onButtonClick", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.tv);
        mButton = (Button) findViewById(R.id.btn);
        mButton.setOnClickListener(this);
        NoticeFinder.inject(this);
    }

    @OnNotice("updata_message")
    public void updata(){
        mTextView.setText(System.currentTimeMillis()+"");
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this, "onButtonClick", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }
}
