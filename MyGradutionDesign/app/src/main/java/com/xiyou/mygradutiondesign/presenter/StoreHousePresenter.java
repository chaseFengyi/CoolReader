package com.xiyou.mygradutiondesign.presenter;

import com.xiyou.mygradutiondesign.R;
import com.xiyou.mygradutiondesign.bean.TrafficSignBean;
import com.xiyou.mygradutiondesign.helper.ApplicationHelper;
import com.xiyou.mygradutiondesign.helper.FinalDBHelper;
import com.xiyou.mygradutiondesign.helper.PictureHelper;
import com.xiyou.mygradutiondesign.sqLite.bean.PictureInfoBean;
import com.xiyou.mygradutiondesign.util.EmptyObjectUtil;
import com.xiyou.mygradutiondesign.util.ResUtil;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengyi on 16/3/13.
 */
public class StoreHousePresenter extends BasePresenter {

    public interface IView{

        void showProgress();

        void dismissProgress();

    }

    private IView view = EmptyObjectUtil.cretateEmptyObject(IView.class);

    public StoreHousePresenter(IView view) {
        this.view = view;
    }

    private int[] pics = {
            R.drawable.sign_indicate, R.drawable.sign_forbid, R.drawable.sign_waring,
            R.drawable.sign_assistant, R.drawable.sign_travel_area, R.drawable.sign_road_works,
            R.drawable.sign_direct_way
    };

    private String[] describes = {
            ResUtil.getString(R.string.sign_indirect), ResUtil.getString(R.string.sign_forbid),
            ResUtil.getString(R.string.sign_waring), ResUtil.getString(R.string.sign_assistant),
            ResUtil.getString(R.string.sign_travel_area), ResUtil.getString(R.string.sign_road_works),
            ResUtil.getString(R.string.sign_direct_way)
    };

    public List<Map<String, Object>> getGridData() {
        List<Map<String, Object>> items = new ArrayList<>();
        Map<String, Object> item;
        int size = pics.length;
        for (int i = 0; i < size; i++) {
            item = new HashMap<>();
            item.put("icon", PictureHelper.getBtmapByID(ApplicationHelper.getApplication(), pics[i]));
            item.put("describe", describes[i]);
            items.add(item);
        }
        return items;
    }

    public List<PictureInfoBean> getTrafficData(){
        view.showProgress();
        //获取数据
        List<PictureInfoBean> pics = FinalDBHelper.findAll(PictureInfoBean.class);
        view.dismissProgress();
        return pics;
    }

}
