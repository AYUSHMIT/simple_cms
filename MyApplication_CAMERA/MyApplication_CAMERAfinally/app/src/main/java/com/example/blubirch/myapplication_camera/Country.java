package com.example.blubirch.myapplication_camera;

/**
 * Created by blubirch on 20/2/17.
 */


import android.widget.Button;
import android.widget.ImageView;

/**
     * Created by blubirch on 20/2/17.
     */
    public class Country {

         public Button button;
    public int inventory_id;
    public ImageView imageview;

       // public   String code = null;
        public String name = null;
        public boolean selected ;

        public Country(int inventory_id, String name, boolean selected) {
            super();
          //  this.code = code;
            this.name = name;
            this.selected = selected;
            this.inventory_id = inventory_id;
        }

        public String getCode() {
            return name;
        }
      //  public void setCode(String code) {
           // this.code = code;
      //  }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }
        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }







