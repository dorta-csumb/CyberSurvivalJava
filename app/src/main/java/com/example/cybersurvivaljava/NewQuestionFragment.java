package com.example.cybersurvivaljava;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cybersurvivaljava.database.CyberSurvivalRepository;
import com.example.cybersurvivaljava.database.entities.Problems;
import com.example.cybersurvivaljava.databinding.FragmentNewQuestionBinding;

public class NewQuestionFragment extends Fragment {

    private FragmentNewQuestionBinding binding;
    private CyberSurvivalRepository repository;

    public NewQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewQuestionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = CyberSurvivalRepository.getRepository(requireActivity().getApplication());

        binding.createQuestionButton.setOnClickListener(v -> createNewQuestion());
        binding.cancelButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    private void createNewQuestion() {
        String name = binding.questionNameEditText.getText().toString();
        String categoryStr = binding.questionCategoryEditText.getText().toString();
        String description = binding.questionDescriptionEditText.getText().toString();
        String correctSolution = binding.correctSolutionEditText.getText().toString();
        String wrong1 = binding.wrongSolution1EditText.getText().toString();
        String wrong2 = binding.wrongSolution2EditText.getText().toString();
        String wrong3 = binding.wrongSolution3EditText.getText().toString();

        if (name.isEmpty() || categoryStr.isEmpty() || description.isEmpty() || correctSolution.isEmpty()
                || wrong1.isEmpty() || wrong2.isEmpty() || wrong3.isEmpty()) {
            toastMaker("All fields must be filled out");
            return;
        }

        int category = Integer.parseInt(categoryStr);

        Problems newProblem = new Problems(category, name, description, correctSolution, wrong1, wrong2, wrong3);
        repository.insertProblem(newProblem);

        toastMaker("Question " + name + " created.");

        getParentFragmentManager().popBackStack();
    }

    private void toastMaker(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
