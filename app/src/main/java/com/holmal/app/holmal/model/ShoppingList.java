package com.holmal.app.holmal.model;

import java.util.HashMap;

/**
 * Model for a ShoppinList object.
 * ShoppingList belongs to a {@link Household}.
 * A ShoppingList has a name and references to the {@link Item}s of this list.
 * Also it can have a category.
 */
public class ShoppingList {

    private String listName;
    private HashMap<String, String> itemsOfThisList;
    private String category;
    private String idBelongingTo;

    /**
     * Constructor for a shopping list
     * @param listName every list has a name.
     * @param category lists can belong to a category. Some categories are provided.
     * @param idBelongingTo a shopping list belongs to a {@link Household}.
     */
    public ShoppingList(String listName, String category, String idBelongingTo) {
        this.listName = listName;
        this.category = category;
        this.idBelongingTo = idBelongingTo;
    }

    public ShoppingList(){
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
