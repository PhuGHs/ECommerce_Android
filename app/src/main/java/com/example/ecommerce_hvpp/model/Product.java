package com.example.ecommerce_hvpp.model;


public class Product {
    private String id;
    private String name;
    private String club;
    private String nation;
    private String season;
    private String description;
    private double price;
    private double point;
    private long sizeM, sizeL, sizeXL;
    private String urlmain, urlsub1, urlsub2, urlthumb;
    private String status;
    private long timeAdded;

    public Product(){

    }

    public Product(String id, String name, String club, String nation, String season, String description, double price, double point, long sizeM, long sizeL, long sizeXL, String urlmain, String urlsub1, String urlsub2, String urlthumb, String status, long timeAdded) {
        this.id = id;
        this.name = name;
        this.club = club;
        this.nation = nation;
        this.season = season;
        this.description = description;
        this.price = price;
        this.point = point;
        this.sizeM = sizeM;
        this.sizeL = sizeL;
        this.sizeXL = sizeXL;
        this.urlmain = urlmain;
        this.urlsub1 = urlsub1;
        this.urlsub2 = urlsub2;
        this.urlthumb = urlthumb;
        this.status = status;
        this.timeAdded = timeAdded;
    }

    public Product(String name, String season, String price, String description, int parseInt, int parseInt1, int parseInt2) {
        
    }

    public Product(String id, String name, String season, String price, String description, int parseInt, int parseInt1, int parseInt2) {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPointAvg() {
        return point;
    }

    public void setPointAvg(double point) {
        this.point = point;
    }

    public long getSizeM() {
        return sizeM;
    }

    public void setSizeM(long sizeM) {
        this.sizeM = sizeM;
    }

    public long getSizeL() {
        return sizeL;
    }

    public void setSizeL(long sizeL) {
        this.sizeL = sizeL;
    }

    public long getSizeXL() {
        return sizeXL;
    }

    public void setSizeXL(long sizeXL) {
        this.sizeXL = sizeXL;
    }

    public String getUrlmain() {
        return urlmain;
    }

    public void setUrlmain(String urlmain) {
        this.urlmain = urlmain;
    }

    public String getUrlsub1() {
        return urlsub1;
    }

    public void setUrlsub1(String urlsub1) {
        this.urlsub1 = urlsub1;
    }

    public String getUrlsub2() {
        return urlsub2;
    }

    public void setUrlsub2(String urlsub2) {
        this.urlsub2 = urlsub2;
    }

    public String getUrlthumb() {
        return urlthumb;
    }

    public void setUrlthumb(String urlthumb) {
        this.urlthumb = urlthumb;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(long timeAdded) {
        this.timeAdded = timeAdded;
    }
}
