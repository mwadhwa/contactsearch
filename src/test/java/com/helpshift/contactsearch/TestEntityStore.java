package com.helpshift.contactsearch;

import com.helpshift.contactsearch.entity.Contact;
import com.helpshift.contactsearch.store.EntityStore;
import com.helpshift.contactsearch.exception.ContactSearchException;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by milap.wadhwa.
 */
public class TestEntityStore {

    private EntityStore<Contact> store;

    public TestEntityStore()
    {
        this.store = new EntityStore<>();
    }

    @Test
    public void testInsertText() throws Exception
    {
        String text = "haris";
        assert store.insert(text,new Contact("haris","john"));
    }

    @Test
    public void testSearchText() throws Exception
    {
        String text = "haris";
        testInsertText();
        Set<Contact> contacts = store.search(text);
        assert contacts != null;
        assert contacts.size() == 1;
        Assert.assertEquals("haris",contacts.iterator().next().getFirstName());
        Assert.assertEquals("john",contacts.iterator().next().getLastName());
    }

    /**
     * StoreValues haris , ha
     * Search value : har
     * return value empty lst
     * @throws Exception
     */

    @Test
    public void testSearchText1() throws Exception
    {
        testSearchText();
        store.insert("ha",new Contact("ha",""));
        Set<Contact> contacts = store.search("har");
        assert contacts.size() == 1;
    }

    /**
     * StoreValues haris , ha
     * Search value : ha
     * return value haris, ha
     * @throws Exception
     */

    @Test
    public void testSearchText2() throws Exception
    {
        testSearchText();
        store.insert("ha",new Contact("ha",""));
        Set<Contact> contacts = store.search("ha");
        assert contacts.size() == 2;
    }


    @Test
    public void testSearchText3() throws Exception
    {
        store.insert("Haris",new Contact("Haris","John"));
        store.insert("Haris",new Contact("Hari",""));
        store.insert("Hemant",new Contact("Hemant","Wali"));
        store.insert("Suresh",new Contact("Suresh","Kumar"));
        Set<Contact> contacts = store.search("Ha");
        assert contacts.size() == 2;
    }

    @Test
    public void testSearchText4() throws Exception
    {
        store.insert("Haris",new Contact("Haris","John"));
        store.insert("Haris",new Contact("Hari",""));
        store.insert("Hemant",new Contact("Hemant","Wali"));
        store.insert("Suresh",new Contact("Suresh","Kumar"));
        Set<Contact> contacts = store.search("H");
        assert contacts.size() == 3;
    }

    @Test
    public void testSearchTextDuplicate() throws Exception
    {
        store.insert("Haris",new Contact("Haris","John"));
        store.insert("Haris",new Contact("Hari",""));
        store.insert("Hemant",new Contact("Hemant","Wali"));
        store.insert("Suresh",new Contact("Suresh","Kumar"));
        store.insert("Haris",new Contact("Haris","John"));
        Set<Contact> contacts = store.search("H");
        assert contacts.size() == 3;
    }

    @Test
    public void testSearchText5() throws Exception
    {
        assert store.search("something").size() == 0;

    }

    @Test(expected = ContactSearchException.class)
    public void testEmptySearchString() throws Exception
    {
        store.search("");
    }

    @Test(expected = ContactSearchException.class)
    public void testInvalidInput() throws Exception
    {
        store.insert("",new Contact("",""));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDelete() throws Exception
    {
        store.delete("something");
    }

}
