package com.giftsearcher.giftsearcherclient;

import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public abstract class LessScrollListener<T> implements AbsListView.OnScrollListener {

    private final ListView listView;
    private final ArrayAdapter<T> listAdapter;

    private int page = 1;
    private void setPage(int page) {
        if (page >= 1)
            this.page = page;
    }

    //Кол-во элементов в запросе(без пагинации)
    private int totalItemsCount;
    //Выполняется ли метод onLoadMore
    private boolean methodRuns = false;

    public LessScrollListener(ListView listView, ArrayAdapter<T> listAdapter, int totalItemsCount) {
        this.listView = listView;
        this.listAdapter = listAdapter;
        this.totalItemsCount = totalItemsCount;
    }

    public LessScrollListener(ListView listView, ArrayAdapter<T> listAdapter, int totalItemsCount, int page) {
        this.listView = listView;
        this.listAdapter = listAdapter;
        this.totalItemsCount = totalItemsCount;
        setPage(page);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //Условие достижение конца списка в listView
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && (
                listView.getLastVisiblePosition() -
                listView.getHeaderViewsCount() -
                listView.getFooterViewsCount()) >= (listAdapter.getCount() - 1)) {

            if ((page + 1) <= Math.ceil(totalItemsCount / (double)10) && !methodRuns) {

                methodRuns = true;
                if (listAdapter.getCount() >= 20) {
                    for (int i = 0; i < 10; i++) {
                        //Удаляет всегда первый элемент списка(самый старый элемент)
                        listAdapter.remove(listAdapter.getItem(0));
                    }
                }
                methodRuns = onLoadMore(page);
                page++;
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) { }

    //Метод подгрузки данных
    public abstract boolean onLoadMore(int page);
}
