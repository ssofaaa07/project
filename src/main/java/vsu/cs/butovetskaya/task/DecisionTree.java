package vsu.cs.butovetskaya.task;

import java.util.ArrayList;
import java.util.Arrays;

public class DecisionTree {
    int depthOfTree;
    public int countLeaf;
    public DecisionTreeNode root;
    DecisionTreeNode node = root;

    public static class DecisionTreeNode {
        double[] question;
        double entropy;
        int samples;
        int[] value;
        DecisionTreeNode leftNode;
        DecisionTreeNode rightNode;
        boolean leaf;

        public DecisionTreeNode(double[] question, double entropy, int samples, int[] value) {
            this.question = question;
            this.entropy = entropy;
            this.samples = samples;
            this.value = value;
        }

        public double[] getQuestion() {
            return question;
        }

        public double getEntropy() {
            return entropy;
        }

        public int getSamples() {
            return samples;
        }

        public int[] getValue() {
            return value;
        }

        public void setLeftNode(DecisionTreeNode leftNode) {
            this.leftNode = leftNode;
        }

        public void setRightNode(DecisionTreeNode rightNode) {
            this.rightNode = rightNode;
        }

        public void setLeaf(boolean leaf) {
            this.leaf = leaf;
        }
    }

    public int[] checkResult(double[] ans) {
        double result = -1;
        for (int i = 0; i <= depthOfTree; i++) {
            if (i == 0) {
                node = root;
            }
            if (node.leaf) {
                int maxCount = 0;
                int maxCol = -1;
                for (int j = 0; j < node.value.length; j++) {
                    if (maxCount < node.value[j]) {
                        maxCount = node.value[j];
                        maxCol = j;
                    }
                }
                result = maxCol;
                break;
            }
            if (ans[(int) node.question[0]] <= node.question[1]) {
                node = node.leftNode;
            } else {
                node = node.rightNode;
            }
        }
        return node.value;
    }

    public void print() {
        print(root, "", true);
    }

    private void print(DecisionTreeNode node, String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + node.samples + " " + Arrays.toString(node.value));
        if (node.leftNode != null) {
            print(node.leftNode, prefix + (isTail ? "    " : "│   "), node.rightNode == null);
        }
        if (node.rightNode != null) {
            print(node.rightNode, prefix + (isTail ? "    " : "│   "), true);
        }
    }
}
