package com.sportsradar.service;

import com.sportsradar.exception.FailedToFinishMatchException;
import com.sportsradar.exception.FailedToStartMatchException;
import com.sportsradar.exception.FailedToUpdateMatchScoreException;
import com.sportsradar.model.Match;

import java.util.Set;


public class LiveScoreBoardServiceImpl implements LiveScoreBoardService{

    @Override
    public void startMatch(String homeTeam, String awayTeam) throws FailedToStartMatchException {

    }

    @Override
    public void updateMatchScore(String homeTeam, String awayTeam, Integer homeTeamScore, Integer awayTeamScore) throws FailedToUpdateMatchScoreException {

    }

    @Override
    public void finishMatch(String homeTeam, String awayTeam) throws FailedToFinishMatchException {

    }

    @Override
    public Set<Match> getSummary() {
        return Set.of();
    }
}
