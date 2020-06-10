package cn.lxbest.wb2020.checkonline;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.OptionsPickerView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cn.lxbest.wb2020.checkonline.Tool.Const;
import cn.lxbest.wb2020.checkonline.Tool.Funcs;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;

public class Add_User_Activity extends AppCompatActivity {

    EditText edit_name,edit_mobile,edit_gsmc,edit_yzm;

    Button add_btn,btn_yzm;

    OptionsPickerView pickerView;

    TextView text_role;

    private int role=-1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("添加用户");
        setContentView(R.layout.add_user_activity);
        edit_name=findViewById(R.id.ygxm_EditText);
        edit_mobile=findViewById(R.id.mobile_EditText);
        edit_gsmc=findViewById(R.id.gsmc_EditText);
        edit_yzm=findViewById(R.id.yzm_EditText);
        text_role=findViewById(R.id.role_textview);
        add_btn=findViewById(R.id.add_btn);
        btn_yzm=findViewById(R.id.button1);

        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                postData();
            }
        });

        text_role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerView.show();
            }
        });

        btn_yzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_yzm.setEnabled(false);
                getYZM();
            }
        });

        pickerView=new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                text_role.setText(Const.Role.roles.get(options1));
                role=Const.Role.roleCode.get(options1);
            }
        }).build();

        pickerView.setPicker(Const.Role.roles);
    }

    //发送验证码
    void getYZM(){
        String phone = edit_mobile.getText().toString().trim();

        if(phone.length()==0){
            Funcs.showtoast(this,"手机号不能为空");
            btn_yzm.setEnabled(true);
            return;
        }

        String url= Funcs.servUrlWQ(Const.Key_Resp_Path.add_user,"step="+1);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put(Const.Field_Table_User.phone, phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        HttpEntity entity=new StringEntity(jsonObject.toString(), HTTP.UTF_8);

        App.http.post(this, url, entity, Const.contentType, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject jsonObject1= Funcs.bytetojson(responseBody);
                if(jsonObject1!=null){
                    parsePhone(jsonObject1);
                }else{
                    btn_yzm.setEnabled(true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(App.env== Const.Env.DEV_TD){
                    Funcs.showtoast(Add_User_Activity.this,"发送失败");
                }else{
                    Funcs.showtoast(Add_User_Activity.this,"发送失败");
                }

                btn_yzm.setEnabled(true);
            }
        });
    }


    void parsePhone(JSONObject jsonObject){
        try {
            int code=jsonObject.getInt(Const.Key_Resp.Code);
            if(code==200){
                Funcs.showtoast(Add_User_Activity.this,"发送成功");
            } else{
                Funcs.showtoast(Add_User_Activity.this,"发送失败");
            }
            btn_yzm.setEnabled(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //提交用户信息
    private void postData(){
        add_btn.setEnabled(false);
        String name=edit_name.getText().toString().trim();
        String mobile=edit_mobile.getText().toString().trim();
        String gsmc=edit_gsmc.getText().toString().trim();
        String yzm=edit_yzm.getText().toString().trim();
        if(name.length()==0||mobile.length()==0||gsmc.length()==0||role<0){
            Funcs.showtoast(this,"请完善信息后提交");
            return;
        }

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put(Const.Field_Table_User.name,name);
            jsonObject.put(Const.Field_Table_User.phone,mobile);
            jsonObject.put(Const.Field_Table_User.gsmc,gsmc);
            jsonObject.put(Const.Field_Table_User.role,role);
            jsonObject.put(Const.YZM,yzm);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url= Funcs.servUrlWQ(Const.Key_Resp_Path.add_user,"step="+2);

        HttpEntity entity=new StringEntity(jsonObject.toString(), HTTP.UTF_8);

        App.http.post(this, url, entity, Const.contentType, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject jsonObject1=Funcs.bytetojson(responseBody);
                if(jsonObject1!=null){
                parseData(jsonObject1);
                }

                add_btn.setEnabled(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Funcs.showtoast(Add_User_Activity.this,"获取数据失败");
                add_btn.setEnabled(true);
            }
        });
    }

    private void parseData(JSONObject jsonObject){
        try {
            int code=jsonObject.getInt(Const.Key_Resp.Code);
            if(code==200){
                Funcs.showtoast(Add_User_Activity.this,"添加成功");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
