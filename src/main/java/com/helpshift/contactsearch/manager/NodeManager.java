package com.helpshift.contactsearch.manager;

import com.helpshift.contactsearch.exception.ContactSearchException;
import java.util.Set;

/**
 * Created by milap.wadhwa.
 */

/**
 * Interface for layer between client and underlying data structure
 * @param <E>
 */
public interface NodeManager<E> {

    /**
     * Given a txt search for all entities which matches this text.
     * Its a soft search criteria. In case of partial match, this will still return that node
     * @param txt - text to be searched
     * @return - Set of Entities matched the text
     * @throws ContactSearchException - throws Exception in case of Empty Text
     */
    Set<E> search(String txt) throws ContactSearchException;

    /**
     * Insert a given entity with given text. In case, If text already contains entities
     * this will add new entity with existing ones
     * @param txt - text value for which entity to store
     * @param entity - entity to be stored
     * @return - true If insert successful else false
     * @throws ContactSearchException - throws exception in case text is invalid
     */
    boolean insert(String txt, E entity) throws ContactSearchException;

}
