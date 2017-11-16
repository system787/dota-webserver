package edu.orangecoastcollege.cs273.controller;

import edu.orangecoastcollege.cs273.model.MatchID;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class QueryExecutorTest {
    private static QueryExecutor mQueryExecutor;

    @BeforeClass
    public static void beforeClass() throws Exception {
        mQueryExecutor = QueryExecutor.getInstance();
    }

    @Test
    public void getQueryMatchTask() throws Exception {
        List<MatchID> matchIDList = new ArrayList<>();
        long[] idList = { 97942759L, 4294967295L, 114611L, 49973220L, 101892699L, 39974939L };

        for (long l : idList) {
            List<MatchID> tempList = mQueryExecutor.getQueryMatchTask(l);


            for (MatchID m : tempList) {
                matchIDList.add(m);
            }
        }

        for (MatchID m : matchIDList) {
            System.out.println(m.toString());
        }
    }

}