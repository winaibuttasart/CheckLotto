package nize.example.com.checklotto;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Top extends AppCompatActivity {

    private ListView listview;
    private ListAdapter adapter;
    private int[] images = {R.drawable.loterry01, R.drawable.loterry02, R.drawable.loterry03, R.drawable.loterry04, R.drawable.loterry05, R.drawable.loterry06, R.drawable.loterry07, R.drawable.loterry08, R.drawable.loterry09, R.drawable.loterry10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        listview = (ListView) findViewById(R.id.mylist);
        adapter = new ListAdapter(this, images);
        listview.setAdapter(adapter);
    }
}
