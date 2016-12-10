package com.xiyou.mygradutiondesign.presenter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.xiyou.mygradutiondesign.R;
import com.xiyou.mygradutiondesign.constant.ArgumentConst;
import com.xiyou.mygradutiondesign.constant.RequestConst;
import com.xiyou.mygradutiondesign.helper.ApplicationHelper;
import com.xiyou.mygradutiondesign.helper.FileHelper;
import com.xiyou.mygradutiondesign.helper.FinalDBHelper;
import com.xiyou.mygradutiondesign.helper.ImageIdentificationHelper;
import com.xiyou.mygradutiondesign.helper.PictureHelper;
import com.xiyou.mygradutiondesign.helper.PrefHelper;
import com.xiyou.mygradutiondesign.sqLite.bean.DestPicInfoBean;
import com.xiyou.mygradutiondesign.sqLite.bean.PictureInfoBean;
import com.xiyou.mygradutiondesign.sqLite.bean.TypeInfoBean;
import com.xiyou.mygradutiondesign.util.ToastUtil;

import net.tsz.afinal.FinalDb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengyi on 16/3/15.
 */
public class MainPresenter extends BasePresenter {

    private Activity context;
    private File capturePhotoPath;

    public MainPresenter(Activity context) {
        this.context = context;
    }

    /**
     * 存储固定的图片信息
     */
    public void storeDestImageInfo(){
        boolean isStore = PrefHelper.getBoolean(ArgumentConst.STORE_PIC_INFO, false);
        if(isStore){
            return;
        }
        thread.start();

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            String[] picName = {"indicate_ganluxianxing", "indicate_kaoyoucedaoluxingshi", "indicate_xiangyouzhuanwan",
                    "indicate_xiangzuozhuanwan1","indicate_xiangzuozhuanwan2","indicate_xiangzuozhuanwan3","indicate_xiangzuozhuanwan4",
                    "indicate_xiangzuozhuanwan5","indicate_xiangzuozhuanwan6","indicate_yunxudiaotou", "indicate_zhixing",
                    "indicate_zhixinghexiangzuozhuanwan_1","indicate_zhixinghexiangzuozhuanwan_2","indicate_zhixinghexiangzuozhuanwan_3",
                    "indicate_zhixinghexiangzuozhuanwan_4","indicate_zhixinghexiangzuozhuanwan_5","indicate_zhixinghexiangzuozhuanwan_6",
                    "indicate_zhixinghexiangzuozhuanwan_7", "indicate_zhixinghexiangzuozhuanwan_8",
                    "forbid_cheliangchangshitingfang",
                    "forbid_chelianglinshihuochangshitingfang", "forbid_jidongcheshiru","forbid_minglaba",
                    "forbid_shiru","forbid_tongxing", "warn_lianxuwanlu", "warn_xiangyoujiwanlu",
                    "warn_yihua", "warn_zhuyixingren", "travel_dongjiliulanqu", "travel_yeyouqu",
                    "travel_lvyouqufangxiang", "roadwork_yidongxingshigongbiaozhi", "roadworks_qianfangshigong",
                    "roadworks_shigonglulan", "roadworks_youdaofengbi", "directway_chedaojiyishangjiaochabiao"};
            String[] picDescribe = {"指示标志：干路先行","指示标志：靠右侧道路行驶","指示标志：向右转弯","指示标志：向左转弯"
                    ,"指示标志：向左转弯","指示标志：向左转弯","指示标志：向左转弯","指示标志：向左转弯","指示标志：向左转弯"
                    ,"指示标志：允许掉头","指示标志：直行","指示标志：直行和向左转弯","指示标志：直行和向左转弯","指示标志：直行和向左转弯",
                    "指示标志：直行和向左转弯","指示标志：直行和向左转弯","指示标志：直行和向左转弯","指示标志：直行和向左转弯",
                    "指示标志：直行和向左转弯","禁令标志：禁止车辆长时停放",
                    "禁令标志：禁止车辆临时或长时停放","禁令标志：禁止机动车驶入","禁令标志：禁止鸣喇叭","禁令标志：禁止驶入",
                    "禁令标志：禁止通行","警告标志：注意连续弯路","警告标志：注意向右急弯路标志","警告标志：注意前方是易滑路段标志",
                    "警告标志：注意行人","旅游区标志：冬季浏览区","旅游区标志：野营区","旅游区标志：旅游区方向","道路施工标志：移动性施工标志",
                    "道路施工标志：前方施工","道路施工标志：施工路栏","道路施工标志：右道封闭","指路标志：大交通量的四车道以上公路交叉路"};
            String[] picBelongType = {"指示标志","指示标志","指示标志","指示标志","指示标志","指示标志","指示标志","指示标志","指示标志",
                    "指示标志","指示标志","指示标志","指示标志","指示标志","指示标志","指示标志","指示标志","指示标志","指示标志",
                    "禁令标志","禁令标志","禁令标志","禁令标志","禁令标志","禁令标志",
                    "警告标志","警告标志","警告标志","警告标志","旅游区标志","旅游区标志","旅游区标志",
                    "道路施工标志","道路施工标志","道路施工标志","道路施工标志","指路标志"};
            int[] picId = {R.drawable.indicate_ganluxianxing, R.drawable.indicate_kaoyoucedaoluxingshi, R.drawable.indicate_xiangyouzhuanwan,
                    R.drawable.indicate_zuozhuanwan_one,R.drawable.indicate_zuozhuanwan_tow,R.drawable.indicate_zuozhuanwan_three,
                    R.drawable.indicate_zuozhuanwan_four,R.drawable.indicate_zuozhuanwan_five,
                    R.drawable.indicate_xiangzuozhuanwan_six, R.drawable.indicate_yunxudiaotou, R.drawable.indicate_zhixing,
                    R.drawable.indicate_zhixingzuozhuan_one,R.drawable.indicate_zhixingzuozhuan_two,R.drawable.indicate_zhixingzuozhuan_three,
                    R.drawable.indicate_zhixingzuozhuan_four,R.drawable.indicate_zhixingzuozhuan_five,R.drawable.indicate_zhixingzuozhuan_six,
                    R.drawable.indicate_zhixingzuozhuan_seven,
                    R.drawable.indicate_zhixinghexiangzuozhuanwan_eight, R.drawable.forbid_cheliangchangshitingfang,
                    R.drawable.forbid_chelianglinshihuochangshitingfang, R.drawable.forbid_jidongcheshiru,R.drawable.forbid_minglaba,
                    R.drawable.forbid_shiru, R.drawable.forbid_tongxing, R.drawable.warn_lianxuwanlu, R.drawable.warn_xiangyoujiwanlu,
                    R.drawable.warn_yihua, R.drawable.warn_zhuyixingren, R.drawable.travel_dongjiliulanqu, R.drawable.travel_yeyouqu,
                    R.drawable.travel_lvyouqufangxiang, R.drawable.roadwork_yidongxingshigongbiaozhi, R.drawable.roadworks_qianfangshigong,
                    R.drawable.roadworks_shigonglulan, R.drawable.roadworks_youdaofengbi, R.drawable.directway_chedaojiyishangjiaochabiao};
            int len = picId.length;
            List<DestPicInfoBean> list = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                DestPicInfoBean picInfoBean = new DestPicInfoBean();
                picInfoBean.setPicName(picName[i]);
                picInfoBean.setPicDescribe(picDescribe[i]);
                picInfoBean.setPicBelongType(picBelongType[i]);
                picInfoBean.setPicID(picId[i]);
                list.add(picInfoBean);
            }
            list = setFeatureValue(list);
            if (list == null || list.size() == 0) {
                return;
            }
            int length = list.size();
            for (int i = 0; i < length; i++) {
                DestPicInfoBean picInfoBean = list.get(i);
                FinalDBHelper.saveDestPicInfo(picInfoBean);
            }
            PrefHelper.put(ArgumentConst.STORE_PIC_INFO,true);
            Message msg = handler.obtainMessage();
            msg.what = 1;
            handler.sendMessage(msg);
        }
    });

    private List<DestPicInfoBean> setFeatureValue( List<DestPicInfoBean> list) {
        if(list == null || list.size() == 0) {
            return null;
        }
        int size = list.size();
        for(int i = 0; i < size; i++) {
            DestPicInfoBean destPicInfoBean = list.get(i);
            Bitmap bitmap = BitmapFactory.decodeResource(ApplicationHelper.getApplication().getResources(), destPicInfoBean.getPicID());
            double[] featureValue =  ImageIdentificationHelper.getHSVColorFeature(bitmap);
            if (featureValue == null) {
                continue;
            }
            destPicInfoBean.setFeatureValue(featureValue);
        }
        return list;
    }

    /**
     * 拍照
     */
    public void takePhoto() {
        capturePhotoPath = FileHelper.newFilePath(ArgumentConst.TEMP_FILE_NAME_FORMAT,
                (System.currentTimeMillis() + ".jpg"));
        if (capturePhotoPath == null) {
            ToastUtil.toast(context, R.string.disk_not_avaliable);
        } else {
            Intent take = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            take.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(capturePhotoPath));
            take.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            try {
                context.startActivityForResult(take, RequestConst.TAKE_PHOTO);
            } catch (ActivityNotFoundException e) {
                ToastUtil.toast(context, "没有可用的相机程序");
            }
        }
    }

    /**
     * 从相册获取
     */
    public void takeGallery() {
        Intent pick = new Intent(Intent.ACTION_PICK);
        pick.setType("image/*");
        try {
            context.startActivityForResult(pick, RequestConst.SELECT_PHOTO);
        } catch (ActivityNotFoundException e) {
            ToastUtil.toast(context, "没有可用的图片管理程序");
        }
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     * @return  返回图片的存储路径对应的File
     */
    public File parseResultOnActivityResult(int requestCode, int resultCode, Intent data) {
        File file = null;
        switch (requestCode) {
            case RequestConst.TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    file = capturePhotoPath;
                    savePicInfoIntoSqLite(file.toString());
                }
                break;
            case RequestConst.SELECT_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    file = PictureHelper.uriToFile(data.getData());
                    savePicInfoIntoSqLite(file.toString());
                }
                break;
        }
        return file;
    }

    /**
     * 将获取的图片信息存放在数据库中
     */

    private void savePicInfoIntoSqLite(String picPath){
        //如果该图片路径已经存在，则不需要再存储在本地数据库中
        List<PictureInfoBean> pics = FinalDBHelper.findAll(PictureInfoBean.class);
        if (pics != null || pics.size() > 0) {
            int len = pics.size();
            for (int i = 0; i < len; i++){
                if (picPath.equals(pics.get(i).getPicStorePosition())){
                    return;
                }
            }
        }
        PictureInfoBean pictureInfoBean = new PictureInfoBean();
        pictureInfoBean.setBelongType("指示标志");
        pictureInfoBean.setPicName("图像识别");
        pictureInfoBean.setPicStorePosition(picPath);
        FinalDBHelper.savePicInfo(pictureInfoBean);
    }

    public void initTypeTable(){
        String[] types = ApplicationHelper.getApplication().getResources()
                .getStringArray(R.array.type_of_traffic_sign);
        FinalDb db = FinalDb.create(context, true);
        List<TypeInfoBean> bean = db.findAll(TypeInfoBean.class);
        if (bean != null && bean.size() > 0) {//数据库中已经有数据,不用重新添加
            return;
        }
        if (types != null && types.length > 0) {
            for (String string : types) {
                TypeInfoBean type = new TypeInfoBean();
                type.setTypeName(string);
                db.save(type);
            }
        }

    }

}
