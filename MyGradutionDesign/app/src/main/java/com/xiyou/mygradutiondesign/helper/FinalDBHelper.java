package com.xiyou.mygradutiondesign.helper;

import com.xiyou.mygradutiondesign.sqLite.bean.DestPicInfoBean;
import com.xiyou.mygradutiondesign.sqLite.bean.PictureInfoBean;

import net.tsz.afinal.FinalDb;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by fengyi on 16/3/15.
 */
public class FinalDBHelper {

    private static FinalDb db;

    private static FinalDb createFinalDB(){
        if (db == null) {
            synchronized (FinalDBHelper.class) {
                if (db == null) {
                    db = FinalDb.create(ApplicationHelper.getApplication());
                }
            }
        }
        return db;
    }

    public static void savePicInfo(PictureInfoBean bean) {
        db = createFinalDB();
        db.save(bean);
    }

    public static void saveDestPicInfo(DestPicInfoBean bean) {
        db = createFinalDB();
        db.save(bean);
    }

    public static void delatePicInfoById(Class<? extends Object> clazz, int id) {
        db = createFinalDB();
        db.deleteById(clazz, id);
    }

    public static void delateDestPicInfoById(Class<? extends Object> clazz, int id) {
        db = createFinalDB();
        db.deleteById(clazz, id);
    }

    public static void deleteObject(Object object) {
        db = createFinalDB();
        db.delete(object);
    }

    public static void deleteByWhere(Class<? extends Object> clazz, String key, Object value){
        db = createFinalDB();
        db.deleteByWhere(clazz, key + "=\"" + value + "\"");
    }

    public static void update(Object object){
        db = createFinalDB();
        db.update(object);
    }

    public static void updateByWhere(Object object, String key, Object value){
        db = createFinalDB();
        db.update(object, key + "=\"" +value + "\"");
    }

    public static PictureInfoBean findPicById(int id, Class<? extends Object> clazz){
        db = createFinalDB();
        return (PictureInfoBean) db.findById(id, clazz);
    }

    public static DestPicInfoBean findDestPicById(int id, Class<? extends Object> clazz){
        db = createFinalDB();
        return (DestPicInfoBean) db.findById(id, clazz);
    }

    public static List<PictureInfoBean> findAll(Class<? extends Object> clazz){
        db = createFinalDB();
        return (List<PictureInfoBean>) db.findAll(clazz);
    }

    public static List<DestPicInfoBean> findDestAll(Class<? extends Object> clazz){
        db = createFinalDB();
        return (List<DestPicInfoBean>) db.findAll(clazz);
    }

    public static List<PictureInfoBean> findAllByWhere(Class<? extends Object> clazz, String key, Object value){
        db = createFinalDB();
        return (List<PictureInfoBean>) db.findAllByWhere(clazz, key + "=\"" + value + "\"");
    }

    public static List<DestPicInfoBean> findDestAllByWhere(Class<? extends Object> clazz, String key, Object value){
        db = createFinalDB();
        return (List<DestPicInfoBean>) db.findAllByWhere(clazz, key + "=\"" + value + "\"");
    }

    /**
     * 模糊搜索
     *
     * @param clazz
     * @param key
     * @param value
     * @return
     */
    public static List<PictureInfoBean> findAllByWhereGenerally(Class<? extends Object> clazz, String key, Object value){
        db = createFinalDB();

        return (List<PictureInfoBean>) db.findAllByWhere(clazz, key + " like \'%" + value + "%\'");
    }

    /**
     * 模糊搜索--参数有多条
     *
     * @param clazz
     * @param key
     * @param value
     * @return
     */
    public static List<PictureInfoBean> findAllByWhereGenerally(Class<? extends Object> clazz, List<Map<String, Object>> params){
        if (params == null || params.size() == 0) {
            return null;
        }
        db = createFinalDB();

        StringBuffer buffer = new StringBuffer();
        //构造参数
        for (int i = 0; i < params.size(); i++){
            Iterator iterator = params.get(i).entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry entry = (Map.Entry)iterator.next();
                buffer.append(entry.getKey());
                buffer.append(" like \'%");
                buffer.append(entry.getValue());
                buffer.append("%\' and ");
            }
        }

        String string = buffer.toString();
        string = string.substring(0, string.length() - 4);
System.out.append("string="+string);
        return (List<PictureInfoBean>) db.findAllByWhere(clazz, string);
    }

}
