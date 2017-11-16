package edu.orangecoastcollege.cs273.controller;

import edu.orangecoastcollege.cs273.api.APIRequest;
import edu.orangecoastcollege.cs273.model.MatchID;
import edu.orangecoastcollege.cs273.model.MatchPlayer;
import edu.orangecoastcollege.cs273.model.User;
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

   // @Test
   //public void getQueryMatchTask() throws Exception {
   //    List<MatchID> matchIDList = new ArrayList<>();
   //    long[] idList = { 97942759L, 4294967295L, 114611L, 49973220L, 101892699L, 39974939L };
   //
   //    for (long l : idList) {
   //        List<MatchID> tempList = mQueryExecutor.scheduleQueryMatchTask(l);


   //        for (MatchID m : tempList) {
   //            matchIDList.add(m);
   //        }
   //    }

   //    for (MatchID m : matchIDList) {
   //        System.out.println(m.toString());
   //    }
   //}

    @Test
    public void testQueryChain() throws Exception {
        long id32 = 114611L;
        APIRequest apiRequest = new APIRequest();
        List<MatchID> matchList = mQueryExecutor.scheduleQueryMatchTask(id32);

        for (MatchID m : matchList) {
            List<MatchPlayer> matchPlayerList = m.getMatchPlayers();
            long[] playerId = new long[matchPlayerList.size()];
            for (int i = 0; i < playerId.length; i++) {
                playerId[i] = apiRequest.convert32to64(matchPlayerList.get(i).getId32());
            }
            List<User> userList = mQueryExecutor.scheduleQueryUserSummaries(playerId);
            for (User u : userList) {
                System.out.println(u.toString());
            }
        }
    }

}