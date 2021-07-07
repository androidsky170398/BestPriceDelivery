package com.tm.tarvemart.sqliteDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 23/2/18.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    Context context;
    //public static final String DATABASE_NAME = "SuposanDistribution.db";
    public static final String DATABASE_NAME = "BestPriceDelevery.db";
    public static final int DATABASE_VERSION = 1;
    public static final String Device_Id = "Device_Id";
    public static final String common_id = "common_id";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*_________________--Creating Table For BeneficiaryRemovalSubtype__________________*/

    public static final String BenRemovalSubType_Id = "BenRemovalSubType_Id";
    public static final String BenRemovalType_Id = "BenRemovalType_Id";
    public static final String BenRemovalSubType_Name = "BenRemovalSubType_Name";
    public static final String BenRemovalSubType_NameEng = "BenRemovalSubType_NameEng";
    public static final String BenRemovalSubType_Description = "BenRemovalSubType_Description";
    public static final String BenRemovalSubType_IsActive = "BenRemovalSubType_IsActive";
    public static final String tbl_DS_BeneficiaryRemovalSubType = "tbl_DS_BeneficiaryRemovalSubType";


    public static final String create_tbl_DS_BeneficiaryRemovalSubType = "create table "
            + tbl_DS_BeneficiaryRemovalSubType
            + "("
            + common_id
            + " integer primary key autoincrement,"
            + BenRemovalSubType_Id
            + " text,"
            + BenRemovalType_Id
            + " text,"
            + BenRemovalSubType_Name
            + " text,"
            + BenRemovalSubType_NameEng
            + " text,"
            + BenRemovalSubType_Description
            + " text,"
            + BenRemovalSubType_IsActive + " text)";


    public static String tbl_Products = "tbl_Products";
    public static String id = "id";
    public static String productname = "productname";
    public static String gst = "gst";
    public static String sku = "sku";
    public static String description = "description";
    public static String type_name_id = "type_name_id";
    public static String type_name = "type_name";
    public static String product_about = "product_about";
    public static String product_ingredient = "product_ingredient";
    public static String productinfo = "productinfo";
    public static String status = "status";
    public static String created_by = "created_by";
    public static String shop_id = "shop_id";
    public static String brand_id = "brand_id";
    public static String brandname = "brandname";
    public static String brand_image = "brand_image";
    public static String product_image = "product_image";
    public static String city_id = "city_id";
    public static String quantity_id = "quantity_id";
    public static String price = "price";
    public static String selling_price = "selling_price";
    public static String discount = "discount";
    public static String total_availability = "total_availability";
    public static String quantity_name = "quantity_name";
    public static String selected_quantity = "selected_quantity";
    public static String city_name = "city_name";

    public static String create_tbl_Products = "create table "
            + tbl_Products
            + "("
            + common_id + " integer primary key autoincrement,"
            + id + " text,"
            + productname + " text,"
            + gst + " text,"
            + sku + " text,"
            + description + " text,"
            + type_name_id + " text,"
            + type_name + " text,"
            + product_about + " text,"
            + product_ingredient + " text,"
            + productinfo + " text,"
            + status + " text,"
            + created_by + " text,"
            + shop_id + " text,"
            + brand_id + " text,"
            + brandname + " text,"
            + brand_image + " text,"
            + product_image + " text,"
            + city_id + " text,"
            + city_name + " text,"
            + quantity_id + " text,"
            + price + " text,"
            + selling_price + " text,"
            + discount + " text,"
            + total_availability + " text,"
            + quantity_name + " text,"
            + selected_quantity + " text)";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_tbl_Products);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {


    }
}