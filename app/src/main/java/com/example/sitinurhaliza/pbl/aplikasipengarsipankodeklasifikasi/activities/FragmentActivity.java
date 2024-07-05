package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.fragments.BAFragment;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.fragments.BarangFragment;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.fragments.FilesFragmentActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentActivity extends AppCompatActivity {

    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        navView = findViewById(R.id.nav_bottom_nav_view);
        navView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Set the title of the selected item and clear the titles of other items
            clearTitlesExcept(item);

            if (itemId == R.id.bottom_home) {
                tampilHomeFragment();
            } else if (itemId == R.id.barang) {
                tampilBarangFragment();
            } else if (itemId == R.id.bottom_files) {
                tampilFilesFragment();
            } else if (itemId == R.id.bottom_profile) {
                tampilkanDialogLogout();
            }
            return true;
        });

        // Default fragment
        if (savedInstanceState == null) {
            tampilHomeFragment();
        }
    }

    private void clearTitlesExcept(MenuItem selectedItem) {
        MenuItem homeItem = navView.getMenu().findItem(R.id.bottom_home);
        MenuItem barangItem = navView.getMenu().findItem(R.id.barang);
        MenuItem filesItem = navView.getMenu().findItem(R.id.bottom_files);
        MenuItem profileItem = navView.getMenu().findItem(R.id.bottom_profile);

        homeItem.setTitle("");
        barangItem.setTitle("");
        filesItem.setTitle("");
        profileItem.setTitle("");

        // Set title for the selected item
        if (selectedItem != null) {
            int itemId = selectedItem.getItemId();
            if (itemId == R.id.bottom_home) {
                selectedItem.setTitle("Home");
            } else if (itemId == R.id.barang) {
                selectedItem.setTitle("File Nota Barang");
            } else if (itemId == R.id.bottom_files) {
                selectedItem.setTitle("Kode Klasifikasi");
            } else if (itemId == R.id.bottom_profile) {
                selectedItem.setTitle("Logout");
            }
        }
    }

    public void tampilHomeFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_frame_layout, new HomeFragment()).commit();
    }

    public void tampilBarangFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_frame_layout, new BAFragment()).commit();
    }

    public void tampilFilesFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_frame_layout, new FilesFragmentActivity()).commit();
    }

    private void tampilkanDialogLogout() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_logout);

        Button btnCancel = dialog.findViewById(R.id.button_cancel);
        Button btnConfirm = dialog.findViewById(R.id.button_confirm);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        dialog.show();
    }



}
