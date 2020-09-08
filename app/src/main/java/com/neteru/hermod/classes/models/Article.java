package com.neteru.hermod.classes.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@SuppressWarnings("unused")
@DatabaseTable(tableName = "articles")
public class Article {

    @DatabaseField(columnName = "id", canBeNull = false)
    private String id;
    @DatabaseField(columnName = "source", canBeNull = false)
    private String source;
    @DatabaseField(columnName = "author")
    private String author;
    @DatabaseField(columnName = "title", canBeNull = false)
    private String title;
    @DatabaseField(columnName = "description")
    private String description;
    @DatabaseField(columnName = "url")
    private String url;
    @DatabaseField(columnName = "urlToImage")
    private String urlToImage;
    @DatabaseField(columnName = "publishedAt")
    private String publishedAt;
    @DatabaseField(columnName = "fav")
    private Boolean fav;

    public Article(){}

    public Article(String id, String source, String author, String title, String description, String url, String urlToImage, String publishedAt, Boolean fav){

        this.id = id;
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.fav = fav;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Boolean getFav() {
        return fav;
    }

    public void setFav(Boolean fav) {
        this.fav = fav;
    }
}
