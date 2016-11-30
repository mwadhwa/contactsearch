package com.helpshift.contactsearch.entity;

import java.util.Set;

/**
 * Created by milap.wadhwa.
 */

/**
 * Interface of underlying data structure used for searching the text
 * @param <E> entity to be stored at node level
 */
public interface Node<E> {
    /**
     * handles adding a child to current node.
     * @param ch - children index
     * @param node - node to be persisted
     */
    void addChild(char ch, Node<E> node);

    /**
     * handles adding entity to current node at the last index of set
     * @param e - entity to be stored at current node
     */
    void addEntity(E e);

    /**
     * getter for all entities stored at current node
     * @return set of entities stored at current node
     */
    Set<E> getAllEntities();

    Node<E> getChild(char ch);

    /**
     * DFS based Deligation Stratergy
     * responsible for fetching all matched entites from himself and Its children
     * @return Set of all Matched Entities from that sub tree
     */
    Set<E> getEntities();

    String getValue();

    void setEntities(Set<E> entities);

    /**
     * Setter method to store compressed value which will be used for
     * later text match
     * @param value - compressed value to be persisted
     */
    void setValue(String value);


}
