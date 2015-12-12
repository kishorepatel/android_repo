package in.blogspot.androidoupsolving.animalsvoiceenglish;

import android.app.Fragment;
import android.content.res.Resources;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.Set;


public class DetailActivity extends AppCompatActivity {
    ArrayList<Animal> allAnimals;
    Set<String> mFavourites;
    public int mPosition = 0;
    public int mSize;
    public int mSpeechOption;

    TextToSpeech mTTS;
    MediaPlayer mMediaPlayer;

    ImageView imgPrevPage;
    ImageView imgForwardPage;
    ImageView imgShuffle;
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

        boolean isFav = getIntent().getBooleanExtra(MainFragment.IS_FAV_TAG, false);

        allAnimals = Utility.getAllAnimals(this, isFav);
        mFavourites = Utility.getFavourites(this);
        mSpeechOption = Utility.getSpeakerOption(this);
        mSize = allAnimals.size();

        setContentView(R.layout.single_item);

        imgPrevPage = (ImageView) findViewById(R.id.practise_page_img_animal_back_btn);
        imgForwardPage = (ImageView) findViewById(R.id.practise_page_img_animal_forward_btn);
        imgShuffle = (ImageView) findViewById(R.id.practise_page_img_animal_shuffle_btn);
        imgAnimalImage = (ImageView) findViewById(R.id.practise_page_img_animal);
        textAnimalName = (TextView) findViewById(R.id.practise_page_animal_text);
        imgFavourite = (ImageView) findViewById(R.id.practise_page_img_favourite);
        imgMakeSound = (ImageView) findViewById(R.id.practise_page_img_make_sound);
        adView = (AdView) findViewById(R.id.practise_page_ad);

        //ZOOM
        zoomOut = AnimationUtils.loadAnimation(this, R.anim.image_zoom_out);
        imgAnimalImage.setAnimation(zoomOut);

        //TEXT TO SPEECH
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                mTTS.setLanguage(Locale.US);
            }
        });

        mTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                if (utteranceId != null)
                    mAnimalMakeSound();
            }

            @Override
            public void onError(String utteranceId) {

            }
        });
        //MEDIAPLAYER
        mMediaPlayer = new MediaPlayer();

        //SWIPE OF IMAGES
        imgAnimalImage.setOnTouchListener(new GestureFlingDetector(this){
            @Override
            public void onSwipeRight() {
                gotoPrevPage();
            }

            @Override
            public void onSwipeLeft() {
                gotoForwardPage();
            }
        });


        //LOADING PAGE
        loadPage();
        loadAd();
    }

    private void loadAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

//--------------ANIMAL NAME SOUND-----------------------------
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


    //--------ANIMAL SPEECH SOUND---------------------------
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

    private void customSpeakAfterName(){
        if(mMediaPlayer.isPlaying())
            mMediaPlayer.stop();

        String animalName = allAnimals.get(mPosition).getName();
        int identifier = getResources().getIdentifier(animalName, "raw", "in.blogspot.androidoupsolving.animalsvoiceenglish");
        mMediaPlayer = MediaPlayer.create(this, identifier);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mSpeakAnimalName();
            }
        });

        mMediaPlayer.start();
    }
    ///---------------------------------PREV_PAGE, SHUFFLE, NEXT_PAGE
    public void prevPage(View v){
        gotoPrevPage();
    }
    public void gotoPrevPage(){
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


    public void forwardPage(View v){
       gotoForwardPage();
    }
    public void gotoForwardPage(){
        Log.d("ddd", "forwardpage");
        mPosition = mPosition + 1;
        mPosition %= mSize;
        loadPage();
    }

    ///-------------------------LOAD PAGE--------------------
    private void loadPage(){
        String animalName = allAnimals.get(mPosition).getName();
        int identifier = getResources().getIdentifier(animalName, "drawable", "in.blogspot.androidoupsolving.animalsvoiceenglish");
        Log.d("ddd", animalName + " : " + identifier + " : " + mPosition);
        imgAnimalImage.setImageResource(identifier); //if problem exists use setImageBitmap from getResources
        textAnimalName.setText(makeFirstLetterCapital(animalName));

        imgAnimalImage.startAnimation(zoomOut);

        if(isFavourite(animalName)){
            imgFavourite.setImageResource(R.mipmap.ic_star_black_36dp);
        }

        switch(mSpeechOption){
            case Utility.SPEAK_ONLY_NAME:{
                mSpeakAnimalName();
                break;
            }
            case Utility.SPEAK_ONLY_SOUND:{
                mAnimalMakeSound();
                break;
            }
            case Utility.SPEAK_NAME_FIRST:{
                HashMap<String, String> myHashParam = new HashMap<>();
                myHashParam.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, Utility.SPEAK_NAME_FIRST+"");
                mTTS.speak(animalName, TextToSpeech.QUEUE_ADD, myHashParam);
                break;
            }
            case Utility.SPEAK_NAME_LAST:{
                customSpeakAfterName();
                break;
            }
            default:{
                //do nothing
            }
        }




    }


    //-------------------FAVOURITE----------
    private boolean isFavourite(String animalName){
        if(mFavourites != null && mFavourites.contains(animalName)){
            return true;
        }
        else{
            return false;
        }
    }
    public void setUnsetFavourite(View v){
        String animalName = allAnimals.get(mPosition).getName();
        if(isFavourite(animalName)){
            removeFromFavourite();
        }
        else{
            addInFavourite();
        }
    }

    private void removeFromFavourite(){

    }

    private void addInFavourite(){

    }

//-------------------------------------------

    private String makeFirstLetterCapital(String animalName){
        if(animalName != null && animalName.length() > 1){
            return Character.toUpperCase(animalName.charAt(0)) + animalName.substring(1);
        }
        else{
            return animalName;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTTS.shutdown();
        mMediaPlayer.release();
    }
}
