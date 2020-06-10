package cn.lxbest.wb2020.checkonline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.lxbest.wb2020.checkonline.Tool.Const;
import cn.lxbest.wb2020.checkonline.Tool.Funcs;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;

public class Add_News_Activity extends AppCompatActivity implements View.OnClickListener {

    EditText et_xtitle,et_xcontent;

    Button btn_addnews;

    ImageView image1,image2,image3;
    ImageView image_temp;//临时储存标量
    int index=1;

    AlertDialog alertDialog;//确认对话框
    //存放从qn返回的qnid
    Map<String,String> qnids=new HashMap<>();

    int xid=0;//修改新闻页面带过来的id

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_news_activity);

        setTitle("添加新闻");
        et_xtitle=findViewById(R.id.et_title);//新闻标题
        et_xcontent=findViewById(R.id.et_content);//新闻内容

        btn_addnews=findViewById(R.id.button2);//添加按钮

        image1=findViewById(R.id.image1);//添加的第1张图
        image2=findViewById(R.id.image2);//添加的第2张图
        image3=findViewById(R.id.image3);//添加的第3张图
        image_temp=image1;

        btn_addnews.setOnClickListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);

        alertDialog=App.getAlterDialog(this, "是否提交", "", new Funcs.CallbackInterface() {
            @Override
            public void onCallback(Object obj) {
                postData();
            }
        });

        Intent intent=getIntent();
        xid=intent.getIntExtra("xid",0);
        if(xid!=0){
            et_xtitle.setText(intent.getStringExtra("xtitle"));
            et_xcontent.setText(intent.getStringExtra("xcontent"));
            Picasso.with(this).load(Funcs.qnUrl(intent.getStringExtra("qnid"))).into(image2);
        }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.image1:
                image_temp=image1;
                index=1;
                getImage();
                break;
            case R.id.image2:
                image_temp=image2;
                index=2;
                getImage();
                break;
            case R.id.image3:
                image_temp=image3;
                index=3;
                getImage();
                break;
            case R.id.button2:
                //提交新闻
                alertDialog.show();
                break;
        }
    }

    //提交 修改 新闻数据
    void postData(){
        btn_addnews.setEnabled(false);
        String url=null;
        if(xid==0){
            //添加新闻
            url=Funcs.servUrlWQ(Const.Key_Resp_Path.news,"uid="+App.user.uid);
        }else{
            //修改新闻
            url=Funcs.servUrlWQ(Const.Key_Resp_Path.news,"xid="+xid+"&type="+1);
        }

        String t=et_xtitle.getText().toString().trim();
        if(t.length()==0){
            Funcs.showtoast(this,"新闻标题不能为空");
            return;
        }
        String c=et_xcontent.getText().toString().trim();
        if(c.length()==0){
            Funcs.showtoast(this,"新闻内容不能为空");
            return;
        }

        JSONObject jsonObject=new JSONObject();

        try {
            jsonObject.put(Const.Field_Table_Xinwen.title,t);
            jsonObject.put(Const.Field_Table_Xinwen.content,c);
            jsonObject.put("qnids",new JSONObject(qnids));

            HttpEntity entity=new StringEntity(jsonObject.toString(), HTTP.UTF_8);

            App.http.post(this, url, entity, Const.contentType, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    JSONObject js=Funcs.bytetojson(responseBody);
                    if(js!=null){
                        parseData(js);
                    }
                    btn_addnews.setEnabled(true);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Funcs.showtoast(Add_News_Activity.this,"获取数据失败");

                    btn_addnews.setEnabled(true);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    void parseData(JSONObject jsonObject){
        try{
            int code=jsonObject.getInt(Const.Key_Resp.Code);
            if(code==200){
                Funcs.showtoast(Add_News_Activity.this,xid==0?"添加成功":"修改成功");
            }else{
                Funcs.showtoast(Add_News_Activity.this,"错误代码");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //向相册拿图片
    private void getImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent,"上传图片"),22);
        App.showLoadingMask(this);
    }

    //从相册返回数据流
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==22&&resultCode== Activity.RESULT_OK&&data!=null){
            Uri uri=data.getData();
            try {
                InputStream is=this.getContentResolver().openInputStream(uri);
                Bitmap bitmap= BitmapFactory.decodeStream(is);
                image_temp.setImageBitmap(bitmap);
                InputStream inputStream=this.getContentResolver().openInputStream(uri);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int n = 0;
                while (-1 != (n = inputStream.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                byte[] bytes=output.toByteArray();
                is.close();
                output.close();
                //将读出的字节传到qn服务器
                App.postImgToQnServer(bytes, new Funcs.CallbackInterface() {
                    @Override
                    public void onCallback(Object obj) {
                        if(obj==null){
                            //上传失败
                            App.hideLoadingMask(Add_News_Activity.this);
                            Funcs.showtoast(Add_News_Activity.this,"上传失败");
                        }else{
                            String qnid= (String) obj;
                            qnids.put("image"+index,qnid);
                            App.hideLoadingMask(Add_News_Activity.this);
                            Funcs.showtoast(Add_News_Activity.this,"上传成功");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            App.hideLoadingMask(Add_News_Activity.this);
        }
    }
}
