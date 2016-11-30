package com.helpshift.contactsearch.manager.impl;

import com.helpshift.contactsearch.entity.Contact;
import com.helpshift.contactsearch.entity.Node;
import com.helpshift.contactsearch.entity.TrieNode;
import com.helpshift.contactsearch.exception.ContactSearchException;
import com.helpshift.contactsearch.manager.NodeManager;
import com.helpshift.contactsearch.util.ContactSearchUtil;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by milap.wadhwa.
 */

/**
 * Implementation of {@link NodeManager}
 */
@Slf4j
public class NodeManagerImpl implements NodeManager<Contact> {

    /** dummy root node. value is always null in this node**/
    private Node<Contact> root;

    public NodeManagerImpl() {
        this.root = new TrieNode<Contact>();
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

    private Node search(Node node, String txt) {
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
                        TrieNode<Contact> newNode = createNode(entity, txt);
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

                        TrieNode<Contact> matchedNode = createNode(null, matchedTxt);
                        child.setValue(storedValueSubstring);
                        TrieNode<Contact> txtToStoreNode = createNode(entity, txtToStore);
                        node.addChild(matchedTxt.charAt(0), matchedNode);
                        matchedNode.addChild(txtToStore.charAt(0), txtToStoreNode);
                        matchedNode.addChild(storedValueSubstring.charAt(0), child);
                    }
                }
            }
        }
        return true;
    }

    private TrieNode<Contact> createNode(Contact entity, String txt) {
        TrieNode<Contact> trieNode = new TrieNode<Contact>();
        if (entity != null)
            trieNode.addEntity(entity);
        trieNode.setValue(txt);
        return trieNode;
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

}
