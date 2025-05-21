package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TypingPracticeView extends LinearLayout {
    public static final String TAG = TypingPracticeView.class.getSimpleName();

    private TextView tvCode;
    private EditText etInput;
    private String targetCode;
    private int currentIndex = 0;
    private Handler blinkHandler = new Handler();
    private boolean showCursor = true;

    private boolean finished = false;

    public TypingPracticeView(Context context) {
        super(context);
        init(context);
    }

    public TypingPracticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);

        tvCode = new TextView(context);
        tvCode.setTextSize(16);
        tvCode.setMovementMethod(new ScrollingMovementMethod());
        addView(tvCode, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        etInput = new EditText(context);
        etInput.setBackgroundColor(Color.BLUE);
        etInput.setTextColor(Color.CYAN);
//        etInput.setTypeface(Typeface.MONOSPACE);
        etInput.setCursorVisible(false);
        etInput.setMaxLines(10);
        etInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        addView(etInput, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Input logic
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentIndex = Math.min(s.length(), targetCode.length());
                updateCodeDisplay();
            }
        });

        // Handle delete key
        etInput.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (currentIndex > 0) {
                    currentIndex--;
                    etInput.setText(etInput.getText().subSequence(0, currentIndex));
                    etInput.setSelection(currentIndex);
                    updateCodeDisplay();
                }
                return true;
            }
            return false;
        });

        etInput.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                boolean hasFocus = b;
                if (hasFocus) {
                    startCursorBlink();
                    showKeyboard();
                } else {
                    // stop cursor blink
                    blinkHandler.removeCallbacksAndMessages(null);
                    // hide keyboard
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }

    public void setCode(String code) {
        this.targetCode = code;
        this.currentIndex = 0;
        etInput.setText("");
        updateCodeDisplay();
    }

    private void updateCodeDisplay() {
        if (finished) {
            return;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();

        for (int i = 0; i < targetCode.length(); i++) {
            char targetChar = targetCode.charAt(i);
            Character typedChar = (i < etInput.getText().length()) ? etInput.getText().charAt(i) : null;

            int start = builder.length();
            builder.append(targetChar);

            if (typedChar != null) {
                if (typedChar == targetChar) {
                    builder.setSpan(new ForegroundColorSpan(Color.GREEN), start, start + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    builder.setSpan(new ForegroundColorSpan(Color.RED), start, start + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else if (i == currentIndex && showCursor) {
                builder.setSpan(new BackgroundColorSpan(Color.BLUE), start, start + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new ForegroundColorSpan(Color.YELLOW), start, start + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        tvCode.setText(builder);

        checkForWinner();
    }

    private void checkForWinner() {
        if (etInput.getText().toString().equals(
                targetCode
        )) {
            finished = true;

            tvCode.setBackgroundColor(Color.YELLOW);
            tvCode.setTextColor(Color.GREEN);

            etInput.setBackgroundColor(Color.YELLOW);
            etInput.setTextColor(Color.CYAN);
        }
    }

    public void startCursorBlink() {
        blinkHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showCursor = !showCursor;
                updateCodeDisplay();
                blinkHandler.postDelayed(this, 500);
            }
        }, 500);
    }

    public void showKeyboard() {
        etInput.setFocusableInTouchMode(true);
        etInput.requestFocus();

        postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(etInput, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);
    }
}
