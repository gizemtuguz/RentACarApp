package com.example.rent_car_app;

import android.os.Bundle;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

//Bu classda araba listesi oluşturup özelliklerini belirledik ayrıca araba resimleriyle özelliklerini eşleştirdik.

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        GridView carGridView = findViewById(R.id.carGridView);

        ArrayList<Car> cars = new ArrayList<>();
        cars.add(new Car("Tesla", "Model S", "2024", "Sedan","Elektrikli","Otomatik","$150","$900", new int[]{
                R.drawable.car_1_0, R.drawable.car_1_1, R.drawable.car_1_2}));
        cars.add(new Car("BMW", "i5 M60 xDrive", "2024", "Sedan", "Elektrikli", "Otomatik", "$140", "$850", new int[]{
                R.drawable.car_2_0, R.drawable.car_2_1, R.drawable.car_2_2}));
        cars.add(new Car("Bugatti", "Divo", "2020", "Coupe", "Benzinli", "Otomatik", "$1000", "$6000", new int[]{
                R.drawable.car_3_0, R.drawable.car_3_1, R.drawable.car_3_2}));
        cars.add(new Car("Porsche", "Panamera 4 E-Hybrid", "2025", "Sedan", "Hybrit", "Otomatik", "$200", "$1200", new int[]{
                R.drawable.car_4_0, R.drawable.car_4_1, R.drawable.car_4_2}));
        cars.add(new Car("Genesis", "GV80", "2025", "SUV", "Benzinli", "Otomatik", "$160", "$950", new int[]{
                R.drawable.car_5_0, R.drawable.car_5_1, R.drawable.car_5_2}));
        cars.add(new Car("Aston Martin", "Vantage", "2025", "Coupe", "Benzinli", "Otomatik", "$300", "$1800", new int[]{
                R.drawable.car_6_0, R.drawable.car_6_1, R.drawable.car_6_2}));
        cars.add(new Car("BMW", "8-Series Gran Coupe", "2023", "Sedan", "Benzinli", "Otomatik", "$180", "$1100", new int[]{
                R.drawable.car_7_0, R.drawable.car_7_1, R.drawable.car_7_2}));
        cars.add(new Car("Mercedes-Benz", "G580", "2025", "SUV", "Benzinli", "Otomatik", "$250", "$1500", new int[]{
                R.drawable.car_8_0, R.drawable.car_8_1, R.drawable.car_8_2}));
        cars.add(new Car("McLaren", "750S Spider", "2024", "Convertible", "Benzinli", "Otomatik", "$800", "$4800", new int[]{
                R.drawable.car_9_0, R.drawable.car_9_1, R.drawable.car_9_2}));
        cars.add(new Car("Land Rover", "Defender 110 V8", "2022", "SUV", "Benzinli", "Otomatik", "$220", "$1300", new int[]{
                R.drawable.car_10_0, R.drawable.car_10_1, R.drawable.car_10_2}));

        CarAdapter carAdapter = new CarAdapter(this, cars);
        carGridView.setAdapter(carAdapter);
    }
}