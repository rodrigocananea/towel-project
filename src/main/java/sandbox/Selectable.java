package sandbox;

import javax.accessibility.AccessibleComponent;

public interface Selectable extends AccessibleComponent {
    boolean isSelected();

    void setSelected(boolean z);
}
