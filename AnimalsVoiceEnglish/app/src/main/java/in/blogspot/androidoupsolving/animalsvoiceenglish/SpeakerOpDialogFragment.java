package in.blogspot.androidoupsolving.animalsvoiceenglish;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;


public class SpeakerOpDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.speaker_options)
                .setSingleChoiceItems(R.array.speaker_option_values, Utility.getSpeakerOption(getActivity()), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utility.setSpeakerOption(getActivity(), which);

                    }
                })
                .setCancelable(true);


        return builder.create();
    }
}
