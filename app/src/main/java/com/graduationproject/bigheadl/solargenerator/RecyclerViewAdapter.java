package com.graduationproject.bigheadl.solargenerator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lshbi on 5/3/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<ReportContent> contentList;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);


        contentList = new ArrayList<>();
        contentList.add(new ReportContent("zhangsan","A1","test",R.drawable.ic_1));
        contentList.add(new ReportContent("lisi","B2","Hello World!",R.drawable.ic_2));
        contentList.add(new ReportContent("wangwu","C3","This is a program.",R.drawable.ic_3));

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView username, device, content;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            username = (TextView) itemView.findViewById(R.id.username);
            device = (TextView) itemView.findViewById(R.id.devicesmodel);
            content = (TextView) itemView.findViewById(R.id.content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() == RecyclerView.NO_POSITION) {
                        Toast.makeText(context, "Click Again.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ReportContent report = contentList.get(getAdapterPosition());
                    ImageView imageView = new ImageView(context);
                    imageView.setImageResource(report.getDrawable());
                    Toast toast = new Toast(context);
                    toast.setView(imageView);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();

                }
            });
        }

        public ImageView getImage(){
            return image;
        }

        public TextView getUsername(){
            return username;
        }

        public TextView getDevice(){
            return device;
        }

        public TextView getContent(){
            return content;
        }
    }

    @Override
    public int getItemCount(){
        return contentList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View itemView = layoutInflater.inflate(R.layout.cardview_item,viewGroup,false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder viewHolder,int position){
        ReportContent content = contentList.get(position);
        viewHolder.getImage().setImageResource(content.getDrawable());
        viewHolder.getUsername().setText(content.getUsername());
        viewHolder.getDevice().setText(content.getDevice());
        viewHolder.getContent().setText(content.getContent());

    }

}