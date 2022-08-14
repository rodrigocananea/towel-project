package com.towel.swing.calendar;

import com.towel.awt.ann.Action;
import com.towel.awt.ann.ActionManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import javax.swing.Icon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

public class CalendarView extends JPanel {
    @Action(method = "openPopup")
    private JButton button;
    private DatePicker datePicker;
    private JTextField editor;
    private String lastValidString;
    private JPopupMenu popup;

    public CalendarView() {
        this(null, null);
    }

    public CalendarView(String pattern) {
        this(null, new SimpleDateFormat(pattern));
    }

    public CalendarView(Locale locale, DateFormat format) {
        this.datePicker = new DatePicker(locale, format);
        this.datePicker.addPropertyChangeListener(new PropertyChangeListener() {
            /* class com.towel.swing.calendar.CalendarView.AnonymousClass1 */

            public void propertyChange(PropertyChangeEvent evt) {
                String prop = evt.getPropertyName();
                if ("day".equals(prop) || "date".equals(prop)) {
                    CalendarView.this.dateSelected(CalendarView.this.datePicker.getDate());
                }
            }
        });
        this.popup = new JPopupMenu();
        this.popup.add(this.datePicker);
        this.lastValidString = "";
        init();
        new ActionManager(this);
    }

    private void init() {
        setLayout(new BorderLayout());
        add(getEditor(), "Center");
        add(getButton(), "East");
    }

    private JTextField getEditor() {
        if (this.editor == null) {
            this.editor = new JTextField(10);
            this.editor.setInputVerifier(new DateInputVerifier(this, null));
        }
        return this.editor;
    }

    private JButton getButton() {
        if (this.button == null) {
            this.button = new JButton(". .");
            this.button.setMargin(new Insets(0, 5, 0, 5));
        }
        return this.button;
    }

    public void setText(String text) {
        getEditor().setText(text);
        commitEdit();
    }

    public String getText() {
        return getEditor().getText();
    }

    public void setIcon(Icon icon) {
        getButton().setIcon(icon);
        if (icon == null) {
            getButton().setText(". .");
        } else {
            getButton().setText("");
        }
    }

    public Calendar getSelectedDate() {
        if (getText().length() > 0) {
            return this.datePicker.getSelectedDate();
        }
        return null;
    }

    public void setSelectedDate(Calendar calendar) {
        this.datePicker.setSelectedDate(calendar);
    }

    public Locale getLocale() {
        return this.datePicker.getLocale();
    }

    public void setLocale(Locale locale) {
        this.datePicker.setLocale(locale);
    }

    public DateFormat getDateFormat() {
        return this.datePicker.getDateFormat();
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.datePicker.setDateFormat(dateFormat);
        if (this.lastValidString.length() > 0) {
            this.lastValidString = dateFormat.format(getSelectedDate().getTime());
        }
        commitEdit();
    }

    public void setPattern(String pattern) {
        setDateFormat(new SimpleDateFormat(pattern));
    }

    public Color getHeaderBackground() {
        return this.datePicker.getHeaderBackground();
    }

    public void setHeaderBackground(Color headerBackground) {
        this.datePicker.setHeaderBackground(headerBackground);
    }

    public Color getWeekDaysBackground() {
        return this.datePicker.getWeekDaysBackground();
    }

    public void setWeekDaysBackground(Color weekDaysBackground) {
        this.datePicker.setWeekDaysBackground(weekDaysBackground);
    }

    public Color getDayPickerBackground() {
        return this.datePicker.getDayPickerBackground();
    }

    public void setDayPickerBackground(Color dayPickerBackground) {
        this.datePicker.setDayPickerBackground(dayPickerBackground);
    }

    public Color getSelectedDayBackground() {
        return this.datePicker.getSelectedDayBackground();
    }

    public void setSelectedDayBackground(Color selectedDayBackground) {
        this.datePicker.setSelectedDayBackground(selectedDayBackground);
    }

    public Color getHeaderForeground() {
        return this.datePicker.getHeaderForeground();
    }

    public void setHeaderForeground(Color headerForeground) {
        this.datePicker.setHeaderForeground(headerForeground);
    }

    public Color getWeekDaysForeground() {
        return this.datePicker.getWeekDaysForeground();
    }

    public void setWeekDaysForeground(Color weekDaysForeground) {
        this.datePicker.setWeekDaysForeground(weekDaysForeground);
    }

    public Color getDayPickerForeground() {
        return this.datePicker.getDayPickerForeground();
    }

    public void setDayPickerForeground(Color dayPickerForeground) {
        this.datePicker.setDayPickerForeground(dayPickerForeground);
    }

    public Color getSelectedDayForeground() {
        return this.datePicker.getSelectedDayForeground();
    }

    public void setSelectedDayForeground(Color selectedDayForeground) {
        this.datePicker.setSelectedDayForeground(selectedDayForeground);
    }

    public void setTodayString(String todayString) {
        this.datePicker.setTodayString(todayString);
    }

    public void setTodayButtonVisible(boolean visible) {
        this.datePicker.setTodayButtonVisible(visible);
    }

    private void openPopup() {
        this.popup.show(this.button, this.button.getWidth() - this.datePicker.getPreferredSize().width, this.button.getHeight());
    }

    public void dateSelected(String strDate) {
        getEditor().setText(strDate);
        commitEdit();
        this.popup.setVisible(false);
    }

    public void setEnabled(boolean enabled) {
        getEditor().setEnabled(enabled);
        getButton().setEnabled(enabled);
        CalendarView.super.setEnabled(enabled);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void commitEdit() {
        String strDate = getText();
        if (strDate.isEmpty() || isValidDate(strDate)) {
            this.lastValidString = strDate;
        } else {
            getEditor().setText(this.lastValidString);
            strDate = this.lastValidString;
        }
        if (strDate.length() > 0) {
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(getDateFormat().parse(strDate));
                this.datePicker.setSelectedDate(cal);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean isValidDate(String strDate) {
        try {
            getDateFormat().parse(strDate);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /* access modifiers changed from: private */
    public class DateInputVerifier extends InputVerifier {
        private DateInputVerifier() {
        }

        /* synthetic */ DateInputVerifier(CalendarView calendarView, DateInputVerifier dateInputVerifier) {
            this();
        }

        public boolean shouldYieldFocus(JComponent input) {
            CalendarView.this.commitEdit();
            return true;
        }

        public boolean verify(JComponent input) {
            String strDate = ((JTextField) input).getText();
            return strDate.isEmpty() || CalendarView.this.isValidDate(strDate);
        }
    }
}
