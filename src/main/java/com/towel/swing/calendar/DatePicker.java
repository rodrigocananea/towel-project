package com.towel.swing.calendar;

import com.towel.cfg.TowelConfig;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;

public class DatePicker extends JPanel {

    private static final String TODAY_TXT_ATTR = "today_txt";
    private CalendarView calendar;
    private DateFormat dateFormat;
    private JLabel[] dayLabels;
    private Color dayPickerBackground;
    private Color dayPickerForeground;
    private JPanel daysPanel;
    private Color headerBackground;
    private Color headerForeground;
    private Locale locale;
    private JLabel monthLabel;
    private String[] monthNames;
    private JPanel monthPanel;
    private JLabel nextMonthLabel;
    private JLabel nextYearLabel;
    private JLabel previousMonthLabel;
    private JLabel previousYearLabel;
    private Calendar selectedDate;
    private Color selectedDayBackground;
    private Color selectedDayForeground;
    private JButton todayButton;
    private JLabel[] weekDayLabels;
    private String[] weekDayNames;
    private Color weekDaysBackground;
    private Color weekDaysForeground;
    private JLabel yearLabel;

    public DatePicker() {
        this(null, null);
    }

    public DatePicker(String pattern) {
        this(null, new SimpleDateFormat(pattern));
    }

    public DatePicker(Locale locale2, DateFormat dateFormat2) {
        locale2 = locale2 == null ? Locale.getDefault() : locale2;
        this.locale = locale2;
        dateFormat2 = dateFormat2 == null ? DateFormat.getDateInstance(2, locale2) : dateFormat2;
        dateFormat2.setLenient(false);
        this.dateFormat = dateFormat2;
        this.selectedDate = getToday();
        init();
        refresh();
    }

    public DatePicker(CalendarView cal, int day, int month, int year) {
        this.calendar = cal;
        this.locale = TowelConfig.getInstance().getDefaultLocale();
        this.selectedDate = getToday();
        if (day > 0) {
            this.selectedDate.set(5, day);
            this.selectedDate.set(2, month - 1);
            this.selectedDate.set(1, year);
        }
        init();
        refresh();
    }

    private void updateButtonTxt(Locale locale2) {
        InputStream is = getClass().getResourceAsStream("/res/strings_" + locale2.toString() + ".properties");
        Properties props = new Properties();
        try {
            props.load(is);
            is.close();
            this.todayButton.setText(props.getProperty(TODAY_TXT_ATTR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        this.headerBackground = Color.LIGHT_GRAY;
        this.weekDaysBackground = new Color(63, 124, 124);
        this.dayPickerBackground = UIManager.getColor("Label.background");
        this.selectedDayBackground = this.dayPickerBackground.darker();
        this.headerForeground = Color.BLACK;
        this.weekDaysForeground = Color.WHITE;
        this.dayPickerForeground = UIManager.getColor("Label.foreground");
        this.selectedDayForeground = UIManager.getColor("Label.foreground");
        setLayout(new BorderLayout());
        add(getMonthPanel(), "North");
        add(getDaysPanel(), "Center");
        add(getTodayButton(), "South");
        updateWeekDays(this.locale);
        updateButtonTxt(this.locale);
    }

    private JPanel getMonthPanel() {
        if (monthPanel == null) {
            monthPanel = new JPanel(new GridBagLayout());

            previousMonthLabel = createLabelWithBorder("<");
            previousMonthLabel.setBackground(headerBackground);
            previousMonthLabel.setForeground(headerForeground);
            previousMonthLabel.addMouseListener(new NavigationListener() {
                @Override
                public void execute() {
                    int oldMonth = selectedDate.get(Calendar.MONTH);
                    selectedDate.add(Calendar.MONTH, -1);
                    firePropertyChange("month", oldMonth, oldMonth - 1);
                    if (oldMonth == 0) {
                        int year = selectedDate.get(Calendar.YEAR);
                        firePropertyChange("year", year + 1, year);
                    }
                    refresh();
                }
            });
            monthPanel.add(previousMonthLabel);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            monthLabel = createLabelWithBorder("");
            monthLabel.setBackground(headerBackground);
            monthLabel.setForeground(headerForeground);
            monthPanel.add(monthLabel, gbc);

            nextMonthLabel = createLabelWithBorder(">");
            nextMonthLabel.setBackground(headerBackground);
            nextMonthLabel.setForeground(headerForeground);
            nextMonthLabel.addMouseListener(new NavigationListener() {
                @Override
                public void execute() {
                    int oldMonth = selectedDate.get(Calendar.MONTH);
                    selectedDate.add(Calendar.MONTH, 1);
                    firePropertyChange("month", oldMonth, oldMonth + 1);
                    if (oldMonth == 11) {
                        int year = selectedDate.get(Calendar.YEAR);
                        firePropertyChange("year", year - 1, year);
                    }
                    refresh();
                }
            });
            monthPanel.add(nextMonthLabel);

            previousYearLabel = createLabelWithBorder("<");
            previousYearLabel.setBackground(headerBackground);
            previousYearLabel.setForeground(headerForeground);
            previousYearLabel.addMouseListener(new NavigationListener() {
                @Override
                public void execute() {
                    int oldYear = selectedDate.get(Calendar.YEAR);
                    selectedDate.add(Calendar.YEAR, -1);
                    firePropertyChange("year", oldYear, oldYear - 1);
                    refresh();
                }
            });
            monthPanel.add(previousYearLabel);

            yearLabel = createLabelWithBorder("");
            yearLabel.setBackground(headerBackground);
            yearLabel.setForeground(headerForeground);
            monthPanel.add(yearLabel);

            nextYearLabel = createLabelWithBorder(">");
            nextYearLabel.setBackground(headerBackground);
            nextYearLabel.setForeground(headerForeground);
            nextYearLabel.addMouseListener(new NavigationListener() {
                @Override
                public void execute() {
                    int oldYear = selectedDate.get(Calendar.YEAR);
                    selectedDate.add(Calendar.YEAR, 1);
                    firePropertyChange("year", oldYear, oldYear + 1);
                    refresh();
                }
            });
            monthPanel.add(nextYearLabel);
        }
        return monthPanel;
    }

    private JPanel getDaysPanel() {
        if (this.daysPanel == null) {
            this.daysPanel = new JPanel(new GridLayout(7, 7));
            this.weekDayLabels = new JLabel[7];
            for (int i = 0; i < 7; i++) {
                this.weekDayLabels[i] = new JLabel();
                this.weekDayLabels[i].setHorizontalAlignment(0);
                this.weekDayLabels[i].setBackground(this.weekDaysBackground);
                this.weekDayLabels[i].setForeground(this.weekDaysForeground);
                this.weekDayLabels[i].setOpaque(true);
                this.daysPanel.add(this.weekDayLabels[i]);
            }
            this.dayLabels = new JLabel[42];
            for (int i2 = 0; i2 < 42; i2++) {
                this.dayLabels[i2] = createLabelWithBorder("");
                this.dayLabels[i2].setBackground(this.dayPickerBackground);
                this.dayLabels[i2].setForeground(this.dayPickerForeground);
                this.dayLabels[i2].addMouseListener(new DaySelectionListener(this, null));
                this.daysPanel.add(this.dayLabels[i2]);
            }
        }
        return this.daysPanel;
    }

    private JButton getTodayButton() {
        if (this.todayButton == null) {
            this.todayButton = new JButton("Today");
            this.todayButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    DatePicker.this.selectedDate = DatePicker.this.getToday();
                    DatePicker.this.setSelectedDay(DatePicker.this.selectedDate.get(5));
                    DatePicker.this.firePropertyChange("day", 0, DatePicker.this.selectedDate.get(5));
                    if (DatePicker.this.calendar != null) {
                        DatePicker.this.calendar.dateSelected(DatePicker.this.getDate());
                    }
                }
            });
        }
        return this.todayButton;
    }

    private void updateWeekDays(Locale locale2) {
        DateFormatSymbols symbols = new DateFormatSymbols(locale2);
        this.weekDayNames = symbols.getShortWeekdays();
        this.monthNames = symbols.getMonths();
        for (int i = 0; i < 7; i++) {
            this.weekDayLabels[i].setText(this.weekDayNames[i + 1]);
        }
    }

    private String getMonthName(int month) {
        return this.monthNames[month];
    }

    /* access modifiers changed from: private */
 /* access modifiers changed from: public */
    private void refresh() {
        this.monthLabel.setText(getMonthName(this.selectedDate.get(2)));
        this.yearLabel.setText(String.valueOf(this.selectedDate.get(1)));
        populateCells();
        setSelectedDay(this.selectedDate.get(5));
    }

    private void populateCells() {
        Calendar cal = getSelectedDate();
        cal.set(5, 1);
        int weekDay = cal.get(7);
        int monthDay = cal.getActualMaximum(5);
        int day = 1;
        for (int i = 0; i < 42; i++) {
            if (i < weekDay - 1 || i > (monthDay + weekDay) - 2) {
                this.dayLabels[i].setText("");
            } else {
                this.dayLabels[i].setText(String.valueOf(day));
                day++;
            }
            this.dayLabels[i].setBackground(this.dayPickerBackground);
            this.dayLabels[i].setForeground(this.dayPickerForeground);
        }
    }

    /* access modifiers changed from: private */
 /* access modifiers changed from: public */
    private Calendar getToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal;
    }

    public String getDate() {
        return this.dateFormat.format(getSelectedDate().getTime());
    }

    public Calendar getSelectedDate() {
        return (Calendar) this.selectedDate.clone();
    }

    public void setSelectedDate(Calendar calendar2) {
        if (calendar2 != null) {
            Calendar oldDate = getSelectedDate();
            this.selectedDate = getToday();
            this.selectedDate.set(calendar2.get(1), calendar2.get(2), calendar2.get(5));
            firePropertyChange("date", oldDate, getSelectedDate());
            refresh();
        }
    }

    public void setSelectedDay(int newDay) {
        for (int i = 0; i < 42; i++) {
            if (this.dayLabels[i].getText().equals(Integer.toString(newDay))) {
                this.selectedDate.set(5, newDay);
                this.dayLabels[i].setBackground(this.selectedDayBackground);
                this.dayLabels[i].setForeground(this.selectedDayForeground);
                return;
            }
        }
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale2) {
        if (locale2 != null) {
            this.locale = locale2;
            updateWeekDays(locale2);
        }
    }

    public DateFormat getDateFormat() {
        return this.dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat2) {
        if (dateFormat2 != null) {
            this.dateFormat = dateFormat2;
        }
    }

    public void setPattern(String pattern) {
        setDateFormat(new SimpleDateFormat(pattern));
    }

    public Color getHeaderBackground() {
        return this.headerBackground;
    }

    public void setHeaderBackground(Color headerBg) {
        if (headerBg != null) {
            this.headerBackground = headerBg;
            this.previousMonthLabel.setBackground(headerBg);
            this.nextMonthLabel.setBackground(headerBg);
            this.monthLabel.setBackground(headerBg);
            this.previousYearLabel.setBackground(headerBg);
            this.nextYearLabel.setBackground(headerBg);
            this.yearLabel.setBackground(headerBg);
        }
    }

    public Color getWeekDaysBackground() {
        return this.weekDaysBackground;
    }

    public void setWeekDaysBackground(Color weekDaysBg) {
        if (weekDaysBg != null) {
            this.weekDaysBackground = weekDaysBg;
            for (int i = 0; i < 7; i++) {
                this.weekDayLabels[i].setBackground(weekDaysBg);
            }
        }
    }

    public Color getDayPickerBackground() {
        return this.dayPickerBackground;
    }

    public void setDayPickerBackground(Color dayPickerBg) {
        if (dayPickerBg != null) {
            this.dayPickerBackground = dayPickerBg;
            refresh();
        }
    }

    public Color getSelectedDayBackground() {
        return this.selectedDayBackground;
    }

    public void setSelectedDayBackground(Color selectedDayBg) {
        if (selectedDayBg != null) {
            this.selectedDayBackground = selectedDayBg;
            refresh();
        }
    }

    public Color getHeaderForeground() {
        return this.headerForeground;
    }

    public void setHeaderForeground(Color headerFg) {
        if (headerFg != null) {
            this.headerForeground = headerFg;
            this.previousMonthLabel.setForeground(headerFg);
            this.nextMonthLabel.setForeground(headerFg);
            this.monthLabel.setForeground(headerFg);
            this.previousYearLabel.setForeground(headerFg);
            this.nextYearLabel.setForeground(headerFg);
            this.yearLabel.setForeground(headerFg);
        }
    }

    public Color getWeekDaysForeground() {
        return this.weekDaysForeground;
    }

    public void setWeekDaysForeground(Color weekDaysFg) {
        if (weekDaysFg != null) {
            this.weekDaysForeground = weekDaysFg;
            for (int i = 0; i < 7; i++) {
                this.weekDayLabels[i].setForeground(weekDaysFg);
            }
        }
    }

    public Color getDayPickerForeground() {
        return this.dayPickerForeground;
    }

    public void setDayPickerForeground(Color dayPickerFg) {
        if (dayPickerFg != null) {
            this.dayPickerForeground = dayPickerFg;
            refresh();
        }
    }

    public Color getSelectedDayForeground() {
        return this.selectedDayForeground;
    }

    public void setSelectedDayForeground(Color selectedDayFg) {
        if (selectedDayFg != null) {
            this.selectedDayForeground = selectedDayFg;
            refresh();
        }
    }

    public void setTodayString(String todayString) {
        getTodayButton().setText(todayString);
    }

    public void setTodayButtonVisible(boolean visible) {
        getTodayButton().setVisible(visible);
    }

    private JLabel createLabelWithBorder(String text) {
        JLabel label = new JLabel(text);
        label.setBorder(BorderFactory.createEtchedBorder());
        label.setHorizontalAlignment(0);
        label.setOpaque(true);
        return label;
    }

    /* access modifiers changed from: private */
    public class DaySelectionListener extends MouseAdapter {

        private DaySelectionListener() {
        }

        /* synthetic */ DaySelectionListener(DatePicker datePicker, DaySelectionListener daySelectionListener) {
            this();
        }

        public void mouseClicked(MouseEvent e) {
            String day = ((JLabel) e.getSource()).getText();
            if (day.length() > 0) {
                DatePicker.this.setSelectedDay(Integer.parseInt(day));
                DatePicker.this.firePropertyChange("day", 0, DatePicker.this.selectedDate.get(5));
                if (DatePicker.this.calendar != null) {
                    DatePicker.this.calendar.dateSelected(DatePicker.this.getDate());
                }
            }
            DatePicker.this.refresh();
        }
    }

    // Listener for navigation labels.
    private class NavigationListener extends MouseAdapter {

        // Timer used to auto repeat execute() method whenever
        // the user holds one of the navigation labels.
        Timer timer;

        public NavigationListener() {
            timer = new Timer(100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    execute();
                }
            });
            timer.setInitialDelay(500);
        }

        public void execute() {
            // This method must be implemented by subclasses
        }

        @Override
        public void mousePressed(MouseEvent e) {
            execute();
            timer.start();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            timer.stop();
        }

    }
}
