package com.helpshift.contactsearch.client;

import com.helpshift.contactsearch.entity.Contact;
import com.helpshift.contactsearch.store.EntityStore;
import com.helpshift.contactsearch.exception.ContactSearchException;
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

    private EntityStore<Contact> store;

    public RequestHandler(EntityStore<Contact> nodeManager) {
        this.store = nodeManager;
    }

    /**
     * Persist the contact to underlying storage.
     * It will persist 2 times, firstName as key and lastName as key
     * 2 times persistence requires to support lookup based on firstName and lastName
     * seprately.
     * @param name name to be stored
     */
    public void insert(String name) throws ContactSearchException{
        if(ContactSearchUtil.isNullOrEmptyString(name))
            throw new ContactSearchException("Invalid Input", ContactSearchException.ErrorCode.INVALID_INPUT);
        StringTokenizer token = new StringTokenizer(name," ");
        String firstName = token.hasMoreTokens() ? token.nextToken() : "";
        String lastName = token.hasMoreTokens() ? token.nextToken() : "";
        Contact contact = new Contact(firstName,lastName);
        store.insert(firstName,contact);
        if(ContactSearchUtil.isNotNullOrEmptyString(lastName))
            store.insert(lastName,contact);
    }

    public Set<Contact> search(String text) throws ContactSearchException{

        StringTokenizer token = new StringTokenizer(text," ");
        String firstName = token.hasMoreTokens() ? token.nextToken() : "";
        String lastName = token.hasMoreTokens() ? token.nextToken() : "";
        Set<Contact> firstNameSearches = store.search(firstName);

        if (ContactSearchUtil.isNotNullOrEmptyString(lastName)) {
            Set<Contact> lastNameSearched = store.search(lastName);
            /**Bug: In case If there is full name search, firstNameSearch list should have only exact name match**/
            firstNameSearches = filterExactName(firstNameSearches,firstName);
            return firstNameSearches.stream()
                .filter(lastNameSearched::contains)
                .collect(Collectors.toSet());
        }

        return firstNameSearches;
    }

    private Set<Contact> filterExactName(Set<Contact> contacts, String firstName)
    {
        return contacts.stream()
            .filter(contact -> contact.getFirstName().equals(firstName))
            .collect(Collectors.toSet());
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
