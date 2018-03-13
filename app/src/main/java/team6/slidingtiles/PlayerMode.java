package team6.slidingtiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PlayerMode extends AppCompatActivity implements View.OnClickListener {

    private Button buttonBasic;
    private Button buttonCutthroat;
    private Button buttonSingle;
    private Button buttonHighScore;

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
    }

    @Override
    public void onClick(View view) {
        if(view == buttonSingle){
            //TODO : redirect to single player mode
            Intent intent = new Intent(PlayerMode.this,MathMode.class);
            startActivity(intent);
        }
        if(view == buttonBasic){

            //TODO : redirect to two player basic mode
            Intent intent = new Intent(PlayerMode.this,MathModeMultiSimple.class);
            startActivity(intent);
        }
        if(view == buttonCutthroat){
            //TODO : redirect to two player cutthroat mode
            Intent intent = new Intent(PlayerMode.this,MathModeMultiSimple.class);
            startActivity(intent);
        }

        if(view == buttonHighScore){
            Intent intent = new Intent(PlayerMode.this,HighScore.class);
            startActivity(intent);
        }
    }
}
