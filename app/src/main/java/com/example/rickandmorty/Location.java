package com.example.rickandmorty;

public class Location {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    private String name;
    private String type;
    private String dimension;

    public Location(String name, String type, String dimension) {
        this.name = name;
        this.type = type;
        this.dimension = dimension;
    }
}
