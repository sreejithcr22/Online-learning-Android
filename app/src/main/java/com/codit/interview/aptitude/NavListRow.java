package com.codit.interview.aptitude;

/**
 * Created by Sreejith on 27-Jul-16.
 */
public class NavListRow {

    int icon;
    String label;

    public NavListRow(int icon, String label) {
        this.icon = icon;
        this.label = label;
    }



    public String getLabel() {
        return label;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
