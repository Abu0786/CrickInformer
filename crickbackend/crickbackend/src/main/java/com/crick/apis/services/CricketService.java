package com.crick.apis.services;

import com.crick.apis.entities.Match;

import java.util.List;
import java.util.Map;

public interface CricketService {

    List<Match> getLiveMatchScores();
    List<List<String>> getIccRanking() ;

    List<Match> getAllMatches();



}
