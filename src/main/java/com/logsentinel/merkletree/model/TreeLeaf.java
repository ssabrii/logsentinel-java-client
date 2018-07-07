package com.logsentinel.merkletree.model;

/**
 * Object holding information about a tree leaf in a Merkle tree
 *
 * @author Stefan Genchev
 * @author https://stefan.genchev.io/
 * @version 1.0
 */
public class TreeLeaf {
    private int index;
    private byte[] data;

    public TreeLeaf(int index) {
        this.index = index;
    }

    public TreeLeaf(int index, byte[] data) {
        this.index = index;
        this.data = data;
    }

    public int getIndex() {
        return index;
    }

    public byte[] getData() {
        return data;
    }
}
