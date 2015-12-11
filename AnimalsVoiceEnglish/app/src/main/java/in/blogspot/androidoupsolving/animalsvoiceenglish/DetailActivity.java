package in.blogspot.androidoupsolving.animalsvoiceenglish;

import android.app.Fragment;
import android.content.res.Resources;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Set;


public class DetailActivity extends AppCompatActivity {
    ArrayList<Animal> allAnimals;
    Set<String> mFavourites;
    public static int mPosition = 0;
    public static int mSize;

    TextToSpeech mTTS;
    MediaPlayer mMediaPlayer;

    ImageView imgPrevPage;
    ImageView imgForwardPage;
    ImageView imgShuffle;
    ImageView imgRingtone;
    ImageView imgAnimalImage;
    TextView textAnimalName;
    ImageView imgFavourite;
    ImageView imgMakeSound;

    AdView adView;

    Animation zoomOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        allAnimals = Utility.getAllAnimals(this);
        mFavourites = Utility.getFavourites(this);
        mSize = allAnimals.size();

        setContentView(R.layout.single_item);

        imgPrevPage = (ImageView) findViewById(R.id.practise_page_img_animal_back_btn);
        imgForwardPage = (ImageView) findViewById(R.id.practise_page_img_animal_forward_btn);
        imgShuffle = (ImageView) findViewById(R.id.practise_page_img_animal_shuffle_btn);
        imgRingtone = (ImageView) findViewById(R.id.practise_page_img_animal_set_ringtone_btn);
        imgAnimalImage = (ImageView) findViewById(R.id.practise_page_img_animal);
        textAnimalName = (TextView) findViewById(R.id.practise_page_animal_text);
        imgFavourite = (ImageView) findViewById(R.id.practise_page_img_favourite);
        imgMakeSound = (ImageView) findViewById(R.id.practise_page_img_make_sound);
        adView = (AdView) findViewById(R.id.practise_page_ad);



        zoomOut = AnimationUtils.loadAnimation(this, R.anim.image_zoom_out);
        imgAnimalImage.setAnimation(zoomOut);

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                mTTS.setLanguage(Locale.US);
            }
        });

        mMediaPlayer = new MediaPlayer();

        Log.d("ddd", "object: " + imgAnimalImage + " ");
        Log.d("ddd", "object: " + mSize + " ");
        Log.d("ddd", "object: mediaPlayer"  + " " + mMediaPlayer);

        loadPage();

        loadAd();
    }

    private void loadAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }


    public void speakAnimalName(View v){
        mSpeakAnimalName();
    }
    private void mSpeakAnimalName(){
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
        if(mTTS.isSpeaking()) {
            mTTS.stop();
        }
        mTTS.speak(allAnimals.get(mPosition).getName(), TextToSpeech.QUEUE_ADD, null);
    }

    public void setUnsetFavourite(View v){

    }

    public void animalMakeSound(View v){
        mAnimalMakeSound();
    }
    private void mAnimalMakeSound(){
        if (mMediaPlayer.isPlaying())
            mMediaPlayer.stop();

        String animalName = allAnimals.get(mPosition).getName();
        int identifier = getResources().getIdentifier(animalName, "raw", "in.blogspot.androidoupsolving.animalsvoiceenglish");
        mMediaPlayer = MediaPlayer.create(this, identifier);
        mMediaPlayer.start();
    }

    public void prevPage(View v){
        Log.d("ddd", "prevPage");
        mPosition = mPosition + mSize - 1;
        mPosition %= mSize;
        loadPage();
    }

    public void shuffleAnimals(View v){
        Random rnd = new Random();
        Animal temp = null;
        for(int i = 0; i < mSize; i++){
            int index = rnd.nextInt(mSize);
            temp = allAnimals.get(i);
            allAnimals.set(i, allAnimals.get(index));
            allAnimals.set(index, temp);
        }
    }

    public void setRingtone(View v){
        //http://stackoverflow.com/questions/1986756/setting-ringtone-in-android
    }

    public void forwardPage(View v){
        Log.d("ddd", "forwardpage");
        mPosition = mPosition + 1;
        mPosition %= mSize;
        loadPage();
    }

    private void loadPage(){
        String animalName = allAnimals.get(mPosition).getName();
        int identifier = getResources().getIdentifier(animalName, "drawable", "in.blogspot.androidoupsolving.animalsvoiceenglish");
        Log.d("ddd", animalName + " : " + identifier + " : " + mPosition);
        imgAnimalImage.setImageResource(identifier); //if problem exists use setImageBitmap from getResources
        textAnimalName.setText(animalName);

        animateImage();


    }

    private void animateImage(){
        imgAnimalImage.startAnimation(zoomOut);
    }
}
