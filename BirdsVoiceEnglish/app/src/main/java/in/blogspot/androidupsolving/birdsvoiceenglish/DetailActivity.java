package in.blogspot.androidupsolving.birdsvoiceenglish;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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

    int counter = 0;

    TextToSpeech mTTS;
    MediaPlayer mMediaPlayer;

    ImageButton imgPrevPage;
    ImageButton imgForwardPage;
    ImageButton imgShuffle;
    ImageView imgAnimalImage;
    TextView textAnimalName;
    ImageButton imgFavourite;
    ImageButton imgMakeSound;

    AdView adView;

    Animation zoomOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ddd", "oncreate");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.single_item);

        boolean isFav = getIntent().getBooleanExtra(MainFragment.IS_FAV_TAG, false);

        allAnimals = Utility.getAllAnimals(this, isFav);
        mFavourites = Utility.getFavourites(this);
        mSpeechOption = Utility.getSpeakerOption(this);
        mSize = allAnimals.size();



        imgPrevPage = (ImageButton) findViewById(R.id.practise_page_img_animal_back_btn);
        imgForwardPage = (ImageButton) findViewById(R.id.practise_page_img_animal_forward_btn);
        imgShuffle = (ImageButton) findViewById(R.id.practise_page_img_animal_shuffle_btn);
        imgAnimalImage = (ImageView) findViewById(R.id.practise_page_img_animal);
        textAnimalName = (TextView) findViewById(R.id.practise_page_animal_text);
        imgFavourite = (ImageButton) findViewById(R.id.practise_page_img_favourite);
        imgMakeSound = (ImageButton) findViewById(R.id.practise_page_img_make_sound);
        adView = (AdView) findViewById(R.id.practise_page_ad);

        //ZOOM
        zoomOut = AnimationUtils.loadAnimation(this, R.anim.image_zoom_out);
        imgAnimalImage.setAnimation(zoomOut);

        //MEDIAPLAYER
        mMediaPlayer = new MediaPlayer();

        //TEXT TO SPEECH
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                mTTS.setLanguage(Locale.US);
                //*****************************************
                //*****************************************
                //MOST IMPORTANT LINE IS BELOW ONE
                //TTS will come to initialize and this is when or after which you have to start speaking
                //http://stackoverflow.com/questions/14563098/text-to-speech-not-working-as-expected
                //*****************************************
                loadPage();
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


        //SWIPE OF IMAGES
        imgAnimalImage.setOnTouchListener(new GestureFlingDetector(this) {
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

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    //--------------ANIMAL NAME SOUND-----------------------------
    public void speakAnimalName(View v) {
        mSpeakAnimalName();
    }

    private void mSpeakAnimalName() {
        stopPlayers();
        mTTS.speak(allAnimals.get(mPosition).getName(), TextToSpeech.QUEUE_ADD, null);
    }


    //--------ANIMAL SPEECH SOUND---------------------------
    public void animalMakeSound(View v) {
        mAnimalMakeSound();
    }

    private void mAnimalMakeSound() {
        stopPlayers();

        String animalName = allAnimals.get(mPosition).getName();
        int identifier = getResources().getIdentifier(animalName, getString(R.string.raw), getString(R.string.package_name));
        mMediaPlayer = MediaPlayer.create(this, identifier);
        mMediaPlayer.start();
    }

    private void customSpeakAfterName() {
        stopPlayers();

        String animalName = allAnimals.get(mPosition).getName();
        int identifier = getResources().getIdentifier(animalName, getString(R.string.raw), getString(R.string.package_name));
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
    public void prevPage(View v) {
        gotoPrevPage();
    }

    public void gotoPrevPage() {
        stopPlayers();
        mPosition = mPosition + mSize - 1;
        mPosition %= mSize;
        loadPage();
    }

    public void shuffleAnimals(View v) {
        Random rnd = new Random();
        Animal temp = null;
        for (int i = 0; i < mSize; i++) {
            int index = rnd.nextInt(mSize);
            temp = allAnimals.get(i);
            allAnimals.set(i, allAnimals.get(index));
            allAnimals.set(index, temp);
        }
        Toast.makeText(this, "Shuffled", Toast.LENGTH_SHORT).show();
    }


    public void forwardPage(View v) {
        gotoForwardPage();
    }

    public void gotoForwardPage() {
        stopPlayers();
        mPosition = mPosition + 1;
        mPosition %= mSize;
        loadPage();
    }

    ///-------------------------LOAD PAGE--------------------
    private void loadPage() {
        String animalName = allAnimals.get(mPosition).getName();
        int identifier = getResources().getIdentifier(animalName, getString(R.string.drawable), getString(R.string.package_name));

        imgAnimalImage.setImageResource(identifier); //if problem exists use setImageBitmap from getResources
        textAnimalName.setText(Utility.makeGoodStringName(this, animalName));

        imgAnimalImage.startAnimation(zoomOut);

        imgFavourite.setImageResource(R.mipmap.ic_star_border_black_48dp);
        if (isFavourite(animalName)) {
            imgFavourite.setImageResource(R.mipmap.ic_star_black_48dp);
        }

        switch (mSpeechOption) {
            case Utility.SPEAK_ONLY_NAME: {
                mSpeakAnimalName();
                break;
            }
            case Utility.SPEAK_ONLY_SOUND: {
                mAnimalMakeSound();
                break;
            }
            case Utility.SPEAK_NAME_FIRST: {

                HashMap<String, String> myHashParam = new HashMap<>();
                myHashParam.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, Utility.SPEAK_NAME_FIRST + "");
                mTTS.speak(animalName, TextToSpeech.QUEUE_ADD, myHashParam);

                break;
            }
            case Utility.SPEAK_NAME_LAST: {
                customSpeakAfterName();
                break;
            }
            default: {
                //do nothing
            }
        }

    }


    //-------------------FAVOURITE----------
    private boolean isFavourite(String animalName) {
        if (mFavourites != null && mFavourites.contains(animalName)) {
            return true;
        } else {
            return false;
        }
    }

    public void setUnsetFavourite(View v) {
        String animalName = allAnimals.get(mPosition).getName();
        if (isFavourite(animalName)) {
            mFavourites.remove(animalName);
            imgFavourite.setImageResource(R.mipmap.ic_star_border_black_48dp);
            removeFromFavourite(animalName);
        } else {
            mFavourites.add(animalName);
            imgFavourite.setImageResource(R.mipmap.ic_star_black_48dp);
            addInFavourite(animalName);
        }
    }

    private void removeFromFavourite(String name) {
        Utility.removeFromFavourite(this, name);
    }

    private void addInFavourite(String name) {
        Utility.writeToFavourite(this, name);
    }

//-------------------------------------------

    private String makeFirstLetterCapital(String animalName) {
        if (animalName != null && animalName.length() > 1) {
            return Character.toUpperCase(animalName.charAt(0)) + animalName.substring(1);
        } else {
            return animalName;
        }
    }

    private void stopPlayers() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        if (mTTS.isSpeaking()) {
            mTTS.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTTS.shutdown();
        mMediaPlayer.release();
    }
}
