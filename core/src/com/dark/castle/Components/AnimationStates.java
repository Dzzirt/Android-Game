package com.dark.castle.Components;

import com.artemis.Component;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DzzirtNik on 29.05.2016.
 */
public class AnimationStates extends Component {
    public class StateData {
        public String name;
        public Boolean isPlaying;
        public Boolean isFlip;

        public StateData(String name, Boolean isPlaying, Boolean isFlip) {
            this.name = name;
            this.isPlaying = isPlaying;
            this.isFlip = isFlip;
        }
    }

    public ArrayList<StateData> priorityList = new ArrayList<StateData>() {
        {
            add(new StateData("Death", false, false));
            add(new StateData("Hurt", false, false));
            add(new StateData("Attack", false, false));
            add(new StateData("Jump", false, false));
            add(new StateData("Slide", false, false));
            add(new StateData("Run", false, false));
            add(new StateData("Stop", true, false));
        }
    };

    public StateData getState(String name) {
        for (StateData data : priorityList) {
            if (data.name.equals(name)) {
                return data;
            }
        }
        return null;
    }
}
