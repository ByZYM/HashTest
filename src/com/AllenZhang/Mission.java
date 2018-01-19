/*
 * Copyright (c) 2018 by AllenZhang
 */

package com.AllenZhang;

import java.io.Serializable;

public class Mission implements Serializable {

    private int id;

    private String name;

    private String lastMissionName;

    private String nextMissionName;

    private Boolean missionComplete=true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMissionName() {
        return lastMissionName;
    }

    public void setLastMissionName(Mission lastMission) {
        this.lastMissionName = lastMission.getName();
    }

    public String getNextMissionName() {
        return nextMissionName;
    }

    public void setNextMissionName(Mission nextMission) {
        this.nextMissionName = nextMission.getName();
    }

    public Boolean getMissionComplete() {
        return missionComplete;
    }

    public void setMissionComplete(Boolean missionComplete) {
        this.missionComplete = missionComplete;
    }

    @Override
    public String toString() {
        return "Mission{" + "id=" + id + ", name='" + name + '\'' + ", lastMissionName='" + lastMissionName + '\'' + ", nextMissionName='" + nextMissionName + '\'' + ", missionComplete=" + missionComplete + '}';
    }
}
