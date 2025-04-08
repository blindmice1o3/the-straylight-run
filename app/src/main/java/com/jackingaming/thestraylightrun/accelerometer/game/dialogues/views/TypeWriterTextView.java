package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.views;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeWriterTextView extends AppCompatTextView
        implements Serializable {
    public static final String TAG = TypeWriterTextView.class.getSimpleName();

    public interface TextCompletionListener extends Serializable {
        void onAnimationFinish();
    }

    private TextCompletionListener textCompletionListener;

    private CharSequence sequence;
    private int mIndex;
    private long delay = 150; //default is 150 milliseconds

    private int widthTV = -1;
    private int heightTV = -1;
    private int numberOfLinePerPage;
    private int currentPageIndex = 0;
    private List<String> pages = new ArrayList<>();
    private String currentPage;
    private boolean laidOutPages = false;

    public TypeWriterTextView(Context context) {
        super(context);
    }

    public TypeWriterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    transient private Handler handler = new Handler();
    transient private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (!laidOutPages) {
                // split text into line-size lengths.
                String[] words = sequence.toString().split("\\s+");
                Map<Integer, String> txtSplittedIntoLines = new HashMap<>();
                StringBuilder sb = new StringBuilder();
                int indexLine = 0;

                for (int i = 0; i < words.length; i++) {
                    String lineCurrentWithNextWord = sb.toString() + words[i];

                    int widthWithNextWord = (int) getPaint().measureText(lineCurrentWithNextWord);
                    if (widthWithNextWord <= widthTV) {
                        // Current line WITH new word will fit on line.
                        sb.append(words[i] + " ");

                        if (i == words.length - 1) {
                            txtSplittedIntoLines.put(indexLine, sb.toString());
                        }
                    } else {
                        // Current line WITH new word will NOT fit on line.
                        // Store current line.
                        txtSplittedIntoLines.put(indexLine, sb.toString());
                        // Set-up for next line.
                        indexLine++;
                        sb = new StringBuilder();
                        // Store new word that didn't fit onto this new line.
                        sb.append(words[i] + " ");
                        if (i == words.length - 1) {
                            txtSplittedIntoLines.put(indexLine, sb.toString());
                        }
                    }
                }

                // split line-size lengths into pages.
                sb = new StringBuilder();
                int counterLine = 0;
                for (int i = 0; i < txtSplittedIntoLines.size(); i++) {
                    if (counterLine < numberOfLinePerPage - 1) {
                        sb.append(
                                txtSplittedIntoLines.get(i)
                        );

                        counterLine++;

                        if (i == txtSplittedIntoLines.size() - 1) {
                            pages.add(sb.toString());
                        }
                    } else {
                        pages.add(sb.toString());

                        sb = new StringBuilder();
                        sb.append(
                                txtSplittedIntoLines.get(i)
                        );
                        counterLine = 1;

                        if (i == txtSplittedIntoLines.size() - 1) {
                            pages.add(sb.toString());
                        }
                    }
                }

                for (String page : pages) {
                    Log.e(TAG, "PAGE: " + page);
                }

                currentPage = pages.get(0);

                laidOutPages = true;
            }

            //////////////////////////////////////////////////////////

            setText(currentPage.subSequence(0, mIndex++));
            if (mIndex <= currentPage.length()) {
                handler.postDelayed(runnable, delay);
            } else {
                if (textCompletionListener != null) {
                    currentPageIndex++;

                    if (currentPageIndex == pages.size()) {
                        textCompletionListener.onAnimationFinish();
                    } else {
                        currentPage = pages.get(currentPageIndex);
                        displayTextWithAnimation(currentPage);
                    }
                }
            }
        }
    };

    public void stopAnimation() {
        handler.removeCallbacks(runnable);
    }

    /**
     * Display text with type writer animation
     *
     * @param txt content will be displayed
     */
    public void displayTextWithAnimation(CharSequence txt) {
        sequence = txt;
        mIndex = 0;

        setText("");
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, delay);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure()");

        if (widthTV < 0 && heightTV < 0) {
            widthTV = widthMeasureSpec;
            heightTV = heightMeasureSpec;
            Log.e(TAG, "widthTV, heightTV: " + widthTV + ", " + heightTV);

            Paint.FontMetrics fm = getPaint().getFontMetrics();
            float heightLine = fm.descent - fm.ascent;
            Log.e(TAG, "heightLine: " + heightLine);
            numberOfLinePerPage = (int) (heightTV / heightLine);
            Log.e(TAG, "numberOfLinePerPage: " + numberOfLinePerPage);
        }
    }

    /**
     * Change the delay value with this method
     *
     * @param m
     */
    public void setCharacterDelay(long m) {
        delay = m;
    }

    public void setTextCompletionListener(TextCompletionListener textCompletionListener) {
        this.textCompletionListener = textCompletionListener;
    }
}
