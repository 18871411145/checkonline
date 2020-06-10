package cn.lxbest.wb2020.checkonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Home_Activity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_1,tv_2,tv_3,tv_4,tv_5,tv_6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activty);

        setTitle("主页");

        tv_1=findViewById(R.id.tv_1);//新闻列表
        tv_2=findViewById(R.id.tv_2);//留言列表
        tv_3=findViewById(R.id.tv_3);//WiFi列表
        tv_4=findViewById(R.id.tv_4);//添加用户
        tv_5=findViewById(R.id.tv_5);//添加新闻
        tv_6=findViewById(R.id.tv_6);//修改简介

        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_3.setOnClickListener(this);
        tv_4.setOnClickListener(this);
        tv_5.setOnClickListener(this);
        tv_6.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        int id=v.getId();
        switch (id){
            case R.id.tv_1:
                intent=new Intent(this,News_Activity.class);
                break;
            case R.id.tv_2:
                intent=new Intent(this,LiuYans_Activity.class);
                break;
            case R.id.tv_3:
                intent=new Intent(this,WiFi_Activity.class);
                break;
            case R.id.tv_4:
                intent=new Intent(this,Add_User_Activity.class);
                break;
            case R.id.tv_5:
                intent=new Intent(this,Add_News_Activity.class);
                break;
            case R.id.tv_6:
                intent=new Intent(this,Web_Abouts_Activity.class);
                break;
        }

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_item:
                App.logout(this);
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
