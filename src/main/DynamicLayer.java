package com.pigmice.frc.lib.dynamic_auto_builder;

import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;

import java.util.function.Supplier;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

public class DynamicLayer {
    private final String name;
    private final String[] enumValues;

    private GenericEntry headerEntry;

    private SendableChooser<String> optionChooser;

    private LayerBehaviour behaviour;

    private Supplier<Command> command;

    boolean singleOption = false;

    public DynamicLayer(String name, Class<?> enumType) {
        this.name = name;

        var enumObjs = enumType.getEnumConstants();
        this.enumValues = new String[enumObjs.length];

        for (int i = 0; i < enumValues.length; i++) {
            enumValues[i] = enumObjs[i].toString();
        }
    }

    public DynamicLayer(String name, Supplier<Command> command) {
        this.name = name;
        this.enumValues = null;
        this.command = command;
        this.singleOption = true;
    }

    public DynamicLayer addConflict() {
        // TODO: maybe write in abstract class
        return this;
    }

    public DynamicLayer addRequirement() {
        // TODO: maybe write in abstract class
        return this;
    }

    public void createWidgets(int column, ShuffleboardTab tab) {
        // Name and toggle switch (if toggleable)
        headerEntry = tab.add(name, false).withPosition(column, 0)
                .withWidget(BuiltInWidgets.kToggleSwitch).getEntry();

        if (singleOption)
            return;

        optionChooser = new SendableChooser<String>();
        for (var name : enumValues) {
            optionChooser.addOption(name, name);
        }

        tab.add(optionChooser).withPosition(column, 1);
    }

    public LayerBehaviour addBehaviour() {
        behaviour = new LayerBehaviour();
        return behaviour;
    }

    public Command getCommand() {
        if (!headerEntry.getBoolean(false))
            return null;

        if (enumValues == null)
            return command.get();

        if (behaviour != null)
            return behaviour.evaluate(optionChooser.getSelected());
        else
            return null;
    }
}
