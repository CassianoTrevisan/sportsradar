package com.sportsradar.service;

import com.sportsradar.exception.FailedToFinishMatchException;
import com.sportsradar.exception.FailedToStartMatchException;
import com.sportsradar.exception.FailedToUpdateMatchScoreException;
import com.sportsradar.model.Match;
import com.sportsradar.model.Score;

import java.time.Instant;
import java.util.List;

public interface LiveScoreBoardService {
    void startMatch(String homeTeam, String awayTeam) throws FailedToStartMatchException;

    void startMatch(String homeTeam, String awayTeam, Instant starTime) throws FailedToStartMatchException;

    void updateMatchScore(String homeTeam, String awayTeam, Integer homeTeamScore, Integer awayTeamScore) throws FailedToUpdateMatchScoreException;

    void finishMatch(String homeTeam, String awayTeam) throws FailedToFinishMatchException;

    List<Match> getSummary();

    Score getMatchScore(Match match);
}
