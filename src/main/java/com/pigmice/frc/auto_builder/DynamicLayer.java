package com.pigmice.frc.auto_builder;

import edu.wpi.first.wpilibj2.command.Command;

import java.util.ArrayList;

public class DynamicLayer {
    private final String name;
    private final String[] enumValues;

    private int column;
    private int currentRow = 1;

    private ArrayList<LayerRestriction> restrictions = new ArrayList<LayerRestriction>();
    private LayerBehaviour behaviour;

    public DynamicLayer(String name, Class<?> enumType) {
        this.name = name;

        var enumObjs = enumType.getEnumConstants();
        this.enumValues = new String[enumObjs.length];

        for (int i = 0; i < enumValues.length; i++) {
            enumValues[i] = enumObjs[i].toString();
        }
    }

    public void createHeader(int column) {
        this.column = column;
        DynamicAutoBuilder.SHUFFLEBOARD_TAB.add(name, false).withPosition(column, 0).getEntry();
    }

    public DynamicLayer addRestriction(LayerRestriction restriction) {
        restrictions.add(restriction);
        currentRow += restriction.initialize(column, currentRow);

        return this;
    }

    public void setBehaviour(LayerBehaviour behaviour) {
        this.behaviour = behaviour;
        currentRow += behaviour.initialize(column, currentRow, enumValues);
    }

    public Command getCommand() {
        for (var restriction : restrictions) {
            if (restriction.evaluate() == false)
                return null;
        }

        if (behaviour != null)
            return behaviour.evaluate();
        else
            return null;
    }
}
