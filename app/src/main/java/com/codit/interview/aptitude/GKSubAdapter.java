package com.codit.interview.aptitude;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Sreejith on 29-Jul-16.
 */
public class GKSubAdapter extends RecyclerView.Adapter<GKSubAdapter.ViewHolder> {

   public   GKSubInterface gkSubInterface;



    public interface GKSubInterface{

         void goToGKQue(View v,String category);


    }

    private HashMap<String,String> subCategories;

    Context context;




    public void add(int position, String item1,String item2) {
        subCategories.put(item1,item2);
        notifyItemInserted(position);

    }

    public GKSubAdapter(HashMap<String,String> subCategories,Context context)
    {
        this.gkSubInterface= (GKSubInterface) context;
        this.subCategories=subCategories;
        this.context=context;
    }

    @Override
    public GKSubAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.gk_sub_row, parent, false);
        ViewHolder vh=new ViewHolder(v);
        return  vh;

    }

    @Override
    public void onBindViewHolder( ViewHolder holder,  int position) {



                Set<String> categories= subCategories.keySet();
                List<String> sub=new ArrayList<>();
                sub.addAll(categories);


                 final String currentCategory= sub.get(position);

                holder.subTitle.setText(currentCategory);


        SharedPreferences progressPreference=context.getSharedPreferences("progress",Context.MODE_PRIVATE);
        int progressint= (int) progressPreference.getFloat(currentCategory,0f);


        holder.attemptedCount.setText("Attempted: "+progressint+"%");
        holder.subProgressbar.setProgress(progressint);



        holder.progressText.setText(progressint+"%");

        holder.rowCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gkSubInterface.goToGKQue(view,currentCategory);

            }
        });


    }

    @Override
    public int getItemCount() {
        return subCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView subTitle,progressText,attemptedCount;
        ProgressBar subProgressbar;
        CardView rowCard;

        public ViewHolder(View itemView) {
            super(itemView);

            attemptedCount=(TextView)itemView.findViewById(R.id.attemptedCount);
            subTitle= (TextView) itemView.findViewById(R.id.subTitle);
            progressText=(TextView)itemView.findViewById(R.id.progressText);
            subProgressbar=(ProgressBar)itemView.findViewById(R.id.subProgressbar);
            rowCard=(CardView)itemView.findViewById(R.id.rowCard);

        }


    }



}


