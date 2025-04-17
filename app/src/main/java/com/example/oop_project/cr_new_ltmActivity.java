package com.example.oop_project;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oop_project.DataProvider.DataProvider;
import com.example.oop_project.model.Lutemon;

/**
 * Activity for creating new Lutemon characters.
 * Handles user input and validation for new Lutemon creation.
 */
public class cr_new_ltmActivity extends AppCompatActivity {

    // UI Components
    private RadioGroup radioGroup;
    private EditText nameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cr_new_ltm);

        // Initialize UI components
        initializeViews();

        // Set up button listeners
        setupButtonListeners();

        // Configure window insets for edge-to-edge display
        setupWindowInsets();
    }

    /**
     * Initializes all view components
     */
    private void initializeViews() {
        radioGroup = findViewById(R.id.radioGroup);
        nameInput = findViewById(R.id.et_nameInput);
    }

    /**
     * Sets up button click listeners
     */
    private void setupButtonListeners() {
        // Create button - validates input and creates new Lutemon
        Button btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(v -> {
            Lutemon newLutemon = createNewLutemon();
            if (newLutemon != null) {
                DataProvider.getInstance().addNewLutemon(newLutemon);

                // Verify creation was successful
                if (DataProvider.getInstance().getLutemonByName(newLutemon.getName()) != null) {
                    Toast.makeText(this, newLutemon.getName() + " created!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this,
                            "Failed to create Lutemon. Name might already exist.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // Cancel button - closes the activity
        Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> finish());
    }

    /**
     * Configures edge-to-edge window insets
     */
    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.create_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Creates a new Lutemon based on user input
     * @return The created Lutemon, or null if validation failed
     */
    private Lutemon createNewLutemon() {
        // Get selected radio button ID
        int selectedId = radioGroup.getCheckedRadioButtonId();
        String lutemonName = nameInput.getText().toString().trim();

        // Validate name input
        if (TextUtils.isEmpty(lutemonName)) {
            nameInput.setError("Name cannot be empty");
            return null;
        }

        // Validate type selection
        if (selectedId == -1) {
            Toast.makeText(this, "Please select a Lutemon type", Toast.LENGTH_SHORT).show();
            return null;
        }

        // Create Lutemon based on selected type
        RadioButton selectedRadio = findViewById(selectedId);
        Lutemon lutemon = null;

        // Each type has different base stats and image
        if (selectedRadio.getId() == R.id.radioWhite) {
            lutemon = new Lutemon(lutemonName, "White", 5, 4, 0, 20,
                    "https://archives.bulbagarden.net/media/upload/thumb/1/1c/1008Miraidon.png/375px-1008Miraidon.png");
        } else if (selectedRadio.getId() == R.id.radioGreen) {
            lutemon = new Lutemon(lutemonName, "Green", 6, 3, 0, 19,
                    "https://archives.bulbagarden.net/media/upload/thumb/a/a2/1007Koraidon.png/375px-1007Koraidon.png");
        } else if (selectedRadio.getId() == R.id.radioPink) {
            lutemon = new Lutemon(lutemonName, "Pink", 7, 2, 0, 18,
                    "https://archives.bulbagarden.net/media/upload/thumb/f/fb/0718Zygarde-Complete.png/165px-0718Zygarde-Complete.png");
        } else if (selectedRadio.getId() == R.id.radioOrange) {
            lutemon = new Lutemon(lutemonName, "Orange", 8, 1, 0, 17,
                    "https://archives.bulbagarden.net/media/upload/thumb/1/17/0716Xerneas.png/375px-0716Xerneas.png");
        } else if (selectedRadio.getId() == R.id.radioBlack) {
            lutemon = new Lutemon(lutemonName, "Black", 9, 0, 0, 16,
                    "https://archives.bulbagarden.net/media/upload/thumb/1/1d/0717Yveltal.png/375px-0717Yveltal.png");
        }

        return lutemon;
    }
}