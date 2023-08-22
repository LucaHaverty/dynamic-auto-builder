package com.pigmice.frc.auto_builder;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;

import java.util.ArrayList;
import java.util.function.Supplier;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

public class DynamicLayer {
    private final String name;
    private final String[] enumValues;

    private GenericEntry headerEntry;

    private SendableChooser<String> optionChooser;

    private ArrayList<LayerRestriction> restrictions = new ArrayList<LayerRestriction>();
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

    public void createWidgets(int column) {
        headerEntry = DynamicAutoBuilder.SHUFFLEBOARD_TAB.add(name, false).withPosition(column, 0)
                .withWidget(BuiltInWidgets.kToggleSwitch).getEntry();

        System.out.println("Creating " + name + column);

        if (singleOption)
            return;

        System.out.println("Multiple options: " + name);

        optionChooser = new SendableChooser<String>();
        for (var name : enumValues) {
            optionChooser.addOption(name, name);
        }

        DynamicAutoBuilder.SHUFFLEBOARD_TAB.add("Choose Option", optionChooser).withPosition(column, 1);
    }

    public DynamicLayer addRestriction(LayerRestriction restriction) {
        restrictions.add(restriction);
        return this;
    }

    public void setBehaviour(LayerBehaviour behaviour) {
        this.behaviour = behaviour;
    }

    public Command getCommand() {
        for (var restriction : restrictions) {
            if (restriction.evaluate() == false)
                return null;
        }

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
