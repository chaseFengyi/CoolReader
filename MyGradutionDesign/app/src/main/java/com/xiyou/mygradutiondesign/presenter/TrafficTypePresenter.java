package com.xiyou.mygradutiondesign.presenter;

import com.xiyou.mygradutiondesign.helper.FinalDBHelper;
import com.xiyou.mygradutiondesign.sqLite.PictureAttributesBean;
import com.xiyou.mygradutiondesign.sqLite.bean.PictureInfoBean;
import com.xiyou.mygradutiondesign.util.EmptyObjectUtil;

import java.util.List;

/**
 * Created by fengyi on 16/3/13.
 */
public class TrafficTypePresenter extends BasePresenter {


    public interface IView{

        void showProgress();

        void dismissProgress();

    }

    private IView view = EmptyObjectUtil.cretateEmptyObject(IView.class);

    public TrafficTypePresenter(IView view) {
        this.view = view;
    }

    /**
     * 根据title从数据库中查找
     *
     * @param title
     * @return
     */
    public List<PictureInfoBean> getTrafficData(String title) {
        view.showProgress();
        // 获取数据
        List<PictureInfoBean> pics = FinalDBHelper.findAllByWhere(PictureInfoBean.class,
                PictureAttributesBean.BELONG_TYPE, title);
        view.dismissProgress();
        return pics;
    }

}
