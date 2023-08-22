package com.pigmice.frc.auto_builder;

import java.util.HashMap;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;

public interface LayerBehaviour {
    public Command evaluate(String option);

    public void initialize(int columnIndex);

    public class MultiOptionLayer implements LayerBehaviour {
        HashMap<String, Supplier<Command>> optionToCommandSet = new HashMap<String, Supplier<Command>>();

        @Override
        public void initialize(int columnIndex) {
            // TODO Auto-generated method stub

        }

        @Override
        public Command evaluate(String option) {
            if (optionToCommandSet.containsKey(option))
                return optionToCommandSet.get(option).get();
            else
                return null;
        }

        public MultiOptionLayer addCommand(Enum<?> optionName, Supplier<Command> command) {
            optionToCommandSet.put(optionName.toString(), command);
            return this;
        }
    }
}