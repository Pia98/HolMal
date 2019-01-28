package com.holmal.app.holmal.ui.registrationfragment1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * for colourChoice in personalInput
 * look at : https://stackoverflow.com/questions/2381560/how-to-group-a-3x3-grid-of-radio-buttons/2383978
 */

public class RadioGridGroup extends TableLayout implements View.OnClickListener {

    private static final String TAG = RadioGroup.class.getName();
    private RadioButton activeRadionButton;

    /**
     * @param context
     */
    public RadioGridGroup(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public RadioGridGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(View v) {
        final RadioButton clickedButton = (RadioButton) v;
        if(activeRadionButton != null){
            activeRadionButton.setChecked(false);
        }
        clickedButton.setChecked(true);
        activeRadionButton = clickedButton;
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        setChildrenOnClickListener((TableRow)child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params){
        super.addView(child, params);
        setChildrenOnClickListener((TableRow) child);
    }

    public void setChildrenOnClickListener(TableRow tableRow){
        final int count = tableRow.getChildCount();
        for(int i = 0; i < count; i++){
            final View view = tableRow.getChildAt(i);
            if(view instanceof RadioButton){
                view.setOnClickListener(this);
            }
        }
    }

    public int getCheckedRadioButtonId(){
        if(activeRadionButton != null){
            return activeRadionButton.getId();
        }
        else return -1;
    }
}