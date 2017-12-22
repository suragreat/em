package com.suragreat.biz.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.suragreat.base.model.SerializableObject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Score extends SerializableObject {
    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public void setRounds(List<Map<String, Integer>> rounds) {
        this.rounds = rounds;
    }

    public void addPlayer(String player) {
        if (StringUtils.isNoneBlank(player)) {
            players.add(player);
        }
    }

    public List<Map<String, Integer>> getRounds() {
        return rounds;
    }

    @JsonIgnore
    public List<List<Integer>> getData() {
        List<List<Integer>> data = new ArrayList<>();
        getRounds().forEach(round -> {
                    List<Integer> list = new ArrayList<>();
                    getPlayers().forEach(p -> {
                        Integer num = round.get(p);
                        if (num == null) {
                            num = 0;
                        }
                        list.add(num);
                    });
                    data.add(list);
                }
        );
        return data;
    }

    public void addRound(Map<String, Integer> round) {
        if (round != null) {
            rounds.add(round);
        }
    }

    private List<String> players = new ArrayList<>();
    private List<Map<String, Integer>> rounds = new ArrayList<>();

    public void removeRound() {
        int size = rounds.size();
        if (size > 0) {
            rounds.remove(size - 1);
        }
    }

    public void removePlayer(String player) {
        players.remove(player);
    }
}
