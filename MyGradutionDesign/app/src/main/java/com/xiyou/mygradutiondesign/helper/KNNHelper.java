package com.xiyou.mygradutiondesign.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xiyou.mygradutiondesign.R;
import com.xiyou.mygradutiondesign.sqLite.bean.DestPicInfoBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fengyi on 2016/5/21.
 */
public class KNNHelper {

    public final static int K = 3;//KNN 中 K 的值

    /**
     * 获取特征向量
     * 将这些文本转换为向量，可以存储于数组中
     * tranfer data to vector
     * @param bitmap
     * @return
     */
    public static double[] data2Vec(Bitmap bitmap){

        return ImageIdentificationHelper.getHSVColorFeature(bitmap);

    }

    /**
     * 计算欧式距离
     *
     * @param a
     * @param b
     * @return
     */
    public static double calDistance(double[] a, double[] b){
        double result = 0.0;
        int temp = 0;
        for(int i = 0; i < a.length; i++){
            temp += (a[i] - b[i])*(a[i] - b[i]);
        }
        result = Math.sqrt(temp);

        return result;
    }

    /**
     * 获取数据库中所有图片对应的特征向量
     *
     * @return
     */
    private static List<Map<double[], DestPicInfoBean>> obtainAllPicturesFeature(){
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

    private static Object getKey(Map map){
        Set set = map.entrySet();
        Object key = null;
        for(Iterator iter = set.iterator(); iter.hasNext();)
        {
            Map.Entry entry = (Map.Entry)iter.next();
            key = entry.getKey();
        }

        return key;
    }

    private static Object getValue(Map map){
        Object key = getKey(map);
        return map.get(key);
    }

    /**
     * 实现分类
     *
     * @param bitmap
     * @return
     */
    public static DestPicInfoBean classify(Bitmap bitmap,List<Map<double[], DestPicInfoBean>> allPicFeatures){
        if(bitmap == null){
            return null;
        }
        if (allPicFeatures == null || allPicFeatures.size() == 0) {
            return null;
        }

        double arr[] = data2Vec(bitmap);

//        result[0] = Integer.parseInt(fileName.split("_")[0]);

        double dis[] = new double[K];
        int num[] = new int[K];

        for(int index = 0; index < K; index++){
            dis[index] = 32;
            num[index] = -1;
        }

        int size = allPicFeatures.size();
        for(int i = 0; i < size; i++){
            Map<double[], DestPicInfoBean> comparePic = allPicFeatures.get(i);
            double[] key = (double[]) getKey(comparePic);
//                int temp_arr[] = data2Vec("samples/trainingDigits/"+i+"_"+j+".txt");
//                double temp_dis = calDistance(arr, temp_arr);
            double temp_dis = calDistance(arr, key);

                for(int k = 0; k < K; k++){
                    if(temp_dis < dis[k]){
                        dis[k] = temp_dis;
                        num[k] = i;
                        break;
                    }
                }
        }

        int count[] = new int[size];

        for(int i = 0; i < size; i++)
            count[i] = 0;

        for(int i = 0; i < K; i++){
            if(num[i]!=-1)
                count[num[i]]++;
        }

        int max = 0;
        int resultIndex = 0;
        for(int i = 0; i < size; i++){
            if(count[i]>max){
                max = count[i];
//                result[1] = i;
                resultIndex = i;
            }
        }
        //没有找到
        if (max == 0) {
            return null;
        }
        return (DestPicInfoBean) getValue(allPicFeatures.get(resultIndex));
    }

    public static DestPicInfoBean obtainPicAttri(Bitmap bitmap){
        List<Map<double[], DestPicInfoBean>> allPicFeatures = obtainAllPicturesFeature();
//        calRate(allPicFeatures);
            return classify(bitmap, allPicFeatures);
    }

    private static void calRate(List<Map<double[], DestPicInfoBean>> allPicFeatures){
        if (allPicFeatures == null || allPicFeatures.size() == 0) {
            System.out.println("the total right rate is: 0");
            return ;
        }
        Bitmap[] bitmaps = new Bitmap[4];
        bitmaps[0] = BitmapFactory.decodeResource(ApplicationHelper.getApplication().getResources(), R.drawable.indicate_zhixingzuozhuan_six_test);
        bitmaps[1] = BitmapFactory.decodeResource(ApplicationHelper.getApplication().getResources(), R.drawable.indicate_zhixingzuozhuan_five_test);
        bitmaps[2] = BitmapFactory.decodeResource(ApplicationHelper.getApplication().getResources(), R.drawable.indicate_zuozhuanwan_three_test);
        bitmaps[3] = BitmapFactory.decodeResource(ApplicationHelper.getApplication().getResources(), R.drawable.indicate_zuozhuanwan_tow_test);

        if (allPicFeatures == null || allPicFeatures.size() == 0) {
            System.out.println("the total right rate is: 0");
            return ;
        }
        double right = 0;
        double sum = 0;
        int size = allPicFeatures.size();
        for(int i = 0; i < 4; i++){
            DestPicInfoBean destPicInfoBean = classify(bitmaps[i], allPicFeatures);
            sum ++;
            if (destPicInfoBean != null) {
                right++;
            }
        }
        double rate = right/sum;
        System.out.println("the total right rate is: " + rate);
    }

   /* public static void main(String args[]){

        double right = 0;
        double sum = 0;

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 50; j++){
                int result[] = classify(""+i+"_"+j+".txt");

                System.out.println("the classifier came back with: "+result[1]+" , the real answer is: " +result[0]);
                sum++;
                if(result[0]==result[1])
                    right++;
            }
        }
        System.out.println("right:"+right);
        System.out.println("sum:"+sum);

        double rate = right/sum;
        System.out.println("the total right rate is: " + rate);

    }*/

}
