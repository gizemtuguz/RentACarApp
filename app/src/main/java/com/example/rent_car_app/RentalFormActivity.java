package com.example.rent_car_app;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RentalFormActivity extends AppCompatActivity {

    // Görünüm bileşenlerini tanımlayan kod bloğu
    private EditText tcNumber, name, email, startDate, endDate;
    private TextView rentalPrice, carDetails;
    private Button saveButton, updateButton, deleteButton, backButton;
    private ListView previousRentalsListView;
    private Calendar startCalendar, endCalendar;
    private DatabaseHelper dbHelper;
    private long rentalId = -1;
    private String carModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_form);

        // Görünüm bileşenlerini bulur.
        tcNumber = findViewById(R.id.tcNumber);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        rentalPrice = findViewById(R.id.rentalPrice);
        carDetails = findViewById(R.id.carDetails);
        saveButton = findViewById(R.id.saveButton);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);
        previousRentalsListView = findViewById(R.id.previousRentalsListView);

        dbHelper = new DatabaseHelper(this);

        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();

        // Başlangıç tarihi seçici
        DatePickerDialog.OnDateSetListener startDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, monthOfYear);
                startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(startDate, startCalendar);
                calculatePrice();
            }
        };

        // Bitiş tarihi seçici
        DatePickerDialog.OnDateSetListener endDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, monthOfYear);
                endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(endDate, endCalendar);
                calculatePrice();
            }
        };

        // Başlangıç tarihi seçici diyaloğunu gösterir.
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RentalFormActivity.this, startDatePicker, startCalendar
                        .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Bitiş tarihi seçici diyaloğunu gösterir.
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RentalFormActivity.this, endDatePicker, endCalendar
                        .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Intent ile gelen verileri alır.
        Intent intent = getIntent();
        rentalId = intent.getLongExtra("RENTAL_ID", -1);
        if (rentalId != -1) {
            loadRentalData(rentalId);
        }

        String brandModel = intent.getStringExtra("brandModel");
        String type = intent.getStringExtra("type");
        String fuel = intent.getStringExtra("fuel");
        String transmission = intent.getStringExtra("transmission");
        String dailyRate = intent.getStringExtra("dailyRate");
        String weeklyRate = intent.getStringExtra("weeklyRate");
        carModel = brandModel;

        carDetails.setText("Araba Özellikleri: " + brandModel + ", " + type + ", " + fuel + ", " + transmission + ", Günlük Bedel: " + dailyRate + ", Haftalık Bedel: " + weeklyRate);

        // Kaydet butonuna tıklama olayını ekler.
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRental();
            }
        });

        // Güncelle butonuna tıklama olayını ekler.
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRental();
            }
        });

        // Sil butonuna tıklama olayını ekler.
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRental();
            }
        });

        // Geri butonuna tıklama olayını ekler.
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // TC kimlik numarasına odaklanma değişikliği dinleyicisi ekler.
        tcNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    loadPreviousRentals(tcNumber.getText().toString());
                }
            }
        });

        // Önceki kiralamaları listeleme öğesi tıklama olayını ekler.
        previousRentalsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rental rental = (Rental) parent.getItemAtPosition(position);
                loadRentalData(rental.getId());
            }
        });
    }

    // Tarih etiketini günceller.
    private void updateLabel(EditText editText, Calendar calendar) {
        String myFormat = "dd/MM/yy"; // İstenen tarih formatı
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(calendar.getTime()));
    }

    // Fiyatı hesaplar.
    private void calculatePrice() {
        if (startDate.getText().toString().isEmpty() || endDate.getText().toString().isEmpty()) {
            return;
        }

        long diff = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
        long days = diff / (1000 * 60 * 60 * 24);
        long dailyRate = 100; // Örnek günlük ücret
        long totalPrice = days * dailyRate;

        rentalPrice.setText("Toplam Kiralama Bedeli: ₺" + totalPrice);
    }

    // Kiralama verilerini kaydeder.
    private void saveRental() {
        String tc = tcNumber.getText().toString();
        String nameText = name.getText().toString();
        String emailText = email.getText().toString();
        String start = startDate.getText().toString();
        String end = endDate.getText().toString();
        String priceText = rentalPrice.getText().toString().replace("Total Price: ₺", "");

        if (tc.isEmpty() || nameText.isEmpty() || emailText.isEmpty() || start.isEmpty() || end.isEmpty() || priceText.isEmpty()) {
            Toast.makeText(this, "Tüm boşlukları doldurunuz.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!dbHelper.isCarAvailable(carModel, start, end)) {
            showConflictDialog();
            return;
        }

        double price = Double.parseDouble(priceText);
        long id = dbHelper.addRental(tc, nameText, emailText, start, end, price, carModel);
        if (id != -1) {
            Toast.makeText(this, "Kiralama talebiniz alındı.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Kiralama taledi alınamadı. Tekrar Deneyiniz.", Toast.LENGTH_SHORT).show();
        }
    }

    // Çakışma diyaloğunu gösterir.
    private void showConflictDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Car Unavailable")
                .setMessage("Seçtiğiniz araç bu tarihlerde kiralanmış durumda. Başka araç seçebilir veya kiralamak istediğiniz tarih aralığını değiştirebilirsiniz.")
                .setPositiveButton("Tarih Değiştir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Kullanıcının tarihleri değiştirmesine izin ver
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Araç Değiştir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Araç seçimine geri dön
                        finish();
                    }
                })
                .show();
    }

    // Kiralama verilerini günceller.
    private void updateRental() {
        if (rentalId == -1) {
            Toast.makeText(this, "Düzenleme yapılacak kiralama mevcut değil.", Toast.LENGTH_SHORT).show();
            return;
        }

        String tc = tcNumber.getText().toString();
        String nameText = name.getText().toString();
        String emailText = email.getText().toString();
        String start = startDate.getText().toString();
        String end = endDate.getText().toString();
        String priceText = rentalPrice.getText().toString().replace("Total Price: ₺", "");

        if (tc.isEmpty() || nameText.isEmpty() || emailText.isEmpty() || start.isEmpty() || end.isEmpty() || priceText.isEmpty()) {
            Toast.makeText(this, "Tüm boşlukları doldurun.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!dbHelper.isCarAvailable(carModel, start, end)) {
            showConflictDialog();
            return;
        }

        double price = Double.parseDouble(priceText);
        int rowsAffected = dbHelper.updateRental(rentalId, tc, nameText, emailText, start, end, price);
        if (rowsAffected > 0) {
            Toast.makeText(this, "Düzenleme başarıyla tamamlandı.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Düzenleme yapılamadı. Tekrar Deneyiniz.", Toast.LENGTH_SHORT).show();
        }
    }

    // Kiralama verilerini siler.
    private void deleteRental() {
        if (rentalId == -1) {
            Toast.makeText(this, "Silinecek kiralama mevcut değil", Toast.LENGTH_SHORT).show();
            return;
        }

        int rowsDeleted = dbHelper.deleteRental(rentalId);
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Kiralama talebi başarıyla silindi.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Kiralama talebi silinemedi.Tekrar Deneyiniz.", Toast.LENGTH_SHORT).show();
        }
    }

    // Kiralama verilerini yükler.
    private void loadRentalData(long rentalId) {
        Cursor cursor = dbHelper.getRental(rentalId);
        if (cursor != null && cursor.moveToFirst()) {
            this.rentalId = rentalId;
            tcNumber.setText(cursor.getString(cursor.getColumnIndexOrThrow("tc_number")));
            name.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            email.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            startDate.setText(cursor.getString(cursor.getColumnIndexOrThrow("start_date")));
            endDate.setText(cursor.getString(cursor.getColumnIndexOrThrow("end_date")));
            rentalPrice.setText("Total Price: ₺" + cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
            cursor.close();
        }
    }

    // Önceki kiralamaları yükler.
    private void loadPreviousRentals(String tcNumber) {
        Cursor cursor = dbHelper.getRentalsByTCNumber(tcNumber);
        ArrayList<Rental> rentals = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow("start_date"));
                String endDate = cursor.getString(cursor.getColumnIndexOrThrow("end_date"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String carModel = cursor.getString(cursor.getColumnIndexOrThrow("car_model"));

                rentals.add(new Rental(id, tcNumber, name, email, startDate, endDate, price, carModel));
            }
            cursor.close();
        }

        ArrayAdapter<Rental> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rentals);
        previousRentalsListView.setAdapter(adapter);
    }

    // Kiralama bilgilerini tutan Rental classı
    public static class Rental {
        private long id;
        private String tcNumber;
        private String name;
        private String email;
        private String startDate;
        private String endDate;
        private double price;
        private String carModel;

        public Rental(long id, String tcNumber, String name, String email, String startDate, String endDate, double price, String carModel) {
            this.id = id;
            this.tcNumber = tcNumber;
            this.name = name;
            this.email = email;
            this.startDate = startDate;
            this.endDate = endDate;
            this.price = price;
            this.carModel = carModel;
        }

        public long getId() {
            return id;
        }

        @Override
        public String toString() {
            return carModel + " - " + startDate + " to " + endDate;
        }
    }
}
