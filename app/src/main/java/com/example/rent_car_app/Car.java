package com.example.rent_car_app;

// Bu classta arabanın hangi özelliklere sahip olduğunu ve bu özelliklerin getter larını tanımladık.
public class Car {
    private String marka;
    private String model;
    private String yıl;
    private String type;
    private String fuel;
    private String transmission;
    private String dailyRate;
    private String weeklyRate;
    private int[] imageResources;

    public Car(String brand, String model, String year, String type, String fuel, String transmission, String dailyRate, String weeklyRate, int[] imageResources) {
        this.marka = brand;
        this.model = model;
        this.yıl = year;
        this.type = type;
        this.fuel = fuel;
        this.transmission = transmission;
        this.dailyRate = dailyRate;
        this.weeklyRate = weeklyRate;
        this.imageResources = imageResources;
    }

    public String getBrand() {
        return marka;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return yıl;
    }

    public String getType() {
        return type;
    }

    public String getFuel() {
        return fuel;
    }

    public String getTransmission() {
        return transmission;
    }

    public String getDailyRate() {
        return dailyRate;
    }

    public String getWeeklyRate() {
        return weeklyRate;
    }

    public int[] getImageResources() {
        return imageResources;
    }
}
