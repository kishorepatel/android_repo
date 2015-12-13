package in.blogspot.androidoupsolving.animalsvoiceenglish;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View rootView = inflater.inflate(R.layout.activity_main, container, false);

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
                startActivity(new Intent(getActivity(), TestActivity.class));
                break;
            }
            case R.id.index_page_btn_favourite: {
                if (Utility.getFavouriteCount(getActivity()) > 0) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra(IS_FAV_TAG, true);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.fav_is_Empty), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.index_page_btn_settings: {
                SettingDialogFragment sdf = new SettingDialogFragment();
                sdf.show(fragmentManager, "Calling from mainfragment");
                break;
            }
            case R.id.index_page_btn_speaker_options: {
                SpeakerOpDialogFragment sodf = new SpeakerOpDialogFragment();
                sodf.show(fragmentManager, "Calling from mainfragment");
                break;
            }
            case R.id.index_page_img_app_store_page: {
                Utility.goToAppStore(getActivity());
                break;
            }
            default: {
                //do nothing
            }
        }
    }

}
