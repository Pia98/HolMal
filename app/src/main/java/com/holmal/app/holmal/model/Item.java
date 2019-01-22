package com.holmal.app.holmal.model;

public class Item {
    private String itemName;
    private String quantity;
    private boolean important;
    private boolean favorite;
    private String itsTask;
    private String additionalInfo;

    private boolean done;
    private int timeDone;

    // das Element wird nicht in DB gespeichert, dient nur dazu DB Id in App zu transportieren
    private transient String storeId;


    public Item() {
    }

    public Item(String itemName, String quantity, boolean important, boolean favorite, String itsTask, String additionalInfo, boolean done, int timeDone) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.important = important;
        this.favorite = favorite;
        this.itsTask = itsTask;
        this.additionalInfo = additionalInfo;
        this.done = done;
        this.timeDone = timeDone;
    }

    public Item(String itemName, String quantity, boolean important, boolean favorite, String additionalInfo) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.important = important;
        this.favorite = favorite;
        this.additionalInfo = additionalInfo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getItsTask() {
        return itsTask;
    }

    public void setItsTask(String itsTask) {
        this.itsTask = itsTask;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getTimeDone() {
        return timeDone;
    }

    public void setTimeDone(int timeDone) {
        this.timeDone = timeDone;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
