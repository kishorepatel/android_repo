package in.blogspot.androidoupsolving.animalsvoiceenglish;


import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.Set;

public class Utility {
    static ArrayList<Animal> allAnimals;

    public static ArrayList<Animal> getAllAnimals(Context context) {
        init();
        return allAnimals;
    }

    private static void init() {
        allAnimals = new ArrayList<>();

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
    }


    public static Set<String> getFavourites(Context context) {
        return null;
    }


}

