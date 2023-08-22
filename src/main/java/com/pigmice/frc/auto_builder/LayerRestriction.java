package com.pigmice.frc.auto_builder;

public interface LayerRestriction {
    public boolean evaluate();

    public void initialize(int shuffleboardColumn);

    public class Toggleable implements LayerRestriction {

        @Override
        public void initialize(int shuffleboardColumn) {

        }

        @Override
        public boolean evaluate() {
            return false;
        }
    }
}
