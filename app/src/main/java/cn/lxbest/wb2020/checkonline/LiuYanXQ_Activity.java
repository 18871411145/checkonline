package cn.lxbest.wb2020.checkonline;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cn.lxbest.wb2020.checkonline.Tool.Const;
import cn.lxbest.wb2020.checkonline.Tool.Funcs;
import cz.msebera.android.httpclient.Header;

public class LiuYanXQ_Activity extends AppCompatActivity {

    TextView tv_title,tv_content;

    ImageView iv_image;

    int xid=0;

    String qnid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liuyanxq_activity);
        setTitle("留言详情");
        tv_title=findViewById(R.id.tv_title);
        tv_content=findViewById(R.id.tv_content);

        Intent intent=getIntent();

        tv_title.setText(intent.getStringExtra("phone"));
        tv_content.setText(intent.getStringExtra("liuyan"));
    }



    //得到新闻详情
    void getData(){
        String url= Funcs.servUrlWQ(Const.Key_Resp_Path.news,"xid="+xid);

        App.http.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject jsonObject=Funcs.bytetojson(responseBody);
                if(jsonObject!=null){
                    parseData(jsonObject);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Funcs.showtoast(LiuYanXQ_Activity.this,"数据获取失败");
            }
        });
    }

    void parseData(JSONObject jsonObject){
        try {
            int code=jsonObject.getInt(Const.Key_Resp.Code);
            if(code==200){
                JSONObject data=jsonObject.getJSONObject(Const.Key_Resp.Data);
                if(Funcs.jsonItemValid(data,Const.Field_Table_Xinwen.title)) tv_title.setText(data.getString(Const.Field_Table_Xinwen.title));
                if(Funcs.jsonItemValid(data,Const.Field_Table_Xinwen.content)) tv_content.setText(data.getString(Const.Field_Table_Xinwen.content));
                if (Funcs.jsonItemValid(data,Const.Field_Table_Xinwen.image)){
                    qnid=data.getString(Const.Field_Table_Xinwen.image);
                    Picasso.with(this).load(Funcs.qnUrl(qnid)).into(iv_image);
                }
            }else{
                Funcs.showtoast(LiuYanXQ_Activity.this,"错误代码");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
