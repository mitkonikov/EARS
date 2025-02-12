package org.um.feri.ears.problems.gp;

import com.google.gson.Gson;
import org.um.feri.ears.individual.generations.gp.GPProgramSolution;
import org.um.feri.ears.individual.generations.gp.GPRampedHalfAndHalf;
import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.individual.generations.gp.GPRandomProgramSolution;
import org.um.feri.ears.operators.gp.*;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.util.Configuration;
import org.um.feri.ears.util.RequestBodyParams;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.List;

public abstract class ProgramProblem extends Problem<ProgramSolution> {

    public static final long serialVersionUID = 2857391887500457501L;

    /**
     * List of base function nodes which can be used during when generating tree individuals
     */
    List<Class<? extends Node>> baseFunctionNodeTypes;

    /**
     * List of base terminal nodes which can be used during when generating tree individuals
     */
    List<Class<? extends Node>> baseTerminalNodeTypes;

    protected Tree.TreeType solutionTreeType;
    protected String treeName; // TODO add support for multiple treeNames in the future

    protected int minTreeDepth;
    protected int maxTreeStartDepth;

    protected int maxTreeEndDepth;
    protected int maxTreeSize;

    protected GPProgramSolution programSolutionGenerator;

    protected  RequestBodyParams requestBodyParams;

    protected String jsonBodyDestFolderPath;

    protected FeasibilityGPOperator[] feasibilityControlOperators;
    protected GPOperator[] bloatControlOperators;

    public ProgramProblem(String name, Tree.TreeType treeType){
        this(name, new ArrayList<>(), new ArrayList<>(), 2, 5, 10, 100, new FeasibilityGPOperator[]{}, new GPOperator[]{}, new GPRandomProgramSolution(), treeType, "BtTree", new RequestBodyParams());
    }

    // Constructor with all parameters
    public ProgramProblem(String name, List<Class<? extends Node>> baseFunctionNodeTypes, List<Class<? extends Node>> baseTerminalNodeTypes, int minTreeDepth, int maxTreeStartDepth, int maxTreeEndDepth, int maxTreeSize, FeasibilityGPOperator[] feasibilityControlOperators, GPOperator[] bloatControlOperators, GPProgramSolution programSolutionGenerator, Tree.TreeType treeType, String treeName, RequestBodyParams requestBodyParams) {
        super(name, 1, 1, 0);
        setBaseFunctionNodeTypes(baseFunctionNodeTypes);
        setBaseTerminalNodeTypes(baseTerminalNodeTypes);
        this.solutionTreeType = treeType;
        this.treeName = treeName;

        this.minTreeDepth = minTreeDepth;
        this.maxTreeStartDepth = maxTreeStartDepth;
        this.maxTreeEndDepth = maxTreeEndDepth;
        this.maxTreeSize = maxTreeSize;

        this.feasibilityControlOperators = feasibilityControlOperators;
        this.bloatControlOperators = bloatControlOperators;
        this.programSolutionGenerator = programSolutionGenerator;

        this.requestBodyParams = requestBodyParams;
    }


    public List<Class<? extends Node>> getBaseFunctionNodeTypes() {
        return baseFunctionNodeTypes;
    }

    public void setBaseFunctionNodeTypes(List<Class<? extends Node>> baseFunctionNodeTypes) {
        this.baseFunctionNodeTypes = baseFunctionNodeTypes;
    }

    public void setBaseFunctionNodeTypesFromStringList(List<String> baseFunctionNodeTypes) {
        this.baseFunctionNodeTypes = new ArrayList<>();
        String packagePrefix = "org.um.feri.ears.individual.representations.gp.";
        for (String nodeType : baseFunctionNodeTypes) {
            try {
                this.baseFunctionNodeTypes.add((Class<? extends Node>) Class.forName(packagePrefix + nodeType.trim()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Class<? extends Node>> getBaseTerminalNodeTypes() {
        return baseTerminalNodeTypes;
    }

    public void setBaseTerminalNodeTypes(List<Class<? extends Node>> baseTerminalNodeTypes) {
        this.baseTerminalNodeTypes = baseTerminalNodeTypes;
    }

    public void setBaseTerminalNodeTypesFromStringList(List<String> baseTerminalNodeTypes) {
        this.baseTerminalNodeTypes = new ArrayList<>();
        String packagePrefix = "org.um.feri.ears.individual.representations.gp.";
        for (String nodeType : baseTerminalNodeTypes) {
            try {
                this.baseTerminalNodeTypes.add((Class<? extends Node>) Class.forName(packagePrefix + nodeType.trim()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Class<? extends  Node> selectRandomNodeType(){
        ArrayList<Class<? extends  Node>> nodeTypes = new ArrayList<>();
        nodeTypes.addAll(baseFunctionNodeTypes);
        nodeTypes.addAll(baseTerminalNodeTypes);

        return nodeTypes.get(RNG.nextInt(nodeTypes.size()));
    }

    public Tree.TreeType getSolutionTreeType() {
        return solutionTreeType;
    }

    public int getMaxTreeStartDepth() {
        return maxTreeStartDepth;
    }

    public int getMaxTreeEndDepth() {
        return maxTreeEndDepth;
    }


    public void setMaxTreeStartDepth(int maxTreeStartDepth) {
        this.maxTreeStartDepth = maxTreeStartDepth;
    }

    public void setMaxTreeEndDepth(int maxTreeEndDepth) {
        this.maxTreeEndDepth = maxTreeEndDepth;
    }


    public int getMinTreeDepth() {
        return minTreeDepth;
    }

    public void setMinTreeDepth(int minTreeDepth) {
        this.minTreeDepth = minTreeDepth;
    }

    public int getMaxTreeSize() {
        return maxTreeSize;
    }

    public void setMaxTreeSize(int maxTreeSize) {
        this.maxTreeSize = maxTreeSize;
    }

    public GPProgramSolution getProgramSolutionGenerator() {
        return programSolutionGenerator;
    }

    public void setProgramSolutionGenerator(GPProgramSolution programSolutionGenerator) {
        this.programSolutionGenerator = programSolutionGenerator;
    }

    public void setProgramSolutionGenerator(Configuration.InitPopGeneratorMethod initPopGeneratorMethod) {
        if(initPopGeneratorMethod == Configuration.InitPopGeneratorMethod.Random){
            this.programSolutionGenerator = new GPRandomProgramSolution();
        }
        else if(initPopGeneratorMethod == Configuration.InitPopGeneratorMethod.RampedHalfAndHalfMethod){
            this.programSolutionGenerator = new GPRampedHalfAndHalf();
        }
    }

    public void setRequestBodyParams(RequestBodyParams requestBodyParams){
        this.requestBodyParams = requestBodyParams;
    }

    public RequestBodyParams getRequestBodyParams(){
        return requestBodyParams;
    }

    public void setJsonBodyDestFolderPath(String jsonBodyDestFolderPath){
        this.jsonBodyDestFolderPath = jsonBodyDestFolderPath;
    }

    public String getJsonBodyDestFolderPath(){
        return jsonBodyDestFolderPath;
    }

    @Override
    public boolean isFeasible(ProgramSolution solution){
        for(FeasibilityGPOperator operator : feasibilityControlOperators){
            if(operator != null && !operator.isSolutionFeasible(solution, this)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void makeFeasible(ProgramSolution solution){
        for(FeasibilityGPOperator operator : feasibilityControlOperators){
            // For each operator, check if the solution is feasible, if not, execute the operator
            if(operator != null && !operator.isSolutionFeasible(solution, this)){
                operator.execute(solution, this);
            }
        }
    }
    @Override
    public ProgramSolution generateRandomEvaluatedSolution() {
        ProgramSolution solution = generateRandomSolution();
        evaluate(solution);
        return solution;
    }

    @Override
    public ProgramSolution generateRandomSolution() {
        ProgramSolution solution = this.programSolutionGenerator.generate(this, 1, treeName);

        // Checks if the generated solution is feasible for all feasibility control operators. If not, make it feasible
        makeFeasible(solution);

        return solution;
    }

    public Node selectRandomTerminalNode(){
        return this.programSolutionGenerator.generateRandomTerminalNode(this);
    }

    public GPOperator[] getBloatControlOperators() {
        return bloatControlOperators;
    }
    @Override
    public void bulkEvaluate(List<ProgramSolution> solutions) {
        for (ProgramSolution solution : solutions) {
            evaluate(solution);
        }
    }

    public void executeBloatedControlOperators(ProgramSolution solution){
        for(GPOperator operator : bloatControlOperators){
            if(operator != null)
                operator.execute(solution, this);
        }
    }

    public void setFeasibilityControlOperatorsFromStringArray(String[] feasibilityControlOperatorsString){
        this.feasibilityControlOperators = new FeasibilityGPOperator[feasibilityControlOperatorsString.length];
        String packagePrefix = "org.um.feri.ears.operators.gp.";
        Gson gson = new Gson();

        for (int i = 0; i < feasibilityControlOperatorsString.length; i++) {
            try {
                String[] operatorParts = feasibilityControlOperatorsString[i].split("-");
                this.feasibilityControlOperators[i] = (FeasibilityGPOperator) gson.fromJson(operatorParts[1], Class.forName(packagePrefix + operatorParts[0].trim()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setBloatControlOperatorsFromStringArray(String[] bloatControlOperatorsString){
        this.bloatControlOperators = new GPOperator[bloatControlOperatorsString.length];
        String packagePrefix = "org.um.feri.ears.operators.gp.";
        Gson gson = new Gson();

        for (int i = 0; i < bloatControlOperatorsString.length; i++) {
            try {
                String[] operatorParts = bloatControlOperatorsString[i].split("-");
                this.bloatControlOperators[i] = (GPOperator) gson.fromJson(operatorParts[1], Class.forName(packagePrefix + operatorParts[0].trim()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
