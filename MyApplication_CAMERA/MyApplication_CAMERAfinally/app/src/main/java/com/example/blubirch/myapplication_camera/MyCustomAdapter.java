package com.example.blubirch.myapplication_camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import java.util.ArrayList;

//import static com.example.blubirch.myapplication_camera.R.layout.simplrow;

/**
 * Created by blubirch on 20/2/17.
 */






public class MyCustomAdapter extends ArrayAdapter<Country> {

    public ArrayList<Country> countryList;
   // private Intent i;
    private Context context;

    public MyCustomAdapter(Context context, int textViewResourceId,
                           ArrayList<Country> countryList) {
        super(context, textViewResourceId, countryList);
        this.countryList = new ArrayList<Country>();
        this.countryList.addAll(countryList);
       // this.i=i;
    }
public ImageView findimage(String name)
{
    ImageView i;
    for (Country c : countryList) {


        if ((c.name).equals(name)) {

            i= c.imageview;
            return i;
        }


}    return null;}


    public boolean findCode(String s)
    {
        for (Country c : countryList) {


            if ((c.name).equals(s)) {

                return c.selected;
            }


        }

        return false;





    }



   // public get button(return )




    private class ViewHolder {
        CheckBox name;
        Button button;
        ImageView imageview;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
       // convertView.findViewById(R.id.)
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            // LayoutInflater vi = (LayoutInflater)getSystemService(
            // Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.simplrow, null);

            holder = new ViewHolder();
            holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
            holder.imageview= (ImageView)convertView.findViewById(R.id.image);

            convertView.setTag(holder);
            holder.button = (Button) convertView.findViewById(R.id.upload);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Country country = countryList.get(position);
        country.imageview= holder.imageview;
       final Context c = parent.getContext();
        country.button= holder.button;

        holder.name.setText(country.getName() +" (" +  country.getCode() + ")");
        holder.name.setChecked(country.isSelected());
        holder.name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                country.setSelected(isChecked);
            }
        });
       holder.button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i=new Intent(c, Imageupload.class);
               i.putExtra("name",country.name);
               i.putExtra("inventory_id",Integer.toString(country.inventory_id));
        ((Activity)c).startActivityForResult(i,1);

           }


       });


        return convertView;

    }

}

