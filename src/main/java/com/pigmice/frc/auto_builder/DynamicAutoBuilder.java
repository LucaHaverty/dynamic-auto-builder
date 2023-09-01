package com.pigmice.frc.auto_builder;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class DynamicAutoBuilder {
    public static final ShuffleboardTab SHUFFLEBOARD_TAB = Shuffleboard.getTab("Auto Builder");
    private final ArrayList<DynamicLayer> layers = new ArrayList<DynamicLayer>();

    public DynamicAutoBuilder addLayer(DynamicLayer layer, LayerBehaviour behaviour) {
        layer.setBehaviour(behaviour);
        layer.createHeader(layers.size());

        layers.add(layer);
        return this;
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
