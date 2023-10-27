package org.um.feri.ears.individual.btdemo.gp.behaviour;

import org.um.feri.ears.individual.btdemo.gp.*;

import java.util.List;
import java.util.Map;

public class ActionNode extends BehaviourTreeNode {

    public ActionNode(BehaviourTreeNodeType nodeType){
        this(nodeType, null);
    }
    public ActionNode(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return -1;
    }
}