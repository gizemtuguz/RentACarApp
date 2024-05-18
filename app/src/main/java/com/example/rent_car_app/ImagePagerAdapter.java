package com.example.rent_car_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ImagePagerAdapter extends PagerAdapter {

    // Context ve resim kaynaklarını tutmak için değişkenler
    private Context context;
    private int[] imageResources;

    // Yapıcı metot, context ve resim kaynaklarını alır
    public ImagePagerAdapter(Context context, int[] imageResources) {
        this.context = context;
        this.imageResources = imageResources;
    }

    // Adapter'de bulunan toplam öğe sayısını döner
    @Override
    public int getCount() {
        return imageResources.length;
    }

    // Verilen görünümün belirli bir nesneden olup olmadığını kontrol eder
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    // Her bir sayfa için görünümü oluşturur ve döner
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // Görünümü şişirmek için LayoutInflater kullan
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.pager_item, container, false);

        // Görünümdeki ImageView'i bul ve resim kaynağını ayarla
        ImageView imageView = itemView.findViewById(R.id.imageView);
        imageView.setImageResource(imageResources[position]);

        // Görünümü konteynıra ekle
        container.addView(itemView);
        return itemView;
    }

    // Belirli bir konumdaki görünümü konteynırdan kaldırır
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
