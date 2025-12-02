package com.example.cybersurvivaljava;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cybersurvivaljava.database.CyberSurvivalRepository;
import com.example.cybersurvivaljava.databinding.FragmentDeleteQuestionBinding;

public class DeleteQuestionFragment extends Fragment {

    private FragmentDeleteQuestionBinding binding;
    private CyberSurvivalRepository repository;
    private QuestionAdapter adapter;

    public DeleteQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDeleteQuestionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = CyberSurvivalRepository.getRepository(requireActivity().getApplication());

        setupRecyclerView();

        repository.getAllProblems().observe(getViewLifecycleOwner(), problems -> {
            adapter.submitList(problems);
        });

        binding.cancelButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });
    }

    private void setupRecyclerView() {
        adapter = new QuestionAdapter(problem -> {
            repository.deleteProblem(problem);
        });
        binding.questionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.questionsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
