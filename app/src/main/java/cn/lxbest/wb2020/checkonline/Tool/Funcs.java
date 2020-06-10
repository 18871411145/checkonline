package cn.lxbest.wb2020.checkonline.Tool;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 通用方法类
 * */
public class Funcs {



    //将byte数组装换成json
    public static JSONObject bytetojson(byte[] response){
        if(response==null)return null;
        try {
            return new JSONObject(new String(response));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static  void showtoast(Context context, String s){
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
    }

    /**判断一个key是否在jsonobject中存在且不为null*/
    public static boolean jsonItemValid(JSONObject jo, String key) {
        boolean v = false;
        try {
            v = jo.has(key) && !isNull(jo.get(key));
        }catch (Exception e) {}
        return v;
    }

    public static boolean isNull(Object o) {
        return o == null || o.toString().toLowerCase().equals("null")||o.toString().length()<=0;
    }


    //将我们服务器和请求连接起来
    public static String servUrl(String url){
        return combineUrl(Const.server, url);
    }

    //将参数，请求，服务器串起来
    public static String servUrlWQ(String url, String query) {
        String u = combineUrl(Const.server, url);
        u += (query == null || query.trim().length() == 0) ? "" : ((query.trim().indexOf('?') == 0 ? "" : "?") + query.trim());
        return u;
    }

    //将七牛服务器和id连接起来
    public static String qnUrl(String qnid) {
        return combineUrl(Const.qnserver, qnid);
    }



    //将服务器和请求连接起来
    public static String combineUrl(String part1, String part2) {
        return (part1 != null ? trim(part1, "/") : "") + (part2 != null ? ("/" + trim(part2, "/")) : "");
    }

    public static String trim(String s1, String s2) {
        int i1 = 0, i2 = s1.length();
        while (i1 < i2 && s1.substring(i1, i1 + 1).equals(s2)) i1++;
        while (i2 > 0 && s1.substring(i2 - 1, i2).equals(s2)) i2--;

        return s1.substring(i1, i2);
    }

    //回调接口
    public interface CallbackInterface {
        void onCallback(Object obj);
    }


}
