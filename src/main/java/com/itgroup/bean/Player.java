package com.itgroup.bean;

public class Player {
    private int pnum;
    private String name;
    private String weight;
    private String rank;
    private String record;
    private String image;
    private String nationality;
    private String style;
    private int age;
    private String debut;

    @Override
    public String toString() {
        return "Player{" + "\n" +
                "이름 = " + name + "\n" +
                "체급 = " + weight + "\n" +
                "랭킹 = " + rank + "\n" +
                "전적 = " + record + "\n" +
                "국적 = " + nationality + "\n" +
                "스타일 = " + style + "\n" +
                "나이 = " + age + "\n" +
                "데뷔일 = " + debut + "\n" +
                '}';
    }

    public Player() {
    }

    public Player(int pnum, String name, String weight, String rank, String record, String image, String nationality, String style, int age, String debut) {
        this.pnum = pnum;
        this.name = name;
        this.weight = weight;
        this.rank = rank;
        this.record = record;
        this.image = image;
        this.nationality = nationality;
        this.style = style;
        this.age = age;
        this.debut = debut;
    }

    public int getPnum() {
        return pnum;
    }

    public void setPnum(int pnum) {
        this.pnum = pnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDebut() {
        return debut;
    }

    public void setDebut(String debut) {
        this.debut = debut;
    }
}


