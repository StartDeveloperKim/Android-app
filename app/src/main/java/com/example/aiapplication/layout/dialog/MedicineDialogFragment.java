package com.example.aiapplication.layout.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.aiapplication.R;
import com.example.aiapplication.medicine.entity.Medicine;

public class MedicineDialogFragment extends DialogFragment {

    private final Medicine medicine;
    private final MedicineDialogListener listener;

    private ImageView imageView;
    private TextView etName;
    private TextView etCode;
    private TextView etInfo;
    private TextView etDangerInfo;

    public MedicineDialogFragment(Medicine medicine, MedicineDialogListener medicineDialogListener) {
        this.listener = medicineDialogListener;
        this.medicine = medicine;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.medicine_dialog, null);

        imageView = dialogView.findViewById(R.id.medicine_image);
        etName = dialogView.findViewById(R.id.medicine_name);
        etCode = dialogView.findViewById(R.id.medicine_code);
        etInfo = dialogView.findViewById(R.id.medicine_effect);
        etDangerInfo = dialogView.findViewById(R.id.dangerous);

        imageView.setImageBitmap(medicine.byteArrayToBitmap());
        etName.setText(medicine.getName());
        etCode.setText(medicine.getCode());
        etInfo.setText(medicine.getInfo());
        etDangerInfo.setText(medicine.getDangerInfo());

        builder.setTitle("약 정보")
                .setView(dialogView)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.deleteMedicineInfo(medicine);
                        dismiss();
                    }
                });

        return builder.create();
    }
}
