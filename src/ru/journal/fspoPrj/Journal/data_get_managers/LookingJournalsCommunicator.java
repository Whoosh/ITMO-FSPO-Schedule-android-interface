package ru.journal.fspoPrj.journal.data_get_managers;

import android.app.Activity;
import android.content.Intent;
import ru.journal.fspoPrj.journal.data_get_managers.groups.Group;
import ru.journal.fspoPrj.journal.data_get_managers.groups.GroupLesson;
import ru.journal.fspoPrj.journal.data_get_managers.groups.GroupsList;
import ru.journal.fspoPrj.journal.data_get_managers.groups.GroupsListExecutor;
import ru.journal.fspoPrj.journal.data_get_managers.visits_light.LightVisitExecutor;
import ru.journal.fspoPrj.journal.data_get_managers.visits_light.LightVisits;
import ru.journal.fspoPrj.public_code.Logger;
import ru.journal.fspoPrj.public_code.humans_entity.Student;
import ru.journal.fspoPrj.server_java.server_info.APIQuery;
import ru.journal.fspoPrj.server_java.server_managers.MainExecutor;
import ru.journal.fspoPrj.server_java.server_managers.ServerCommunicator;

public class LookingJournalsCommunicator extends ServerCommunicator {

    public static final int GROUPS_LIST_QUERY = 1;
    public static final int LIGHT_VISITS_QUERY = 2;

    protected static int lastQueryID;

    protected String groupsListKeyQuery;
    protected String lessonListKeyQuery;
    protected String lightVisitsQuery;

    protected GroupsList groupsList;
    protected LightVisits lightVisits;

    public LookingJournalsCommunicator(Activity caller) {
        sendGroupListQuery(caller);
    }

    public void resendLastQuery(Activity caller) {
        super.sendQueryToServer(caller, makeQueryExecutor());
    }

    protected MainExecutor makeQueryExecutor() {
        switch (lastQueryID) {
            case GROUPS_LIST_QUERY: {
                return new GroupsListExecutor(groupsListKeyQuery, lessonListKeyQuery, GROUPS_LIST_QUERY);
            }
            case LIGHT_VISITS_QUERY: {
                return new LightVisitExecutor(lightVisitsQuery, LIGHT_VISITS_QUERY);
            }
        }
        return null;
    }

    public void cacheData(Intent data, int caller) {
        switch (caller) {
            case GROUPS_LIST_QUERY: {
                groupsList = (GroupsList) data.getSerializableExtra(groupsListKeyQuery);
                data.removeExtra(groupsListKeyQuery);
            }
            break;
            case LIGHT_VISITS_QUERY: {
                lightVisits = (LightVisits) data.getSerializableExtra(lightVisitsQuery);
                data.removeExtra(lightVisitsQuery);
            }
        }
    }

    protected void sendGroupListQuery(Activity caller) {
        groupsListKeyQuery = APIQuery.GET_GROUP_LIST.getLink(getToken(), getYearID());
        lessonListKeyQuery = APIQuery.GET_GROUP_JOURNAL.getLink(getToken(), getYearID());
        lastQueryID = GROUPS_LIST_QUERY;
        super.sendQueryToServer(caller, makeQueryExecutor());
    }

    public void sendGroupVisitsLightQuery(Activity caller, GroupLesson lesson) {
        lightVisitsQuery = APIQuery.GET_JOURNAL_VISITS_BY_GROUP_LIGHT
                .getLink(getToken(), getYearID(), lesson.getStringLessonID(), lesson.getStringGroupID());
        lastQueryID = LIGHT_VISITS_QUERY;
        super.sendQueryToServer(caller, makeQueryExecutor());
    }

    public LightVisits getLightVisits() {
        return lightVisits;
    }

    public String[] getSortedGroups() {
        try {
            return groupsList.getSortedGroups();
        } catch (NullPointerException ex) {
            Logger.printError(ex, getClass());
            return new String[0];
        }
    }

    public Student[] getStudents(Group group) {
        return groupsList.getStudents(group.getGroupNumber());
    }

    public GroupLesson[] getLessons(Group group, int semester) {
        return groupsList.getLessons(group.getGroupNumber(), semester);
    }

    public Group getGroup(int groupNumber) {
        return groupsList.getGroup(groupNumber);
    }

    public int getFirstPossiblySemester(Group selectedGroup) {
        return groupsList.getFirstPossiblySemester(selectedGroup);
    }

    public Integer[] getAllSemesters(Group selectedGroup) {
        return groupsList.getAllSemesters(selectedGroup.getGroupNumber());
    }
}
