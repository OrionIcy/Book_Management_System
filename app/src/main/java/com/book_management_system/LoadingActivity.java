package com.book_management_system;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.book_management_system.com.AndroidUI.MainActivity;


public class LoadingActivity extends Activity
{
    Handler handler=new Handler();
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler.postDelayed(new Runnable()
        {
            public void run()
            {
                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        },1500);
    }
}
