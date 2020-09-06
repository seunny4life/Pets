package com.example.android.pets.data;

public class Pets {


    private String name;
    private String breed;
    private double weight;
    private int gender;
    private int id;

    public Pets() {
    }

    public Pets(int id, String name, String breed, int gender, double weight) {
        this.name = name;
        this.breed = breed;
        this.weight = weight;
        this.gender = gender;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
