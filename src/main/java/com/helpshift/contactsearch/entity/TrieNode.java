package com.helpshift.contactsearch.entity;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by milap.wadhwa.
 */

/**
 * Implementation class of {@link Node}
 * @param <E>
 */
public class TrieNode<E> implements Node<E>{

    /**compressed values stored at this node**/
    private String value;
    /**children nodes linked with current node**/
    private Node<E>[] children;
    /**set of entities persisted at current node**/
    private Set<E> entities;

    public TrieNode()
    {
        children = new TrieNode[254];
        entities = new HashSet<E>();
    }

    public Node<E> getChild(char ch)
    {
        return children[ch];
    }

    public void addEntity(E e)
    {
        entities.add(e);
    }

    public void addChild(char ch, Node<E> node)
    {
        children[ch] = node;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Override public boolean deleteChild(char ch) {
        throw new UnsupportedOperationException();
    }

    public Set<E> getEntities()
    {
        return this.entities;
    }

    public void setEntities(Set<E> entities)
    {
        this.entities = entities;
    }

    public Set<E> getAllEntities()
    {
        Set<E> entities = new LinkedHashSet<E>();
        entities.addAll(this.entities);
        for(Node node : children)
        if(node != null)
            entities.addAll(node.getAllEntities());
        return entities;
    }

}
