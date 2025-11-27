package com.example.cybersurvivaljava;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cybersurvivaljava.database.CyberSurvivalRepository;
import com.example.cybersurvivaljava.database.entities.User;
import com.example.cybersurvivaljava.databinding.FragmentNewAdminBinding;

public class NewAdmin extends Fragment {

    private FragmentNewAdminBinding binding;
    private CyberSurvivalRepository repository;

    public NewAdmin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentNewAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the repository instance
        repository = CyberSurvivalRepository.getRepository(requireActivity().getApplication());

        // Set the click listener for the create button
        binding.registerAdminButton.setOnClickListener(v -> createNewAdmin());
        binding.cancelRegisterAdminButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    private void createNewAdmin() {
        String username = binding.usernameAdminRegisterEditText.getText().toString();
        if (username.isEmpty()) {
            toastMaker("Username cannot be empty");
            return;
        }
        LiveData<User> userObserver = repository.getUserByUsername(username);
        userObserver.observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                toastMaker("Username not available");
            } else {
                String password = binding.passwordAdminRegisterEditText.getText().toString();
                String passwordCheck = binding.passwordCheckAdminRegisterEditText.getText().toString();
                if (password.isEmpty() || passwordCheck.isEmpty()) {
                    toastMaker("Password fields cannot be empty");
                } else if (!password.equals(passwordCheck)) {
                    toastMaker("Passwords do not match");
                } else {
                    User newUser = new User(username, password);
                    newUser.setAdmin(true);
                    repository.insertUser(newUser);
                    toastMaker("Admin user " + username + " created.");
                    // Go back to the previous screen (the admin button list)
                    getParentFragmentManager().popBackStack();
                }
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Release the binding object when the view is destroyed to avoid memory leaks
        binding = null;
    }
}