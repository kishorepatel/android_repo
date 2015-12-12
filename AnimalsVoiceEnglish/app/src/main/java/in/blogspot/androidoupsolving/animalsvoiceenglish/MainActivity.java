package in.blogspot.androidoupsolving.animalsvoiceenglish;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_container);
        //we are loading fragment in XML file (main_acitity_contianer) using android:name
    }

}