package com.holmal.app.holmal.model;

public class ShoppingList {
    private String listName;
    private Item[] itemsOfThisList;
    private String category;

    public ShoppingList() {
    }

    public ShoppingList(String listName, Item[] itemsOfThisList, String category) {
        this.listName = listName;
        this.itemsOfThisList = itemsOfThisList;
        this.category = category;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public Item[] getItemsOfThisList() {
        return itemsOfThisList;
    }

    public void setItemsOfThisList(Item[] itemsOfThisList) {
        this.itemsOfThisList = itemsOfThisList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
