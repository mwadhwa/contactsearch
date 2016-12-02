package com.helpshift.contactsearch.store;

import com.helpshift.contactsearch.entity.Contact;
import com.helpshift.contactsearch.exception.ContactSearchException;
import com.helpshift.contactsearch.util.ContactSearchUtil;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by milap.wadhwa.
 */

/**
 * Entity Store for storing and query entity based on given text.
 * Current version of EntityStore {@version 1.0.0} implements Compressed Tries for Storing and Searching
 * @param <E> Entity to be stored
 */
@Slf4j
public class EntityStore<E> {

    /** dummy root node. value is always null in this node**/
    private Node<Contact> root;

    public EntityStore() {
        this.root = new Node<>();
    }

    /**
     * Search:
     1) Contact is not present
     return empty set
     2) Contact is present with exact match :
     return current tree entities
     * @param txt - text to be searched
     * @return - set of Entities matched
     * @throws ContactSearchException
     */
    public Set<Contact> search(String txt) throws ContactSearchException{

        if(txt != null && txt.isEmpty())
        {
            raiseException("Text to be search is empty",
                ContactSearchException.ErrorCode.EMPTY_TEXT);
        }

        Node node = search(root, txt);
        return node != null ? node.getAllEntities() : new HashSet<>();
    }

    private Node search(
        Node node, String txt) {
        char firstChar = txt.charAt(0);

        if (node == null)
            return null;

        Node<Contact> child = node.getChild(firstChar);
        if (child != null) {
            String storedValue = child.getValue();
            int matchLength = match(storedValue, txt);
            if (matchLength == 0)
                return child;
            else {
                if (matchLength < 0) {
                    String subText = txt.substring(-matchLength);
                    return search(child, subText);
                }
                else {
                    if (txt.length() == matchLength) //text is smaller then storedValue
                    {
                        return child;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Insert can have following 4 cases :
     1) contact is not present
     Simply create a new node with compressed value as name and add to the parent
     2) contact is already present
     In this case, add the entity to already existing node
     3) contact is super set of text which is already present in trie
     e.q. stored contact - "Mi Yang"
     contact to store - "Milap Wadhwa"
     Create a new node, and attach this node with exiting node
     4) contact is partially matching with the text stored
     e.q. stored contact - "Mike John"
     contact to store - "Milap Wadhwa"
     In this case, split the compressed node and create 2 new nodes which points to spited node
     * @param txt - text value for which entity to store
     * @param entity - entity to be stored
     * @return true if insert is successful else false
     * @throws ContactSearchException
     */

    public boolean insert(String txt, Contact entity) throws ContactSearchException{
        if(ContactSearchUtil.isNullOrEmptyString(txt))
        {
            raiseException("Invalid input for insert",
                ContactSearchException.ErrorCode.INVALID_INPUT);
        }
        return insert(root, txt, entity);
    }

    /**
     * Implementation will be almost same as of insert. Currently throwing {@link UnsupportedOperationException}
     * @param txt
     * @return
     * @throws ContactSearchException
     */
    public boolean delete(String txt) throws ContactSearchException {
        throw new UnsupportedOperationException();
    }

    private boolean insert(Node node, String txt, Contact entity) {

        if (txt == null || txt.length() == 0)
            return false;
        char firstChar = txt.charAt(0);

        Node<Contact> child = node.getChild(firstChar);

        if (child == null) //when node not present
        {
            node.addChild(firstChar, createNode(entity, txt));
        }
        else {
            String storedValue = child.getValue();
            int matchLength = match(storedValue, txt);
            if (matchLength == 0) // node already present with same value
            {
                child.addEntity(entity);
            }
            else {
                if (matchLength < 0) //stored value is subset of valueToBeStored
                {
                    String subText = txt.substring(-matchLength);
                    return insert(child, subText, entity);

                }
                else {
                    if (txt.length() == matchLength) //text is smaller then storedValue
                    {
                        Node<Contact> newNode = createNode(entity, txt);
                        node.addChild(firstChar, newNode);
                        char newFirstChild = storedValue.charAt(txt.length());
                        newNode.addChild(newFirstChild, child);
                    }
                    else // child node already holds compressed tries.
                    // and this tri is larger than text to be stored.
                    // this gives gurantee that there is no child present earlier
                    {
                        String storedValueSubstring = storedValue.substring(matchLength);
                        String txtToStore = txt.substring(matchLength);
                        String matchedTxt = txt.substring(0, matchLength);

                        Node<Contact> matchedNode = createNode(null, matchedTxt);
                        child.setValue(storedValueSubstring);
                        Node<Contact> txtToStoreNode = createNode(entity, txtToStore);
                        node.addChild(matchedTxt.charAt(0), matchedNode);
                        matchedNode.addChild(txtToStore.charAt(0), txtToStoreNode);
                        matchedNode.addChild(storedValueSubstring.charAt(0), child);
                    }
                }
            }
        }
        return true;
    }

    private Node<Contact> createNode(Contact entity, String txt) {
        Node<Contact> Node = new Node<Contact>();
        if (entity != null)
            Node.addEntity(entity);
        Node.setValue(txt);
        return Node;
    }

    /**
     * returns 0, +ve , -ve based on below computation If storedValue and valueToBeStored are same
     * returns 0 If stored value is subset of valueToBeStored return (-) length of common substring
     * else return +ve length till value is matched
     */

    private int match(String storedValue, String valueToBeStored) {
        int idx = 0;
        while (storedValue.charAt(idx) == valueToBeStored.charAt(idx)) {
            idx++;
            if (valueToBeStored.length() == idx || storedValue.length() == idx)
                break;
        }

        if (idx == valueToBeStored.length() && valueToBeStored.length() == storedValue.length())
            return 0;
        else {
            if (idx == storedValue.length()) {
                return -(idx);
            }
            else {
                return idx;
            }
        }

    }

    private void raiseException(String message, ContactSearchException.ErrorCode errorCode) throws ContactSearchException
    {
        log.warn(message);
        throw new ContactSearchException(message, errorCode);
    }

    private class Node<E>
    {
        /**compressed values stored at this node**/
        private String value;
        /**children nodes linked with current node**/
        private Node[] children;
        /**set of entities persisted at current node**/
        private Set<E> entities;

        private Node()
        {
            children = new Node[254];
            entities = new HashSet<E>();
        }

        private Node<E> getChild(char ch)
        {
            return children[ch];
        }

        private void addEntity(E e)
        {
            entities.add(e);
        }

        private void addChild(char ch, Node<E> node)
        {
            children[ch] = node;
        }

        private String getValue()
        {
            return value;
        }

        private void setValue(String value)
        {
            this.value = value;
        }

        public boolean deleteChild(char ch) {
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

        private Set<E> getAllEntities()
        {
            Set<E> entities = new LinkedHashSet<E>();
            entities.addAll(this.entities);
            for(Node node : children)
                if(node != null)
                    entities.addAll(node.getAllEntities());
            return entities;
        }

    }

}
