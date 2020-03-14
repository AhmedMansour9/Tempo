package bekya.bekyaa.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bekya.bekyaa.Interface.itemViewinterface;
import bekya.bekyaa.Model.Retrivedata;
import bekya.bekyaa.R;
import bekya.bekyaa.Time;
import bekya.bekyaa.Interface.Token_View;
import bekya.bekyaa.Model.meesage;
import bekya.bekyaa.tokenid.SharedPrefManager;

public class lastmessage extends RecyclerView.Adapter<lastmessage.MyViewHolder> {
    private List<Retrivedata> mArrayList;
    itemViewinterface itemclick;
    List<meesage> array=new ArrayList<>();
    public imgclick btnclick;
    Context context;
    String LastDate="";
    Token_View token_view;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image,imageedit,imgdelete;
        CardView itemcard;
        private AdView adView;

        public TextView T_user,T_Time;
        public MyViewHolder(View view) {
            super(view);
            T_user=view.findViewById(R.id.T_user);
            T_Time=view.findViewById(R.id.T_Time);
            adView = view.findViewById(R.id.adView);

        }

    }
    public lastmessage( List<meesage> moviesList , Context context) {
        this.array = moviesList;
        this.context=context;

    }
    public void setClickListener(Token_View token_views) {

        token_view=token_views;
    }
    public void setClickButton(imgclick btnclic){
        this.btnclick=btnclic;
    }

    @Override
    public lastmessage.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_lastmessage, parent, false);
        return new lastmessage.MyViewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(final lastmessage.MyViewHolder holder, final int position) {
//        String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).format(new Date());
//
//
//        Time.getInstance().parse(date);
        String dtStart = array.get(position).getDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date = format.parse(dtStart);
            Time a=new Time(context);
            String time= a.timeAgo(date);
            holder.T_Time.setText(time);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //        if(LastDate.equals(datee)){
//            holder.T_Time.setVisibility(View.GONE);
//
//        }else {
//            holder.T_Time.setVisibility(View.VISIBLE);
//            LastDate=datee;
//            holder.T_Time.setText(datee);
//
//        }
        holder.T_user.setText(array.get(position).getMsg());
        if(position==1){
            holder.adView.setVisibility(View.VISIBLE);
            MobileAds.initialize(context, "ca-app-pub-3940256099942544~3347511713");
            AdRequest adRequest = new AdRequest.Builder().build();
            holder.adView.loadAd(adRequest);
        }else {
            holder.adView.setVisibility(View.GONE);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token=  SharedPrefManager.getInstance(context).getDeviceToken();
                if(array.get(position).getFrom().equals(token)){
                    token_view.token(array.get(position).getTo());
                }else {
                    token_view.token(array.get(position).getFrom());
                }



            }
        });

//        if(array.get(position).getId().equals("Admin")){
//            holder.T_user.setBackground(context.getResources().getDrawable(R.drawable.bcuser));
//            holder.T_user.setTextColor(Color.WHITE);
//
//
//        }else {
//
//            holder.T_user.setBackground(context.getResources().getDrawable(R.drawable.bcadmin));
//            holder.T_user.setTextColor(Color.BLACK);
//            holder.T_user.setText(array.get(position).getMsg());
//
//        }

    }




    @Override
    public int getItemCount() {
        return   array.size();

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}




