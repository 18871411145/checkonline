package cn.lxbest.wb2020.checkonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.lxbest.wb2020.checkonline.Modle.Wifi;
import cn.lxbest.wb2020.checkonline.Tool.Const;
import cn.lxbest.wb2020.checkonline.Tool.Funcs;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;

public class WiFi_Activity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private WifiManager wifiManager;

    ListView listView;
    List<Wifi> list_wifi=new ArrayList<>();
    WifiAdapter adapter=new WifiAdapter();

    Button btn_refresh,btn_post;



    private int state=1; //1：普通状态  2：选择状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("WiFi");
        listView=findViewById(R.id.list_wifi);
        btn_refresh=findViewById(R.id.btn_refresh);
        btn_post=findViewById(R.id.btn_post);
        btn_post.setVisibility(View.GONE);
        //必须用全局上下文调用
        wifiManager= (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);



//        //检查手机是否有定位权限，如果没有就申请授权
//        if(PermissionChecker.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PermissionChecker.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this,new String[] { Manifest.permission.ACCESS_FINE_LOCATION}, 22);
//        }else{
            getWifiList();
//        }


        //刷新列表
        btn_refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getWifiList();
            }
        });

        //上传选中WiFi
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWiFiToService();
            }
        });


        //点击先择WiFi提交
        listView.setOnItemClickListener(this);
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode==22){
//            if(grantResults[0]==PermissionChecker.PERMISSION_GRANTED){
//                //成功获取权限
//                Toast.makeText(WiFi_Activity.this,"授权成功",Toast.LENGTH_LONG).show();
//                getWifiList();
//            }else{
//                Toast.makeText(WiFi_Activity.this,"授权失败",Toast.LENGTH_LONG).show();
//            }
//
//        }
//    }

    //获取wifi列表
    void getWifiList(){
        if(wifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLED){

            WifiInfo info=wifiManager.getConnectionInfo();
            Log.i("wifi:",info.getSSID());

            List<ScanResult> list=wifiManager.getScanResults();

            list_wifi=new ArrayList<>();
            for(ScanResult sr:list){
                list_wifi.add(new Wifi(sr));
            }

            listView.setAdapter(adapter);

        }else{
            Toast.makeText(this,"请打开wifi连接",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
        final TextView name_wifi=view.findViewById(R.id.wifi_ssid);
        if(state==1) return;
        //如果不是选中状态将对应WiFi变为选中状态
        if(!list_wifi.get(position).ifON){
            list_wifi.get(position).ifON=true;
            name_wifi.setTextColor(getResources().getColor(R.color.red));
        }else {
            list_wifi.get(position).ifON=false;
            name_wifi.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void sendWiFiToService() {
        btn_post.setEnabled(false);
        String url= Funcs.servUrl(Const.Key_Resp_Path.post_wifi);

        JSONObject jsonObject=new JSONObject();
        List<HashMap<String,Object>> list=new ArrayList<>();

        for(final Wifi wifi:list_wifi){
            if(wifi.ifON) list.add(new HashMap<String, Object>(){{
                put(Const.Field_wifi_Table.wifiSSID,wifi.ssid);
                put(Const.Field_Table_User.uid,App.user.uid);
            }});
        }

        if(list.size()==0){
            closemode();
            return;
        }

        try {
            jsonObject.put(Const.Key_Resp.Data,new JSONArray(list));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpEntity entity=new StringEntity(jsonObject.toString(), HTTP.UTF_8);

        App.http.post(this,url,entity,Const.contentType, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject jsonObject=Funcs.bytetojson(responseBody);
                if(jsonObject!=null){
                parseData(jsonObject);
                }

                btn_post.setEnabled(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(App.env==Const.Env.DEV_TD){
                    Funcs.showtoast(WiFi_Activity.this,"添加成功");
                    closemode();
                }else{
                    Funcs.showtoast(WiFi_Activity.this,"添加失败");
                    closemode();
                }

                btn_post.setEnabled(true);
            }
        });
    }

    private  void parseData(JSONObject jsonObject){
        try {
            int code=jsonObject.getInt(Const.Key_Resp.Code);
            if(code==200){
                Funcs.showtoast(this,"添加成功");
            }else{

            }

            closemode();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class Container{
        TextView ssid;
    }

    class WifiAdapter extends BaseAdapter{

        Container container;

        @Override
        public int getCount() {
            return list_wifi.size();
        }

        @Override
        public Object getItem(int position) {
            return list_wifi.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            if(view==null){
                view= LayoutInflater.from(getBaseContext()).inflate(R.layout.wifi_listitem,null);
                container=new Container();
                container.ssid=view.findViewById(R.id.wifi_ssid);
                view.setTag(container);
            }else container= (Container) view.getTag();

            Wifi data=list_wifi.get(position);
            container.ssid.setText(data.ssid);
            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_wifi,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent=null;
        switch (item.getItemId()){
            case R.id.add_wifi:
                if(state==1){
                    openmode();
                }else{
                    closemode();
                }
                break;
        }
        return true;
    }


    //开启/关闭 wifi选择模式
    private void openmode(){
        btn_refresh.setVisibility(View.GONE);
        btn_post.setVisibility(View.VISIBLE);
        state=2;
    }
    //关闭选择模式
    private void closemode(){
        btn_refresh.setVisibility(View.VISIBLE);
        btn_post.setVisibility(View.GONE);
        //将所有选中状态变为未选中状态
        for(Wifi wifi:list_wifi){
            wifi.ifON=false;
        }
        getWifiList();
        state=1;
    }


}
