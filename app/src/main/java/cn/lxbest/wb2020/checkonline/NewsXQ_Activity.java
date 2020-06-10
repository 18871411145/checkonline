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

public class NewsXQ_Activity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_title,tv_content;

    Button btn_xg,btn_sc;

    ImageView iv_image;

    int xid=0;

    String qnid;

    AlertDialog alertDialog;//确认对话框
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsxq_activity);
        setTitle("新闻详情");
        tv_title=findViewById(R.id.tv_title);//新闻标题
        tv_content=findViewById(R.id.tv_content);//新闻具体内容

        btn_xg=findViewById(R.id.btn_xg);//修改按钮
        btn_sc=findViewById(R.id.btn_sc);//删除按钮

        iv_image=findViewById(R.id.iv_image);

        btn_xg.setOnClickListener(this);
        btn_sc.setOnClickListener(this);

        Intent intent=getIntent();
        xid=intent.getIntExtra("xid",0);

        alertDialog=App.getAlterDialog(this, "是否删除", "", new Funcs.CallbackInterface() {
            @Override
            public void onCallback(Object obj) {
                deleteNews();
            }
        });

        if(xid!=0){
            //得到新闻id获取新闻信息
            getData();
        }else{
            Funcs.showtoast(NewsXQ_Activity.this,"没有新闻id");
        }
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.btn_xg:
                //修改新闻

                String xtitle=tv_title.getText().toString().trim();
                String xcontent=tv_content.getText().toString().trim();

                Intent intent=new Intent(this,Add_News_Activity.class);
                intent.putExtra("xid",xid);
                intent.putExtra("xtitle",xtitle);
                intent.putExtra("xcontent",xcontent);
                intent.putExtra("qnid",qnid);
                startActivity(intent);
                break;
            case R.id.btn_sc:
                //删除新闻
                alertDialog.show();
                break;
        }
    }

    //删除新闻
    void deleteNews(){
        btn_sc.setEnabled(false);
        String url=Funcs.servUrlWQ(Const.Key_Resp_Path.news,"xid="+xid+"&type="+2);
        App.http.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject jsonObject=Funcs.bytetojson(responseBody);
                if(jsonObject!=null){
                    parseData1(jsonObject);
                }

                btn_sc.setEnabled(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Funcs.showtoast(NewsXQ_Activity.this,"获取数据失败");
                btn_sc.setEnabled(true);
            }
        });
    }

    void parseData1(JSONObject jsonObject){
        try {
            int code=jsonObject.getInt(Const.Key_Resp.Code);
            if(code==200){
                Funcs.showtoast(NewsXQ_Activity.this,"删除成功");
            }else{
                Funcs.showtoast(NewsXQ_Activity.this,"错误代码");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
                Funcs.showtoast(NewsXQ_Activity.this,"数据获取失败");
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
                Funcs.showtoast(NewsXQ_Activity.this,"错误代码");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
