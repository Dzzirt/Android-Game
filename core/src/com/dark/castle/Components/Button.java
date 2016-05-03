package com.dark.castle.Components;

import com.artemis.Component;

/**
 * Created by DzzirtNik on 01.05.2016.
 */
public class Button extends Component{

    public enum State {
        NORMAL,
        PRESSED
    }

    public State state = State.NORMAL;
}
