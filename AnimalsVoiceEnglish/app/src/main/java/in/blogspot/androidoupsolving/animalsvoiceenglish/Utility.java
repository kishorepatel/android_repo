package in.blogspot.androidoupsolving.animalsvoiceenglish;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class Utility {
    public static final String prefFileName = "androidoupsolvingPREF";
    public static final String prefSpeakerOptionKey = "androidoupsolvingSO";
    public static final int SPEAK_ONLY_NAME = 0;
    public static final int SPEAK_ONLY_SOUND = 1;
    public static final int SPEAK_NAME_FIRST = 2;
    public static final int SPEAK_NAME_LAST = 3;


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

        options.clear();
        options.add("peacock");
        options.add("tiger");
        options.add("buffalo");
        options.add("cat");
        allAnimals.add(new Animal("tiger", options));

        options.clear();
        options.add("peacock");
        options.add("tiger");
        options.add("buffalo");
        options.add("cat");
        allAnimals.add(new Animal("cat", options));

        options.clear();
        options.add("zebra");
        options.add("tiger");
        options.add("buffalo");
        options.add("cat");
        allAnimals.add(new Animal("zebra", options));


        return allAnimals;
    }


    public static Set<String> getFavourites(Context context) {
        return null;
    }

    public static int getFavouriteCount(Context context) {
        Set<String> fav = getFavourites(context);
        if (fav == null || fav.size() == 0)
            return 0;
        else
            return fav.size();
    }

    public static void setSpeakerOption(Context context, int value) {
        Log.d("ddd", "Util:SpeakerOp: " + value);
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

    }
}

