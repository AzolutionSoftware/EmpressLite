package com.azolution.empresshr.adepter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.azolution.empresshr.R;
import com.azolution.empresshr.model.AttendanceHistory;

import java.util.ArrayList;

public class AttendanceHistoryAdepter extends RecyclerView.Adapter<AttendanceHistoryAdepter.AttendanceHistoryViewHolder>{


    private ArrayList<AttendanceHistory> attendanceHistoriList;
    private Context context;

    public AttendanceHistoryAdepter(ArrayList<AttendanceHistory> attendanceHistoriList, Context context) {
        this.attendanceHistoriList = attendanceHistoriList;
        this.context = context;
    }

    @NonNull
    @Override
    public AttendanceHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_attendance_history_item,parent,false);
        return new AttendanceHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceHistoryViewHolder holder, int position) {
        AttendanceHistory history = attendanceHistoriList.get(position);
        holder.date.setText("3");
        holder.intime.setText(history.getIntime());
        holder.outTime.setText(history.getOutTime());
        holder.working.setText(history.getWorkingHour());
    }

    @Override
    public int getItemCount() {
        return attendanceHistoriList.size();
    }

    class AttendanceHistoryViewHolder extends RecyclerView.ViewHolder{
        private TextView date,intime,outTime,working;
        public AttendanceHistoryViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.list_date);
            intime = itemView.findViewById(R.id.list_intime);
            outTime = itemView.findViewById(R.id.list_outtime);
            working = itemView.findViewById(R.id.list_working);
        }
    }
}
