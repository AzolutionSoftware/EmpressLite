package com.azolution.empresshr.adepter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.azolution.empresshr.R;
import com.azolution.empresshr.model.LeaveApplicationHistory;

import java.util.ArrayList;

public class LeaveHistoryAdepter extends RecyclerView.Adapter<LeaveHistoryAdepter.LeaveHistoryViewHolder> {

    private ArrayList<LeaveApplicationHistory>historiesList;
    private Context context;
    private String employeeName;

    public LeaveHistoryAdepter(ArrayList<LeaveApplicationHistory> historiesList, Context context,String employeeName) {
        this.historiesList = historiesList;
        this.context = context;
        this.employeeName = employeeName;
    }

    @NonNull
    @Override
    public LeaveHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_leave_history_row,parent,false);
        return new LeaveHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveHistoryViewHolder holder, int position) {
        if (!(position % 2 == 0)){
            holder.root.setBackgroundColor(Color.parseColor("#f7d28b"));
        }else {
            holder.root.setBackgroundColor(Color.parseColor("#a3d7a6"));
        }

        LeaveApplicationHistory history = historiesList.get(position);
        holder.nameText.setText("Name : "+employeeName);
        holder.typeText.setText("Leave Type : "+history.getLeaveTypeName());
        holder.statusText.setText("Status : "+history.getStatus());

    }

    @Override
    public int getItemCount() {
        return historiesList.size();
    }

    class LeaveHistoryViewHolder extends RecyclerView.ViewHolder{
        private TextView nameText,typeText,dateText,appliedDateText,approveDateText,statusText;
        private LinearLayout root;
        LeaveHistoryViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.list_leave_history_row_employeeName);
            typeText = itemView.findViewById(R.id.list_leave_history_row_type);
            dateText = itemView.findViewById(R.id.list_leave_history_row_leaveDate);
            appliedDateText = itemView.findViewById(R.id.list_leave_history_row_leave_applyDate);
            approveDateText = itemView.findViewById(R.id.list_leave_history_row_approve_date);
            statusText = itemView.findViewById(R.id.list_leave_history_row_approve_status);
            root = itemView.findViewById(R.id.list_leave_history_row_root);
        }
    }
}
