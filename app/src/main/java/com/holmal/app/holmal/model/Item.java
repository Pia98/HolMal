package com.holmal.app.holmal.model;

/**
 * Model for an item. Items are part of a shopping list of a household and specify what needs to be bought.
 * They have a name. Additionally a user can set a quantity, the importance, say that they will take care of it or
 * provide additional information on the item.
 * Items also belong to a shopping list, and can be done.
 */
public class Item {

    private String itemName;
    private String quantity;
    private boolean important;
    private boolean favorite;
    private String itsTask;
    private String additionalInfo;
    private String belongsTo;

    private boolean done;
    private int timeDone;


    /**
     * constructor for an item
     * @param itemName name of the item
     * @param quantity amount to be bought
     * @param important boolean that gives information on how urgent the item is
     * @param favorite is this item a favourite
     * @param itsTask person who will buy this item
     * @param additionalInfo additional information on the item
     * @param done state of item (has it been bought)
     * @param timeDone time since someone bought it
     * @param belongsTo shopping list the item belongs to
     */
    public Item(String itemName, String quantity, boolean important, boolean favorite, String itsTask, String additionalInfo, boolean done, int timeDone, String belongsTo) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.important = important;
        this.favorite = favorite;
        this.itsTask = itsTask;
        this.additionalInfo = additionalInfo;
        this.done = done;
        this.timeDone = timeDone;
        this.belongsTo = belongsTo;
    }

    public Item(){
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
    public String getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(String belongsTo) {
        this.belongsTo = belongsTo;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemName='" + itemName + '\'' +
                ", quantity='" + quantity + '\'' +
                ", important=" + important +
                ", favorite=" + favorite +
                ", itsTask='" + itsTask + '\'' +
                ", additionalInfo='" + additionalInfo + '\'' +
                ", belongsTo='" + belongsTo + '\'' +
                ", done=" + done +
                ", timeDone=" + timeDone +
                '}';
    }
}
