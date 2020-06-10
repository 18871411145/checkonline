package cn.lxbest.wb2020.checkonline.Modle;

import org.json.JSONObject;

import java.util.Comparator;

import cn.lxbest.wb2020.checkonline.Tool.Const;
import cn.lxbest.wb2020.checkonline.Tool.Funcs;

/**新闻信息容器*/
public class News implements Comparable<News> {
    public int xid;//新闻id
    public String xtitle;//新闻标题
    public String xcontent;//新闻内容
    public String fbsj;//新闻发布日期
    public String image1;//图片qnid
//    public String image2;
//    public String image3;
    public int adder;//发布人id

    public News(JSONObject jsonObject) {
        try{
            if(Funcs.jsonItemValid(jsonObject, Const.Field_Table_Xinwen.xid)) xid=jsonObject.getInt(Const.Field_Table_Xinwen.xid);
            if(Funcs.jsonItemValid(jsonObject,Const.Field_Table_Xinwen.title)) xtitle=jsonObject.getString(Const.Field_Table_Xinwen.title);
            if(Funcs.jsonItemValid(jsonObject,Const.Field_Table_Xinwen.content)) xcontent=jsonObject.getString(Const.Field_Table_Xinwen.content);
            if(Funcs.jsonItemValid(jsonObject,Const.Field_Table_Xinwen.fbsj)) fbsj=jsonObject.getString(Const.Field_Table_Xinwen.fbsj);
            if(Funcs.jsonItemValid(jsonObject,Const.Field_Table_Xinwen.image)) image1=jsonObject.getString(Const.Field_Table_Xinwen.image);
            if(Funcs.jsonItemValid(jsonObject, Const.Field_Table_Xinwen.adder)) adder=jsonObject.getInt(Const.Field_Table_Xinwen.adder);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public int compareTo(News o) {
        int i=o.xid-this.xid;
        return i;
    }
}
