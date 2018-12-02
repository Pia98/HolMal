package com.holmal.app.holmal.model;

public class Item {
    private String itemName;
    private int quantity;
    private boolean important;
    private boolean favorite;
    private Person itsTask;
    private String additionalInfo;

    private boolean done;
    private int timeDone;

    public Item() {
    }

    public Item(String itemName, int quantity, boolean important, boolean favorite, Person itsTask, String additionalInfo, boolean done, int timeDone) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.important = important;
        this.favorite = favorite;
        this.itsTask = itsTask;
        this.additionalInfo = additionalInfo;
        this.done = done;
        this.timeDone = timeDone;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
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

    public Person getItsTask() {
        return itsTask;
    }

    public void setItsTask(Person itsTask) {
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
}
