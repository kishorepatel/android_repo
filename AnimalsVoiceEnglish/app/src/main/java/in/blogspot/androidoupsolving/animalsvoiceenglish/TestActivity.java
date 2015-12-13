package in.blogspot.androidoupsolving.animalsvoiceenglish;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;


public class TestActivity extends AppCompatActivity {
    public int TOTAL_QUESTION = 10;

    ArrayList<Animal> quizAnimalsList;
    int mPosition;
    int mListSize;
    Button btnOption1;
    Button btnOption2;
    Button btnOption3;
    Button btnOption4;
    ImageView quizImage;
    ImageButton quizSoundBtn;
    ImageButton quizPrevBtn;
    ImageButton quizNextBtn;
    TextView txtPageNumber;

    AdView adView;

    Animation zoomOut;
    TextToSpeech mTTS;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_single_item);

        quizAnimalsList = Utility.getQuizAnimalCandidates(this, TOTAL_QUESTION);
        mPosition = 0;
        mListSize = quizAnimalsList.size();

        btnOption1 = (Button) findViewById(R.id.quiz_option_1);
        btnOption2 = (Button) findViewById(R.id.quiz_option_2);
        btnOption3 = (Button) findViewById(R.id.quiz_option_3);
        btnOption4 = (Button) findViewById(R.id.quiz_option_4);
        quizImage = (ImageView) findViewById(R.id.quiz_img_question);
        quizSoundBtn = (ImageButton) findViewById(R.id.quiz_sound_btn);
        quizPrevBtn = (ImageButton) findViewById(R.id.quiz_img_back_btn);
        quizNextBtn = (ImageButton) findViewById(R.id.quiz_img_forward_btn);
        txtPageNumber = (TextView) findViewById(R.id.quiz_text_page_no);
        adView = (AdView) findViewById(R.id.quiz_ad_view);

        //ZOOM IMAGE
        zoomOut = AnimationUtils.loadAnimation(this, R.anim.image_zoom_out);
        quizImage.setAnimation(zoomOut);

        //TEXT TO SPEECH
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                mTTS.setLanguage(Locale.US);
            }
        });

        //ANIMAL SOUND MEDIA
        mediaPlayer = new MediaPlayer();

        //---image fling
        quizImage.setOnTouchListener(new GestureFlingDetector(this) {
            @Override
            public void onSwipeRight() {
                gotoPrevPage();
            }

            @Override
            public void onSwipeLeft() {
                gotoNextPage();
            }
        });


        loadPage();
        loadAd();
    }

    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }


    public void handlerQuizBtnOption1(View v) {
        String btnText = btnOption1.getText().toString();
        if (btnText.equals(quizAnimalsList.get(mPosition).getName())) {
            btnOption1.setBackground(getResources().getDrawable(R.drawable.right_button));
            correctToast(btnText);
        } else {
            btnOption1.setBackground(getResources().getDrawable(R.drawable.wrong_button));
            wrongToast(btnText);
        }
    }

    public void handlerQuizBtnOption2(View v) {
        String btnText = btnOption2.getText().toString();
        if (btnText.equals(quizAnimalsList.get(mPosition).getName())) {
            btnOption2.setBackground(getResources().getDrawable(R.drawable.right_button));
            correctToast(btnText);
        } else {
            btnOption2.setBackground(getResources().getDrawable(R.drawable.wrong_button));
            wrongToast(btnText);
        }
    }

    public void handlerQuizBtnOption3(View v) {
        String btnText = btnOption3.getText().toString();
        if (btnText.equals(quizAnimalsList.get(mPosition).getName())) {
            btnOption3.setBackground(getResources().getDrawable(R.drawable.right_button));
            correctToast(btnText);
        } else {
            btnOption3.setBackground(getResources().getDrawable(R.drawable.wrong_button));
            wrongToast(btnText);
        }
    }

    public void handlerQuizBtnOption4(View v) {
        String btnText = btnOption4.getText().toString();
        if (btnText.equals(quizAnimalsList.get(mPosition).getName())) {
            btnOption4.setBackground(getResources().getDrawable(R.drawable.right_button));
            correctToast(btnText);
        } else {
            btnOption4.setBackground(getResources().getDrawable(R.drawable.wrong_button));
            wrongToast(btnText);
        }
    }

    public void handlerQuizAnimalSound(View v) {
        if (mTTS.isSpeaking())
            mTTS.stop();
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();

        Log.d("ddd", "quiz sound clicked");
        String animalName = quizAnimalsList.get(mPosition).getName();
        int identifier = getResources().getIdentifier(animalName, "raw", "in.blogspot.androidoupsolving.animalsvoiceenglish");
        mediaPlayer = MediaPlayer.create(this, identifier);
        mediaPlayer.start();
    }

    public void handlerQuizPrevBtn(View v) {
        gotoPrevPage();
    }

    private void gotoPrevPage() {
        if (mPosition == 0) {
            Toast.makeText(this, "Cannot go backward", Toast.LENGTH_SHORT).show();
            speakMessage("Cannot go backward");
        } else {
            mPosition = (mPosition - 1 + mListSize) % mListSize;
            loadPage();
        }
    }


    public void handlerQuizNextBtn(View v) {
        gotoNextPage();
    }

    private void gotoNextPage() {
        if (mPosition + 1 >= quizAnimalsList.size()) {
            Toast.makeText(this, "Quiz Ended. Cannot go forward.", Toast.LENGTH_SHORT).show();
            speakMessage("Quiz Ended. Cannot go forward.");
        } else {
            mPosition = (mPosition + 1) % mListSize;
            loadPage();
        }
    }


    private void loadPage() {
        String animalName = quizAnimalsList.get(mPosition).getName();
        int identifier = getResources().getIdentifier(animalName, "drawable", "in.blogspot.androidoupsolving.animalsvoiceenglish");
        quizImage.setImageResource(identifier);
        txtPageNumber.setText((mPosition + 1) + "/" + (mListSize));
        shuffleAndSetOptions(quizAnimalsList.get(mPosition).getChoices());
    }

    private void shuffleAndSetOptions(ArrayList<String> op) {

        //----------TODO------------
        //ALSO SET BUTTON COLORS TO NORMAL
        btnOption1.setBackground(getResources().getDrawable(R.drawable.normal_button));
        btnOption2.setBackground(getResources().getDrawable(R.drawable.normal_button));
        btnOption3.setBackground(getResources().getDrawable(R.drawable.normal_button));
        btnOption4.setBackground(getResources().getDrawable(R.drawable.normal_button));


        Random rnd = new Random();
        String tmp = null;
        for (int i = 0; i < 4; i++) {
            int index = rnd.nextInt(4);
            tmp = op.get(i);
            op.set(i, op.get(index));
            op.set(index, tmp);
        }

        //NOTE: index is zero based
        btnOption1.setText(op.get(0));
        btnOption2.setText(op.get(1));
        btnOption3.setText(op.get(2));
        btnOption4.setText(op.get(3));

    }

    private void correctToast(String animalName) {
        String msg = makeFirstLetterCapital(animalName) + " is correct";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        speakMessage(msg);
    }

    private void wrongToast(String animalName) {
        String msg = makeFirstLetterCapital(animalName) + " is wrong";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        speakMessage(msg);
    }

    private void speakMessage(String msg) {
        if (mTTS.isSpeaking())
            mTTS.stop();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

        mTTS.speak(msg, TextToSpeech.QUEUE_ADD, null);
    }

    private String makeFirstLetterCapital(String animalName) {
        if (animalName != null && animalName.length() > 1) {
            return Character.toUpperCase(animalName.charAt(0)) + animalName.substring(1);
        } else {
            return animalName;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTTS.shutdown();
        mediaPlayer.release();
    }
}
