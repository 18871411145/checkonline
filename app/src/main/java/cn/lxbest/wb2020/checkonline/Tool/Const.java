package cn.lxbest.wb2020.checkonline.Tool;


import java.util.ArrayList;
import java.util.List;

public class Const {

    public static final String domain = "cn.lxbest.wb2020.checkonline";
    public static final String server = "http://39.100.102.110:8080/checkonline_service";
    public static final String qnserver = "http://qnyeyimg.lxbest.cn";
    public static final String contentType="application/json";


    public static class Key_SharedPref {
        public static String Account = "acc"; //用于保存账户信息的key
    }

    public static class Env {
        /**
         * 开发环境+客户端假数据
         */
        public static final int DEV_TD = 1;
        /**
         * 开发环境+服务器ok
         */
        public static final int DEV_OK = 2;


    }


    public static class Key_Resp {

        public static String Code = "code";
        public static String Data = "data";

    }
    /**请求url*/
    public static class Key_Resp_Path{

        //向服务器拿qntoken
        public static String QnToken = "/qntoken";
        //登录
        public static String login ="login";
        //传wifi ssid给服务器
       public static String post_wifi ="wifi";
       //添加用户
       public static String add_user ="add_user";
       //新闻相关请求
        public  static String news ="news";
       //修改园区介绍
        public static String js ="js";
       //获取web留言
        public static String lianxiwm="lianxiwm";
    }



    //用户字段
    public static class Field_Table_User{
        public static String uid ="uid";//用户id
        public static String name ="name";//员工姓名
        public static String phone ="phone";//电话号码
        public static String gsmc="company";//公司名称
        public static String role="role";//角色
    }

    public static String YZM="yzm";//验证码

    //wifi
    public static class Field_wifi_Table{
        public static String wifiSSID="ssid";
    }

    //role
    public static class Role{
        public static int Admin=990;//管理员
        public static int member=980;//普通员工

        public static String Admin_value="管理员";
        public static String member_value="普通员工";

        public static List<String> roles = new ArrayList<String>() {{
            add(Admin_value);
            add(member_value);
        }};

        public static List<Integer> roleCode = new ArrayList<Integer>() {{
            add(Admin);
            add(member);
        }};
    }

    //新闻
    public static class Field_Table_Xinwen{
        public static String xid="xid";
        public static String title="xtitle";
        public static String content="xcontent";
        public static String image="image";
        public static String adder="adder";
        public static String fbsj="fbsj";
    }

    //留言
    public static class Field_Table_LiuYan{
        public static String id="id";
        public static String phone="phone";
        public static String content="content";

    }

}
