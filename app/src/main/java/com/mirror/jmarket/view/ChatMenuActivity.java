package com.mirror.jmarket.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mirror.jmarket.R;
import com.mirror.jmarket.databinding.ActivityChatMenuBinding;

public class ChatMenuActivity extends AppCompatActivity {

    public static final String TAG = "ChatmenuActivity";

    private ActivityChatMenuBinding chatMenuBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatMenuBinding = ActivityChatMenuBinding.inflate(getLayoutInflater());
        setContentView(chatMenuBinding.getRoot());

        overridePendingTransition(R.anim.fadein_up, R.anim.none);


        // button

        chatMenuBinding.completeDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent data = new Intent();
                data.putExtra("data", "complete");
                setResult(RESULT_OK, data);
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_up);

            }
        });

        chatMenuBinding.outChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("data", "out");
                setResult(RESULT_OK, data);
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_up);
            }
        });

        chatMenuBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_up);
            }
        });
    }
}