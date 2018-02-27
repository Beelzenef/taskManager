package com.example.taskmng.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taskmng.R;
import com.example.taskmng.model.Task;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by paco on 6/02/18.
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {
    public ArrayList<Task> mTasks;

    public TasksAdapter(){
        this.mTasks = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.textView1) TextView name;
        @BindView(R.id.textView2) TextView link;
        //private TextView name, link;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //name = (TextView) itemView.findViewById(R.id.textView1);
            //link = (TextView) itemView.findViewById(R.id.textView2);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View siteView = inflater.inflate(R.layout.item_view, parent, false);

        // Return a new holder instance
        return new ViewHolder(siteView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = mTasks.get(position);

        holder.name.setText(task.getName());
        holder.link.setText(task.getDesc());
    }

    public void setSites(ArrayList<Task> tasks) {
        mTasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public Task getAt(int position){
        Task task;
        task = this.mTasks.get(position);
        return task;
    }

    public void add(Task task) {
        this.mTasks.add(task);
        notifyItemInserted(mTasks.size() - 1);
        notifyItemRangeChanged(0, mTasks.size() - 1);
    }

    public void modifyAt(Task task, int position) {
        this.mTasks.set(position, task);
        notifyItemChanged(position);
    }

    public void removeAt(int position) {
        this.mTasks.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, mTasks.size() - 1);
    }
}
