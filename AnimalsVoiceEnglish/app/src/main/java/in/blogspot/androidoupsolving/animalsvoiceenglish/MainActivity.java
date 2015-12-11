package in.blogspot.androidoupsolving.animalsvoiceenglish;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Advanceable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnPractise;
    Button btnTest;
    Button btnSpeakerOptions;
    Button btnSettings;
    Button btnFavourites;
    ImageView imgAppStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        btnPractise = (Button) findViewById(R.id.index_page_btn_practise);
        btnTest = (Button) findViewById(R.id.index_page_btn_test);
        btnSpeakerOptions = (Button) findViewById(R.id.index_page_btn_speaker_options);
        btnSettings = (Button) findViewById(R.id.index_page_btn_settings);
        btnFavourites = (Button) findViewById(R.id.index_page_btn_favourite);
        imgAppStore = (ImageView) findViewById(R.id.index_page_img_app_store_page);


        btnPractise.setOnClickListener(this);
        btnTest.setOnClickListener(this);
        btnSpeakerOptions.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnFavourites.setOnClickListener(this);
        imgAppStore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.index_page_btn_practise:{
                startActivity(new Intent(this, DetailActivity.class));
                break;
            }
            case R.id.index_page_btn_test:{
                break;
            }
            case R.id.index_page_btn_favourite:{

                break;
            }
            case R.id.index_page_btn_settings:{

                break;
            }
            case R.id.index_page_btn_speaker_options:{

                break;
            }
            case R.id.index_page_img_app_store_page:{
                break;
            }
            default:{
                //don nothing
            }
        }
    }
}