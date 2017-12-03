package edu.orangecoastcollege.cs273.controller;

import edu.orangecoastcollege.cs273.api.APIRequest;
import edu.orangecoastcollege.cs273.model.Hero;
import edu.orangecoastcollege.cs273.model.MatchDetails;
import edu.orangecoastcollege.cs273.model.MatchID;
import edu.orangecoastcollege.cs273.model.User;

import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryExecutor {

    private static QueryExecutor mInstance;

    private static ScheduledExecutorService mUserService;
    private static ScheduledExecutorService mMatchService;
    private static ScheduledExecutorService mMatchDetailService;
    private static APIRequest mAPIRequest;

    private static final String TAG = "QueryExecutor";

    public QueryExecutor() {}

    public static QueryExecutor getInstance(APIRequest apiRequest) {
        if (mInstance == null) {
            mInstance = new QueryExecutor();
        }
        mAPIRequest = apiRequest;
        mUserService = Executors.newSingleThreadScheduledExecutor();
        mMatchService = Executors.newSingleThreadScheduledExecutor();
        mMatchDetailService = Executors.newSingleThreadScheduledExecutor();

        return mInstance;
    }

    public List<MatchID> scheduleQueryMatchTask(long id32) {
        QueryMatchTask queryMatchTask = new QueryMatchTask();
        queryMatchTask.setUser(id32);
        ScheduledFuture<List<MatchID>> scheduledFuture = mMatchService.schedule(queryMatchTask, 1050, TimeUnit.MILLISECONDS);

        try {
            List<MatchID> matchIDList = scheduledFuture.get();
            return matchIDList;
        } catch (InterruptedException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "InterruptedException occurred in scheduleQueryMatchTask", e);
        } catch (ExecutionException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "ExecutionException occurred in scheduleQueryMatchTask", e);
        }
        return null;
    }

    public long scheduleQueryUserId(String vanityName) {
        QueryUserIdTask userIdTask = new QueryUserIdTask();
        userIdTask.setUserVanityName(vanityName);
        ScheduledFuture<Long> scheduledFuture = mUserService.schedule(userIdTask, 1050, TimeUnit.MILLISECONDS);

        try {
            long userId = scheduledFuture.get();
            return userId;
        } catch (InterruptedException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "InterruptedException occurred in scheduleQueryUserId", e);
        } catch (ExecutionException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "ExecutionException occurred in scheduleQueryUserId", e);
        }
        return -1;
    }

    public List<User> scheduleQueryUserSummaries(long[] steamId64) {
        QueryUserSummaries query = new QueryUserSummaries();
        query.setSteamId64Array(steamId64);
        ScheduledFuture<List<User>> scheduledFuture = mUserService.schedule(query, 1050, TimeUnit.MILLISECONDS);

        try {
            List<User> userList = scheduledFuture.get();
            return userList;
        } catch (InterruptedException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "InterruptedException occurred in scheduleQueryUserSummaries", e);
        } catch (ExecutionException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "ExecutionException occurred in scheduleQueryUserSummaries", e);
        }
        return null;
    }

    public List<Hero> scheduleQueryHeroesList() {
        QueryHeroesList query = new QueryHeroesList();
        ScheduledFuture<List<Hero>> scheduledFuture = mMatchService.schedule(query, 1050, TimeUnit.MILLISECONDS);

        try {
            List<Hero> heroList = scheduledFuture.get();
            return heroList;
        } catch (InterruptedException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "InterruptedException occurred in scheduleQueryHeroesList", e);
        } catch (ExecutionException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "ExecutionException occurred in scheduleQueryHeroesList", e);
        }
        return null;
    }

    public List<MatchDetails> scheduleMatchDetailsList(long[] matchId) {
        QueryMatchDetails query = new QueryMatchDetails();
        query.setMatchId(matchId);
        ScheduledFuture<List<MatchDetails>> scheduledFuture = mMatchDetailService.schedule(query, 1050, TimeUnit.MILLISECONDS);

        try {
            List<MatchDetails> matchDetailsList = scheduledFuture.get();
            return matchDetailsList;
        } catch (InterruptedException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "InterruptedException occurred in scheduleMatchDetailsList", e);
        } catch (ExecutionException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "ExecutionException occurred in scheduleMatchDetailsList", e);
        }
        return null;
    }

    /**
     * Classes for Queries
     */

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

    private class QueryUserIdTask implements Callable<Long> {
        private String userVanityName;

        public void setUserVanityName(String vanityName) {
            userVanityName = vanityName;
        }

        @Override
        public Long call() throws Exception {
            if (userVanityName == null) {
                return null;
            }
            return mAPIRequest.get32FromVanity(userVanityName);
        }
    }

    private class QueryUserSummaries implements Callable<List<User>> {
        private long[] steamId64Array;

        public void setSteamId64Array(long[] array) {
            steamId64Array = array;
        }

        @Override
        public List<User> call() throws Exception {
            if (steamId64Array == null) {
                return null;
            }
            return mAPIRequest.getUserSummaries(steamId64Array);
        }
    }

    private class QueryHeroesList implements Callable<List<Hero>> {
        @Override
        public List<Hero> call() throws Exception {
            return mAPIRequest.getAllHeroes();
        }
    }

    private class QueryMatchDetails implements Callable<List<MatchDetails>> {
        private long[] matchId;

        public void setMatchId(long[] matchId) {
            this.matchId = matchId;
        }

        @Override
        public List<MatchDetails> call() throws Exception {
            if (matchId == null) {
                return null;
            }
            return mAPIRequest.getMatchDetails(matchId);
        }
    }
}
