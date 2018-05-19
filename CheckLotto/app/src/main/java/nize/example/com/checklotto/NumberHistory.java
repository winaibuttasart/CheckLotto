package nize.example.com.checklotto;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NumberHistory extends AppCompatActivity {
    private EditText read;
    private Button click;
    private TextView show;
    public DatabaseReference historyRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_history);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        read = (EditText) findViewById(R.id.read);
        click = (Button) findViewById(R.id.click);
        show = (TextView) findViewById(R.id.show);

        read.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View arg0, int arg1, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (arg1 == KeyEvent.KEYCODE_ENTER)) {
                    doOnclick();
                    return true;
                }
                return false;
            }
        });

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOnclick();
            }
        });

    }

    private void doOnclick() {
        String len = read.getText().toString();
        if (len.length() == 6 || len.length() == 3 || len.length() == 2) {
            historyRef = database.getReference("history").child(read.getText().toString());
            historyRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> array = new ArrayList<String>();
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            array.add(snapshot.getValue().toString());
                        }
                        String strcheck = "";
                        for (int i = 0; i < array.size(); i++) {
                            strcheck += array.get(i) + "\n";
                        }
                        show.setText(strcheck);
                    } else {
                        show.setText("หมายเลข " + read.getText().toString() + " ยังไม่มีประวัติการออกรางวัล");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "กรุณากรอก 6 , 3 , 2 ตัวค่ะ", Toast.LENGTH_SHORT).show();
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(read.getWindowToken(), 0);
    }
}
