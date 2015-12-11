package in.blogspot.androidoupsolving.animalsvoiceenglish;

import java.io.Serializable;
import java.util.ArrayList;


public class Animal implements Serializable {
    public String name;
    public ArrayList<String> choices;

    public Animal(String name, ArrayList<String> choices) {
        setName(name);
        setChoices(choices);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }
}
