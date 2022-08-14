package com.towel.collections.paginator;

import com.towel.el.FieldResolver;
import java.util.List;

public interface Paginator<T> {
    @Deprecated
    void filter(String str, FieldResolver fieldResolver);

    int getCurrentPage();

    List<T> getData();

    int getMaxPage();

    List<T> nextResult();

    void setCurrentPage(int i);

    void setData(List<T> list);
}
