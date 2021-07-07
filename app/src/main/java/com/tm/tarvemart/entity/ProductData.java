package com.tm.tarvemart.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductData implements Serializable {
    String common_id;
    public String getCommon_id() {
        return common_id;
    }
    public void setCommon_id(String common_id) {
        this.common_id = common_id;
    }
    String id,
            productname,
            gst,
            sku,
            description,
            type_name_id,
            type_name,
            product_about,
            product_ingredient,
            productinfo,
            status,
            created_by,
            shop_id,
            brand_id,
            brandname,
            brand_image,
            product_image,
            category_name,parent_category_name;

    List<CityPriceData> cityprice = new ArrayList<>();

    public String getParent_category_name() {
        return parent_category_name;
    }
    public void setParent_category_name(String parent_category_name) {
        this.parent_category_name = parent_category_name;
    }

    public String getCategory_name() {
        return category_name;
    }
    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType_name_id() {
        return type_name_id;
    }

    public void setType_name_id(String type_name_id) {
        this.type_name_id = type_name_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getProduct_about() {
        return product_about;
    }

    public void setProduct_about(String product_about) {
        this.product_about = product_about;
    }

    public String getProduct_ingredient() {
        return product_ingredient;
    }

    public void setProduct_ingredient(String product_ingredient) {
        this.product_ingredient = product_ingredient;
    }

    public String getProductinfo() {
        return productinfo;
    }

    public void setProductinfo(String productinfo) {
        this.productinfo = productinfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getBrand_image() {
        return brand_image;
    }

    public void setBrand_image(String brand_image) {
        this.brand_image = brand_image;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public List<CityPriceData> getCityprice() {
        return cityprice;
    }

    public void setCityprice(List<CityPriceData> cityprice) {
        this.cityprice = cityprice;
    }

    public static List<ProductData> createJsonInList(String str) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ProductData>>() {
        }.getType();
        return gson.fromJson(str, type);
    }

    public static ProductData createJsonInObject(String str) {
        Gson gson = new Gson();
        Type type = new TypeToken<ProductData>() {
        }.getType();
        return gson.fromJson(str, type);
    }

    //added on 23/5/2019

    String city_id,
            quantity_id,
            price,
            selling_price,
            discount,
            total_availability,
            quantity_name,
            selected_quantity,
            city_name;

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getQuantity_id() {
        return quantity_id;
    }

    public void setQuantity_id(String quantity_id) {
        this.quantity_id = quantity_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotal_availability() {
        return total_availability;
    }

    public void setTotal_availability(String total_availability) {
        this.total_availability = total_availability;
    }

    public String getQuantity_name() {
        return quantity_name;
    }

    public void setQuantity_name(String quantity_name) {
        this.quantity_name = quantity_name;
    }

    public String getSelected_quantity() {
        return selected_quantity;
    }

    public void setSelected_quantity(String selected_quantity) {
        this.selected_quantity = selected_quantity;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }


    String product_id, product_quantity_id, product_selling_price, product_discount;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_quantity_id() {
        return product_quantity_id;
    }

    public void setProduct_quantity_id(String product_quantity_id) {
        this.product_quantity_id = product_quantity_id;
    }

    public String getProduct_selling_price() {
        return product_selling_price;
    }

    public void setProduct_selling_price(String product_selling_price) {
        this.product_selling_price = product_selling_price;
    }

    public String getProduct_discount() {
        return product_discount;
    }

    public void setProduct_discount(String product_discount) {
        this.product_discount = product_discount;
    }

    String invoice_id, customer_id,
            actual_price, total_price,
            address_id, coupan_price,
            coupan_code, coupan_id, delivery_time,
            delivery_type, standard, cgst, sgst,
            payment_mode, delivery_date, order_date,
            cancel_reason, delivery_address, cancel_by;

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getActual_price() {
        return actual_price;
    }

    public void setActual_price(String actual_price) {
        this.actual_price = actual_price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getCoupan_price() {
        return coupan_price;
    }

    public void setCoupan_price(String coupan_price) {
        this.coupan_price = coupan_price;
    }

    public String getCoupan_code() {
        return coupan_code;
    }

    public void setCoupan_code(String coupan_code) {
        this.coupan_code = coupan_code;
    }

    public String getCoupan_id() {
        return coupan_id;
    }

    public void setCoupan_id(String coupan_id) {
        this.coupan_id = coupan_id;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getCancel_by() {
        return cancel_by;
    }

    public void setCancel_by(String cancel_by) {
        this.cancel_by = cancel_by;
    }


    List<ProductData> orders = new ArrayList<>();

    public List<ProductData> getOrders() {
        return orders;
    }

    public void setOrders(List<ProductData> orders) {
        this.orders = orders;
    }


    String product_name, order_id, product_price, cityid_of_product, product_quantity_name, product_img_link;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getCityid_of_product() {
        return cityid_of_product;
    }

    public void setCityid_of_product(String cityid_of_product) {
        this.cityid_of_product = cityid_of_product;
    }

    public String getProduct_quantity_name() {
        return product_quantity_name;
    }

    public void setProduct_quantity_name(String product_quantity_name) {
        this.product_quantity_name = product_quantity_name;
    }

    public String getProduct_img_link() {
        return product_img_link;
    }

    public void setProduct_img_link(String product_img_link) {
        this.product_img_link = product_img_link;
    }
}
