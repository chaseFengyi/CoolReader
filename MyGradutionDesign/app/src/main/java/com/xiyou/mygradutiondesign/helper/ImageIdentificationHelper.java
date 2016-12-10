package com.xiyou.mygradutiondesign.helper;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fengyi on 2016/5/12.
 * 图像识别算法
 */
public class ImageIdentificationHelper {

    /**
     * @param bitmap
     * @return
     */
    private static Mat imagePath2RGBMat(Bitmap bitmap) {
        Mat rgbMat = new Mat();
        Utils.bitmapToMat(bitmap, rgbMat);
        return rgbMat;
    }

    /**
     * 计算彩色图像的 'HSV' 颜色矩特征.
     *
     * @param bitmap
     * @return  计算得到的直方图特征向量, 是一个 20 维的列向量
     */
    public static Mat CalcImgHSVColorMoment(Bitmap bitmap) {
        Mat rgbMat = imagePath2RGBMat(bitmap);
        Mat hsvMat = ChangeImgColorSpace(rgbMat);
        hsvMat = QuantizeHSVColorSpace(hsvMat);
        Mat feature = CalcImgBlockFeature(hsvMat);
        Mat out = Mat.zeros(20,1, CvType.CV_8UC1);
        //计算 'Hue' 和 '饱和度' 矩阵, 每个矩阵计算 10 维, 总计为 20 维特征向量
        setValue(out,feature,0,0,20);
        return out;
    }

    /**
     * 计算 'Hue' 和 '饱和度' 矩阵, 每个矩阵计算 10 维, 总计为 20 维特征向量
     *
     * @param Vout
     * @param hsvMat
     * @param value 0->h 1->s
     * @param start Vout坐标的开始
     * @param end Vout坐标的结束
     * @return
     */
    //Vout( 11 : 20 ) = CalcImgBlockFeature( Q( :, :, 2 ) );
    private static void setValue(Mat Vout, Mat hsvMat, int value, int start, int end) {
        if (Vout == null || hsvMat == null || start > end) {
            return ;
        }
        int len = (end - start) / 2;
        for (int i = 0; i < len; i ++){
            double[] get = hsvMat.get(i,0);
            if (get == null && get.length != 3) {
                continue;
            }
            double[] h = new double[1];
            h[0] = get[0];
            Vout.put(i * 2,0,h);
            double[] s = new double[1];
            s[0] = get[1];
            Vout.put(i * 2 + 1, 0, s);
        }

    }

    /**
     * 计算图像块的 2 维矩特征
     *
     * @param blk
     */
    private static double[][] CalcBlkMoments(Mat blk){
        if (blk == null) {
            return null;
        }
        double[][] value = new double[2][3];
        int row = blk.rows();
        int col = blk.cols();
        double m1H = 0,m1S = 0,m1V = 0;
        double m2H = 0,m2S = 0,m2V = 0;
        double sum1 = 0, sum2 = 0;
        for (int j = 0; j < col; j++) {
            double sumH = 0;
            double sumS = 0;
            double sumV = 0;
            for (int i = 0; i < row; i++) {
                double[] val = blk.get(i,j);
                if (val == null) {
                    continue;
                }
                sumH += val[0];
                sumS += val[1];
                sumV += val[2];
            }
            m1H += sumH / row;
            m1S += sumS / row;
            m1V += sumV / row;
        }
        m1H = m1H / col;
        m1S = m1S / col;
        m1V = m1V / col;

        //m2 = sqrt( sum( sum( ( Blk - m1 ) .^ 2 ) ) / ( size( Blk, 1 ) * size( Blk, 2 ) ) );
        sum2 = row * col;
        double sum1H = 0,sum1S = 0,sum1V = 0;
        for (int j = 0; j < col; j++) {
            for (int i = 0; i < row; i++) {
                double[] val = blk.get(i,j);
                if (val == null) {
                    continue;
                }
                double sumH = 0,sumS = 0,sumV = 0;
                sumH += val[0];
                sumS += val[1];
                sumV += val[2];
                sum1H += Math.pow(sumH-m1H, 2);
                sum1S += Math.pow(sumS-m1S, 2);
                sum1V += Math.pow(sumV-m1V, 2);
            }
            sum1H += sum1H / sum2;
            sum1S += sum1S / sum2;
            sum1V += sum1V / sum2;
        }

        m2H = Math.sqrt(sum1H);
        m2S = Math.sqrt(sum1S);
        m2V = Math.sqrt(sum1V);
        value[0][0] = m1H;
        value[0][1] = m1S;
        value[0][2] = m1V;
        value[1][0] = m2H;
        value[1][1] = m2S;
        value[1][2] = m2V;
        return value;
    }

    /**
     * 将图像分为 5 块, 每块计算 2 维矩特征, 总计得到 10 维特征向量
     *
     * @param rgbMat
     */
    private static Mat CalcImgBlockFeature(Mat rgbMat) {
        if(rgbMat == null) {return null;}
        Mat vb = Mat.zeros(10, 1, CvType.CV_8UC3);
        //图像均匀分为 '4' 个子块 .
        int Ml = (int) Math.floor((rgbMat.rows() / 2));int Nl = (int) Math.floor(rgbMat.cols() / 2);
        int m1,m2,n1,n2;Mat blk1,blk;
        for (int p = 1; p <= 2; p++) {
            for (int q = 1; q <= 2; q++) {
                //得到子块
                m1 = ( p - 1 ) * Ml ;m2 = m1 + Ml - 1;n1 = ( q - 1 ) * Nl ;n2 = n1 + Nl - 1;
                blk1 = rgbMat.rowRange(m1,m2);blk = blk1.colRange(n1,n2);
                double[][] value = CalcBlkMoments(blk);
                if (value == null) {continue;}
                double[] m11 = new double[3];double[] m22 = new double[3];
                m11[0] = value[0][0];m11[1] = value[0][1];m11[2] = value[0][2];
                m22[0] = value[1][0];m22[1] = value[1][1];m22[2] = value[1][2];
                vb.put(( p - 1 ) * 2 + q, 0,m11);vb.put((p - 1) * 2 + q + 1, 0, m22);
            }
        }
         /* 图像中心和这 '4' 个子块完全一样大小的子块 . 图像的中心点坐标*/
        m1 = Ml - (int) Math.floor((Ml / 2));m2 = m1 + Ml - 1;
        n1 = Nl - (int) Math.floor((Nl / 2));n2 = n1 + Nl - 1;
        blk1 = rgbMat.rowRange(m1, m2);blk = blk1.colRange(n1,n2);
        double[][] value = CalcBlkMoments(blk);
        if (value == null) {return vb;}
        double[] v1 = new double[3];double[] v2 = new double[3];
        v1[0] = value[0][0];v1[1] = value[0][1];v1[2] = value[0][2];
        v2[0] = value[1][0];v2[1] = value[1][1];v2[2] = value[1][2];
        vb.put(8,0,v1);vb.put(9,0,v2);
        return vb;
    }

    private static BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(ApplicationHelper.getApplication()) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("tag", "OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    /**
     * 计算图像的 '线条方向直方图' 特征
     *
     * @param bitmap
     * @param IsFoldingHistogram  是否使用文献中的改进算法
     * @return
     */
    public static Mat CalcImgLineDirectionHistogram(Bitmap bitmap, boolean IsFoldingHistogram){
//        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, ApplicationHelper.getApplication(), mLoaderCallback);
        Mat rgbMat = imagePath2RGBMat(bitmap);
        Mat grayMat = new Mat();
        //对图像进行灰度化
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_BGRA2GRAY, 4);
        Mat cannyMat = cannyEdgeDetection(rgbMat, grayMat);
        int[][] coordinate = find(cannyMat);
        //角度范围值为 ( 0° - 180° ), 以 10° 为单位量化, 得到的直方图
        int degUnit = 10;
        int binNum = (int) Math.floor(180.0/degUnit);
        Mat vt = Mat.zeros(binNum, 1, CvType.CV_8UC1);
        //依次计算每个边缘点的线条方向角度值
        int Xmax = grayMat.cols();
        int Ymax = grayMat.rows();
        int length = coordinate.length;
        for (int p = 0; p < length; p++) {
            //得到边缘点的坐标
            int xt = coordinate[p][0];
            int yt = coordinate[p][1];
            //计算边缘点的方向梯度相角值( 弧度值 )并转换为角度值
            // 利用差分法, 并设 dx = dy = 1
            int dx = 1,dy = 1;
            if ( ( xt + 1 ) <= Xmax ){
                dx = 1;
            }else {
                dx = -1;
            }
            if ( ( yt + 1 ) <= Ymax ) {
                dy = 1;
            } else {
                dy = -1;
            }
            //计算线条方向角度值
            double[] val1Y = grayMat.get(( yt + dy ), xt);
            double[] val2Y = grayMat.get(yt, xt);
            if (val1Y == null || val2Y == null) {
                continue;
            }
            double DY = val1Y[0] - val2Y[0];
            double[] val1X = grayMat.get(yt, ( xt + dx ));
            double[] val2X = grayMat.get(yt, xt);
            if (val1X == null || val2X == null) {
                continue;
            }
            double DX = val1X[0] - val2X[0];
            double at;
            if ( ( DX == 0 ) && ( DY == 0 ) ) {
                at = 0; //否则此时 'atan( DY / DX ) = NaN' ...
            }else {
                at = Math.atan(DY / DX) * (180 / Math.PI) + 90;
            }
            //量化 ...
            int k = 0;
            if ( at == 180 ) {
                k = binNum;
            }else {
                k = (int) (Math.floor(at / degUnit) + 1);
            }
            double[] value = new double[1];
            double[] getValue = vt.get(k,0);
            if (getValue == null) {
                continue;
            }
            getValue[0] += 1;
            vt.put(k,0,getValue);
        }
        Mat out;
        //利用改进算法再次计算线条方向直方图
        if (IsFoldingHistogram) {
            /*
            * % 将图像的线条方向直方图进行两次对折( 取两对折个值的较大值为对折后的结果 ), 一次
  % 对折结果为 ( 0, 90° ), 再次对折结果为 ( 0, 45° ) ...

  %  5° 量化方案 - 36 bin( 原始算法 ) - 9 bin( 改进算法 )
  % 10° 量化方案 - 18 bin( 原始算法 ) - 5 bin( 改进算法 )
  % 15° 量化方案 - 12 bin( 原始算法 ) - 3 bin( 改进算法 )

  % '对折' 相当于 '镜像' 处理 ...

  % 第 1 次对折 ...
            * */
            vt = FoldHistogram(vt);
            //第 2 次对折
            out = FoldHistogram(vt);
        }else {
            out = vt;
        }
        return out;

    }

    /**
     * 将输入的直方图对折后输出
     *
     * @param mat
     * @return
     */
    private static Mat FoldHistogram(Mat mat) {
        if (mat == null) {
            return null;
        }
        Mat out = new Mat();
        mat.copyTo(out);
        //对折
        int lh = mat.cols() > mat.rows() ? mat.cols() : mat.rows();
        for (int p = 0; p < Math.floor(lh / 2.0); p++){
            double[] val1 = mat.get(p,0);
            double[] val2 = mat.get(lh-p+1,0);
            if (val1 == null || val2 == null) {
                continue;
            }
            double max = val1[0] > val2[0] ? val1[0] : val2[0];
            double[] print = new double[1];
            print[0] = max;
            out.put(p,0,max);
            //如果 'Lh' 为奇数, 则直方图居中位置的值直接拷贝
            if ((lh % 2) != 0) {
                double[] val = mat.get((int) (Math.floor(lh / 2.0) + 1), 0);
                if (val == null) {
                    continue;
                }
                double[] dou = new double[1];
                dou = val;
                out.put(p+1,0,dou);
            }
        }
        return out;
    }

    /**
     * 得到边缘点的坐标
     *
     * @param cannyMat
     * @return
     */
    private static int[][] find(Mat cannyMat){
        if (cannyMat == null) {
            return null;
        }
        List<Map<Integer, Integer>> coordinate = new ArrayList<>();
        int col = cannyMat.cols();
        int row = cannyMat.rows();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                double[] value = cannyMat.get(i,j);
                if (value == null) {
                    continue;
                }
                Map<Integer,Integer> map = new HashMap<>();
                if (value[0] == 1){
                    map.put(i,j);
                    coordinate.add(map);
                }

            }
        }
        if (coordinate == null){
            return null;
        }
        int size = coordinate.size();
        int[][] cordinate = new int[size][2];
        for (int i = 0; i < size; i++){
            Map<Integer,Integer> map = coordinate.get(i);
            if (map == null) {
                continue;
            }
            Set keys = map.keySet();
            if(keys != null) {
                Iterator iterator = keys.iterator();
                while (iterator.hasNext()) {
                    int key = (int) iterator.next();
                    int value = map.get(key);
                    cordinate[i][0] = key;
                    cordinate[i][1] = value;
                }
            }
        }
        return cordinate;
    }

    /**
     * 对图像进行 'canny' 边缘检测
     *
     * @param rgbMat
     * @return
     */
    private static Mat cannyEdgeDetection(Mat rgbMat, Mat grayMat){
        Mat cannyMat = new Mat();
        int HYSTERESIS_THRESHOLD1 = 80;
        int HYSTERESIS_THRESHOLD2 = 100;
//        int ACCUMULATOR_THRESHOLD = 50;
//        int MINLINELENGTH = 100;
//        int MAXLINEGAP = 80;
        Mat lines = new Mat();
        Imgproc.Canny(grayMat, cannyMat, HYSTERESIS_THRESHOLD1, HYSTERESIS_THRESHOLD2);
//        Imgproc.cvtColor(cannyMat, rgbMat, Imgproc.COLOR_GRAY2RGBA, 4);
//        Imgproc.HoughLinesP(cannyMat, lines, 1, Math.PI / 180, ACCUMULATOR_THRESHOLD, MINLINELENGTH, MAXLINEGAP);
//        int HOUGH_LINE_COUNT = 5;
//        for (int x = 0; x < lines.cols() && x < HOUGH_LINE_COUNT; x++) {
//            double[] vec = lines.get(0, x);
//            if(vec!=null) {
//                double x1 = vec[0],
//                        y1 = vec[1],
//                        x2 = vec[2],
//                        y2 = vec[3];
//                Point start = new Point(x1, y1);
//                Point end = new Point(x2, y2);
//                Imgproc.line(rgbMat, start, end, new Scalar(255, 0, 0), 3);
//            }
//        }
        return cannyMat;
    }

    /**
     * 计算彩色图像的 'HSV' 颜色直方图特征
     *
     * @param bitmap
     * @return
     */
    private static Mat CalcImgHSVColorHistogram(Bitmap bitmap) {
        Mat rgbMat = imagePath2RGBMat(bitmap);
        Mat hsvMat = ChangeImgColorSpace(rgbMat);
        if (hsvMat == null) {
            return null;
        }
        hsvMat = QuantizeHSVColorSpace(hsvMat);
        Mat return2DimenMat = CalcImgHSVMatrix(hsvMat);
        Mat binsFeatureVector = CalcHistogram(return2DimenMat, 72);
        return binsFeatureVector;
    }

    public static double[] getHSVColorFeature(Bitmap bitmap){
        if (bitmap == null) {
            return null;
        }
        Mat feature = CalcImgHSVColorMoment(bitmap);
        if (feature == null) {
            return null;
        }
        int row = feature.rows();
        double[] print = new double[row];
        for (int i = 0; i < row; i++) {
            double[] val = feature.get(i,0);
            if (val == null) {
                continue;
            }
           print[i] = val[0];
        }
        return print;
    }

    /**
     * 计算得到彩色图像的 'HSV' 颜色直方图特征向量( '72' 柄 )
     * 本函数用来计算一个量化矩阵的直方图向量. 所谓的量化矩阵表示经过某种计算得到
     % 的具有整数值的矩阵，它表示了一些值的分布情况. 典型的例子是图像经过某种处理
     % 后得到的视觉特征矩阵.
     *
     * @param mat  将量化后的 'HSV' 颜色空间三维数据转换为二维矩阵数据.
     *             二维矩阵，是一个量化矩阵
     * @param BinsNum   计算后直方图的柄数
     * @return 计算得到的直方图特征向量，是一个列向量
     */
    private static Mat CalcHistogram(Mat mat, int BinsNum){
        if(mat == null || BinsNum < 0) {
            return null;
        }
        Mat zeroMat = Mat.zeros(BinsNum, 1, CvType.CV_8UC1);
        int row = mat.rows();
        int col = mat.cols();

        for (int i = 0; i < BinsNum; i++) {
            double[] sum = new double[1];
            double subSum = 0;
            for (int j = 0; j < row; j++) {
                for(int k = 0; k < col; k++) {
                    double[] value = mat.get(j,k);
                    if (value == null) {
                        continue;
                    }
                    if (value[0] == (i - 1)){
                        subSum += value[0];
                    }
                }
            }
            sum[0] += subSum;
            zeroMat.put(i,0,sum);
        }

        return zeroMat;
    }

    /**
     * /将量化后的 HSV 颜色空间三维彩色图像数据转换为二维矩阵数据
     *
     * @param hsvMat
     * @return
     */
    private static Mat CalcImgHSVMatrix(Mat hsvMat){
        if (hsvMat == null) {
            return null;
        }
        int col = hsvMat.cols();
        int row = hsvMat.rows();
        Mat return2DimenMat = new Mat();
        Imgproc.cvtColor(hsvMat, return2DimenMat, Imgproc.COLOR_RGB2GRAY);
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                double[] hsv = hsvMat.get(i,j);
                double value = change3Dimens22Dimens(hsv);
                if (value == -65536){
                    continue;
                }
                double[] mat = return2DimenMat.get(i, j);
                if (mat == null) {
                    continue;
                }
                mat[0] = value;
                return2DimenMat.put(i,j,mat);
            }
        }
        return return2DimenMat;
    }

    /**
     * 量化经 'RGB' 颜色空间转换后的 'HSV' 颜色空间
     *
     * @param hsvMat
     * @return
     */
    private static Mat QuantizeHSVColorSpace(Mat hsvMat){
        if (hsvMat == null) {
            return null;
        }
        int row = hsvMat.rows();
        int col = hsvMat.cols();
        //对HSV颜色空间矩阵进行量化
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                double[] hsv =  hsvMat.get(i,j);
                if (hsv == null || hsv.length != 3){
                    continue;
                }
                int h = quantizationHSpace(hsv[0]);
                int s = quantizationSSpace(hsv[1]);
                int v = quantizationVSpace(hsv[2]);
                hsv[0] = h;
                hsv[1] = s;
                hsv[2] = v;
                hsvMat.put(i,j,hsv);
            }
        }
        return hsvMat;
    }

    /**
     * 将输入的彩色图像的颜色空间由 'RGB' 转换为 'HSV'
     *
     * @param rgbMat
     * @return
     */
    private static Mat ChangeImgColorSpace(Mat rgbMat){
        if (rgbMat == null) {
            return null;
        }
        int rows = rgbMat.rows();
        int cols = rgbMat.cols();
        Mat hsvMat = Mat.zeros(rows, cols, CvType.CV_8UC3);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double[] value = rgbMat.get(i, j);
                if (value == null || value.length != 4){
                    continue;
                }
                double vv = max(value[0], value[1], value[2]);
                double v = vv / 255;
                double s = (vv - min(value[0], value[1], value[2])) / vv;
                double hh = getHH(value[0], value[1], value[2], vv);
                double h = 60 * hh;
                double[] put = new double[3];
                put[0] = h;
                put[1] = s;
                put[2] = v;
                hsvMat.put(i,j,put);
            }
        }
        return hsvMat;
    }

    private static double getHH(double r, double g, double b, double vv){
        double max = max(r, g, b);
        double min = min(r, g, b);
        if (r == max && g == min) {
            return (5 + getBB(r,g,b,vv));
        }else if (r == max && g != min) {
            return (1 - getGG(r,g,b,vv));
        } else if (g == max && b == min) {
            return (1 + getRR(r,g,b,vv));
        } else if (g == max && b != min) {
            return (3 - getBB(r,g,b,vv));
        } else if (b == max && r == min) {
            return (3 + getGG(r,g,b,vv));
        } else {
            return (5 - getRR(r,g,b,vv));
        }
    }

    private static double getRR(double r, double g, double b,double vv){
        if (vv - min(r,g,b) == 0) {
            return 0;
        }
        return (vv - r) / (vv - min(r,g,b));
    }

    private static double getGG(double r, double g, double b,double vv){
        if (vv - min(r,g,b) == 0) {
            return 0;
        }
        return (vv - g) / (vv - min(r,g,b));
    }

    private static double getBB(double r, double g, double b,double vv){
        if (vv - min(r,g,b) == 0) {
            return 0;
        }
        return (vv - b) / (vv - min(r,g,b));
    }

    private static double max(double r, double g, double b){
        if (r >= g && r >= b) {
            return r;
        }else if ((g >= r && g >= b)) {
            return g;
        }else if ((b >= r && b >= g)) {
            return b;
        }
        return r;
    }

    private static double min(double r, double g, double b){
        if (r <= g && r <= b) {
            return r;
        }else if ((g <= r && g <= b)) {
            return g;
        }else if ((b <= r && b <= g)) {
            return b;
        }
        return r;
    }

    private static double change3Dimens22Dimens(double[] matrix){
        if(matrix == null || matrix.length != 3) {
            return -65536;
        }
        //预定义的计算系数
        int qs = 3,qv = 3;
        double h = matrix[0],s = matrix[1], v = matrix[2];
        return (h * (qs * qv) + s * qv + v);
    }

    private static int quantizationHSpace(double h){
        if ((h <= 20) || (h >= 316)){
            return 0;
        }else if (( h >= 21 )  && ( h <= 40 )){
            return 1;
        }else if (( h >= 41 )  & ( h <= 75 )){
            return 2;
        }else if (( h >= 76 )  & ( h <= 155 )){
            return 3;
        }else if (( h >= 156 ) & ( h <= 190 )){
            return 4;
        }else if (( h >= 191 ) & ( h <= 270 )){
            return 5;
        }else if (( h >= 271 ) & ( h <= 295 )){
            return 6;
        }else if (( h >= 296 ) & ( h <= 315 )){
            return 7;
        }else {
            return 0;
        }
    }

    private static int quantizationSSpace(double s){
        if (( s >= 0 )  & ( s <= 0.2 )){
            return 0;
        }else if (( s > 0.2 ) & ( s <= 0.7 )){
            return 1;
        }else if (( s > 0.7 ) & ( s <= 1.0 )){
            return 2;
        }else {
            return 0;
        }
    }

    private static int quantizationVSpace(double v){
        if (( v >= 0 )  & ( v <= 0.2 )){
            return 0;
        }else if (( v > 0.2 ) & ( v <= 0.7 )){
            return 1;
        }else if (( v > 0.7 ) & ( v <= 1.0 )){
            return 2;
        }else {
            return 0;
        }
    }

}
