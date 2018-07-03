package com.azolution.empresshr.adepter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.azolution.empresshr.R;
import com.azolution.empresshr.model.LeaveType;

import java.util.List;

public class LeaveTypeAdepter extends RecyclerView.Adapter<LeaveTypeAdepter.LeaveTypeViewHolder>{

    private List<LeaveType> leaveTypeList;
    private Context context;
    private onRecyclerViewItemClickListener onCheckBoxClickListener;



    public LeaveTypeAdepter(List<LeaveType> leaveTypeList, Context context) {
        this.leaveTypeList = leaveTypeList;
        this.context = context;
    }

    @NonNull
    @Override
    public LeaveTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_leave_type_row,parent,false);
        return new LeaveTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveTypeViewHolder holder, int position) {
        final LeaveType type = leaveTypeList.get(position);
        holder.leaveTypeName.setText(type.getLeaveTypeName());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckBoxClickListener.onItemClickListener(type.getLeaveTypeName(),type.getLeaveTypeId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return leaveTypeList.size();
    }

    class LeaveTypeViewHolder extends RecyclerView.ViewHolder{
        private TextView leaveTypeName;
        private View view;
        LeaveTypeViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            leaveTypeName = itemView.findViewById(R.id.list_leave_type_name);
        }
    }

    public void setOnItemClickListener(onRecyclerViewItemClickListener checkBoxClickListener) {
        this.onCheckBoxClickListener = checkBoxClickListener;
    }

    public interface onRecyclerViewItemClickListener{
        void onItemClickListener(String type, Integer id);
    }
}
