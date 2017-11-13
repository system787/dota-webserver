package edu.orangecoastcollege.cs273.model;

import java.util.List;

public class MatchDetails {
    private long mMatchID;
    private long mMatchSeqNum;
    private List<MatchDetailPlayer> mMatchDetailPlayerList;
    private boolean mRadiantWin;
    private int mDuration;
    private int mFirstBloodTime;
    private int mLobbyType;
    private int mNumPlayers;
    private int mGameMode;      // 0 - None
                                // 1 - All Pick
                                // 2 - Captain's Mode
                                // 3 - Random Draft
                                // 4 - Single Draft
                                // 5 - All Random
                                // 6 - Intro
                                // 7 - Diretide
                                // 8 - Reverse Captain's Mode
                                // 9 - The Greeviling
                                // 10 - Tutorial
                                // 11 - Mid Only
                                // 12 - Least Played
                                // 13 - New Player Pool
                                // 14 - Compendium Matchmaking
                                // 15 - Co-op vs Bots
                                // 16 - Captains Draft
                                // 18 - Ability Draft
                                // 20 - All Random Deathmatch
                                // 21 - 1v1 Mid Only
                                // 22 - Ranked Matchmaking


    public MatchDetails(long matchID, long matchSeqNum, List<MatchDetailPlayer> matchDetailPlayerList,
                        boolean radiantWin, int duration, int firstBloodTime,
                        int lobbyType, int numPlayers, int gameMode) {
        mMatchID = matchID;
        mMatchSeqNum = matchSeqNum;
        mMatchDetailPlayerList = matchDetailPlayerList;
        mRadiantWin = radiantWin;
        mDuration = duration;
        mFirstBloodTime = firstBloodTime;
        mLobbyType = lobbyType;
        mNumPlayers = numPlayers;
        mGameMode = gameMode;
    }
}
