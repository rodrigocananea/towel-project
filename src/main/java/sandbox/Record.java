package sandbox;

/* compiled from: PagingTable */
class Record {
    static int counter;
    static String[] headers = {"Record Number", "Batch Number", "Reserved"};
    String[] data;

    public Record() {
        StringBuilder sb = new StringBuilder();
        int i = counter;
        counter = i + 1;
        this.data = new String[]{sb.append(i).toString(), new StringBuilder().append(System.currentTimeMillis()).toString(), "Reserved"};
    }

    public String getValueAt(int i) {
        return this.data[i];
    }

    public static String getColumnName(int i) {
        return headers[i];
    }

    public static int getColumnCount() {
        return headers.length;
    }
}
