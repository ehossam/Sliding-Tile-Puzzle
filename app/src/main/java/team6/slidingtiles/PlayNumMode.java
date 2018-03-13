package team6.slidingtiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlayNumMode extends AppCompatActivity {

    public Button alonebutton;
    public Button aibutton;

    public void onAIMode(){
        aibutton= findViewById(R.id.aibutton);
        aibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayNumMode.this,AiMode.class);
                startActivity(intent);
            }
        });
    }


    public void onAloneMode(){
        alonebutton = findViewById(R.id.alonebutton);
        alonebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayNumMode.this,NumberMode.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_num_mode);
        onAIMode();
        onAloneMode();
    }
}
