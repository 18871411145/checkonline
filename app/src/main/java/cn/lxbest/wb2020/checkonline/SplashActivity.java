package cn.lxbest.wb2020.checkonline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import cn.lxbest.wb2020.checkonline.Tool.Const;
import cn.lxbest.wb2020.checkonline.Tool.Funcs;

/**开始界面activity*/
public class SplashActivity extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        if(prefExisted()){//判断preference是否有数据
            App.refreshUserFromPreference();
            Intent intent=new Intent(this, Home_Activity.class);
            startActivity(intent);
        }else{
            //preference里面没有电话数据，表示没有登陆过，前往登录界面
            Intent intent=new Intent(this,Login_activity.class);
            startActivity(intent);
        }
    }



    //if preference account existed ?
    private boolean prefExisted() {
        SharedPreferences prefs = App.sharedPreferences(Const.Key_SharedPref.Account);
        String mobile=prefs.getString(Const.Field_Table_User.phone, null);

        return !Funcs.isNull(mobile);
    }
}
