package cn.lxbest.wb2020.checkonline.Modle;

import org.json.JSONObject;

import cn.lxbest.wb2020.checkonline.Tool.Const;
import cn.lxbest.wb2020.checkonline.Tool.Funcs;

/**新闻信息容器*/
public class LiuYan {
    public int id;//新闻id
    public String phone;//留言电话
    public String content;//留言内容


    public LiuYan(JSONObject jsonObject) {
        try{
            if(Funcs.jsonItemValid(jsonObject, Const.Field_Table_LiuYan.id)) id=jsonObject.getInt(Const.Field_Table_LiuYan.id);
            if(Funcs.jsonItemValid(jsonObject,Const.Field_Table_LiuYan.phone)) phone=jsonObject.getString(Const.Field_Table_LiuYan.phone);
            if(Funcs.jsonItemValid(jsonObject,Const.Field_Table_LiuYan.content)) content=jsonObject.getString(Const.Field_Table_LiuYan.content);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
