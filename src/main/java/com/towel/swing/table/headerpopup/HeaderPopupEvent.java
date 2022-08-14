package com.towel.swing.table.headerpopup;

import java.util.EventObject;

public class HeaderPopupEvent extends EventObject {
    private int modelIndex;

    public HeaderPopupEvent(Object source, int modelIndex2) {
        super(source);
        this.modelIndex = modelIndex2;
    }

    public int getModelIndex() {
        return this.modelIndex;
    }
}
