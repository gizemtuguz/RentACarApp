package com.example.rent_car_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class CarDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        // Görünüm bileşenlerini bulur
        ViewPager detailViewPager = findViewById(R.id.detailViewPager);
        TextView carBrandModel = findViewById(R.id.carBrandModel);
        TextView carType = findViewById(R.id.carType);
        TextView carFuel = findViewById(R.id.carFuel);
        TextView carTransmission = findViewById(R.id.carTransmission);
        TextView carDailyRate = findViewById(R.id.carDailyRate);
        TextView carWeeklyRate = findViewById(R.id.carWeeklyRate);
        Button rentButton = findViewById(R.id.rentButton);
        Button backButton = findViewById(R.id.backButton);

        // Intent ile gelen verileri alır
        Intent intent = getIntent();
        String brand = intent.getStringExtra("brand");
        String model = intent.getStringExtra("model");
        String year = intent.getStringExtra("year");
        String type = intent.getStringExtra("type");
        String fuel = intent.getStringExtra("fuel");
        String transmission = intent.getStringExtra("transmission");
        String dailyRate = intent.getStringExtra("dailyRate");
        String weeklyRate = intent.getStringExtra("weeklyRate");
        int[] imageResources = intent.getIntArrayExtra("imageResources");

        // Görüntüleri görüntülemek için adaptörü ayarladık.
        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(this, imageResources);
        detailViewPager.setAdapter(imagePagerAdapter);

        // Araba bilgilerini görünüm bileşenlerine yerleştirir
        carBrandModel.setText(brand + " " + model);
        carType.setText(type);
        carFuel.setText(fuel);
        carTransmission.setText(transmission);
        carDailyRate.setText("Günlük Kiralama Bedeli: ₺" + dailyRate);
        carWeeklyRate.setText("Haftalık Kiralama Bedeli: ₺" + weeklyRate);

        // Kiralama butonuna tıklama olayını ekledik.
        rentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiralama formu aktivitesini başlatır.
                Intent rentalFormIntent = new Intent(CarDetailActivity.this, RentalFormActivity.class);
                rentalFormIntent.putExtra("brandModel", brand + " " + model);
                rentalFormIntent.putExtra("type", type);
                rentalFormIntent.putExtra("fuel", fuel);
                rentalFormIntent.putExtra("transmission", transmission);
                rentalFormIntent.putExtra("dailyRate", dailyRate);
                rentalFormIntent.putExtra("weeklyRate", weeklyRate);
                startActivity(rentalFormIntent);
            }
        });

        // Geri butonuna tıklama olayını ekledik.
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
