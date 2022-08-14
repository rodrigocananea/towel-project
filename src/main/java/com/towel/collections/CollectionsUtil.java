package com.towel.collections;

import com.towel.collections.aggr.AggregateFunc;
import com.towel.collections.filter.Filter;
import com.towel.el.FieldResolver;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CollectionsUtil {
    public static <T> List<T> filter(List<T> coll, Filter<T> filter) {
        List<T> result = new ArrayList<>();
        for (T t : coll) {
            if (filter.accept(t)) {
                result.add(t);
            }
        }
        return result;
    }

    public static <T> int firstIndexOf(List<T> coll, Filter<T> filter) {
        int i = coll.size();
        for (int j = 0; j < i; j++) {
            if (filter.accept(coll.get(j))) {
                return j;
            }
        }
        return -1;
    }

    public static <T> int lastIndexOf(List<T> coll, Filter<T> filter) {
        int i = coll.size();
        int idx = -1;
        for (int j = 0; j < i; j++) {
            if (filter.accept(coll.get(j))) {
                idx = j;
            }
        }
        return idx;
    }

    public static <T> int firstIndexOf(T[] tArr, Filter<T> filter) {
        int i = tArr.length;
        for (int j = 0; j < i; j++) {
            if (filter.accept(tArr[j])) {
                return j;
            }
        }
        return -1;
    }

    public static <T> int lastIndexOf(T[] tArr, Filter<T> filter) {
        int i = tArr.length;
        int idx = -1;
        for (int j = 0; j < i; j++) {
            if (filter.accept(tArr[j])) {
                idx = j;
            }
        }
        return idx;
    }

    public static <T> Integer[] allMatchIndex(List<T> list, Filter<T> filter) {
        Integer[] result = new Integer[list.size()];
        int currentIdx = 0;
        for (int i = 0; i < list.size(); i++) {
            if (filter.accept(list.get(i))) {
                result[currentIdx] = Integer.valueOf(i);
                currentIdx++;
            }
        }
        return (Integer[]) trim(result);
    }

    public static Object[] trim(Object[] obj) {
        int nullIndex = -1;
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] == null) {
                nullIndex = i;
            }
        }
        int nullIndex2 = nullIndex + 1;
        Object[] objs = new Object[nullIndex2];
        for (int i2 = 0; i2 < nullIndex2; i2++) {
            objs[i2] = obj[i2];
        }
        return objs;
    }

    public static <T> T aggregate(AggregateFunc<T> func, List<T> l) {
        func.init();
        for (T t : l) {
            func.update(t);
        }
        return func.getResult();
    }

    public static <T> T aggregate(AggregateFunc<T> func, List<?> l, String field) {
        return (T) aggregate(func, split(l, field));
    }

    public static <T> List<T> split(List<?> list, String fieldName) {
        ArrayList arrayList = new ArrayList();
        if (!list.isEmpty()) {
            FieldResolver resolver = new FieldResolver(list.get(0).getClass(), fieldName);
            Iterator<?> it = list.iterator();
            while (it.hasNext()) {
                arrayList.add(resolver.getValue(it.next()));
            }
        }
        return arrayList;
    }
}
