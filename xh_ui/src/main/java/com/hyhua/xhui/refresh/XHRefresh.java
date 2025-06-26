package com.hyhua.xhui.refresh;

public interface XHRefresh {
    /**
     * 刷新时是否禁止滚动
     *
     * @param disableRefreshScroll 是否禁止滚动
     */
    void setDisableRefreshScroll(boolean disableRefreshScroll);

    /**
     * 刷新完成
     */
    void refreshFinished();

    /**
     * 设置下拉刷新监听器
     *
     * @param xhRefreshListener 下拉刷新监听器
     */
    void setRefreshListener(XHRefreshListener xhRefreshListener);

    /**
     * 设置下拉刷新的视图
     *
     * @param xhOverView 下拉刷新的视图
     */
    void setRefreshOverView(XHOverView xhOverView);

    interface XHRefreshListener {
        void onRefresh();

        boolean enableRefresh();
    }
}
