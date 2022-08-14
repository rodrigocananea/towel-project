package com.towel.collections.paginator;

import com.towel.collections.CollectionsUtil;
import com.towel.collections.filter.Filter;
import com.towel.el.FieldResolver;
import java.util.ArrayList;
import java.util.List;

public class ListPaginator<T> implements Paginator<T> {
    private int currentPagination;
    private List<T> list;
    private int listSize;
    private int maxPage;
    private List<T> original;
    private int pageResults;

    protected ListPaginator(int listSize2, int resultsPerPage) {
        this.pageResults = resultsPerPage;
        this.listSize = listSize2;
        this.currentPagination = 0;
        calcPages();
    }

    public ListPaginator(List<T> list2) {
        this(list2, list2.size());
    }

    public ListPaginator(List<T> list2, int resultsPerPage) {
        this.original = list2;
        this.list = new ArrayList(list2);
        this.pageResults = resultsPerPage;
        calcPages();
    }

    private void calcPages() {
        this.currentPagination = 0;
        this.listSize = this.list.size();
        if (this.pageResults != 0) {
            if (this.listSize % this.pageResults == 0) {
                this.maxPage = (this.listSize / this.pageResults) - 1;
            } else {
                this.maxPage = this.listSize / this.pageResults;
            }
        }
    }

    @Override // com.towel.collections.paginator.Paginator
    public List<T> nextResult() {
        int toIndex = (this.currentPagination + 1) * this.pageResults;
        if (toIndex > this.list.size()) {
            toIndex = this.list.size();
        }
        List<T> list2 = this.list.subList(this.currentPagination * this.pageResults, toIndex);
        this.currentPagination++;
        return list2;
    }

    @Override // com.towel.collections.paginator.Paginator
    public void setData(List<T> list2) {
        this.list = list2;
        calcPages();
    }

    @Override // com.towel.collections.paginator.Paginator
    public List<T> getData() {
        return this.list;
    }

    @Override // com.towel.collections.paginator.Paginator
    public int getCurrentPage() {
        return this.currentPagination;
    }

    @Override // com.towel.collections.paginator.Paginator
    public void setCurrentPage(int page) {
        this.currentPagination = page;
    }

    @Override // com.towel.collections.paginator.Paginator
    public int getMaxPage() {
        return this.maxPage;
    }

    @Override // com.towel.collections.paginator.Paginator
    public void filter(final String text, final FieldResolver field) {
        this.list = CollectionsUtil.filter(this.original, new Filter<T>() {
            /* class com.towel.collections.paginator.ListPaginator.AnonymousClass1 */

            @Override // com.towel.collections.filter.Filter
            public boolean accept(T obj) {
                Object objR = field.getValue(obj);
                if (objR == null) {
                    return false;
                }
                return objR.toString().toUpperCase().contains(text.toUpperCase());
            }
        });
    }
}
