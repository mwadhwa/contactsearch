package com.helpshift.contactsearch.client;

import com.helpshift.contactsearch.entity.Contact;
import com.helpshift.contactsearch.exception.ContactSearchException;
import com.helpshift.contactsearch.manager.NodeManager;
import com.helpshift.contactsearch.util.ContactSearchUtil;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * Created by milap.wadhwa.
 */

/**
 * Request Handler responsible for handling client request
 *Invokes underlying storage manager for operations
 */
public class RequestHandler {

    private NodeManager<Contact> nodeManager;

    public RequestHandler(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }

    /**
     * Persist the contact to underlying storage.
     * It will persist 2 times, firstName as key and lastName as key
     * 2 times persistence requires to support lookup based on firstName and lastName
     * seprately.
     * @param name
     */
    public void insert(String name) throws ContactSearchException{
        if(ContactSearchUtil.isNullOrEmptyString(name))
            throw new ContactSearchException("Invalid Input", ContactSearchException.ErrorCode.INVALID_INPUT);
        StringTokenizer token = new StringTokenizer(name," ");
        String firstName = token.hasMoreTokens() ? token.nextToken() : "";
        String lastName = token.hasMoreTokens() ? token.nextToken() : "";
        Contact contact = new Contact(firstName,lastName);
        nodeManager.insert(firstName,contact);
        if(ContactSearchUtil.isNotNullOrEmptyString(lastName))
        nodeManager.insert(lastName,contact);
    }

    public Set<Contact> search(String text) throws ContactSearchException{

        StringTokenizer token = new StringTokenizer(text," ");
        String firstName = token.hasMoreTokens() ? token.nextToken() : "";
        String lastName = token.hasMoreTokens() ? token.nextToken() : "";
        Set<Contact> firstNameSearches = nodeManager.search(firstName);

        if (ContactSearchUtil.isNotNullOrEmptyString(lastName)) {
            Set<Contact> lastNameSearched = nodeManager.search(lastName);
            return firstNameSearches.stream()
                .filter(lastNameSearched::contains)
                .collect(Collectors.toSet());
        }

        return firstNameSearches;
    }

    /**
     * currently throwing {@link UnsupportedOperationException}
     * @param name
     * @throws ContactSearchException
     */
    public void delete(String name) throws ContactSearchException
    {
        throw new UnsupportedOperationException();
    }

}
