package nize.example.com.checklotto;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    private Spinner spinnerDay, spinnerMonth, spinnerYear;
    private TextView prizeanswer, tvdate;
    private ArrayList<String> listYear = new ArrayList<String>();
    private ArrayList<String> listMonth = new ArrayList<String>();
    private ArrayList<String> listDay = new ArrayList<String>();
    public DatabaseReference back2Ref, back3Ref, front3Ref, firstRef, secondRef, thirdRef, fourRef, fiveRef, nearbyRef;
    public DatabaseReference yearRef, monthRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private String getYear, getMonth, getDay, allPrize;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        spinnerYear = (Spinner) findViewById(R.id.year);
        spinnerMonth = (Spinner) findViewById(R.id.month);
        spinnerDay = (Spinner) findViewById(R.id.day);
        prizeanswer = (TextView) findViewById(R.id.prizeanswer);
        tvdate = (TextView) findViewById(R.id.tvdate);

        loadListYear();
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, listYear);
        spinnerYear.setAdapter(adapterYear);

//        spinnerYear.setOnItemSelectedListener(this);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getYear = listYear.get(position);
//                Toast.makeText(History.this, "Select : " + getYear, Toast.LENGTH_SHORT).show();
                yearRef = database.getReference(getYear + "");
                yearRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listMonth.clear();
                        listDay.clear();

                        if (dataSnapshot.getChildrenCount() > 0) {
                            String testprint = "";
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                //code
                                listMonth.add(snapshot.getKey());
                            }
                            ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(History.this,
                                    android.R.layout.simple_dropdown_item_1line, listMonth);
                            spinnerMonth.setAdapter(adapterMonth);

                        } else {
                            Toast.makeText(History.this, "ไม่พบข้อมูลของเดือนนี้", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(History.this, "Select : " + listMonth.get(position), Toast.LENGTH_SHORT).show();
                getMonth = listMonth.get(position);
                monthRef = database.getReference(getYear).child(getMonth);
                monthRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listDay.clear();

                        if (dataSnapshot.getChildrenCount() > 0) {
                            String testprint = "";
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                //code
                                listDay.add(snapshot.getKey());
                            }
                            ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(History.this,
                                    android.R.layout.simple_dropdown_item_1line, listDay);
                            spinnerDay.setAdapter(adapterDay);

                        } else {
                            Toast.makeText(History.this, "ไม่พบข้อมูลของเดือนนี้", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(History.this, "Select : " + listMonth.get(position), Toast.LENGTH_SHORT).show();
                AsynTaskRunnder runner = new AsynTaskRunnder();
                getDay = listDay.get(position);
                tvdate.setText(getDay + "/" + getMonth + "/" + getYear);
                allPrize = "";
                back2Ref = database.getReference(getYear).child(getMonth).child(getDay).child("back2");
                back3Ref = database.getReference(getYear).child(getMonth).child(getDay).child("back3");
                front3Ref = database.getReference(getYear).child(getMonth).child(getDay).child("front3");
                firstRef = database.getReference(getYear).child(getMonth).child(getDay).child("firstPrize");
                secondRef = database.getReference(getYear).child(getMonth).child(getDay).child("second-prize");
                thirdRef = database.getReference(getYear).child(getMonth).child(getDay).child("third-prize");
                fourRef = database.getReference(getYear).child(getMonth).child(getDay).child("four-prize");
                fiveRef = database.getReference(getYear).child(getMonth).child(getDay).child("five-prize");
                nearbyRef = database.getReference(getYear).child(getMonth).child(getDay).child("nearby");


                //First-Prize
                firstRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            allPrize += "\nรางวัลที่ 1";
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                allPrize += "\n\t\t" + snapshot.getValue().toString();
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
                            allPrize += "\n\nรางวัลใกล้เคียงรางวัลที่ 1";
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                allPrize += "\n\t\t" + snapshot.getValue().toString();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                //Back2-Prize
                back2Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            allPrize += "\n\nรางวัลเลขท้าย 2 ตัว";
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                allPrize += "\n\t\t" + snapshot.getValue().toString();
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
                            allPrize += "\n\nรางวัลเลขท้าย 3 ตัว";
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                allPrize += "\n\t\t" + snapshot.getValue().toString();
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
                            allPrize += "\n\nรางวัลเลขหน้า 3 ตัว";
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                allPrize += "\n\t\t" + snapshot.getValue().toString();
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
                            allPrize += "\n\nรางวัลที่ 2";
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                allPrize += "\n\t\t" + snapshot.getValue().toString();
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
                            allPrize += "\n\nรางวัลที่ 3";
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                allPrize += "\n\t\t" + snapshot.getValue().toString();
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
                            allPrize += "\n\nรางวัลที่ 4";
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                allPrize += "\n\t\t" + snapshot.getValue().toString();
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
                            allPrize += "\n\nรางวัลที่ 5";
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                allPrize += "\n\t\t" + snapshot.getValue().toString();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                runner.execute(2 + "");
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (allPrize.length() > 10) {
                            prizeanswer.setText(allPrize);
                        } else {
                            prizeanswer.setText("ไม่มีข้อมูลรางวัลของวันที่นี้");
                        }
                    }
                }, 3000);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void loadListYear() {
        listYear.add("2561");
        listYear.add("2560");
        listYear.add("2559");
        listYear.add("2558");
        listYear.add("2557");
        listYear.add("2556");
        listYear.add("2555");
    }

    private class AsynTaskRunnder extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(History.this);
            progressDialog.setTitle("กำลังโหลดข้อมูล");
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
            resp = "กำลังโหลดข้อมูลลอตเตอรี่";
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... text) {
            progressDialog.setMessage("กำลังโหลดข้อมูลลอตเตอรี่ ..");
        }
    }
}
