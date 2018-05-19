package nize.example.com.checklotto;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CheckLottery extends AppCompatActivity {

    private Button clickcheck;
    private EditText writecheck;
    public DatabaseReference refSetDate, back2Ref, back3Ref, front3Ref, firstRef, secondRef, thirdRef, fourRef, fiveRef, nearbyRef;
    private TextView date, tv2;

    private static final String TAG = "WelcomeActivity";
    private CurrentDate currentDate;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private int tmpDate = 0;
    Handler handler;

    List<String> prize;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_lottery);
        currentDate = new CurrentDate();


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        date = (TextView) findViewById(R.id.date);
        writecheck = ((EditText) findViewById(R.id.writecheck));
        writecheck.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        clickcheck = (Button) findViewById(R.id.clickcheck);
        tv2 = (TextView) findViewById(R.id.tv2);


        setDate();

        writecheck.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View arg0, int arg1, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (arg1 == KeyEvent.KEYCODE_ENTER)) {
                    doOnClick();
                    return true;
                }
                return false;
            }
        });

        clickcheck.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(final View view) {
                doOnClick();
            }
        });

    }

    private void doOnClick() {
        AsynTaskRunnder runner = new AsynTaskRunnder();
        prize = new ArrayList<String>();
        String strNumber = writecheck.getText().toString();
        if (strNumber.length() == 6) {
            final int back2 = Integer.parseInt(strNumber.substring(4, 6));
            final int back3 = Integer.parseInt(strNumber.substring(3, 6));
            final int front3 = Integer.parseInt(strNumber.substring(0, 3));
            final int fullNumber = Integer.parseInt(strNumber);

            //ตรวจหวยที่นี่นะจ๊ะ
//                    Toast.makeText(getApplicationContext(), "ตรวจหวยยยยย", Toast.LENGTH_SHORT).show();

            back2Ref = database.getReference(currentDate.getYear() + "").child(currentDate.getMonth() + "").child(tmpDate + "").child("back2");
            back3Ref = database.getReference(currentDate.getYear() + "").child(currentDate.getMonth() + "").child(tmpDate + "").child("back3");
            front3Ref = database.getReference(currentDate.getYear() + "").child(currentDate.getMonth() + "").child(tmpDate + "").child("front3");
            firstRef = database.getReference(currentDate.getYear() + "").child(currentDate.getMonth() + "").child(tmpDate + "").child("firstPrize");
            secondRef = database.getReference(currentDate.getYear() + "").child(currentDate.getMonth() + "").child(tmpDate + "").child("second-prize");
            thirdRef = database.getReference(currentDate.getYear() + "").child(currentDate.getMonth() + "").child(tmpDate + "").child("third-prize");
            fourRef = database.getReference(currentDate.getYear() + "").child(currentDate.getMonth() + "").child(tmpDate + "").child("four-prize");
            fiveRef = database.getReference(currentDate.getYear() + "").child(currentDate.getMonth() + "").child(tmpDate + "").child("five-prize");
            nearbyRef = database.getReference(currentDate.getYear() + "").child(currentDate.getMonth() + "").child(tmpDate + "").child("nearby");


            //Back2-Prize
            back2Ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (back2 == Integer.parseInt(snapshot.getValue().toString())) {
                                prize.add("เลข " + back2 + " ถูกรางวัลเลขท้าย 2 ตัว");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            //Back3-Prize
            back3Ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (back3 == Integer.parseInt(snapshot.getValue().toString())) {
                                prize.add("เลข " + back3 + " ถูกรางวัลเลขท้าย 3 ตัว");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            //Front3-Prize
            front3Ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (front3 == Integer.parseInt(snapshot.getValue().toString())) {
                                prize.add("เลข " + front3 + " ถูกรางวัลเลขหน้า 3 ตัว");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            //First-Prize
            firstRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (fullNumber == Integer.parseInt(snapshot.getValue().toString())) {
                                prize.add("เลข " + fullNumber + " ถูกรางวัลที่ 1");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            //Second-Prize
            secondRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (fullNumber == Integer.parseInt(snapshot.getValue().toString())) {
                                prize.add("เลข " + fullNumber + " ถูกรางวัลที่ 2");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            //Third-Prize
            thirdRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (fullNumber == Integer.parseInt(snapshot.getValue().toString())) {
                                prize.add("เลข " + fullNumber + " ถูกรางวัลที่ 3");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            //Four-Prize
            fourRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (fullNumber == Integer.parseInt(snapshot.getValue().toString())) {
                                prize.add("เลข " + fullNumber + " ถูกรางวัลที่ 4");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            //Five-Prize
            fiveRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (fullNumber == Integer.parseInt(snapshot.getValue().toString())) {
                                prize.add("เลข " + fullNumber + " ถูกรางวัลที่ 5");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            //Nearby-Prize
            nearbyRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (fullNumber == Integer.parseInt(snapshot.getValue().toString())) {
                                prize.add("เลข " + fullNumber + " ถูกรางวัลใกล้เคียงรางวัลที่ 1");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


            runner.execute(3 + "");
            //Delay for check prize
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (prize.size() > 0) {
                        String show = "";
                        for (int i = 0; i < prize.size(); i++) {
                            show += prize.get(i) + "\n";
                        }
                        tv2.setText(show);

                    } else {
//                                Toast.makeText(getApplicationContext(), "เสียใจด้วยจ้าา คุณไม่ถูกรางวัลไหนเลย", Toast.LENGTH_SHORT).show();
                        tv2.setText(" เสียใจด้วยจ้า คุณไม่ถูกรางวัลอะไรเลย");
                    }

                }
            }, 3000);


        } else {
            Toast.makeText(getApplicationContext(), "กรุณากรอกลอตเตอรี่ให้ถูกต้อง", Toast.LENGTH_SHORT).show();
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(writecheck.getWindowToken(), 0);
    }

    public void setDate() {
        refSetDate = database.getReference(currentDate.getYear() + "").child(currentDate.getMonth() + "");
        refSetDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        int nowDate = Integer.parseInt(snapshot.getKey());
                        if (nowDate > tmpDate) {
                            tmpDate = nowDate;
                        }
                    }
                    refSetDate = database.getReference(currentDate.getYear() + "").child(currentDate.getMonth() + "").child(tmpDate + "").child("numericDate");

                    refSetDate.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String value = String.valueOf(dataSnapshot.getValue());
                            date.setText(value);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    date.setText("ไม่พบข้อมูลการออกล็อตเตอรี่เดือนนี้");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private class AsynTaskRunnder extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CheckLottery.this);
            progressDialog.setTitle("กำลังตรวจเช็ครางวัล กรุณารอ ..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < Integer.parseInt(strings[0]); i++) {
                try {
                    publishProgress(String.valueOf(i));
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            resp = "กำลังตรวจเช็ครางวัล";
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... text) {
            progressDialog.setMessage("กำลังตรวจเช็ครางวัล กรุณารอ " + text[0] + " วินาที");
        }
    }
}

