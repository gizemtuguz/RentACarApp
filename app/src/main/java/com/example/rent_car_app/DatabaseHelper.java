package com.example.rent_car_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//Bu classta basic SQL yapılarıyla küçük bir database tasarımı yaptık ve gerekli verileri depolayan kodlar yazdık.
public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "CarRental.db";
    private static final int DATABASE_VERSION = 2;

    // Tablo isimleri
    private static final String TABLE_CARS = "cars";
    private static final String TABLE_RENTALS = "rentals";

    // Ortak sütun isimleri
    private static final String COLUMN_ID = "id";

    // Cars tablosunun sütun isimleri
    private static final String COLUMN_BRAND = "brand";
    private static final String COLUMN_MODEL = "model";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_FUEL = "fuel";
    private static final String COLUMN_TRANSMISSION = "transmission";
    private static final String COLUMN_DAILY_RATE = "daily_rate";
    private static final String COLUMN_WEEKLY_RATE = "weekly_rate";
    private static final String COLUMN_IMAGE_RESOURCES = "image_resources";

    // Rentals tablosunun sütun isimleri
    private static final String COLUMN_TC_NUMBER = "tc_number";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_START_DATE = "start_date";
    private static final String COLUMN_END_DATE = "end_date";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_CAR_MODEL = "car_model"; // Araba modeli sütunu eklendi

    // Yapıcı metot, context ve veritabanı bilgilerini alır
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Veritabanı ilk oluşturulduğunda çağrılır
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Cars tablosunu oluştur
        String CREATE_CARS_TABLE = "CREATE TABLE " + TABLE_CARS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_BRAND + " TEXT,"
                + COLUMN_MODEL + " TEXT,"
                + COLUMN_YEAR + " TEXT,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_FUEL + " TEXT,"
                + COLUMN_TRANSMISSION + " TEXT,"
                + COLUMN_DAILY_RATE + " TEXT,"
                + COLUMN_WEEKLY_RATE + " TEXT,"
                + COLUMN_IMAGE_RESOURCES + " TEXT" + ")";
        db.execSQL(CREATE_CARS_TABLE);

        // Rentals tablosunu oluştur
        String CREATE_RENTALS_TABLE = "CREATE TABLE " + TABLE_RENTALS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TC_NUMBER + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_START_DATE + " TEXT,"
                + COLUMN_END_DATE + " TEXT,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_CAR_MODEL + " TEXT" + ")"; // Araba modeli sütunu eklendi
        db.execSQL(CREATE_RENTALS_TABLE);
    }

    // Veritabanı güncellendiğinde çağrılır
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RENTALS);
        onCreate(db);
    }

    // Araba verilerini ekler
    public long addCar(String brand, String model, String year, String type, String fuel, String transmission, String dailyRate, String weeklyRate, String imageResources) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BRAND, brand);
        values.put(COLUMN_MODEL, model);
        values.put(COLUMN_YEAR, year);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_FUEL, fuel);
        values.put(COLUMN_TRANSMISSION, transmission);
        values.put(COLUMN_DAILY_RATE, dailyRate);
        values.put(COLUMN_WEEKLY_RATE, weeklyRate);
        values.put(COLUMN_IMAGE_RESOURCES, imageResources);
        return db.insert(TABLE_CARS, null, values);
    }

    // Kiralama verilerini ekler
    public long addRental(String tcNumber, String name, String email, String startDate, String endDate, double price, String carModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TC_NUMBER, tcNumber);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_START_DATE, startDate);
        values.put(COLUMN_END_DATE, endDate);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_CAR_MODEL, carModel);
        return db.insert(TABLE_RENTALS, null, values);
    }

    // Kiralama verilerini getirir
    public Cursor getRental(long rentalId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_RENTALS, null, COLUMN_ID + "=?", new String[]{String.valueOf(rentalId)}, null, null, null);
    }

    // TC numarasına göre kiralamaları getirir
    public Cursor getRentalsByTCNumber(String tcNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_RENTALS, null, COLUMN_TC_NUMBER + "=?", new String[]{tcNumber}, null, null, null);
    }

    // Kiralama verilerini günceller
    public int updateRental(long rentalId, String tcNumber, String name, String email, String startDate, String endDate, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TC_NUMBER, tcNumber);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_START_DATE, startDate);
        values.put(COLUMN_END_DATE, endDate);
        values.put(COLUMN_PRICE, price);
        return db.update(TABLE_RENTALS, values, COLUMN_ID + "=?", new String[]{String.valueOf(rentalId)});
    }

    // Kiralama verilerini siler
    public int deleteRental(long rentalId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_RENTALS, COLUMN_ID + "=?", new String[]{String.valueOf(rentalId)});
    }

    // Arabanın müsait olup olmadığını kontrol eder
    public boolean isCarAvailable(String carModel, String startDate, String endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_RENTALS +
                " WHERE " + COLUMN_CAR_MODEL + "=? AND (" +
                "(? BETWEEN " + COLUMN_START_DATE + " AND " + COLUMN_END_DATE + ") OR " +
                "(? BETWEEN " + COLUMN_START_DATE + " AND " + COLUMN_END_DATE + ") OR " +
                "(? <= " + COLUMN_START_DATE + " AND ? >= " + COLUMN_END_DATE + "))";
        Cursor cursor = db.rawQuery(query, new String[]{carModel, startDate, endDate, startDate, endDate});
        boolean available = !cursor.moveToFirst();
        cursor.close();
        return available;
    }
}
