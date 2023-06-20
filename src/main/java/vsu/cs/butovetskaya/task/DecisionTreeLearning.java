package vsu.cs.butovetskaya.task;

import java.util.*;

public class DecisionTreeLearning {

    String[][] trainTable;
    ArrayList<double[]> currentTable;
    ArrayList<double[]> currentTableLeft;
    ArrayList<double[]> currentTableRight;
    double[][] currentInfoGain;
    ArrayList<Integer> listOfClasses;
    double[][] questions;
    int maxDepth = 10000;
    int minSamplesLeaf = 1;
    int maxLeafNodes = 10000;
    public DecisionTree dtree = new DecisionTree();

    public DecisionTreeLearning(String[][] trainTable) {
        this.trainTable = trainTable;
        this.currentTable = new ArrayList<>();
        double[][] arr = new double[trainTable.length - 1][trainTable[0].length];
        for (int i = 0; i < trainTable.length - 1; i++) {
            for (int j = 0; j < trainTable[0].length; j++) {
                arr[i][j] = Double.parseDouble(String.valueOf(trainTable[i+1][j]));
            }
        }
        currentTable.addAll(Arrays.asList(arr));
        createListOfClasses();
    }

    public DecisionTreeLearning(String[][] trainTable, int maxDepth, int minSamplesLeaf, int maxLeafNodes) {
        this.trainTable = trainTable;
        this.currentTable = new ArrayList<>();
        double[][] arr = new double[trainTable.length - 1][trainTable[0].length];
        for (int i = 0; i < trainTable.length - 1; i++) {
            for (int j = 0; j < trainTable[0].length; j++) {
                arr[i][j] = Double.parseDouble(String.valueOf(trainTable[i+1][j]));
            }
        }
        currentTable.addAll(Arrays.asList(arr));
        createListOfClasses();
        this.maxDepth = maxDepth;
        this.minSamplesLeaf = minSamplesLeaf;
        this.maxLeafNodes = maxLeafNodes;
    }

    public void setTrainTable(String[][] trainTable) {
        this.trainTable = trainTable;
    }

    public void findQuestions() {
        if (currentTable.size() > 1) {
            this.questions = new double[currentTable.size() - 1][currentTable.get(0).length - 1];
            double[] sortedArr = new double[currentTable.size()];
            for (int c = 0; c < currentTable.get(0).length - 1; c++) {
                for (int r = 0; r < currentTable.size(); r++) {
                    sortedArr[r] = currentTable.get(r)[c];
                }
                sortedArr = Arrays.stream(sortedArr).sorted().toArray();
                for (int r = 0; r < sortedArr.length - 1; r++) {
                    double value = (sortedArr[r] + sortedArr[r + 1]) / 2;
                    questions[r][c] = value;
                }
                Arrays.fill(sortedArr, '0');
            }
        } else {
            this.questions = new double[1][1];
            questions[0][0] = 0;
        }
    }

    public void createListOfClasses() {
        HashSet<Integer> setOfClasses = new HashSet<>();
        int item = -1;
        for (int i = 1; i < trainTable.length; i++) {
            item = (int) Double.parseDouble(trainTable[i][trainTable[0].length - 1]);
            setOfClasses.add(item);
        }
        listOfClasses = new ArrayList<>();
        listOfClasses.addAll(setOfClasses);
    }

    public double infoGain() {
        return entropy(currentTable) - ((double) currentTableLeft.size() / currentTable.size()) * entropy(currentTableLeft) -
                ((double) currentTableRight.size() / currentTable.size()) * entropy(currentTableRight);
    }

    public void createCurrentTables(int row, int col) {
        currentTableLeft = new ArrayList<>();
        currentTableRight = new ArrayList<>();
        for (int r = 0; r < currentTable.size(); r++) {
            if (currentTable.get(r)[col] <= questions[row][col]) {
                currentTableLeft.add(currentTable.get(r));
            } else {
                currentTableRight.add(currentTable.get(r));
            }
        }

    }

    public double chance(List<double[]> table, double item) {
        int count = 0;
        for (int i = 0; i < table.size(); i++) {
            if (table.get(i)[currentTable.get(0).length - 1] == item) {
                count++;
            }
        }
        return (double) count / (table.size());
    }

    public void compareInfoGain() {
        this.currentInfoGain = new double[currentTable.size() - 1][currentTable.get(0).length - 1];
        for (int r = 0; r < questions.length; r++) {
            for (int c = 0; c < questions[0].length; c++) {
                createCurrentTables(r, c);
                currentInfoGain[r][c] = infoGain();
            }
        }
    }

    public double entropy(List<double[]> table) {
        double entropy = 0;
        for (int i = 0; i < listOfClasses.size(); i++) {
            double p = chance(table, listOfClasses.get(i));
            if (p == 0) {
                entropy += 0;
            } else {
                entropy += p * (Math.log(p) / Math.log(2));
            }
        }
        return -entropy;
    }

    public double[] findMaxEntropy() {
        double maxEntropy = -1;
        int colOfMaxEntropy = -1;
        int rowOfMaxEntropy = -1;
        double queOfMaxEntropy = questions[0][0];
        for (int r = 0; r < currentInfoGain.length; r++) {
            for (int c = 0; c < currentInfoGain[0].length; c++) {
                if (maxEntropy < currentInfoGain[r][c]) {
                    maxEntropy = currentInfoGain[r][c];
                    rowOfMaxEntropy = r;
                    colOfMaxEntropy = c;
                    queOfMaxEntropy = questions[r][c];
                }
            }
        }
        return new double[]{maxEntropy, rowOfMaxEntropy, colOfMaxEntropy, queOfMaxEntropy};
    }

    public ArrayList<Integer> countClasses() {
        ArrayList<Integer> countClasses = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < listOfClasses.size(); i++) {
            for (int r = 0; r < currentTable.size(); r++) {
                if (currentTable.get(r)[currentTable.get(0).length - 1] == listOfClasses.get(i)) {
                    count++;
                }
            }
            countClasses.add(count);
            count = 0;
        }
        return countClasses;
    }

    public void start() {
        this.currentTableLeft = new ArrayList<>();
        this.currentTableRight = new ArrayList<>();
        findQuestions();
    }

    public DecisionTree.DecisionTreeNode createTreeNode() {
        DecisionTree.DecisionTreeNode dtreenode = new DecisionTree.DecisionTreeNode(new double[]{0, 0}, 0, 0, new ArrayList<Integer>());
        this.start();
        ArrayList<Integer> countClasses = countClasses();
        if (!countClasses.contains(0) && currentTable.size() > minSamplesLeaf) {
            compareInfoGain();
            double[] question = findMaxEntropy();
            double ent = question[0];
            int queRow = (int) question[1];
            int queCol = (int) question[2];
            double que = question[3];

            dtreenode = new DecisionTree.DecisionTreeNode(new double[]{queCol, que}, ent, currentTable.size(), countClasses);

            if (trainTable.length - 1 == currentTable.size()) {
                dtree.root = dtreenode;
                dtree.depthOfTree = 0;
            }
            dtree.depthOfTree += 1;
            dtreenode.setLeaf(false);
            ArrayList<double[]> oldCurrentTable = currentTable;
            createCurrentTables(queRow, queCol);
            this.currentTable = currentTableLeft;
            dtreenode.setLeftNode(this.createTreeNode());
            this.currentTable = oldCurrentTable;
            findQuestions();
            createCurrentTables(queRow, queCol);
            this.currentTable = currentTableRight;
            dtreenode.setRightNode(this.createTreeNode());
        } else {
            dtreenode = new DecisionTree.DecisionTreeNode(null, 0, currentTable.size(), countClasses);
            dtreenode.setLeaf(true);
            dtreenode.setLeftNode(null);
            dtreenode.setRightNode(null);
            dtree.countLeaf += 1;
        }
        return dtreenode;
    }

}

