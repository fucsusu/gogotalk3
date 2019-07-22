package com.gogotalk.presenter;

import com.gogotalk.model.api.ApiService;
import com.gogotalk.model.entity.BookLevelBean;
import com.gogotalk.model.entity.GoGoBean;
import com.gogotalk.model.entity.WeekMakeBean;
import com.gogotalk.model.util.CommonSubscriber;
import com.gogotalk.model.util.RxUtil;
import java.util.List;
import javax.inject.Inject;

public class ClassListPresenter extends RxPresenter<ClassListContract.View> implements ClassListContract.Presenter {

    private ApiService mApiService;

    @Inject
    public ClassListPresenter(ApiService apiService){
        this.mApiService=apiService;
    }


    @Override
    public void getLevelListData() {
        addSubscribe(mApiService.getLevelListData()
        .compose(RxUtil.rxSchedulerHelper())
        .compose(RxUtil.handleMyResult(getView(),false))
        .subscribeWith(new CommonSubscriber<List<BookLevelBean>>(getView()) {
            @Override
            public void onNext(List<BookLevelBean> levelBeans) {
                if (levelBeans == null) return;
                if (levelBeans.size() == 0) return;
                getView().updateLevelRecelyerViewData(levelBeans);
            }

            @Override
            public boolean isShowLoading() {
                return false;
            }

            @Override
            public boolean isHideLoading() {
                return false;
            }
        }));
    }

    @Override
    public void getClassByLevel(int level) {
        addSubscribe(mApiService.getClassByLevel(String.valueOf(level))
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(),false))
                .subscribeWith(new CommonSubscriber<List<GoGoBean>>(getView()) {
                    @Override
                    public void onNext(List<GoGoBean> beans) {
                        if (beans == null) return;
                        if (beans.size() == 0) return;
                        if (beans.get(0).getChapterData() == null) return;
                        if (beans.get(0).getChapterData().size() == 0) return;
                        getView().updateUnitAndClassRecelyerViewData(beans);
                    }

                })
        );
    }

    @Override
    public void getWeekMakeBean() {
        addSubscribe(mApiService.getWeekMakeBean()
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(),false))
                .subscribeWith(new CommonSubscriber<List<WeekMakeBean>>(getView()) {
                    @Override
                    public void onNext(List<WeekMakeBean> beans) {
                        getView().setDataToYuyueDialogShow(beans);
                    }
                })
        );
    }

    @Override
    public void orderClass(int bookID, int chapterID, String lessonTime) {
        addSubscribe(mApiService.orderClass(bookID,chapterID,lessonTime)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(),false))
                .subscribeWith(new CommonSubscriber<Object>(getView()) {
                    @Override
                    public void onNext(Object bean) {
                        getView().onOrderClassSuccess();
                    }
                })
        );
    }

}
