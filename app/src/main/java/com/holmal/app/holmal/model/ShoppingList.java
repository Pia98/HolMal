package com.holmal.app.holmal.model;

import java.util.HashMap;

public class ShoppingList {
    private String listName;
    private HashMap<String, String> itemsOfThisList;
    private String category;

    private String idBelongingTo;

    public ShoppingList() {
    }

    public ShoppingList(String listName, String category, String idBelongingTo) {
        this.listName = listName;
        this.category = category;
        this.idBelongingTo = idBelongingTo;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public HashMap<String, String> getItemsOfThisList() {
        return itemsOfThisList;
    }

    public void setItemsOfThisList(HashMap<String, String> itemsOfThisList) {
        this.itemsOfThisList = itemsOfThisList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIdBelongingTo() {
        return idBelongingTo;
    }

    public void setIdBelongingTo(String idBelongingTo) {
        this.idBelongingTo = idBelongingTo;
    }

    @Override
    public String toString() {
        return "ShoppingList{" +
                "listName='" + listName + '\'' +
                ", itemsOfThisList=" + itemsOfThisList +
                ", category='" + category + '\'' +
                ", idBelongingTo='" + idBelongingTo + '\'' +
                '}';
    }
}
