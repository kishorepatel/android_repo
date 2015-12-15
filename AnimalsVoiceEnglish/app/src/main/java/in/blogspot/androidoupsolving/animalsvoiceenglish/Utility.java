package in.blogspot.androidoupsolving.animalsvoiceenglish;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    }

    public static void writeToFavourite(Context context, String name) {
        Set<String> whatIsInFile = getFavourites(context);
        if (whatIsInFile != null && whatIsInFile.contains(name)) {
            return;
        }

        whatIsInFile.add(name);
        createNewFileAndAddAll(context, whatIsInFile); //overwrite all the contents in file
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

    public static void goToAppStore(Context context, boolean thisAPP) {
        if(thisAPP) {
            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
            marketIntent.setData(Uri.parse(context.getString(R.string.goto_my_app_play_store_market_animal)));
            if (marketIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(marketIntent);
            } else {
                Intent webAppStoreIntent = new Intent(Intent.ACTION_VIEW);
                webAppStoreIntent.setData(Uri.parse(context.getString(R.string.goto_my_app_play_store_web_animal)));
                if (webAppStoreIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(webAppStoreIntent);
                } else {
                    Toast.makeText(context, "Install play store to open", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
            marketIntent.setData(Uri.parse(context.getString(R.string.goto_my_app_play_store_market_bird)));
            if (marketIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(marketIntent);
            } else {
                Intent webAppStoreIntent = new Intent(Intent.ACTION_VIEW);
                webAppStoreIntent.setData(Uri.parse(context.getString(R.string.goto_my_app_play_store_web_bird)));
                if (webAppStoreIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(webAppStoreIntent);
                } else {
                    Toast.makeText(context, "Install play store to open", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private static ArrayList<Animal> init() {
        ArrayList<Animal> allAnimals = new ArrayList<>();

        ArrayList<String> options = new ArrayList<>();

        options.add("cow");
        options.add("horse");
        options.add("lion");
        options.add("monkey");
        allAnimals.add(new Animal("horse", options));

        options = new ArrayList<>();
        options.add("tiger");
        options.add("pig");
        options.add("wolf");
        options.add("leopard");
        allAnimals.add(new Animal("tiger", options));

        options = new ArrayList<>();
        options.add("peacock");
        options.add("turtle");
        options.add("koala");
        options.add("chicken");
        allAnimals.add(new Animal("peacock", options));

        options = new ArrayList<>();
        options.add("sheep");
        options.add("panda");
        options.add("dog");
        options.add("cat");
        allAnimals.add(new Animal("panda", options));

        options = new ArrayList<>();
        options.add("gorilla");
        options.add("hippopotamus");
        options.add("elephant");
        options.add("bear");
        allAnimals.add(new Animal("elephant", options));

        //--------------------------------------------------------------------------

        options = new ArrayList<>();
        options.add("dolphin");
        options.add("duck");
        options.add("chicken");
        options.add("turkey");
        allAnimals.add(new Animal("dolphin", options));


        options = new ArrayList<>();
        options.add("fox");
        options.add("camel");
        options.add("deer");
        options.add("antelope");
        allAnimals.add(new Animal("antelope", options));


        options = new ArrayList<>();
        options.add("cow");
        options.add("bear");
        options.add("gorilla");
        options.add("rhinoceros");
        allAnimals.add(new Animal("bear", options));


        options = new ArrayList<>();
        options.add("boar");
        options.add("bison");
        options.add("panda");
        options.add("beaver");
        allAnimals.add(new Animal("boar", options));


        options = new ArrayList<>();
        options.add("tiger");
        options.add("cheetah");
        options.add("cat");
        options.add("fox");
        allAnimals.add(new Animal("cheetah", options));


        options = new ArrayList<>();
        options.add("chicken");
        options.add("peacock");
        options.add("rooster");
        options.add("turkey");
        allAnimals.add(new Animal("chicken", options));


        options = new ArrayList<>();
        options.add("panda");
        options.add("boar");
        options.add("cow");
        options.add("deer");
        allAnimals.add(new Animal("cow", options));


        options = new ArrayList<>();
        options.add("camel");
        options.add("shark");
        options.add("dolphin");
        options.add("crocodile");
        allAnimals.add(new Animal("crocodile", options));


        options = new ArrayList<>();
        options.add("deer");
        options.add("cheetah");
        options.add("antelope");
        options.add("goat");
        allAnimals.add(new Animal("deer", options));

        options = new ArrayList<>();
        options.add("cat");
        options.add("dog");
        options.add("wolf");
        options.add("fox");
        allAnimals.add(new Animal("dog", options));

        options = new ArrayList<>();
        options.add("tiger");
        options.add("sheep");
        options.add("cheetah");
        options.add("lion");
        allAnimals.add(new Animal("lion", options));


        options = new ArrayList<>();
        options.add("duck");
        options.add("crow");
        options.add("seal");
        options.add("peacock");
        allAnimals.add(new Animal("duck", options));


        options = new ArrayList<>();
        options.add("hippopotamus");
        options.add("horse");
        options.add("bison");
        options.add("cow");
        allAnimals.add(new Animal("bison", options));

        options = new ArrayList<>();
        options.add("boar");
        options.add("fox");
        options.add("kangaroo");
        options.add("wolf");
        allAnimals.add(new Animal("fox", options));


        options = new ArrayList<>();
        options.add("rhinoceros");
        options.add("monkey");
        options.add("gorilla");
        options.add("bear");
        allAnimals.add(new Animal("gorilla", options));

        options = new ArrayList<>();
        options.add("hippopotamus");
        options.add("rhinoceros");
        options.add("beaver");
        options.add("elephant");
        allAnimals.add(new Animal("hippopotamus", options));

        options = new ArrayList<>();
        options.add("chicken");
        options.add("rooster");
        options.add("turkey");
        options.add("peacock");
        allAnimals.add(new Animal("turkey", options));

        options = new ArrayList<>();
        options.add("elephant");
        options.add("donkey");
        options.add("cow");
        options.add("camel");
        allAnimals.add(new Animal("camel", options));


        options = new ArrayList<>();
        options.add("tiger");
        options.add("jaguar");
        options.add("lion");
        options.add("cheetah");
        allAnimals.add(new Animal("jaguar", options));

        options = new ArrayList<>();
        options.add("boar");
        options.add("goat");
        options.add("hippopotamus");
        options.add("pig");
        allAnimals.add(new Animal("pig", options));


        options = new ArrayList<>();
        options.add("camel");
        options.add("kangaroo");
        options.add("koala");
        options.add("panda");
        allAnimals.add(new Animal("kangaroo", options));

        options = new ArrayList<>();
        options.add("pig");
        options.add("koala");
        options.add("beaver");
        options.add("lemur");
        allAnimals.add(new Animal("koala", options));

        options = new ArrayList<>();
        options.add("koala");
        options.add("boar");
        options.add("lemur");
        options.add("monkey");
        allAnimals.add(new Animal("lemur", options));


        options = new ArrayList<>();
        options.add("wolf");
        options.add("goat");
        options.add("sheep");
        options.add("zebra");
        allAnimals.add(new Animal("goat", options));


        options = new ArrayList<>();
        options.add("monkey");
        options.add("koala");
        options.add("panda");
        options.add("bear");
        allAnimals.add(new Animal("monkey", options));

        options = new ArrayList<>();
        options.add("dolphin");
        options.add("shark");
        options.add("penguin");
        options.add("orca");
        allAnimals.add(new Animal("orca", options));


        options = new ArrayList<>();
        options.add("duck");
        options.add("camel");
        options.add("koala");
        options.add("frog");
        allAnimals.add(new Animal("frog", options));

        options = new ArrayList<>();
        options.add("cheetah");
        options.add("cat");
        options.add("cow");
        options.add("dog");
        allAnimals.add(new Animal("cat", options));


        options = new ArrayList<>();
        options.add("bison");
        options.add("boar");
        options.add("moose");
        options.add("goat");
        allAnimals.add(new Animal("moose", options));


        options = new ArrayList<>();
        options.add("hippopotamus");
        options.add("gorilla");
        options.add("rhinoceros");
        options.add("beaver");
        allAnimals.add(new Animal("rhinoceros", options));


        options = new ArrayList<>();
        options.add("tiger");
        options.add("camel");
        options.add("deer");
        options.add("zebra");
        allAnimals.add(new Animal("zebra", options));

        options = new ArrayList<>();
        options.add("rooster");
        options.add("chicken");
        options.add("turkey");
        options.add("duck");
        allAnimals.add(new Animal("rooster", options));

        options = new ArrayList<>();
        options.add("seal");
        options.add("shark");
        options.add("dolphin");
        options.add("turtle");
        allAnimals.add(new Animal("seal", options));


        options = new ArrayList<>();
        options.add("goat");
        options.add("camel");
        options.add("pig");
        options.add("sheep");
        allAnimals.add(new Animal("sheep", options));


        options = new ArrayList<>();
        options.add("boar");
        options.add("buffalo");
        options.add("cow");
        options.add("donkey");
        allAnimals.add(new Animal("buffalo", options));

        options = new ArrayList<>();
        options.add("eagle");
        options.add("crocodile");
        options.add("snake");
        options.add("squirrel");
        allAnimals.add(new Animal("snake", options));


        options = new ArrayList<>();
        options.add("turtle");
        options.add("crocodile");
        options.add("bear");
        options.add("seal");
        allAnimals.add(new Animal("turtle", options));

        options = new ArrayList<>();
        options.add("fox");
        options.add("dog");
        options.add("wolf");
        options.add("sheep");
        allAnimals.add(new Animal("wolf", options));


        options = new ArrayList<>();
        options.add("eagle");
        options.add("turkey");
        options.add("duck");
        options.add("crow");
        allAnimals.add(new Animal("eagle", options));

        options = new ArrayList<>();
        options.add("tiger");
        options.add("donkey");
        options.add("cheetah");
        options.add("panther");
        allAnimals.add(new Animal("panther", options));

        options = new ArrayList<>();
        options.add("buffalo");
        options.add("boar");
        options.add("camel");
        options.add("warthog");
        allAnimals.add(new Animal("warthog", options));

        options = new ArrayList<>();
        options.add("beaver");
        options.add("boar");
        options.add("panda");
        options.add("turtle");
        allAnimals.add(new Animal("beaver", options));

        options = new ArrayList<>();
        options.add("duck");
        options.add("eagle");
        options.add("crow");
        options.add("seal");
        allAnimals.add(new Animal("crow", options));

        options = new ArrayList<>();
        options.add("penguin");
        options.add("shark");
        options.add("duck");
        options.add("seal");
        allAnimals.add(new Animal("penguin", options));

        options = new ArrayList<>();
        options.add("snake");
        options.add("beaver");
        options.add("squirrel");
        options.add("lemur");
        allAnimals.add(new Animal("squirrel", options));

        options = new ArrayList<>();
        options.add("goat");
        options.add("dog");
        options.add("donkey");
        options.add("horse");
        allAnimals.add(new Animal("donkey", options));


        options = new ArrayList<>();
        options.add("dolphin");
        options.add("shark");
        options.add("duck");
        options.add("moose");
        allAnimals.add(new Animal("shark", options));


        return allAnimals;
    }

}

