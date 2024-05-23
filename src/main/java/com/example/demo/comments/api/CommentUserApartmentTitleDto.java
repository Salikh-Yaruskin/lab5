package com.example.demo.comments.api;

public class CommentUserApartmentTitleDto extends CommentDto {
    private String title;
    private String user;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
