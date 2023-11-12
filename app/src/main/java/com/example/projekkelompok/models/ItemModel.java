package com.example.projekkelompok.models;

public class ItemModel {
    private String task, id;

    public ItemModel(){}

    public ItemModel(String task, String id) {
        this.task = task;
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
