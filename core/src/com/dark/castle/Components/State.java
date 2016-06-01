package com.dark.castle.Components;

import com.artemis.Component;

/**
 * Created by DzzirtNik on 29.05.2016.
 */
public class State extends Component {
    public enum EntityState {
        Stop(4),
        LeftMove(3),
        RightMove(3),
        Jump(2),
        Attack(1),
        Death(0);

        private final int value;
        private EntityState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    public EntityState state = EntityState.Stop;
}
