package cn.lxbest.wb2020.checkonline;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.lxbest.wb2020.checkonline.Tool.Const;
import cn.lxbest.wb2020.checkonline.Tool.Funcs;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;


/**修改web主页的 关于园区 和 京工空间*/
public class Web_Abouts_Activity extends AppCompatActivity implements View.OnClickListener {

    EditText et_js,et_js2;//园区介绍

    EditText et_foot1,et_foot2,et_foot3;//脚步信息： 联系电话  联系邮箱

    Button btn_submit;//提交按钮

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_abouts_activity);

        setTitle("修改介绍");

        et_js=findViewById(R.id.et_about);
        et_js2=findViewById(R.id.et_about2);

        et_foot1=findViewById(R.id.et_foot1);
        et_foot2=findViewById(R.id.et_foot2);
        et_foot3=findViewById(R.id.et_foot3);

        btn_submit=findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(this);

        getData();

    }

    //获取主页简介信息
    void getData(){
        String url=Funcs.servUrl(Const.Key_Resp_Path.js);
        App.http.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject jsonObject=Funcs.bytetojson(responseBody);
                if(jsonObject!=null){
                    fillData(jsonObject);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Funcs.showtoast(Web_Abouts_Activity.this,"获取数据失败");
            }
        });
    }

    void fillData(JSONObject jsonObject){
        try {
            int code=jsonObject.getInt(Const.Key_Resp.Code);
            if(code==200){
                JSONArray data=jsonObject.getJSONArray(Const.Key_Resp.Data);
                et_js.setText(data.getJSONObject(0).getString("js1"));
                et_js2.setText(data.getJSONObject(0).getString("js2"));
                et_foot1.setText(data.getJSONObject(0).getString("foot1"));
                et_foot2.setText(data.getJSONObject(0).getString("foot2"));
                et_foot3.setText(data.getJSONObject(0).getString("foot3"));
            }else{
                Funcs.showtoast(Web_Abouts_Activity.this,"错误代码");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        //提交园区介绍信息给服务器
        postData();
    }


    void postData(){
        btn_submit.setEnabled(false);
        String url= Funcs.servUrl(Const.Key_Resp_Path.js);

        String js1=et_js.getText().toString().trim();
        String js2=et_js2.getText().toString().trim();

        String foot1=et_foot1.getText().toString().trim();
        String foot2=et_foot2.getText().toString().trim();
        String foot3=et_foot3.getText().toString().trim();

        JSONObject js=new JSONObject();

        try {
            js.put("js1",js1);
            js.put("js2",js2);
            js.put("foot1",foot1);
            js.put("foot2",foot2);
            js.put("foot3",foot3);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpEntity entity=new StringEntity(js.toString(), HTTP.UTF_8);

        App.http.post(this, url, entity, Const.contentType, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject jsonObject=Funcs.bytetojson(responseBody);
                if(jsonObject!=null){
                    parseData(jsonObject);
                }

                btn_submit.setEnabled(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Funcs.showtoast(Web_Abouts_Activity.this,"获取数据失败");
                btn_submit.setEnabled(true);
            }
        });
    }

    void parseData(JSONObject jsonObject){
        try{
            int code=jsonObject.getInt(Const.Key_Resp.Code);
            if(code==200){
                Funcs.showtoast(Web_Abouts_Activity.this,"修改成功");
            }else{
                Funcs.showtoast(Web_Abouts_Activity.this,"错误代码");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
