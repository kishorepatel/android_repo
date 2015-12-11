package in.blogspot.androidoupsolving.animalsvoiceenglish;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Advanceable;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.single_item);

        ImageView view = (ImageView) findViewById(R.id.img_animal);

        Animation zoomin = AnimationUtils.loadAnimation(this, R.anim.image_zoom_out);
        view.setAnimation(zoomin);

//        if(convertView instanceof AdView)
//            return convertView;
//
//        if(convertView == null){
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.ad_day, parent, false);
//        }
//
//        AdView adCur = (AdView) convertView.findViewById(R.id.ads_day);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adCur.loadAd(adRequest);

        AdView ad  = (AdView) findViewById(R.id.ads_main);
        AdRequest req = new AdRequest.Builder().build();
        ad.loadAd(req);

        view.startAnimation(zoomin);

    }
}