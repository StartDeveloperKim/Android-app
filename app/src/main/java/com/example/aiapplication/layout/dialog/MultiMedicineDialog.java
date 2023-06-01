package com.example.aiapplication.layout.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.aiapplication.R;
import com.example.aiapplication.medicine.dto.MultiMedicineResponse;

import java.util.List;

public class MultiMedicineDialog extends DialogFragment {

    private final List<MultiMedicineResponse> results;

    public MultiMedicineDialog(List<MultiMedicineResponse> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View view = getLayoutInflater().inflate(R.layout.multiple_results_dialog, null);

        builder.setTitle("결과")
                .setView(view);

        LinearLayout resultLayout = view.findViewById(R.id.result_layout);
        ScrollView comparisonScrollView = view.findViewById(R.id.comparison_scroll_view);

        LinearLayout comparisonLayout = new LinearLayout(view.getContext());
        comparisonLayout.setOrientation(LinearLayout.VERTICAL);

        return super.onCreateDialog(savedInstanceState);
    }
}
