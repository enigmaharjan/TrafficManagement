package com.example.trafficmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    ImageView topArrow,leftArrow,rightArrow,downArrow;
    TextView tvLight,tvTop,tvLeft,tvRight,tvDown, override;
    String streetLight;
    Boolean overrride;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topArrow = findViewById(R.id.topArrow);
        leftArrow = findViewById(R.id.leftArrow);
        rightArrow = findViewById(R.id.rightArrow);
        downArrow = findViewById(R.id.downArrow);
        tvLight = findViewById(R.id.tvStreetLight);
        tvTop = findViewById(R.id.tvTop);
        tvLeft = findViewById(R.id.tvLeft);
        tvRight = findViewById(R.id.tvRight);
        tvDown = findViewById(R.id.tvDown);
        override = findViewById(R.id.override);

        overrride = false;

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("light").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                        streetLight = dataSnapshot.child("value").getValue().toString();
                        tvLight.setText(streetLight);
                        if(streetLight.equals("on")) {
                            tvLight.setBackgroundColor(Color.GREEN);
                        }
                        else{
                            tvLight.setBackgroundColor(Color.MAGENTA);
                        }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        firebaseDatabase.getReference("traffic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                        final String light = dataSnapshot.child("light").getValue().toString();
                        firebaseDatabase.getReference("trafficValue").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                        String trafficValue = dataSnapshot.child("value").getValue().toString();
                                        if(light.equals("top")){
                                            tvTop.setText("Traffic Count: "+trafficValue);
                                            tvRight.setText("");
                                            tvLeft.setText("");
                                            tvDown.setText("");
                                        }
                                        else if(light.equals("left")) {
                                            tvLeft.setText("Traffic Count: "+trafficValue);
                                            tvRight.setText("");
                                            tvDown.setText("");
                                            tvTop.setText("");
                                        }
                                        else if(light.equals("right")) {
                                            tvRight.setText("Traffic Count: "+trafficValue);
                                            tvDown.setText("");
                                            tvLeft.setText("");
                                            tvTop.setText("");
                                        }
                                        else if(light.equals("down")){
                                            tvDown.setText("Traffic Count: "+trafficValue);
                                            tvRight.setText("");
                                            tvLeft.setText("");
                                            tvTop.setText("");
                                        }
                                        else{
                                            tvRight.setText("");
                                            tvLeft.setText("");
                                            tvDown.setText("");
                                            tvTop.setText("");
                                        }
                                    }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(MainActivity.this, "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        if(light.equals("top")) {
                            topArrow.setColorFilter(Color.argb(0, 0, 255, 0));
                            downArrow.setColorFilter(Color.argb(255, 255, 0, 0));
                            leftArrow.setColorFilter(Color.argb(255, 255, 0, 0));
                            rightArrow.setColorFilter(Color.argb(255, 255, 0, 0));
                        }
                        else if(light.equals("left")) {
                            topArrow.setColorFilter(Color.argb(255, 255, 0, 0));
                            downArrow.setColorFilter(Color.argb(255, 255, 0, 0));
                            leftArrow.setColorFilter(Color.argb(0, 0, 255, 0));
                            rightArrow.setColorFilter(Color.argb(255, 255, 0, 0));
                        }
                        else if(light.equals("right")) {
                            topArrow.setColorFilter(Color.argb(255, 255, 0, 0));
                            downArrow.setColorFilter(Color.argb(255, 255, 0, 0));
                            leftArrow.setColorFilter(Color.argb(255, 255, 0, 0));
                            rightArrow.setColorFilter(Color.argb(0, 0, 255, 0));
                        }
                        else if(light.equals("down")){
                            topArrow.setColorFilter(Color.argb(255, 255, 0, 0));
                            downArrow.setColorFilter(Color.argb(0, 0, 255, 0));
                            leftArrow.setColorFilter(Color.argb(255, 255, 0, 0));
                            rightArrow.setColorFilter(Color.argb(255, 255, 0, 0));
                        }
                        else{
                            topArrow.setColorFilter(Color.argb(255, 255, 0, 0));
                            downArrow.setColorFilter(Color.argb(255, 255, 0, 0));
                            leftArrow.setColorFilter(Color.argb(255, 255, 0, 0));
                            rightArrow.setColorFilter(Color.argb(255, 255, 0, 0));
                        }
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        tvLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(streetLight.equals("on")) {
                    firebaseDatabase.getReference("light").child("value").setValue("off");
                }
                else{
                    firebaseDatabase.getReference("light").child("value").setValue("on");
                }
            }
        });

        override.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(overrride) {
                    firebaseDatabase.getReference("traffic").child("light").setValue("top");
                    firebaseDatabase.getReference("trafficValue").child("value").setValue("0");
                    overrride = false;
                    override.setText("Override\n System");
                }
                else{
                    firebaseDatabase.getReference("traffic").child("light").setValue("override");
                    overrride = true;
                    override.setText("Restore\n System");
                }
            }
        });
    }
}
