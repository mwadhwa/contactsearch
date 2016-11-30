package com.helpshift.contactsearch;

import com.helpshift.contactsearch.entity.Contact;
import com.helpshift.contactsearch.exception.ContactSearchException;
import com.helpshift.contactsearch.manager.NodeManager;
import com.helpshift.contactsearch.manager.impl.NodeManagerImpl;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by milap.wadhwa.
 */
public class TestNodeManagerImpl {

    private NodeManager<Contact> nodeManager;

    public TestNodeManagerImpl()
    {
        this.nodeManager = new NodeManagerImpl();
    }

    @Test
    public void testInsertText() throws Exception
    {
        String text = "haris";
        assert nodeManager.insert(text,new Contact("haris","john"));
    }

    @Test
    public void testSearchText() throws Exception
    {
        String text = "haris";
        testInsertText();
        Set<Contact> contacts = nodeManager.search(text);
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
        nodeManager.insert("ha",new Contact("ha",""));
        Set<Contact> contacts = nodeManager.search("har");
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
        nodeManager.insert("ha",new Contact("ha",""));
        Set<Contact> contacts = nodeManager.search("ha");
        assert contacts.size() == 2;
    }


    @Test
    public void testSearchText3() throws Exception
    {
        nodeManager.insert("Haris",new Contact("Haris","John"));
        nodeManager.insert("Haris",new Contact("Hari",""));
        nodeManager.insert("Hemant",new Contact("Hemant","Wali"));
        nodeManager.insert("Suresh",new Contact("Suresh","Kumar"));
        Set<Contact> contacts = nodeManager.search("Ha");
        assert contacts.size() == 2;
    }

    @Test
    public void testSearchText4() throws Exception
    {
        nodeManager.insert("Haris",new Contact("Haris","John"));
        nodeManager.insert("Haris",new Contact("Hari",""));
        nodeManager.insert("Hemant",new Contact("Hemant","Wali"));
        nodeManager.insert("Suresh",new Contact("Suresh","Kumar"));
        Set<Contact> contacts = nodeManager.search("H");
        assert contacts.size() == 3;
    }

    @Test
    public void testSearchTextDuplicate() throws Exception
    {
        nodeManager.insert("Haris",new Contact("Haris","John"));
        nodeManager.insert("Haris",new Contact("Hari",""));
        nodeManager.insert("Hemant",new Contact("Hemant","Wali"));
        nodeManager.insert("Suresh",new Contact("Suresh","Kumar"));
        nodeManager.insert("Haris",new Contact("Haris","John"));
        Set<Contact> contacts = nodeManager.search("H");
        assert contacts.size() == 4;
    }

    @Test
    public void testSearchText5() throws Exception
    {
        assert nodeManager.search("something").size() == 0;

    }

    @Test(expected = ContactSearchException.class)
    public void testEmptySearchString() throws Exception
    {
        nodeManager.search("");
    }

    @Test(expected = ContactSearchException.class)
    public void testInvalidInput() throws Exception
    {
        nodeManager.insert("",new Contact("",""));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDelete() throws Exception
    {
        nodeManager.delete("something");
    }

}
