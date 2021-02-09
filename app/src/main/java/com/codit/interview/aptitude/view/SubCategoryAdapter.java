package com.codit.interview.aptitude.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codit.interview.aptitude.R;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Sreejith on 24-Feb-16.
 */
public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {


    private AdapterInterface adapterInterface;
    Context context;
    private static final int CARD_TYPE=1;
   static int counter=0;
    final int[] actualWidth = new int[1];
    final int[] actualHeight = new int[1];
    SharedPreferences progressPreference;
    ArrayList<Integer> adPos=new ArrayList<>();

boolean adsRemoved=false;

    public interface AdapterInterface
{
     void goToQuestion(View v, String category, int minutes, int seconds);
     void showTimer(int min, int sec, String currentCategory);
}



    private LinkedHashMap<String,String> subCategories;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView subQueTitle;
        public TextView total;
        public TextView attempted;
        public CardView rowCard;
        public ImageButton btnTimer;
        public TextView timerText;
        ProgressBar subProgressBar;
        TextView attemptedCount;
        NativeExpressAdView adView;



        public ViewHolder(View itemView) {
            super(itemView);

            subQueTitle= (TextView)itemView.findViewById(R.id.subQueTitle);

            rowCard=(CardView)itemView.findViewById(R.id.rowCard);
            btnTimer=(ImageButton)itemView.findViewById(R.id.btnTimer);
            timerText=(TextView)itemView.findViewById(R.id.timerText);

            subProgressBar=(ProgressBar)itemView.findViewById(R.id.subProgressbar);
            attemptedCount=(TextView)itemView.findViewById(R.id.attemptedCount);
        }


    }



    public void add(int position, String item1,String item2) {
        subCategories.put(item1,item2);
        notifyItemInserted(position);

    }



    @Override
    public SubCategoryAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {



            View v;
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_recycler_row, parent, false);
            ViewHolder vh=new ViewHolder(v);
            return  vh;





    }

    public SubCategoryAdapter(LinkedHashMap<String,String> list, Context context, ArrayList<Integer> ads)

    {
        this.context=context;
        this.adapterInterface=(AdapterInterface)context;
        subCategories=list;
        progressPreference=context.getSharedPreferences("progress", Context.MODE_PRIVATE);
        this.adPos=ads;

    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {



        Set<String> categories= subCategories.keySet();
        final List<String> sub=new ArrayList<>();
        sub.addAll(categories);







        final String currentCategory= sub.get(position);
        SharedPreferences progressPreference=context.getSharedPreferences("progress",Context.MODE_PRIVATE);
        int progress= (int) progressPreference.getFloat(currentCategory,0f);

        holder.attemptedCount.setText("Attempted: "+progress+"%");
        holder.subProgressBar.setProgress(progress);






        final String time=subCategories.get(currentCategory).toString();
        holder.timerText.setText(time);

       String timeString[]= time.split(":");
        final int mins=Integer.valueOf(timeString[0]);
        final int secs=Integer.valueOf(timeString[1]);

        holder.subQueTitle.setText(currentCategory);




        holder.rowCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {



                adapterInterface.goToQuestion(v,currentCategory,mins,secs);

            }
        });


        holder.btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterInterface.showTimer(mins,secs,currentCategory);


            }
        });

        holder.timerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterInterface.showTimer(mins,secs,currentCategory);
            }
        });

    }




    @Override
    public int getItemCount() {
        return subCategories.size();
    }


    @Override
    public int getItemViewType(int position) {

        return CARD_TYPE;
    }



}
