package in.blogspot.androidoupsolving.animalsvoiceenglish;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainFragment extends Fragment implements View.OnClickListener {
    public static final String IS_FAV_TAG = "Fav_list";
    Button btnPractise;
    Button btnTest;

    ImageButton btnSpeakerOptions;
    ImageButton btnSettings;
    ImageButton btnFavourites;
    ImageButton imgAppStore;

    FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("ddd", "in the mainFramgnet oncreate");
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View rootView = inflater.inflate(R.layout.activity_main, container, false);
        Log.d("ddd", "mainFramgnet oncreate: " + rootView);
        btnPractise = (Button) rootView.findViewById(R.id.index_page_btn_practise);
        btnTest = (Button) rootView.findViewById(R.id.index_page_btn_test);
        btnSpeakerOptions = (ImageButton) rootView.findViewById(R.id.index_page_btn_speaker_options);
        btnSettings = (ImageButton) rootView.findViewById(R.id.index_page_btn_settings);
        btnFavourites = (ImageButton) rootView.findViewById(R.id.index_page_btn_favourite);
        imgAppStore = (ImageButton) rootView.findViewById(R.id.index_page_img_app_store_page);


        btnPractise.setOnClickListener(this);
        btnTest.setOnClickListener(this);
        btnSpeakerOptions.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnFavourites.setOnClickListener(this);
        imgAppStore.setOnClickListener(this);

        fragmentManager = getActivity().getSupportFragmentManager();

        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.index_page_btn_practise: {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(IS_FAV_TAG, false);
                startActivity(intent);
                break;
            }
            case R.id.index_page_btn_test: {
                Log.d("ddd", "settings");
                startActivity(new Intent(getActivity(), TestActivity.class));
                break;
            }
            case R.id.index_page_btn_favourite: {
                Log.d("ddd", "favourites");
                if (Utility.getFavouriteCount(getActivity()) > 0) {
                    Intent intent = new Intent(getActivity(), TestActivity.class);
                    intent.putExtra(IS_FAV_TAG, true);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Favourite is empty", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.index_page_btn_settings: {
                Log.d("ddd", "settings");
                SettingDialogFragment sdf = new SettingDialogFragment();
                sdf.show(fragmentManager, "Calling from mainfragment");
                break;
            }
            case R.id.index_page_btn_speaker_options: {
                Log.d("ddd", "settings");
                SpeakerOpDialogFragment sodf = new SpeakerOpDialogFragment();
                sodf.show(fragmentManager, "Calling from mainfragment");
                break;
            }
            case R.id.index_page_img_app_store_page: {
                Utility.goToAppStore(getActivity());
                break;
            }
            default: {
                //don nothing
            }
        }
    }

}
