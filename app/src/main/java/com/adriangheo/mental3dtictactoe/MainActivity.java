package com.adriangheo.mental3dtictactoe;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFS_CONSTANT = "sharedPrefs";
    public static final String FIRST_PLAYER_SELECTION_CONSTANT = "textConstantForPlayerSelected";
    public static final String DIFFICULTY_SELECTION_CONSTANT = "textConstantForDifficultySelected";
    public static final String TYPE_OF_OPPONENT_SELECTION_CONSTANT = "textConstantForTypeOfOpponentSelected";
    public static final String BUTTON_MATRICES_VISIBILITY_CONSTANT = "textConstantForMatricesVisibility";
    public static final String WALL_SELECTION_CONSTANT = "textConstantForEnabledWallObstacles";

    RadioGroup rgSetPlayerFirstV2, rgSetDifficultyV2,
            rgSetOpponentV2, rgSetMatricesVisibiltyV2, rgEnableWallObstacles;

    RadioButton radioButtonPlayerSymbolV2, radioButtonFirstPlayerV2, radioButtonChosenDifficultyV2,
            radioButtonSetOpponentV2, radioButtonSetMatricesVisibiltyV2, radioButtonEnableWallObstacles;

    Button buttonOpenTTT3D, buttonOpenInstructionsActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonOpenTTT3D = findViewById(R.id.start_game_button);
        buttonOpenInstructionsActivity = findViewById(R.id.instructions_button);

        rgSetPlayerFirstV2 = findViewById(R.id.radio_gr_set_first_player_v2);
        rgSetDifficultyV2 = findViewById(R.id.radio_gr_set_difficulty_v2);
        rgSetOpponentV2 = findViewById(R.id.radio_gr_set_opponent_v2);
        rgSetMatricesVisibiltyV2 = findViewById(R.id.radio_gr_set_matrix_interactivity);
        rgEnableWallObstacles = findViewById(R.id.radio_gr_enable_wall_obstacles);

        buttonOpenTTT3D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMyPreferences();
                openTTT3D();
            }
        });

        buttonOpenInstructionsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstructionsActivity();
            }
        });
    }

    public void openTTT3D(){
        Intent intent = new Intent(this, TTT3D.class);
        startActivity(intent);
    }


    public void openInstructionsActivity(){
        Intent intent = new Intent(this, InstructionsActivity.class);
        startActivity(intent);
    }

    public void saveMyPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_CONSTANT, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        int enbledWallObstacles = rgEnableWallObstacles.getCheckedRadioButtonId();
        radioButtonEnableWallObstacles = findViewById(enbledWallObstacles);
        if(radioButtonEnableWallObstacles.getText().toString().equals(getString(R.string.enabled_wall_obstacles))){
            editor.putString(WALL_SELECTION_CONSTANT, "D");
        } else {
            editor.putString(WALL_SELECTION_CONSTANT, "E");
        }


        int matricesVisibilityOnOrOff = rgSetMatricesVisibiltyV2.getCheckedRadioButtonId();
        radioButtonSetMatricesVisibiltyV2 = findViewById(matricesVisibilityOnOrOff);
        if(radioButtonSetMatricesVisibiltyV2.getText().toString().equals("Classic Interactive Matrix")){
            editor.putString(BUTTON_MATRICES_VISIBILITY_CONSTANT, "V");
        } else {
            editor.putString(BUTTON_MATRICES_VISIBILITY_CONSTANT, "I");
        }



        int humanPlayerFirstOrSecond = rgSetPlayerFirstV2.getCheckedRadioButtonId();
        radioButtonFirstPlayerV2 = findViewById(humanPlayerFirstOrSecond);

        if(radioButtonFirstPlayerV2.getText().toString().equals("CpuFirst")) {
            editor.putString(FIRST_PLAYER_SELECTION_CONSTANT, "CPU_First");
        } else {
            editor.putString(FIRST_PLAYER_SELECTION_CONSTANT, "Human_First");
        }


        int selectedDifficultyV1 = rgSetDifficultyV2.getCheckedRadioButtonId();
        radioButtonChosenDifficultyV2 = findViewById(selectedDifficultyV1);
        if(radioButtonChosenDifficultyV2.getText().toString().equals("easy")) {
            editor.putString(DIFFICULTY_SELECTION_CONSTANT, "E");
        } else if(radioButtonChosenDifficultyV2.getText().toString().equals("medium")) {
            editor.putString(DIFFICULTY_SELECTION_CONSTANT, "M");
        } else {
            editor.putString(DIFFICULTY_SELECTION_CONSTANT, "H");
        }


        int opponentType = rgSetOpponentV2.getCheckedRadioButtonId();
        radioButtonSetOpponentV2 = findViewById(opponentType);
        if(radioButtonSetOpponentV2.getText().toString().equals("PlayerVsCpu")){
            editor.putString(TYPE_OF_OPPONENT_SELECTION_CONSTANT, "C");
        }else{
            editor.putString(TYPE_OF_OPPONENT_SELECTION_CONSTANT, "H");
        }

        editor.apply();

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }
}