package com.pigmice.frc.lib.dynamic_auto_builder;

import java.util.HashMap;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;

public class LayerBehaviour {
    HashMap<String, Supplier<Command>> optionToCommandSet = new HashMap<String, Supplier<Command>>();

    public LayerBehaviour addCommand(Enum<?> optionName, Supplier<Command> command) {
        optionToCommandSet.put(optionName.toString(), command);
        return this;
    }

    public Command evaluate(String option) {
        if (optionToCommandSet.containsKey(option))
            return optionToCommandSet.get(option).get();
        else
            return null;
    }
}
