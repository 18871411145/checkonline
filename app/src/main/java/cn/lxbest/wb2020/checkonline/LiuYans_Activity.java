package cn.lxbest.wb2020.checkonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.lxbest.wb2020.checkonline.Modle.LiuYan;
import cn.lxbest.wb2020.checkonline.Modle.News;
import cn.lxbest.wb2020.checkonline.Tool.Const;
import cn.lxbest.wb2020.checkonline.Tool.Funcs;
import cz.msebera.android.httpclient.Header;

/**新闻展示页面*/
public class LiuYans_Activity extends AppCompatActivity implements OnRefreshListener, OnLoadMoreListener {

    ListView listView;//新闻列表
    List<LiuYan> list=new ArrayList<>();//适配器列表
    NewsAdapter adapter=new NewsAdapter();
    RefreshLayout refreshLayout;//刷新list外围框架
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.news_activity);

        setTitle("留言列表");

        listView=findViewById(R.id.lv_xinwen);
        refreshLayout=findViewById(R.id.refresh);
        adapter=new NewsAdapter();

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击进入详情页
                String phone=list.get(position).phone;
                String liuyan=list.get(position).content;
                Intent intent=new Intent(LiuYans_Activity.this,LiuYanXQ_Activity.class);
                intent.putExtra("phone",phone);
                intent.putExtra("liuyan",liuyan);
                startActivity(intent);
            }
        });

        getData(false);
    }

    int have=0;//显示留言数量
    //获取新闻信息
    private void getData(boolean more){
        list=new ArrayList<>();
        String url= Funcs.servUrl(Const.Key_Resp_Path.lianxiwm);
        if(more){
            url=Funcs.servUrlWQ(Const.Key_Resp_Path.lianxiwm,"m="+have);
        }

        App.http.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject jsonObject=Funcs.bytetojson(responseBody);
                if(jsonObject!=null){
                    parseData(jsonObject);
                }else{
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Funcs.showtoast(LiuYans_Activity.this,"连接服务器失败");
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }
        });
    }


    void parseData(JSONObject jsonObject){
        try {
            int code=jsonObject.getInt(Const.Key_Resp.Code);
            if(code==200){
                JSONArray data=jsonObject.getJSONArray(Const.Key_Resp.Data);
                for(int i=0;i<data.length();i++){
                    JSONObject js=data.getJSONObject(i);
                    list.add(new LiuYan(js));
                }
                have=list.size();
                Collections.sort(list);
                listView.setAdapter(adapter);
            }else{
                Funcs.showtoast(LiuYans_Activity.this,"返回错误代码");
            }
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData(false);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        getData(true);
    }

    class Container{
        TextView tv_title,tv_content;
    }

    class NewsAdapter extends BaseAdapter{

        Container container;
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view==null){
                container=new Container();
                view= LayoutInflater.from(getBaseContext()).inflate(R.layout.news_listitem,null);
                container.tv_title=view.findViewById(R.id.tv_title);
                container.tv_content=view.findViewById(R.id.tv_content);
                view.setTag(container);
            }else container= (Container) view.getTag();

            LiuYan data=list.get(position);

            container.tv_title.setText(data.phone);
            container.tv_content.setText(data.content);

            return view;
        }
    }
}
