package com.example.First_prj.MenuAndSwitchers;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.example.First_prj.ForAllCode.*;

public class Menu extends LinearLayout implements View.OnTouchListener {

    private ItemMenu itemsLayout[];
    private Context context;
    private int itemsLength;
    private MenuStarter menuStarter;

    public Menu(Context context, int mightCode) {
        super(context);
        super.setOrientation(VERTICAL);
        this.context = context;
        int[] sections = getSections(mightCode);
        itemsLength = sections.length;
        menuStarter = new MenuStarter(context);
        LinearLayout scrollableLayout = new LinearLayout(context);
        ScrollView scrollView = new ScrollView(context);
        itemsLayout = new ItemMenu[itemsLength];

        scrollableLayout.setOrientation(VERTICAL);
        for (int i = 0; i < itemsLength; i++) {
            itemsLayout[i] = new ItemMenu(context,Data.getFunctionName(sections[i]));
            scrollableLayout.addView(itemsLayout[i]);
            itemsLayout[i].setOnTouchListener(this);
        }

        scrollView.addView(scrollableLayout);
        setSettingsHead();
        super.addView(scrollView);
    }

    private void selectNewActivity(int pointToIndex) {
        for (int i = 0; i < menuStarter.getActivityCounts(); i++)
            if (itemsLayout[pointToIndex].getStringText().equals(menuStarter.getActivityKey(i)))
                menuStarter.startActivity(i);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        for (int i = 0; i < itemsLength; i++)
            if (view.equals(itemsLayout[i]) && motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                itemsLayout[i].setBlinkedColor();
                selectNewActivity(i);
                return true;
            } else if (view.equals(itemsLayout[i])) {
                itemsLayout[i].setBlinkedColorBack();
                return true;
            }
        return true;
    }

    public void setBack() {
        for (int i = 0; i < itemsLength; i++)
            itemsLayout[i].setBlinkedColorBack();
    }

    public int[] getSections(int mightCode) {
        switch (mightCode) {
            case Constants.TEACHER_CODE:
                return Data.getTeacherSet();
            case Constants.STUDENT_CODE:
                return Data.getStudentSet();
            case Constants.PARENT_CODE:
                return Data.getParentSet();
            case Constants.STUDENT_TEACHER_CODE:
                return Data.getTeacherStudentSet();
            case Constants.ERROR_CODE: {
                //@TODO по какойто причине не смогли вытащить значение.. подумать как обработать ошибку.
            }
            default:{
                return new int[]{1,1};
            }
        }
    }

    private void setSettingsHead() {
        LinearLayout headLayout = new LinearLayout(context);
        headLayout.setOrientation(LinearLayout.HORIZONTAL);
        headLayout.addView(new Icon(context, android.R.drawable.ic_menu_agenda));
        headLayout.addView(new SerifTextView(context, "\tМеню", 22));
        super.addView(headLayout);
        super.addView(new BoldGradientLine(context, 3));
        super.addView(new TransparentEmptyView(context, 15));
    }
}
