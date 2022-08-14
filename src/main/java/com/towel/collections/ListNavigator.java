package com.towel.collections;

import java.util.List;

public class ListNavigator<T> implements Navigator<T> {
    private int currentIndex;
    private List<T> list;

    public ListNavigator(List<T> list2) {
        this.list = list2;
    }

    @Override // com.towel.collections.Navigator
    public int size() {
        return this.list.size();
    }

    @Override // com.towel.collections.Navigator
    public T get(int idx) {
        return this.list.get(idx);
    }

    @Override // com.towel.collections.Navigator
    public T next() {
        this.currentIndex++;
        if (this.currentIndex == size()) {
            this.currentIndex = size() - 1;
        }
        return get(this.currentIndex);
    }

    @Override // com.towel.collections.Navigator
    public T previous() {
        this.currentIndex--;
        if (this.currentIndex == -1) {
            this.currentIndex = 0;
        }
        return get(this.currentIndex);
    }
}
