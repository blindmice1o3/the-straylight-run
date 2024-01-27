package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.dialogfragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.EspressoShot;

public class EspressoShotControlDialogFragment extends DialogFragment {
    public static final String TAG = EspressoShotControlDialogFragment.class.getSimpleName();
    public static final String REQUEST_KEY = "espressoShotControl";
    public static final String BUNDLE_KEY_TYPE = "type";
    public static final String BUNDLE_KEY_QUANTITY = "quantity";

    public enum AmountOfWater {RISTRETTO, LONG;}

    public enum AmountOfBean {HALF_DECAF, UPDOSED;}

    private Button buttonBlonde, buttonSignature, buttonDecaf;
    private Button buttonSingle, buttonDouble, buttonTriple;

    private RadioGroup radiogroupAmountOfWater, radiogroupAmountOfBean;
    private RadioButton radiobuttonRistretto, radiobuttonLong;
    private RadioButton radiobuttonHalfDecaf, radiobuttonUpdosed;

    private EspressoShot.Type typeSelected = EspressoShot.Type.SIGNATURE;
    private int quantitySelected = 0;

    private AmountOfWater amountOfWaterSelected = null;
    private AmountOfBean amountOfBeanSelected = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.dialogfragment_espresso_shot_control, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        buttonBlonde = view.findViewById(R.id.button_blonde);
        buttonSignature = view.findViewById(R.id.button_signature);
        buttonDecaf = view.findViewById(R.id.button_decaf);
        buttonSingle = view.findViewById(R.id.button_single);
        buttonDouble = view.findViewById(R.id.button_double);
        buttonTriple = view.findViewById(R.id.button_triple);

        buttonBlonde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "BLONDE");
                typeSelected = EspressoShot.Type.BLONDE;
            }
        });
        buttonSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "SIGNATURE");
                typeSelected = EspressoShot.Type.SIGNATURE;
            }
        });
        buttonDecaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "DECAF");
                typeSelected = EspressoShot.Type.DECAF;
            }
        });
        buttonSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "SINGLE");
                quantitySelected = 1;
                dismiss();
            }
        });
        buttonDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "DOUBLE");
                quantitySelected = 2;
                dismiss();
            }
        });
        buttonTriple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "TRIPLE");
                quantitySelected = 3;
                dismiss();
            }
        });

        radiogroupAmountOfWater = view.findViewById(R.id.radiogroup_amount_of_water);
        radiobuttonRistretto = view.findViewById(R.id.radiobutton_ristretto);
        radiobuttonRistretto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "radioButtonRistretto.onClick(View)");
                if (amountOfWaterSelected == AmountOfWater.RISTRETTO) {
                    amountOfWaterSelected = null;
                    radiogroupAmountOfWater.clearCheck();
                } else {
                    amountOfWaterSelected = AmountOfWater.RISTRETTO;
                }
            }
        });
        radiobuttonLong = view.findViewById(R.id.radiobutton_long);
        radiobuttonLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "radiobuttonLong.onClick(View)");
                if (amountOfWaterSelected == AmountOfWater.LONG) {
                    amountOfWaterSelected = null;
                    radiogroupAmountOfWater.clearCheck();
                } else {
                    amountOfWaterSelected = AmountOfWater.LONG;
                }
            }
        });

        radiogroupAmountOfBean = view.findViewById(R.id.radiogroup_amount_of_bean);
        radiobuttonHalfDecaf = view.findViewById(R.id.radiobutton_half_decaf);
        radiobuttonHalfDecaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "radiobuttonHalfDecaf.onClick(View)");
                if (amountOfBeanSelected == AmountOfBean.HALF_DECAF) {
                    amountOfBeanSelected = null;
                    radiogroupAmountOfBean.clearCheck();
                } else {
                    amountOfBeanSelected = AmountOfBean.HALF_DECAF;
                }
            }
        });
        radiobuttonUpdosed = view.findViewById(R.id.radiobutton_updosed);
        radiobuttonUpdosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "radiobuttonUpdosed.onClick(View)");
                if (amountOfBeanSelected == AmountOfBean.UPDOSED) {
                    amountOfBeanSelected = null;
                    radiogroupAmountOfBean.clearCheck();
                } else {
                    amountOfBeanSelected = AmountOfBean.UPDOSED;
                }
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.e(TAG, "onDismiss()");

        sendBackResult();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.e(TAG, "onCancel()");
    }

    private void sendBackResult() {
        Log.e(TAG, "sendBackResult()");

        Bundle result = new Bundle();
        result.putSerializable(BUNDLE_KEY_TYPE, typeSelected);
        result.putInt(BUNDLE_KEY_QUANTITY, quantitySelected);
        getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
    }
}
