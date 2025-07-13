package com.inotes.model;

public interface Note {
    void setTitle(String title);
    void setContent(String content);
    void setTypeId(int typeId);
    String getTitle();
    String getContent();
    String getType();
    int getTypeId();
    void save();
    void delete();
    int getId();
    void setId(int id);
}