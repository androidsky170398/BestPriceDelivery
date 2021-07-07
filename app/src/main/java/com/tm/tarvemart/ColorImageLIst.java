package com.tm.tarvemart;

public class ColorImageLIst {
    private String galary_no_id, galary_productid, galary_imgurl,galary_status,galary_created,
            galary_galary_id;

        public ColorImageLIst(String galary_no_id, String galary_productid,
                              String galary_imgurl, String galary_status,
                              String galary_created, String galary_galary_id) {
            this.galary_no_id=galary_no_id;
            this.galary_productid=galary_productid;
            this.galary_imgurl=galary_imgurl;
            this.galary_status=galary_status;
            this.galary_created=galary_created;
            this.galary_galary_id=galary_galary_id;

        }


        public String getGalary_no_id()
        {
            return galary_no_id;
        }

        public String getGalary_productid()
        {
            return galary_productid;
        }
        public String getGalary_imgurl(){
            return galary_imgurl;
        }
        public String getGalary_status() {
            return galary_status;
        }
        public String getGalary_created(){
            return galary_created;
        }

    public String getGalary_galary_id(){
        return galary_galary_id;
    }
    }
