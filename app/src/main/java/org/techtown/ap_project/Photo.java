package org.techtown.ap_project;

public class Photo {
    private String id;
    private String path;
    private boolean isSelected;

    public Photo(String id, String path) {
        this.id = id;
        this.path = path;
        this.isSelected = false;
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}