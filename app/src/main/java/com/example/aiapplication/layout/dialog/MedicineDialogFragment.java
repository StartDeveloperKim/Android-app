package com.example.aiapplication.layout.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.aiapplication.R;
import com.example.aiapplication.medicine.entity.Medicine;

public class MedicineDialogFragment extends DialogFragment {

    private final Medicine medicine;

    public MedicineDialogFragment(Medicine medicine) {
        this.medicine = medicine;
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
//
//        LayoutInflater inflater = requireActivity().getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.medicine_dialog, null);
//    }
}
