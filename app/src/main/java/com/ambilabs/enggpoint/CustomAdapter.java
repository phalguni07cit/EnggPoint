package com.ambilabs.enggpoint;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by LOL on 09-Nov-17.
 */

public class CustomAdapter extends BaseAdapter {

    ArrayList<Pdf> pdfArrayList;
    Context c;
    Boolean typeCheck;
    String title;
    String subject;

    public CustomAdapter(Context c, ArrayList<Pdf> pdfArrayList,Boolean typeCheck,String subject)
    {
        this.c = c;
        this.pdfArrayList = pdfArrayList;
        this.typeCheck =typeCheck;
        this.subject = subject;
    }

    @Override
    public int getCount() {
        return pdfArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return pdfArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
        {
            convertView = LayoutInflater.from(c).inflate(R.layout.cards,parent,false);
        }
        final Pdf mpdf = (Pdf) this.getItem(position);

        TextView subtv = (TextView)convertView.findViewById(R.id.sub_tv_id);
        if(typeCheck) {
            TextView topictv = (TextView) convertView.findViewById(R.id.topic_type_tv_id);
            topictv.setText(mpdf.getTopic());
            title = mpdf.getTopic();
        }
        else
        {
            TextView typetv = (TextView)convertView.findViewById(R.id.topic_type_tv_id);
            typetv.setText(mpdf.getType()+", ");

            TextView montv = (TextView)convertView.findViewById(R.id.month_tv_id);
            montv.setText(mpdf.getMonth()+", ");
            TextView yeartv = (TextView)convertView.findViewById(R.id.year_tv_id);
            yeartv.setText(mpdf.getYear());
            title = mpdf.getType()+" "+mpdf.getMonth()+" "+mpdf.getYear();

        }
        TextView authortv = (TextView)convertView.findViewById(R.id.author_tv_id);
        TextView posttv = (TextView)convertView.findViewById(R.id.post_tv_id);
        TextView orgtv = (TextView)convertView.findViewById(R.id.org_tv_id);
        TextView datetimetv = (TextView)convertView.findViewById(R.id.datetime_tv_id);
        ImageView img = (ImageView)convertView.findViewById(R.id.imgPdf);
        authortv.setText(mpdf.getAuthor());
        posttv.setText(mpdf.getPost()+", ");
        orgtv.setText(mpdf.getOrg());
        datetimetv.setText(mpdf.getDatetime());
        subtv.setText(subject);
        img.setImageResource(R.drawable.pdficon);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPdfView(mpdf.getUrl(),title);
            }
        });

        return convertView;
    }

    private void openPdfView(String path,String topic)
    {
        Intent next = new Intent (c, PdfOpen.class);
        next.putExtra("PATH",path);
        next.putExtra("TITLE",topic);
        c.startActivity(next);
    }

}
