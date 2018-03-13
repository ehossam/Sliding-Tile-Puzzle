package team6.slidingtiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class PlayerMode extends AppCompatActivity implements View.OnClickListener {

    private Button buttonBasic;
    private Button buttonCutthroat;
    private Button buttonSingle;
    private Button buttonHighScore;
    private int no_rounds = 0;

    private CheckBox checkbox1,checkbox2,checkbox3;

    @Override
    protected void onResume() {
        if(checkbox1.isChecked()){
            checkbox1.setChecked(false);
            checkbox1.setSelected(false);
        }
        if(checkbox2.isChecked()){
            checkbox2.setChecked(false);
            checkbox2.setSelected(false);
        }
        if(checkbox3.isChecked()){
            checkbox3.setChecked(false);
            checkbox3.setSelected(false);
        }
        super.onResume();
            }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_mode);

        buttonBasic = findViewById(R.id.button_basic);
        buttonCutthroat = findViewById(R.id.button_cutthroat);
        buttonSingle = findViewById(R.id.button_single);
        buttonHighScore = findViewById(R.id.button_highscore);

        buttonBasic.setOnClickListener(this);
        buttonCutthroat.setOnClickListener(this);
        buttonSingle.setOnClickListener(this);
        buttonHighScore.setOnClickListener(this);
        addListenerOnC1();
        addListenerOnC2();
        addListenerOnC3();

    }

    public void addListenerOnC1() {
        checkbox1 = (CheckBox) findViewById(R.id.checkbox1);
        checkbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                   no_rounds = 1;
                    Log.d("no of rounds", String.valueOf(no_rounds));
                }
            }
        });

    }

    public void addListenerOnC2() {
        checkbox2 = (CheckBox) findViewById(R.id.checkbox2);
        checkbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    no_rounds = 2;
                    Log.d("no of rounds", String.valueOf(no_rounds));
                }
            }
        });
    }

    public void addListenerOnC3() {
        checkbox3 = (CheckBox) findViewById(R.id.checkbox3);
        checkbox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    no_rounds = 3;
                    Log.d("no of rounds", String.valueOf(no_rounds));
                }
            }
        });
    }
    @Override
    public void onClick(View view) {
        if(view == buttonSingle){
            Intent intent = new Intent(PlayerMode.this,MathMode.class);
            startActivity(intent);
        }
        if(view == buttonBasic){
            Intent intent = new Intent(PlayerMode.this,MathModeMultiSimple.class);
            intent.putExtra("rounds",no_rounds);
            startActivity(intent);
        }
        if(view == buttonCutthroat){
            //TODO : redirect to two player cutthroat mode
//            Intent intent = new Intent(PlayerMode.this,Cutthroat.class);
//            startActivity(intent);
        }

        if(view == buttonHighScore){
            Intent intent = new Intent(PlayerMode.this,HighScore.class);
            startActivity(intent);
        }
    }
}
