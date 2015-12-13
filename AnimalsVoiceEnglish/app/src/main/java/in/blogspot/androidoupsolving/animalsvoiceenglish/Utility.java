package in.blogspot.androidoupsolving.animalsvoiceenglish;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Utility {
    public static final String favFile = "androidoupsolvingFAV";

    public static final String prefFileName = "androidoupsolvingPREF";
    public static final String prefSpeakerOptionKey = "androidoupsolvingSO";
    public static final int SPEAK_ONLY_NAME = 0;
    public static final int SPEAK_ONLY_SOUND = 1;
    public static final int SPEAK_NAME_FIRST = 2;
    public static final int SPEAK_NAME_LAST = 3;


    public static Set<String> getFavourites(final Context context) {
        Set<String> curFav = new HashSet<>();

        BufferedReader reader = null;
        try {
            File file = new File(context.getFilesDir(), Utility.favFile);
            if (!file.exists())
                file.createNewFile();
            reader = new BufferedReader(new InputStreamReader(context.openFileInput(Utility.favFile)));

            String fav = "";
            while ((fav = reader.readLine()) != null) {
                curFav.add(fav);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return curFav;
    }

    public static int getFavouriteCount(Context context) {
        Set<String> fav = getFavourites(context);
        if (fav == null || fav.size() == 0)
            return 0;
        else
            return fav.size();
    }

    public static void removeFromFavourite(Context context, String name) {
        Set<String> whatIsInFile = getFavourites(context);
        if (whatIsInFile == null || !whatIsInFile.contains(name)) {
            return;
        }

        whatIsInFile.remove(name);
        createNewFileAndAddAll(context, whatIsInFile); //overwrite all the contents in file

        getFavourites(context);
    }

    public static void writeToFavourite(Context context, String name) {
        Set<String> whatIsInFile = getFavourites(context);
        if (whatIsInFile != null && whatIsInFile.contains(name)) {
            return;
        }

        whatIsInFile.add(name);
        createNewFileAndAddAll(context, whatIsInFile); //overwrite all the contents in file

        getFavourites(context);
    }

    private static void createNewFileAndAddAll(final Context context, final Set<String> favSet) {
        //basically overwriteing everything in the file

        BufferedWriter writer = null;
        try {
            File file = new File(context.getFilesDir(), Utility.favFile);
            file.createNewFile();

            writer = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(Utility.favFile, Context.MODE_PRIVATE)));


            if (favSet == null || favSet.size() == 0) {
                return;
            }

            for (String s : favSet) {
                writer.write(s);
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public static ArrayList<Animal> getAllAnimals(Context context, boolean isFav) {
        ArrayList<Animal> allAnimals = init();


        if (isFav) {
            ArrayList<Animal> curRetList = new ArrayList<>();
            Set<String> favSet = getFavourites(context);
            for (int i = 0; i < allAnimals.size(); i++) {
                if (favSet.contains(allAnimals.get(i).getName())) {
                    curRetList.add(allAnimals.get(i));
                }
            }
            return curRetList;
        } else {
            return allAnimals;
        }
    }

    private static ArrayList<Animal> init() {
        ArrayList<Animal> allAnimals = new ArrayList<>();

        ArrayList<String> options = new ArrayList<>();

        options.add("peacock");
        options.add("tiger");
        options.add("buffalo");
        options.add("cat");
        allAnimals.add(new Animal("peacock", options));

        options = new ArrayList<>();
        options.add("peacock");
        options.add("tiger");
        options.add("buffalo");
        options.add("cat");
        allAnimals.add(new Animal("tiger", options));

        options = new ArrayList<>();
        options.add("peacock");
        options.add("tiger");
        options.add("buffalo");
        options.add("cat");
        allAnimals.add(new Animal("cat", options));

        options = new ArrayList<>();
        options.add("zebra");
        options.add("tiger");
        options.add("buffalo");
        options.add("cat");
        allAnimals.add(new Animal("zebra", options));


        return allAnimals;
    }

    public static void setSpeakerOption(Context context, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(prefSpeakerOptionKey, value).apply();
    }

    public static int getSpeakerOption(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(prefSpeakerOptionKey, SPEAK_ONLY_NAME);
    }

    public static ArrayList<Animal> getQuizAnimalCandidates(Context context, int count) {
        ArrayList<Animal> allAnimals = getAllAnimals(context, false);
        int size = allAnimals.size();

        count = Math.min(count, size);

        Random rnd = new Random();
        Animal temp = null;
        for (int i = 0; i < size; i++) {
            int index = rnd.nextInt(size);
            temp = allAnimals.get(i);
            allAnimals.set(i, allAnimals.get(index));
            allAnimals.set(index, temp);
        }

        ArrayList<Animal> quizCandidates = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            quizCandidates.add(allAnimals.get(i));
        }

        return quizCandidates;
    }

    public static void goToAppStore(Context context) {
        Intent marketIntent = new Intent(Intent.ACTION_VIEW);
        marketIntent.setData(Uri.parse(context.getString(R.string.goto_my_app_play_store_market)));
        if (marketIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(marketIntent);
        } else {
            Intent webAppStoreIntent = new Intent(Intent.ACTION_VIEW);
            webAppStoreIntent.setData(Uri.parse(context.getString(R.string.goto_my_app_play_store_web)));
            if (webAppStoreIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(webAppStoreIntent);
            } else {
                Toast.makeText(context, "Install play store to open", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

