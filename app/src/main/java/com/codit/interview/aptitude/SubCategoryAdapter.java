package com.codit.interview.aptitude;

import android.annotation.SuppressLint;
import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.LayoutInflater;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
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
    private static final int AD_TYPE=0;
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
            adView=(NativeExpressAdView)itemView.findViewById(R.id.adId);
        }


    }



    public void add(int position, String item1,String item2) {
        subCategories.put(item1,item2);
        notifyItemInserted(position);

    }



    @Override
    public SubCategoryAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {




        if(viewType==AD_TYPE)
        {

            counter++;

            final CardView card= (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_row, parent, false);


            final NativeExpressAdView adview= new NativeExpressAdView(context);
            adview.setId(R.id.adId);

            TextView loading=new TextView(context);
            loading.setText("Loading..");

            loading.setGravity(Gravity.CENTER);
            loading.setId(R.id.loading_text);
            loading.setTextColor(Color.parseColor("#757575"));



            card.addView(adview,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            card.addView(loading,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));




            if (progressPreference.getInt("ad_width",0)== 0) {
                card.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressLint("NewApi")
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {




                        int width = card.getWidth();
                        int height = card.getHeight();


                        float density=context.getResources().getDisplayMetrics().density;
                        actualWidth[0] = (int) (width/ density);
                        actualHeight[0] = (int) (height/density);


                            if (Build.VERSION.SDK_INT<=Build.VERSION_CODES.LOLLIPOP) {

                                progressPreference.edit().putInt("ad_width",actualWidth[0] - 3).apply();
                                progressPreference.edit().putInt("ad_height",actualHeight[0]-3).apply();


                            }
                            else
                            {
                                progressPreference.edit().putInt("ad_width",actualWidth[0] - 2).apply();
                                progressPreference.edit().putInt("ad_height",actualHeight[0]-2).apply();

                            }



                        if (Build.VERSION.SDK_INT<=Build.VERSION_CODES.LOLLIPOP) {

                            card.setRadius(0);
                            card.setPreventCornerOverlap(false);
                            card.setUseCompatPadding(true);

                            card.setContentPadding(-card.getPaddingLeft() - 1, -card.getPaddingRight() - 1, -card.getPaddingTop() - 1, -card.getPaddingTop() - 1);
                            adview.setAdSize(new AdSize(progressPreference.getInt("ad_width",0), progressPreference.getInt("ad_height",0)));
                            adview.setAdUnitId(context.getString(R.string.apti_recycler_1));

                        }
                        else
                        {
                            adview.setAdSize(new AdSize(progressPreference.getInt("ad_width",0), progressPreference.getInt("ad_height",0)));

                            adview.setAdUnitId(context.getString(R.string.apti_recycler_1));

                        }

                        AdRequest adRequest = new AdRequest.Builder()
                                .build();

                        adview.loadAd(adRequest);
                        adview.setVisibility(View.GONE);



                        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                            card.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        else
                            card.getViewTreeObserver().removeGlobalOnLayoutListener(this);


                    }
                });
                //end
            }
            else
            {
                if (Build.VERSION.SDK_INT<=Build.VERSION_CODES.LOLLIPOP) {

                    card.setRadius(0);
                    card.setPreventCornerOverlap(false);
                    card.setUseCompatPadding(true);

                    card.setContentPadding(-card.getPaddingLeft() - 1, -card.getPaddingRight() - 1, -card.getPaddingTop() - 1, -card.getPaddingTop() - 1);
                    adview.setAdSize(new AdSize(progressPreference.getInt("ad_width",0), progressPreference.getInt("ad_height",0)));
                    adview.setAdUnitId(context.getString(R.string.apti_recycler_1));

                }
                else
                {
                    adview.setAdSize(new AdSize(progressPreference.getInt("ad_width",0), progressPreference.getInt("ad_height",0)));

                    adview.setAdUnitId(context.getString(R.string.apti_recycler_1));

                }

                AdRequest adRequest = new AdRequest.Builder()
                        .build();

                adview.loadAd(adRequest);
                adview.setVisibility(View.GONE);


            }

            ViewHolder vh=new ViewHolder(card);





            return  vh;



        }

        else {
            View v;
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_recycler_row, parent, false);
            ViewHolder vh=new ViewHolder(v);
            return  vh;


        }





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



        if(getItemViewType(position)==AD_TYPE)
        {



            holder.adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    holder.itemView.findViewById(R.id.loading_text).setVisibility(View.GONE);
                    holder.adView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);

                   if(adsRemoved==false) {
                       for (int pos : adPos) {
                            subCategories.remove(sub.get(pos));
                            notifyItemRemoved(pos);

                            notifyDataSetChanged();
                        }
                        adsRemoved=true;
                    }

                }
            });

            return ;
        }


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


        Set<String> categories= subCategories.keySet();
        List<String> sub=new ArrayList<>();
        sub.addAll(categories);

        if(sub.get(position).contains("ad"))
        {
            return AD_TYPE;
        }
        return CARD_TYPE;
    }



}
