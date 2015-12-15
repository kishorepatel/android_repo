package in.blogspot.androidupsolving.birdsvoiceenglish;


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
    public static final String favFile = "androidoupsolvingFAVbird";

    public static final String prefFileName = "androidoupsolvingPREFbird";
    public static final String prefSpeakerOptionKey = "androidoupsolvingSObird";
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
        if (thisAPP) {
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
        } else {
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
    }


    private static ArrayList<Animal> init() {
        ArrayList<Animal> allAnimals = new ArrayList<>();

        ArrayList<String> options = new ArrayList<>();

        options.add("parrot");
        options.add("woodpecker");
        options.add("sparrow");
        options.add("peacock");
        allAnimals.add(new Animal("peacock", options));

        options = new ArrayList<>();
        options.add("puffin");
        options.add("swallow");
        options.add("cockatoo");
        options.add("toucan");
        allAnimals.add(new Animal("cockatoo", options));

        options = new ArrayList<>();
        options.add("finch");
        options.add("bee9eater");
        options.add("roller");
        options.add("kingfisher");
        allAnimals.add(new Animal("kingfisher", options));

        options = new ArrayList<>();
        options.add("turkey");
        options.add("vulture");
        options.add("owl");
        options.add("bat");
        allAnimals.add(new Animal("bat", options));

        options = new ArrayList<>();
        options.add("robin");
        options.add("roller");
        options.add("finch");
        options.add("swift");
        allAnimals.add(new Animal("finch", options));

        options = new ArrayList<>();
        options.add("hoopoe");
        options.add("cockatoo");
        options.add("toucan");
        options.add("parrot");
        allAnimals.add(new Animal("hoopoe", options));

        options = new ArrayList<>();
        options.add("swift");
        options.add("swallow");
        options.add("gull");
        options.add("albatross");
        allAnimals.add(new Animal("swallow", options));

        options = new ArrayList<>();
        options.add("pelican");
        options.add("heron");
        options.add("swan");
        options.add("cormorant");
        allAnimals.add(new Animal("swan", options));

        options = new ArrayList<>();
        options.add("ibis");
        options.add("heron");
        options.add("albatross");
        options.add("stork");
        allAnimals.add(new Animal("stork", options));

        options = new ArrayList<>();
        options.add("pigeon");
        options.add("sparrow");
        options.add("swallow");
        options.add("sandpiper");
        allAnimals.add(new Animal("pigeon", options));

        options = new ArrayList<>();
        options.add("sparrow");
        options.add("humming9bird");
        options.add("kingfisher");
        options.add("crow");
        allAnimals.add(new Animal("humming9bird", options));

        options = new ArrayList<>();
        options.add("bat");
        options.add("fregatidae");
        options.add("turkey");
        options.add("cormorant");
        allAnimals.add(new Animal("fregatidae", options));

        options = new ArrayList<>();
        options.add("ibis");
        options.add("goose");
        options.add("crane");
        options.add("pelican");
        allAnimals.add(new Animal("pelican", options));

        options = new ArrayList<>();
        options.add("parrot");
        options.add("roller");
        options.add("puffin");
        options.add("bee9eater");
        allAnimals.add(new Animal("bee9eater", options));

        options = new ArrayList<>();
        options.add("spoonbill");
        options.add("goose");
        options.add("sparrow");
        options.add("stork");
        allAnimals.add(new Animal("spoonbill", options));

        options = new ArrayList<>();
        options.add("tern");
        options.add("swift");
        options.add("eagle");
        options.add("humming9bird");
        allAnimals.add(new Animal("swift", options));

        options = new ArrayList<>();
        options.add("robin");
        options.add("puffin");
        options.add("sparrow");
        options.add("magpie");
        allAnimals.add(new Animal("sparrow", options));

        options = new ArrayList<>();
        options.add("ibis");
        options.add("cormorant");
        options.add("crane");
        options.add("gull");
        allAnimals.add(new Animal("ibis", options));

        options = new ArrayList<>();
        options.add("toucan");
        options.add("ibis");
        options.add("ostrich");
        options.add("cormorant");
        allAnimals.add(new Animal("cormorant", options));

        options = new ArrayList<>();
        options.add("roller");
        options.add("sandpiper");
        options.add("parrot");
        options.add("woodpecker");
        allAnimals.add(new Animal("sandpiper", options));

        options = new ArrayList<>();
        options.add("toucan");
        options.add("swallow");
        options.add("swift");
        options.add("tern");
        allAnimals.add(new Animal("tern", options));

        options = new ArrayList<>();
        options.add("rhea");
        options.add("toucan");
        options.add("ibis");
        options.add("heron");
        allAnimals.add(new Animal("toucan", options));

        options = new ArrayList<>();
        options.add("vulture");
        options.add("albatros");
        options.add("crane");
        options.add("penguin");
        allAnimals.add(new Animal("penguin", options));

        options = new ArrayList<>();
        options.add("magpie");
        options.add("peacock");
        options.add("pelican");
        options.add("gull");
        allAnimals.add(new Animal("magpie", options));

        options = new ArrayList<>();
        options.add("sparrow");
        options.add("plover");
        options.add("sandpiper");
        options.add("roller");
        allAnimals.add(new Animal("roller", options));

        options = new ArrayList<>();
        options.add("rhea");
        options.add("swan");
        options.add("stork");
        options.add("goose");
        allAnimals.add(new Animal("goose", options));

        options = new ArrayList<>();
        options.add("heron");
        options.add("bat");
        options.add("cormorant");
        options.add("crow");
        allAnimals.add(new Animal("crow", options));

        options = new ArrayList<>();
        options.add("guineafowl");
        options.add("ostrich");
        options.add("vulture");
        options.add("emu");
        allAnimals.add(new Animal("ostrich", options));

        options = new ArrayList<>();
        options.add("fregatidae");
        options.add("turkey");
        options.add("guineafowl");
        options.add("stork");
        allAnimals.add(new Animal("turkey", options));

        options = new ArrayList<>();
        options.add("robin");
        options.add("roller");
        options.add("swallow");
        options.add("peacock");
        allAnimals.add(new Animal("robin", options));

        options = new ArrayList<>();
        options.add("eagle");
        options.add("emu");
        options.add("rhea");
        options.add("guineafowl");
        allAnimals.add(new Animal("guineafowl", options));

        options = new ArrayList<>();
        options.add("crane");
        options.add("magpie");
        options.add("kingfisher");
        options.add("ibis");
        allAnimals.add(new Animal("crane", options));

        options = new ArrayList<>();
        options.add("swift");
        options.add("hoopoe");
        options.add("owl");
        options.add("bat");
        allAnimals.add(new Animal("owl", options));

        options = new ArrayList<>();
        options.add("eagle");
        options.add("cormorant");
        options.add("vulture");
        options.add("toucan");
        allAnimals.add(new Animal("vulture", options));

        options = new ArrayList<>();
        options.add("kingfisher");
        options.add("spoonbill");
        options.add("stork");
        options.add("woodpecker");
        allAnimals.add(new Animal("woodpecker", options));

        options = new ArrayList<>();
        options.add("vulture");
        options.add("rhea");
        options.add("owl");
        options.add("ostrich");
        allAnimals.add(new Animal("rhea", options));

        options = new ArrayList<>();
        options.add("gull");
        options.add("parrot");
        options.add("albatross");
        options.add("tern");
        allAnimals.add(new Animal("gull", options));

        options = new ArrayList<>();
        options.add("bee9eater");
        options.add("humming9bird");
        options.add("sparrow");
        options.add("plover");
        allAnimals.add(new Animal("plover", options));

        options = new ArrayList<>();
        options.add("gull");
        options.add("parrot");
        options.add("crow");
        options.add("cockatoo");
        allAnimals.add(new Animal("parrot", options));

        options = new ArrayList<>();
        options.add("ibis");
        options.add("fregatidae");
        options.add("albatross");
        options.add("swan");
        allAnimals.add(new Animal("albatross", options));

        options = new ArrayList<>();
        options.add("puffin");
        options.add("heron");
        options.add("pelican");
        options.add("penguin");
        allAnimals.add(new Animal("heron", options));

        options = new ArrayList<>();
        options.add("eagle");
        options.add("vulture");
        options.add("rhea");
        options.add("turkey");
        allAnimals.add(new Animal("eagle", options));

        options = new ArrayList<>();
        options.add("puffin");
        options.add("spoonbill");
        options.add("toucan");
        options.add("woodpecker");
        allAnimals.add(new Animal("puffin", options));

        options = new ArrayList<>();
        options.add("vulture");
        options.add("emu");
        options.add("ostrich");
        options.add("guineafowl");
        allAnimals.add(new Animal("emu", options));

        return allAnimals;
    }


    public static String makeGoodStringName(Context context, String name){
        if(name.indexOf("9") >= 0){
            name = name.replace("9", " ");
        }

        String goodName = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        return goodName;
    }
}

