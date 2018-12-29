package com.xuhe.codebook20;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView show_lv;
    List<Data> list = new ArrayList<>();
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private DataAdapter adapter;
    private String delete_content;
    private String edit_title;
    private String edit_time;
    private FloatingActionButton fab;
    private MenuItem.OnMenuItemClickListener mOnMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            final SQLiteDatabase db = dbHelper.getWritableDatabase();
            switch (item.getItemId()){
                case 0:
                    //delete the record
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    View view = View.inflate(MainActivity.this,R.layout.delete_tip,null);
                    Button cancelBtn = view.findViewById(R.id.cancel_btn);
                    Button sureBtn = view.findViewById(R.id.sure_btn);
                    final AlertDialog dialog = builder.setView(view).create();
                    dialog.show();
                    sureBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            db.delete("Book","content = ?",new String[]{delete_content});
                    /*Toast.makeText(MainActivity.this, String.valueOf(list_position), Toast.LENGTH_SHORT).show();*/
                    /*Toast.makeText(MainActivity.this, "Delete", Toast.LENGTH_SHORT).show();*/
                            ShowRecord();
                            dialog.dismiss();
                        }
                    });
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    break;
                case 1:
                    //update the record
                    Bundle bundle = new Bundle();
                    bundle.putString("delete_content",delete_content);
                    bundle.putString("edit_title",edit_title);
                    Intent editIntent = new Intent(MainActivity.this,AddActivity.class);
                    editIntent.putExtras(bundle);
                    startActivityForResult(editIntent,2);
                    /*Toast.makeText(MainActivity.this, "Edit", Toast.LENGTH_SHORT).show();*/
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        dbHelper = new MyDatabaseHelper(this, "db_bwl", null, 1);
        show_lv = (ListView) findViewById(R.id.content);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(MainActivity.this,AddActivity.class);
                startActivityForResult(addIntent,1);
            }
        });
        //点击事件
        show_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view1 = View.inflate(MainActivity.this,R.layout.details_info,null);
                TextView content = view1.findViewById(R.id.content_details);
                TextView topic = view1.findViewById(R.id.topic);
                topic.setText(String.format("主题：%s", list.get(position).getmTitle()));
                content.setText(String.format("%s\n\n创建时间：%s", list.get(position).getmContent(), list.get(position).getmTime()));
                builder.setView(view1).create();
                builder.show();
            }
        });
        //长按事件
        show_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                delete_content = list.get(position).getmContent();
                edit_title = list.get(position).getmTitle();
                edit_time = list.get(position).getmTime();
                return false;
            }
        });
        show_lv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Please Choose");
                menu.add(0,0,0,"delete").setOnMenuItemClickListener(mOnMenuItemClickListener);
                menu.add(0,1,0,"edit").setOnMenuItemClickListener(mOnMenuItemClickListener);
            }
        });
        //query the database and get the record
        ShowRecord();
    }

    private void ShowRecord() {
        list.clear();
        if (db == null) {
            db = dbHelper.getReadableDatabase();
        }
        Cursor cursor = db.query("Book",null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do{
                //遍历Cursor对象，取出数据
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String date = cursor.getString(cursor.getColumnIndex("time"));
                Data data =new Data(title,content,date);
                list.add(data);
                /*list.add(title);
                list.add(content);
                list.add(date);
                adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);*/
            }while (cursor.moveToNext());
        }
        cursor.close();
        if (adapter == null) {
            adapter = new DataAdapter(MainActivity.this, R.layout.list_bwl, list);
        }else {
            adapter.notifyDataSetChanged();
        }
        show_lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                //修改密码
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this).setTitle("设置密码").setIcon(R.drawable.setting).setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String password = editText.getText().toString();
                                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                                editor.putString("password",password);
                                editor.apply();
                            }
                        }).setNegativeButton("取消",null).show();
                break;
            case R.id.add:
                Intent addIntent = new Intent(MainActivity.this,AddActivity.class);
                startActivityForResult(addIntent,1);
                /*SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //写入数据
                values.put("title","first");
                values.put("content","this a text");
                values.put("time","2017年4月1日");
                db.insert("Book",null,values);
                values.clear();
                values.put("title","second");
                values.put("content","this another text");
                values.put("time","2017年4月1日");
                db.insert("Book",null,values);
                values.clear();*/
                break;
            case R.id.about:
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.show();
                Window window = alertDialog.getWindow();
                window.setContentView(R.layout.function);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Calendar calendar = Calendar.getInstance();
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minutes = String.valueOf(calendar.get(Calendar.MINUTE));
        db = dbHelper.getWritableDatabase();
        if (Integer.parseInt(minutes) < 10){
            minutes = "0" + minutes;
        }
        String date = String.valueOf(calendar.get(Calendar.YEAR)) + "年" + String.valueOf(calendar.get(Calendar.MONTH) +1
                + "月" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH) + "日"));
        switch (resultCode) {
            case 0:
                Toast.makeText(this, "save fault", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Bundle bundle = data.getExtras();
                String title = bundle.getString("title_from_Add");
                String content = bundle.getString("content_from_Add");
                ContentValues values = new ContentValues();
                values.put("title", title);
                values.put("content", content);
                values.put("time", date + hour + ":" + minutes);
                db.insert("Book", null, values);
                values.clear();
                ShowRecord();
                break;
            case 2:
                Bundle update_bundle = data.getExtras();
                String edit_title = update_bundle.getString("title_from_Add");
                String edit_content = update_bundle.getString("content_from_Add");
                ContentValues update_values = new ContentValues();
                update_values.put("title", edit_title);
                update_values.put("content", edit_content);
                db.update("Book", update_values, "time = ?", new String[]{edit_time});
                update_values.clear();
                ShowRecord();
                break;
            default:
                break;
        }
    }
}
