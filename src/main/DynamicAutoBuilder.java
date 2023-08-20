package com.pigmice.frc.lib.dynamic_auto_builder;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class DynamicAutoBuilder {
    public static final ShuffleboardTab SHUFFLEBOARD_TAB = Shuffleboard.getTab("Auto Builder");
    private final DynamicLayer[] layers;

    private Command test;

    public DynamicAutoBuilder(DynamicLayer... layers) {
        this.layers = layers;
        test = Commands.print("Test");

        for (int i = 0; i < layers.length; i++) {
            layers[i].createWidgets(i, SHUFFLEBOARD_TAB);
        }
    }

    public void update() {
        // TODO: remove if not needed
    }

    private Command[] getCommands() {
        var commands = new ArrayList<Command>();
        for (var layer : layers) {
            var command = layer.getCommand();
            if (command != null) {
                commands.add(command);
            }
        }
        return commands.toArray(new Command[0]);
    }

    public Command buildFullRoutine() {
        var commands = getCommands();
        if (commands.length != 0)
            return Commands.sequence(commands);
        else
            return new InstantCommand();
    }

    public void buildAndRun() {
        buildFullRoutine().schedule();
    }
}
