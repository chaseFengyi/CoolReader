package com.xiyou.mygradutiondesign.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiyou.mygradutiondesign.R;
import com.xiyou.mygradutiondesign.helper.ApplicationHelper;
import com.xiyou.mygradutiondesign.helper.FinalDBHelper;
import com.xiyou.mygradutiondesign.helper.ImageIdentificationHelper;
import com.xiyou.mygradutiondesign.helper.ImageLoaderHelper;
import com.xiyou.mygradutiondesign.helper.KNNHelper;
import com.xiyou.mygradutiondesign.sqLite.PictureAttributesBean;
import com.xiyou.mygradutiondesign.sqLite.bean.DestPicInfoBean;
import com.xiyou.mygradutiondesign.sqLite.bean.PictureInfoBean;
import com.xiyou.mygradutiondesign.util.EmptyObjectUtil;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fengyi on 16/3/14.
 */
public class TrafficDetailPresenter extends BasePresenter {

    public interface IView{

        void showProgress();

        void dismissProgress();

        void setPicDescribe(String describe);

        void checkBelongType(String belongType);

    }

    IView iView = EmptyObjectUtil.cretateEmptyObject(IView.class);

    public TrafficDetailPresenter(IView view) {
        this.iView = view;
    }

    /**
     * 获取点击“所属类别”后，显示的条目
     *
     * @return
     */
    public List<Map<String, String>> getItems() {
        // 建立数据源
        String[] types = ApplicationHelper.getApplication().getResources()
                .getStringArray(R.array.type_of_traffic_sign);
        if (types == null || types.length == 0) {
            return null;
        }
        List<Map<String, String>> items = new ArrayList<>();
        for (String item : types) {
            Map<String, String> map = new HashMap<>();
            map.put("belongType", item);
            items.add(map);
        }
        return items;
    }

    /**
     * 显示图片
     *
     * @param avatarUrl
     * @param avatar
     */
    public void loadImage(String avatarUrl, ImageView avatar) {
        ImageLoadingListener listener = new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        };
        ImageLoaderHelper.loadBitmap(avatarUrl, avatar, listener);
    }

    /**
     * 根据图片的存储地址在数据库中查找该图片的详细信息
     *
     * @param picPath 图片的存储地址
     */
    public PictureInfoBean getPicInfoByAddress(String picPath) {
        List<PictureInfoBean> pics = FinalDBHelper.findAllByWhere(PictureInfoBean.class,
                PictureAttributesBean.PIC_STORE_POSITION, picPath);
        /*if (pics != null && pics.size() == 1) {
            return pics.get(0);
        }*/
        if (pics != null && pics.size() > 0) {
            return pics.get(0);
        }
        return null;
    }

    public void updatePicInfo(PictureInfoBean pictureInfoBean) {
        FinalDBHelper.update(pictureInfoBean);
    }

    /**
     * 根据图片的路径，获取图片对象，并对图片进行分析，查看该图片的功能
     * 处理方式放在子线程里面
     *
     * @param picPath
     * @return
     */

    final int TAG = 1;
    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == TAG) {
                DestPicInfoBean destPicInfoBean = (DestPicInfoBean) msg.obj;
                String describe;
                String belongType;
                if (destPicInfoBean == null){
                    describe = null;
                    belongType = null;
                }else {
                    describe = destPicInfoBean.getPicDescribe();
                    belongType = destPicInfoBean.getPicBelongType();
                }
                iView.dismissProgress();
                iView.setPicDescribe(describe);
                iView.checkBelongType(belongType);
            }
        }
    };
    private class MyRunnable implements Runnable{
        String picPath;
        public MyRunnable(String picPath){
            this.picPath = picPath;
        }

        @Override
        public void run() {
            Bitmap bitmap = BitmapFactory.decodeFile(picPath);
//                double[] newFeature = vector(bitmap);
//            double[] newFeature = ImageIdentificationHelper.getHSVColorFeature(bitmap);
//            DestPicInfoBean bean = compareFeature(newFeature);
            DestPicInfoBean bean = KNNHelper.obtainPicAttri(bitmap);
            System.out.println("minminminmin="+bean);
                Message message = handler.obtainMessage();
                message.obj = bean;
                message.what = TAG;
                handler.sendMessage(message);
        }
    }

    public void getPicDesc(final String picPath) {
        if (!OpenCVLoader.initDebug() || TextUtils.isEmpty(picPath)) {
            Log.e("tag","opencv error");
            return ;
        }
        iView.showProgress();
        new Thread(new MyRunnable(picPath)).start();

            /*Utils.matToBitmap(grayMat, grayBitmap);


            //归一化伪代码：
            Matrix matrix = new Matrix();
            int rows = bitmap.getHeight(), columns = bitmap.getWidth();
            float x = (float) Math.floor(10*48/(rows));//向下求整
            float y = (float) Math.floor(10*48/(columns));
            matrix.setScale(x/10,y/10);
            Bitmap bm = Bitmap.createBitmap(grayBitmap, 0, 0,grayBitmap.getWidth(),grayBitmap.getHeight(), matrix, true);
            Mat m = new Mat();
            Mat qiege = Mat.zeros(48,48, CvType.CV_8SC1);
            Utils.bitmapToMat(bm,m);
            for(int mrow = 0;mrow<m.rows();mrow++){
                for(int mcol = 0;mcol<m.cols();mcol++){
                    qiege.put(mrow, mcol, m.get(mrow, mcol));//将矩阵值放入标准的48x48矩阵中
                }
            }

            //图像特征提取
            System.out.println("qiege===mat:"+qiege);*/
    }

    /**
     * 将现在图片特征与数据库中所有图片进行比较，找出最相近的图片
     *
     * @param feature
     * @return 返回对该图片的描述
     */
    private DestPicInfoBean compareFeature(double[] feature){
        List<Map<double[], DestPicInfoBean>> allPicFeatures = obtainAllPicturesFeature();
        if (allPicFeatures == null || allPicFeatures.size() == 0) {
            return null;
        }

        DestPicInfoBean destPicInfoBean = null;
        int size = allPicFeatures.size();
        double min = 65536;
        for (int i = 0; i < size; i++) {
            Map<double[], DestPicInfoBean> comparePic = allPicFeatures.get(i);
            Set set = comparePic.entrySet();

            double[] key = null;
            DestPicInfoBean value = null;
            for(Iterator iter = set.iterator(); iter.hasNext();)
            {
                Map.Entry entry = (Map.Entry)iter.next();

                key = (double[])entry.getKey();
                value = (DestPicInfoBean)entry.getValue();
            }
            int len = key.length;
            double oushilen = 0.0;

            for (int j = 0; j < len; j++) {
                oushilen += Math.pow((feature[j] - key[j]), 2);
            }
            if (oushilen < min) {
                min = oushilen;
                destPicInfoBean = value;
            }
        }
        System.out.println("minminminmin="+min);
        System.out.println("minminminmin="+((min<0.000001) && (min>-0.000001)));
        System.out.println("minminminmin="+destPicInfoBean);
       if((min<0.000001) && (min>-0.000001)) {
           return destPicInfoBean;
       } else {
           return null;
       }
    }

    /**
     * 获取数据库中所有图片对应的特征向量
     *
     * @return
     */
    private List<Map<double[], DestPicInfoBean>> obtainAllPicturesFeature(){
        List<DestPicInfoBean> destPicInfoBeans = FinalDBHelper.findDestAll(DestPicInfoBean.class);
        if (destPicInfoBeans == null) {
            return null;
        }

        List<Map<double[], DestPicInfoBean>> features = new ArrayList<>();
        int len = destPicInfoBeans.size();
        for (int i = 0; i < len; i++) {
            DestPicInfoBean bean = destPicInfoBeans.get(i);
//            Bitmap bitmap = BitmapFactory.decodeResource(ApplicationHelper.getApplication().getResources(), bean.getPicID());
            Map<double[], DestPicInfoBean> map = new HashMap<>();
////            map.put(vector(bitmap), bean);
//            map.put( ImageIdentificationHelper.getHSVColorFeature(bitmap),bean);
            double[] featureValue = bean.getFeatureValue();
            if(featureValue == null) {
                Bitmap bitmap = BitmapFactory.decodeResource(ApplicationHelper.getApplication().getResources(), bean.getPicID());
                featureValue = ImageIdentificationHelper.getHSVColorFeature(bitmap);
                bean.setFeatureValue(featureValue);
                FinalDBHelper.delateDestPicInfoById(DestPicInfoBean.class, bean.getDestPicId());
                FinalDBHelper.saveDestPicInfo(bean);
            }
            map.put(featureValue,bean);
            features.add(map);
        }
        return features;
    }

    /**
     * 将矩阵分为5部分，并分别求5部分的均值与方差，构造10维列向量
     *
     * @param bitmap
     * @return
     */
    private double[] vector(Bitmap bitmap) {
        Mat rgbMat = new Mat();
        Mat grayMat = new Mat();

        Utils.bitmapToMat(bitmap, rgbMat);
        //灰度化代码
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);//rgbMat to gray grayMat
        // Utils.matToBitmap(grayMat, grayBitmap); //convert mat to bitmap
        //二值化代码
        Imgproc.threshold(grayMat, grayMat, 0, 255, Imgproc.THRESH_OTSU);
        if (grayMat == null) {
            return null;
        }

        double[] vector = new double[10];
        int row = grayMat.rows();
        int col = grayMat.cols();
        int co = col / 2, ro = row / 2;
        //(1)
        //是 24x24与24x24
        System.out.println("colcol:" + (col / 2 - 1));
        double average = average(grayMat, 0, col / 2 - 1, 0, row / 2 - 1);
        double variance = variance(grayMat,average, 0, col / 2 - 1, 0, row / 2 - 1);
        vector[0] = average;
        vector[1] = variance;

        //(2)
        //是 24(25-47)x24与24x24
        average = average(grayMat, co, col, 0, row / 2 - 1);
        variance = variance(grayMat,average, co, col, 0, row / 2 - 1);
        vector[2] = average;
        vector[3] = variance;

        //(3)
        //是 24(25-47)x24
        average = average(grayMat, col / 4 - 1, col * 3 / 4 , row / 4 - 1, row * 3 / 4);
        variance = variance(grayMat,average, col / 4 - 1, col * 3 / 4, row / 4 - 1, row * 3 / 4);
        vector[4] = average;
        vector[5] = variance;

        //(4)
        //是 24(25-47)x24(25-47)
        average = average(grayMat, 0, col / 2 - 1, ro, row);
        variance = variance(grayMat,average, 0, col / 2 - 1, ro, row);
        vector[6] = average;
        vector[7] = variance;

        //(5)
        //是 24(12-35)x24(12-35)
        average = average(grayMat, co, col, ro, row);
        variance = variance(grayMat,average, co, col, ro, ro);
        vector[8] = average;
        vector[9] = variance;
        return vector;
    }

    private double average(Mat mat, int leftTopDot, int rightTopDot, int leftBottomDot, int rightBottomDot){
        if (mat == null) {
            return -1;
        }
        int len = (rightTopDot - leftTopDot) + 1;
        double sum = 0;

        /*double[] prin = mat.get(1,1);
        if (prin != null) {
            for (int i = 0; i < prin.length; i++){
                System.out.println("get:::"+prin[i]);
            }
        }*/
        int row = rightBottomDot - leftBottomDot + 1;
        int col = rightTopDot - leftTopDot + 1;
        for (int i = leftTopDot; i < rightTopDot; i++) {
            for (int j = leftBottomDot; j < rightBottomDot; j++) {
                double[] save = mat.get(i,j);
                if (save == null){
                    continue;
                }
                sum += save[0];
            }
        }

        return sum / (row * col);
    }

    //方差
    private double variance(Mat mat,double average, int leftTopDot, int rightTopDot, int leftBottomDot, int rightBottomDot){
        if (mat == null) {
            return -1;
        }

        int row = rightBottomDot - leftBottomDot + 1;
        int col = rightTopDot - leftTopDot + 1;
        int len = (rightTopDot - leftTopDot) + 1;
        double sum = 0.0;
        for (int i = leftTopDot; i < rightTopDot; i++) {
            for (int j = leftBottomDot; j < rightBottomDot; j++) {
                double[] sav = mat.get(i,j);
                if (sav == null) {
                    continue;
                }
                double num = sav[0] - average;
                sum += Math.pow(num, 2);
            }
        }
        sum = sum / (row * col);
        return Math.pow(sum, 0.5);
    }

}
