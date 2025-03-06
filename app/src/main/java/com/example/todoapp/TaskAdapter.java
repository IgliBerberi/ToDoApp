package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;
    private OnTaskActionListener listener;

    // Interface for task actions
    public interface OnTaskActionListener {
        void onDeleteTask(Task task);
        void updateTask(Task task);
    }

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;

        // Set up the listener if the context implements our interface
        if (context instanceof OnTaskActionListener) {
            this.listener = (OnTaskActionListener) context;
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskTextView.setText(task.getTitle());
        holder.priorityTextView.setText(task.getPriorityText());
        holder.completedCheckBox.setChecked(task.isCompleted());

        // Set priority indicator color
        int priorityColor;
        switch (task.getPriority()) {
            case 1: // Low
                priorityColor = Color.parseColor("#4CAF50"); // Green
                break;
            case 2: // Medium
                priorityColor = Color.parseColor("#FFC107"); // Yellow
                break;
            case 3: // High
                priorityColor = Color.parseColor("#F44336"); // Red
                break;
            default:
                priorityColor = Color.parseColor("#FFC107"); // Yellow
        }
        holder.priorityIndicator.setBackgroundColor(priorityColor);

        // Show completion status visually
        if (task.isCompleted()) {
            // Visual indicators for completed task
            holder.cardView.setCardBackgroundColor(Color.parseColor("#F5F5F5")); // Light grey
            holder.taskTextView.setTextColor(Color.GRAY);
            holder.taskTextView.setPaintFlags(holder.taskTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.statusTextView.setVisibility(View.VISIBLE);
            holder.statusTextView.setText("Completed");
            holder.statusTextView.setTextColor(Color.parseColor("#4CAF50")); // Green
        } else {
            // Visual indicators for incomplete task
            holder.cardView.setCardBackgroundColor(Color.WHITE);
            holder.taskTextView.setTextColor(Color.BLACK);
            holder.taskTextView.setPaintFlags(holder.taskTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.statusTextView.setVisibility(View.VISIBLE);
            holder.statusTextView.setText("Pending");
            holder.statusTextView.setTextColor(Color.parseColor("#FFA000")); // Amber
        }

        // Set up delete button click listener
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Task taskToDelete = taskList.get(adapterPosition);

                    // Notify listener (MainActivity) to delete from database
                    if (listener != null) {
                        listener.onDeleteTask(taskToDelete);
                    }

                    // Remove from local list
                    taskList.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                }
            }
        });

        holder.detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TaskDetailActivity.class);
                intent.putExtra("taskId", task.getId());
                intent.putExtra("taskTitle", task.getTitle());
                intent.putExtra("taskDescription", task.getDescription());
                intent.putExtra("taskPriority", task.getPriority());
                intent.putExtra("taskCompleted", task.isCompleted());
                context.startActivity(intent);
            }
        });

        holder.completedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = holder.completedCheckBox.isChecked();
                task.setCompleted(isChecked);

                // Save changes to database using the activity's method
                if (listener != null) {
                    listener.updateTask(task);
                }

                // Update UI
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // Add this method to support adding tasks
    public void addTask(Task task) {
        taskList.add(task);
        notifyItemInserted(taskList.size() - 1);
    }

    public void setTasks(List<Task> tasks) {
        this.taskList = tasks;
        notifyDataSetChanged();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTextView, priorityTextView, statusTextView;
        ImageButton deleteButton, detailsButton;
        CheckBox completedCheckBox;
        View priorityIndicator;
        CardView cardView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTextView = itemView.findViewById(R.id.taskTextView);
            priorityTextView = itemView.findViewById(R.id.priorityTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            detailsButton = itemView.findViewById(R.id.detailsButton);
            completedCheckBox = itemView.findViewById(R.id.completedCheckBox);
            priorityIndicator = itemView.findViewById(R.id.priorityIndicator);
            cardView = (CardView) itemView;
        }
    }
}