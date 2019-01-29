package com.holmal.app.holmal.ui.registrationfragment1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * this class is needed for the color choice in registration
 * enables more rows for radiogroup than one
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

    /**
     * onClickListener
     * @param v
     */
    @Override
    public void onClick(View v) {
        final RadioButton clickedButton = (RadioButton) v;
        if(activeRadionButton != null){
            activeRadionButton.setChecked(false);
        }
        clickedButton.setChecked(true);
        activeRadionButton = clickedButton;
    }

    /**
     * from TableLayout
     * adds the clickListener to every child in our case a radio button
     * @param child
     * @param index
     * @param params
     */
    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        setChildrenOnClickListener((TableRow)child);
    }

    /**
     * from TableLayout
     * adds the clickListener to every child in our case a radio button
     * @param child
     * @param params
     */
    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params){
        super.addView(child, params);
        setChildrenOnClickListener((TableRow) child);
    }

    /**
     * stets the onClickListener on the children of a row
     * @param tableRow
     */
    public void setChildrenOnClickListener(TableRow tableRow){
        final int count = tableRow.getChildCount();
        for(int i = 0; i < count; i++){
            final View view = tableRow.getChildAt(i);
            if(view instanceof RadioButton){
                view.setOnClickListener(this);
            }
        }
    }

    /**
     *
     * @return the clicked button
     */
    public int getCheckedRadioButtonId(){
        if(activeRadionButton != null){
            return activeRadionButton.getId();
        }
        else return -1;
    }
}
