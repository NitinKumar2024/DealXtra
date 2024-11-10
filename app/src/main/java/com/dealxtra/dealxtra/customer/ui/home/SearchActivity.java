package com.dealxtra.dealxtra.customer.ui.home;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dealxtra.dealxtra.R;
import com.dealxtra.dealxtra.customer.adapters.RecentSearchAdapter;
import com.dealxtra.dealxtra.customer.models.RecentSearch;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private MaterialCardView searchCard;
    private EditText searchEditText;
    private ImageView voiceSearchButton;
    private RecyclerView recentSearchesRecyclerView;

    private LinearLayout recentSearchesContainer;
    private MaterialButton clearHistoryButton;

    private RecentSearchAdapter recentSearchAdapter;
    private List<RecentSearch> recentSearches;

    private static final int VOICE_SEARCH_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        initViews();
        setupSearchBar();
        setupRecyclerView();
        loadRecentSearches();
        setupClickListeners();
    }

    private void initViews() {
        searchCard = findViewById(R.id.searchCard);
        searchEditText = findViewById(R.id.searchEditText);
        voiceSearchButton = findViewById(R.id.voiceSearchButton);
        recentSearchesRecyclerView = findViewById(R.id.recentSearchesRecyclerView);

        recentSearchesContainer = findViewById(R.id.recentSearchesContainer);
        clearHistoryButton = findViewById(R.id.clearHistoryButton);

        // Setup back button
        findViewById(R.id.backButton).setOnClickListener(v -> onBackPressed());

        // Request focus for the EditText
        searchEditText.requestFocus();

        // Add a delay to ensure the keyboard shows up
        new Handler().postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 500); // 200 ms delay to show keyboard

        // Get search query from intent
        String query = getIntent().getStringExtra("query");
        if (query != null && !query.isEmpty()) {
            searchEditText.setText(query);

        }


    }

    private void setupSearchBar() {
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(searchEditText.getText().toString().trim());
                return true;
            }
            return false;
        });

        // Clear button functionality
        findViewById(R.id.clearButton).setOnClickListener(v -> {
            searchEditText.setText("");
            updateSearchBarState(false);
        });
    }

    private void setupRecyclerView() {
        recentSearchAdapter = new RecentSearchAdapter(new ArrayList<>(), this::onRecentSearchClicked);
        recentSearchesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recentSearchesRecyclerView.setAdapter(recentSearchAdapter);
        recentSearchesRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
    }

    private void loadRecentSearches() {
        // Load from SharedPreferences in a real app
        recentSearches = new ArrayList<>();
        updateUI();
    }

    private void setupClickListeners() {
        clearHistoryButton.setOnClickListener(v -> showClearHistoryDialog());

        voiceSearchButton.setOnClickListener(v -> startVoiceSearch());

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSearchBarState(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void startVoiceSearch() {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.voice_search_prompt));
            startActivityForResult(intent, VOICE_SEARCH_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            showVoiceSearchError();
        }
    }

    private void showVoiceSearchError() {
        Snackbar.make(searchCard,
                R.string.voice_search_not_available,
                Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_SEARCH_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && !matches.isEmpty()) {
                String query = matches.get(0);
                searchEditText.setText(query);
                performSearch(query);
            }
        }
    }

    private void updateSearchBarState(boolean hasText) {
        findViewById(R.id.clearButton).setVisibility(hasText ? View.VISIBLE : View.GONE);
        voiceSearchButton.setVisibility(hasText ? View.GONE : View.VISIBLE);
    }

    private void performSearch(String query) {
        if (query.isEmpty()) return;

        // Hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);

        // Add to recent searches
        addToRecentSearches(query);

        // Launch results activity
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);

    }

    private void addToRecentSearches(String query) {
        // Remove if exists (to avoid duplicates)
        recentSearches.removeIf(search -> search.getQuery().equals(query));

        // Add to beginning of list
        recentSearches.add(0, new RecentSearch(query, System.currentTimeMillis()));

        // Limit list size
        if (recentSearches.size() > 10) {
            recentSearches.remove(recentSearches.size() - 1);
        }

        // Save to SharedPreferences in a real app
        updateUI();
    }

    private void updateUI() {
        if (recentSearches.isEmpty()) {
            recentSearchesContainer.setVisibility(View.GONE);
        } else {
            recentSearchesContainer.setVisibility(View.VISIBLE);

        }
    }

    private void showClearHistoryDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.clear_history_title)
                .setMessage(R.string.clear_history_message)
                .setPositiveButton(R.string.clear, (dialog, which) -> {
                    recentSearches.clear();
                    updateUI();
                    // Clear SharedPreferences in a real app
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void onRecentSearchClicked(RecentSearch recentSearch) {
        searchEditText.setText(recentSearch.getQuery());
        searchEditText.setSelection(recentSearch.getQuery().length());
        performSearch(recentSearch.getQuery());
    }
}
