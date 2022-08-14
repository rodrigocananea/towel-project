package com.towel.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappedMap<U, T, K> implements Serializable {
    private K defaultValue;
    private Map<U, Map<T, K>> map = new HashMap();

    public MappedMap() {
    }

    public MappedMap(K defaultObject) {
        this.defaultValue = defaultObject;
    }

    public void put(U firstKey, T secondKey, K value) {
        Map<T, K> uMap = this.map.get(firstKey);
        if (uMap == null) {
            uMap = new HashMap<>();
            this.map.put(firstKey, uMap);
        }
        uMap.put(secondKey, value);
    }

    public K get(U firstKey, T secondKey) {
        Map<T, K> uMap = this.map.get(firstKey);
        if (uMap == null) {
            return this.defaultValue;
        }
        K k = uMap.get(secondKey);
        if (k == null) {
            return this.defaultValue;
        }
        return k;
    }

    public Map<T, K> getMap(U key) {
        return this.map.get(key);
    }

    public List<U> getKeys() {
        List<U> list = new ArrayList<>();
        for (Map.Entry<U, Map<T, K>> ent : this.map.entrySet()) {
            list.add(ent.getKey());
        }
        return list;
    }

    public K getDefaultValue() {
        return this.defaultValue;
    }
}
