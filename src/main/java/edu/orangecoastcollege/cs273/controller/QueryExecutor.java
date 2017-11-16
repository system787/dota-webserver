package edu.orangecoastcollege.cs273.controller;

import edu.orangecoastcollege.cs273.api.APIRequest;
import edu.orangecoastcollege.cs273.model.MatchID;

import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryExecutor {

    private static QueryExecutor mInstance;
    private static ScheduledExecutorService mUserService;
    private static ScheduledExecutorService mMatchService;
    private static final String TAG = "QueryExecutor";

    private APIRequest mAPIRequest = new APIRequest();

    public QueryExecutor() {}

    public static QueryExecutor getInstance() {
        if (mInstance == null) {
            mInstance = new QueryExecutor();
        }

        mUserService = Executors.newSingleThreadScheduledExecutor();
        mMatchService = Executors.newSingleThreadScheduledExecutor();

        return mInstance;
    }

    public List<MatchID> getQueryMatchTask(long id32) {
        QueryMatchTask queryMatchTask = new QueryMatchTask();
        queryMatchTask.setUser(id32);
        ScheduledFuture<List<MatchID>> scheduledFuture = mMatchService.schedule(queryMatchTask, 1100, TimeUnit.MILLISECONDS);
        List<MatchID> matchIDList;
        try {
            matchIDList = scheduledFuture.get();
            return matchIDList;
        } catch (InterruptedException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Interrupted exception occurred in getQueryMatchTask", e);
        } catch (ExecutionException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "ExecutionException occurred in getQueryMatchTask", e);
        }
        return null;
    }

    private class QueryMatchTask implements Callable<List<MatchID>> {
        private long userId;
        private final int numMatches = 25;

        public void setUser(long id32) {
            userId = id32;
        }

        @Override
        public List<MatchID> call() throws Exception {
            if (userId == 0) {
                return null;
            }
            return mAPIRequest.getMatches(userId, numMatches);
        }
    }

}
