package com.logsentinel.merkletree.model;

/**
 * Object holding information about a Merkle Tree Hash (MTH)
 *
 * @author Stefan Genchev
 * @author https://stefan.genchev.io/
 * @version 1.0
 */
public class TreeHead {
    private byte[] signedTreeHead;
    private int treeSize;

    public TreeHead(byte[] signedTreeHead, int treeSize) {
        this.signedTreeHead = signedTreeHead;
        this.treeSize = treeSize;
    }

    public byte[] getSignedTreeHead() {
        return signedTreeHead;
    }

    public int getTreeSize() {
        return treeSize;
    }


}
