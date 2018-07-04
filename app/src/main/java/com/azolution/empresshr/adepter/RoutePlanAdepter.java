package com.azolution.empresshr.adepter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.azolution.empresshr.R;
import com.azolution.empresshr.model.RoutePlan;

import java.util.ArrayList;

public class RoutePlanAdepter extends RecyclerView.Adapter<RoutePlanAdepter.RoutePlanViewHolder>{

    private ArrayList<RoutePlan> routePlanList;
    private Context context;

    public RoutePlanAdepter(ArrayList<RoutePlan> routePlanList, Context context) {
        this.routePlanList = routePlanList;
        this.context = context;
    }

    @NonNull
    @Override
    public RoutePlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_route_plan_item,parent,false);
        return new RoutePlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutePlanViewHolder holder, int position) {
        RoutePlan plan = routePlanList.get(position);
        holder.outletName.setText("Outlet Name : "+plan.getOutletName());
        holder.geoLOcation.setText("Geo Location : "+plan.getGeoLocation());
        holder.visitStartTime.setText("VisitStart Time : 10AM");
    }

    @Override
    public int getItemCount() {
        return routePlanList.size();
    }

    class RoutePlanViewHolder extends RecyclerView.ViewHolder {
        private TextView outletName,geoLOcation,visitStartTime;
        RoutePlanViewHolder(View itemView) {
            super(itemView);

            outletName = itemView.findViewById(R.id.outlet_name);
            geoLOcation = itemView.findViewById(R.id.geo_location);
            visitStartTime = itemView.findViewById(R.id.visitStartTime);
        }
    }
}
