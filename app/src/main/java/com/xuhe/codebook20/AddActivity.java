package com.xuhe.codebook20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    private EditText title_et,content_et;
    private Button save_btn,back_btn;
    private boolean update = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.sign_tool_bar);
        setSupportActionBar(toolbar);
        back_btn = (Button) findViewById(R.id.back);
        save_btn = (Button) findViewById(R.id.save);
        title_et = (EditText) findViewById(R.id.title_et);
        content_et = (EditText) findViewById(R.id.content_et);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String edit_title = bundle.getString("edit_title");
            String edit_content = bundle.getString("delete_content");
            title_et.setText(edit_title);
            content_et.setText(edit_content);
            update = true;
        }
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0);
                finish();
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title_from_Add",title_et.getText().toString());
                bundle.putString("content_from_Add",content_et.getText().toString());
                Intent dataIntent = new Intent();
                dataIntent.putExtras(bundle);
                if (update) {
                    setResult(2,dataIntent);
                }else
                    setResult(1, dataIntent);
                finish();
            }
        });
    }
}
