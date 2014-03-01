package ru.journal.fspoPrj.journal.looking_journal.elements.main_table;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import ru.journal.fspoPrj.journal.config.Config;
import ru.journal.fspoPrj.journal.data_get_managers.visits_light.LightExercisesInfo;
import ru.journal.fspoPrj.journal.data_get_managers.visits_light.LightVisits;
import ru.journal.fspoPrj.journal.data_get_managers.visits_light.Visit;
import ru.journal.fspoPrj.journal.public_journal_elements.custom_cell.EvolutionCell;
import ru.journal.fspoPrj.public_code.custom_desing_elements.lines.HorizontalLine;
import ru.journal.fspoPrj.public_code.custom_desing_elements.lines.VerticalLine;
import ru.journal.fspoPrj.public_code.humans_entity.Student;

import java.util.HashMap;

public class TableWithMarks extends LinearLayout {

    private Context context;
    private MScrollView scroller;
    private Student[] students;
    private HashMap<Integer, Visit[]> visits;
    private LightExercisesInfo[] lightExercisesInfo;

    public TableWithMarks(Context context) {
        super(context);
        this.context = context;
        setBackgroundColor(Color.BLACK);
        scroller = new MScrollView(context);
    }

    public void createTable(LightVisits lightVisits, Student[] students) {
        super.removeAllViews();
        scroller.removeAllViews();

        this.lightExercisesInfo = lightVisits.getExercisesInfo();
        this.visits = lightVisits.getStudentVisits();
        this.students = students;

        initMatrix();
    }

    private void initMatrix() {
        LinearLayout row = new LinearLayout(context);
        LinearLayout rowStack = new LinearLayout(context);
        rowStack.setOrientation(LinearLayout.VERTICAL);

        LightExercisesInfo.TypeState[] states = LightExercisesInfo.TypeState.values();

        for (Student student : students) {
            Visit[] sVisits = visits.get(student.getIntegerID());
            for (int j = 0; j < sVisits.length; j++) {
                row.addView(new EvolutionCell(context, sVisits[j], states[lightExercisesInfo[j].getType()]));
            }
            rowStack.addView(row);
            row = new LinearLayout(context);
        }

        rowStack.addView(new HorizontalLine(context, Color.BLACK, Config.getJournalEndLineWidth()));
        scroller.addView(rowStack);
        super.addView(scroller);
        super.addView(new VerticalLine(context, Color.BLACK, Config.getJournalEndLineWidth()));
    }

    public void scrollScrollerTo(int x, int y) {
        scroller.scrollTo(x, y);
    }

    public int getScrollerY() {
        return scroller.getScrollY();
    }

    public void restoreState(LightVisits lightVisits, Student[] students) {
        createTable(lightVisits, students);
    }
}
