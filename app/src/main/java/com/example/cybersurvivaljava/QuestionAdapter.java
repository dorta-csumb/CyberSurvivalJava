package com.example.cybersurvivaljava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersurvivaljava.database.entities.Problems;
import com.example.cybersurvivaljava.databinding.ItemQuestionBinding;

import java.util.function.Consumer;

public class QuestionAdapter extends ListAdapter<Problems, QuestionAdapter.QuestionViewHolder> {

    private final Consumer<Problems> onDeleteCallback;

    public QuestionAdapter(Consumer<Problems> onDeleteCallback) {
        super(new DiffUtil.ItemCallback<Problems>() {
            @Override
            public boolean areItemsTheSame(@NonNull Problems oldItem, @NonNull Problems newItem) {
                return oldItem.getProblemId() == newItem.getProblemId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Problems oldItem, @NonNull Problems newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.onDeleteCallback = onDeleteCallback;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuestionBinding binding = ItemQuestionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new QuestionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Problems currentProblem = getItem(position);
        holder.bind(currentProblem);
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {
        private final ItemQuestionBinding binding;

        public QuestionViewHolder(ItemQuestionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.deleteQuestionButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteCallback.accept(getItem(position));
                }
            });
        }

        public void bind(Problems problem) {
            binding.questionNameTextView.setText(problem.getProblemName());
        }
    }
}
