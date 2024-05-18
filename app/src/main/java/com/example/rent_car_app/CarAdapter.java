package com.example.rent_car_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class CarAdapter extends BaseAdapter {
    // Context ve araba listesini tutmak için değişkenler tanımladık.
    private Context context;
    private ArrayList<Car> cars;

    // Yapıcı metot, context ve araba listesini alır.
    public CarAdapter(Context context, ArrayList<Car> cars) {
        this.context = context;
        this.cars = cars;
    }

    // Listede kaç tane araba olduğunu döner.
    @Override
    public int getCount() {
        return cars.size();
    }

    // Belirtilen konumdaki arabayı döner.
    @Override
    public Object getItem(int position) {
        return cars.get(position);
    }

    // Belirtilen konumun ID'sini döner.
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Görünüm daha önce oluşturulmamışsa, yeni bir görünüm oluştur
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_car, parent, false);
        }

        Car currentCar = (Car) getItem(position);

        ViewPager carViewPager = convertView.findViewById(R.id.carViewPager);
        TextView carBrandModel = convertView.findViewById(R.id.carBrandModel);
        TextView carYear = convertView.findViewById(R.id.carYear);
        TextView carTransmission = convertView.findViewById(R.id.carTransmission);

        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(context, currentCar.getImageResources());
        carViewPager.setAdapter(imagePagerAdapter);

        carBrandModel.setText(currentCar.getBrand() + " " + currentCar.getModel());
        carYear.setText(currentCar.getYear());
        carTransmission.setText(currentCar.getTransmission());

        // Görünüme tıklanma olayını ekler.
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mevcut arabanın detaylarını içeren yeni bir intent oluşturur.
                Intent intent = new Intent(context, CarDetailActivity.class);
                intent.putExtra("brand", currentCar.getBrand());
                intent.putExtra("model", currentCar.getModel());
                intent.putExtra("year", currentCar.getYear());
                intent.putExtra("type", currentCar.getType());
                intent.putExtra("fuel", currentCar.getFuel());
                intent.putExtra("transmission", currentCar.getTransmission());
                intent.putExtra("dailyRate", currentCar.getDailyRate());
                intent.putExtra("weeklyRate", currentCar.getWeeklyRate());
                intent.putExtra("imageResources", currentCar.getImageResources());

                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
