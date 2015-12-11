package in.blogspot.androidoupsolving.animalsvoiceenglish;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by NEW on 10-12-2015.
 */
public class TestActivity extends AppCompatActivity{
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_single_item);

        ImageView imgAnimalImage = (ImageView) findViewById(R.id.img_test_question);
        adView = (AdView) findViewById(R.id.test_ad_view);

        Animation zoomOut = AnimationUtils.loadAnimation(this, R.anim.image_zoom_out);
        imgAnimalImage.setAnimation(zoomOut);
        imgAnimalImage.startAnimation(zoomOut);

        loadAd();
    }

    private void loadAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
