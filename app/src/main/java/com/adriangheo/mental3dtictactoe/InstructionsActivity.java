package com.adriangheo.mental3dtictactoe;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;


public class InstructionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        TextView text = (TextView) findViewById(R.id.tv_link_privacy_policy);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }
}