package com.tm.tarvemart.sqliteDatabase;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tm.tarvemart.entity.ProductData;

import java.util.ArrayList;
import java.util.List;


public class DataSource {
    Context context;
    SQLiteDatabase db;
    SQLiteHelper sqLiteHelper;

    public DataSource(Context context) {
        sqLiteHelper = new SQLiteHelper(context);
        this.context = context;
    }

    public void open() {
        db = sqLiteHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteHelper.close();

    }

    /*_____________Insertion For tbl_Products_______________________*/

    public long insertInto_tbl_Products(ProductData productData) {
        long i = 0;
        ContentValues values = new ContentValues();
        values.put(sqLiteHelper.id, productData.getId());
        values.put(sqLiteHelper.productname, productData.getProductname());
        values.put(sqLiteHelper.gst, productData.getGst());
        values.put(sqLiteHelper.sku, productData.getSku());
        values.put(sqLiteHelper.description, productData.getDescription());
        values.put(sqLiteHelper.type_name_id, productData.getType_name_id());
        values.put(sqLiteHelper.type_name, productData.getType_name());
        values.put(sqLiteHelper.product_about, productData.getProduct_about());
        values.put(sqLiteHelper.product_ingredient, productData.getProduct_ingredient());
        values.put(sqLiteHelper.productinfo, productData.getProductinfo());
        values.put(sqLiteHelper.status, productData.getStatus());
        values.put(sqLiteHelper.created_by, productData.getCreated_by());
        values.put(sqLiteHelper.shop_id, productData.getShop_id());
        values.put(sqLiteHelper.brand_id, productData.getBrand_id());
        values.put(sqLiteHelper.brandname, productData.getBrandname());
        values.put(sqLiteHelper.brand_image, productData.getBrand_image());
        values.put(sqLiteHelper.product_image, productData.getProduct_image());

        /*For price*/
        values.put(sqLiteHelper.city_id, productData.getCity_id());
        values.put(sqLiteHelper.quantity_id, productData.getQuantity_id());
        values.put(sqLiteHelper.price, productData.getPrice());
        values.put(sqLiteHelper.selling_price, productData.getSelling_price());
        values.put(sqLiteHelper.discount, productData.getDiscount());
        values.put(sqLiteHelper.total_availability, productData.getTotal_availability());
        values.put(sqLiteHelper.quantity_name, productData.getQuantity_name());
        values.put(sqLiteHelper.selected_quantity, productData.getSelected_quantity());
        values.put(sqLiteHelper.city_name, productData.getCity_name());
        i = db.insert(sqLiteHelper.tbl_Products, null, values);
        return i;
    }

    public List<ProductData> selectInto_tbl_Products(String id, String qtyName) {
        String query = null;
        List<ProductData> list = new ArrayList<>();
        ProductData productData;
        if (id != null && qtyName != null && !id.equalsIgnoreCase("") && qtyName.equalsIgnoreCase("")) {
            query = "Select * from " + sqLiteHelper.tbl_Products + " where " + sqLiteHelper.id + "='" + id + "'";
        } else if (id != null && qtyName != null && !id.equalsIgnoreCase("") && !qtyName.equalsIgnoreCase("")) {
            query = "Select * from " + sqLiteHelper.tbl_Products + " where " + sqLiteHelper.id + "='" + id + "' and " + sqLiteHelper.quantity_name + "='" + qtyName + "'";
        } else {
            query = "Select * from " + sqLiteHelper.tbl_Products;
        }
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            productData = new ProductData();
            productData.setCommon_id(cursor.getString(cursor.getColumnIndex(sqLiteHelper.common_id)));
            productData.setId(cursor.getString(cursor.getColumnIndex(sqLiteHelper.id)));
            productData.setProductname(cursor.getString(cursor.getColumnIndex(sqLiteHelper.productname)));
            productData.setGst(cursor.getString(cursor.getColumnIndex(sqLiteHelper.gst)));
            productData.setSku(cursor.getString(cursor.getColumnIndex(sqLiteHelper.sku)));
            productData.setDescription(cursor.getString(cursor.getColumnIndex(sqLiteHelper.description)));
            productData.setType_name_id(cursor.getString(cursor.getColumnIndex(sqLiteHelper.type_name_id)));
            productData.setType_name(cursor.getString(cursor.getColumnIndex(sqLiteHelper.type_name)));
            productData.setProduct_about(cursor.getString(cursor.getColumnIndex(sqLiteHelper.product_about)));
            productData.setProduct_ingredient(cursor.getString(cursor.getColumnIndex(sqLiteHelper.product_ingredient)));
            productData.setProductinfo(cursor.getString(cursor.getColumnIndex(sqLiteHelper.productinfo)));
            productData.setStatus(cursor.getString(cursor.getColumnIndex(sqLiteHelper.status)));
            productData.setCreated_by(cursor.getString(cursor.getColumnIndex(sqLiteHelper.created_by)));
            productData.setShop_id(cursor.getString(cursor.getColumnIndex(sqLiteHelper.shop_id)));
            productData.setBrand_id(cursor.getString(cursor.getColumnIndex(sqLiteHelper.brand_id)));
            productData.setBrandname(cursor.getString(cursor.getColumnIndex(sqLiteHelper.brandname)));
            productData.setBrand_image(cursor.getString(cursor.getColumnIndex(sqLiteHelper.brand_image)));
            productData.setProduct_image(cursor.getString(cursor.getColumnIndex(sqLiteHelper.product_image)));
            /*for price*/
            productData.setCity_id(cursor.getString(cursor.getColumnIndex(sqLiteHelper.city_id)));
            productData.setQuantity_id(cursor.getString(cursor.getColumnIndex(sqLiteHelper.quantity_id)));
            productData.setPrice(cursor.getString(cursor.getColumnIndex(sqLiteHelper.price)));
            productData.setSelling_price(cursor.getString(cursor.getColumnIndex(sqLiteHelper.selling_price)));
            productData.setDiscount(cursor.getString(cursor.getColumnIndex(sqLiteHelper.discount)));
            productData.setTotal_availability(cursor.getString(cursor.getColumnIndex(sqLiteHelper.total_availability)));
            productData.setQuantity_name(cursor.getString(cursor.getColumnIndex(sqLiteHelper.quantity_name)));
            productData.setSelected_quantity(cursor.getString(cursor.getColumnIndex(sqLiteHelper.selected_quantity)));
            productData.setCity_name(cursor.getString(cursor.getColumnIndex(sqLiteHelper.city_name)));
            list.add(productData);
            cursor.moveToNext();
        }
        return list;
    }

    public int deleteInto_tbl_Products(String common_id) {
        int i = 0;
        if (common_id != null && !common_id.equalsIgnoreCase("")) {
            //String[] column = {sqLiteHelper.id};
            String where = sqLiteHelper.common_id + "=" + common_id;
            i = db.delete(SQLiteHelper.tbl_Products, where, null);
        } else {
            i = db.delete(SQLiteHelper.tbl_Products, null, null);
        }
        return i;
    }

    public int deleteInto_tbl_Products(String id, String actualPrice) {
        int i = 0;
        if (id != null && !id.equalsIgnoreCase("")) {
            //String[] column = {sqLiteHelper.id};
            String where = sqLiteHelper.id + "=" + id + " and " + sqLiteHelper.selling_price + "=" + actualPrice;
            i = db.delete(SQLiteHelper.tbl_Products, where, null);
        } else {
            i = db.delete(SQLiteHelper.tbl_Products, null, null);
        }
        return i;
    }

    public int updateInto_tbl_Products(String common_id, ProductData productData) {
        int i = 0;
        String where = sqLiteHelper.common_id + "=" + common_id;
        ContentValues values = new ContentValues();
        values.put(sqLiteHelper.productname, productData.getProductname());
        values.put(sqLiteHelper.gst, productData.getGst());
        values.put(sqLiteHelper.sku, productData.getSku());
        values.put(sqLiteHelper.description, productData.getDescription());
        values.put(sqLiteHelper.type_name_id, productData.getType_name_id());
        values.put(sqLiteHelper.type_name, productData.getType_name());
        values.put(sqLiteHelper.product_about, productData.getProduct_about());
        values.put(sqLiteHelper.product_ingredient, productData.getProduct_ingredient());
        values.put(sqLiteHelper.productinfo, productData.getProductinfo());
        values.put(sqLiteHelper.status, productData.getStatus());
        values.put(sqLiteHelper.created_by, productData.getCreated_by());
        values.put(sqLiteHelper.shop_id, productData.getShop_id());
        values.put(sqLiteHelper.brand_id, productData.getBrand_id());
        values.put(sqLiteHelper.brandname, productData.getBrandname());
        values.put(sqLiteHelper.brand_image, productData.getBrand_image());
        values.put(sqLiteHelper.product_image, productData.getProduct_image());
        /*for price*/
        values.put(sqLiteHelper.city_id, productData.getCity_id());
        values.put(sqLiteHelper.quantity_id, productData.getQuantity_id());
        values.put(sqLiteHelper.price, productData.getPrice());
        values.put(sqLiteHelper.selling_price, productData.getSelling_price());
        values.put(sqLiteHelper.discount, productData.getDiscount());
        values.put(sqLiteHelper.total_availability, productData.getTotal_availability());
        values.put(sqLiteHelper.quantity_name, productData.getQuantity_name());
        values.put(sqLiteHelper.selected_quantity, productData.getSelected_quantity());
        values.put(sqLiteHelper.city_name, productData.getCity_name());
        i = db.update(sqLiteHelper.tbl_Products, values, where, null);
        return i;
    }


    public int selectCountInto_tbl_Products(String id) {
        String query = null;
        List<ProductData> list = new ArrayList<>();
        ProductData productData;
        if (id != null && !id.equalsIgnoreCase("")) {
            query = "Select * from " + sqLiteHelper.tbl_Products + " where " + sqLiteHelper.id + "=" + id;
        } else {
            query = "Select * from " + sqLiteHelper.tbl_Products;
        }
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        return count;
    }

}






