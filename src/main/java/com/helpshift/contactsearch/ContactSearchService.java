package com.helpshift.contactsearch;

import com.helpshift.contactsearch.client.RequestHandler;
import com.helpshift.contactsearch.entity.Contact;
import com.helpshift.contactsearch.exception.ContactSearchException;
import com.helpshift.contactsearch.manager.impl.NodeManagerImpl;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by milap.wadhwa.
 */
public class ContactSearchService {

    private RequestHandler requestHandler;

    private ContactSearchService()
    {
        this.requestHandler = new RequestHandler(new NodeManagerImpl());
    }

    public static void main(String args[])
    {
        Scanner scanner = new Scanner(System.in);
        ContactSearchService service = new ContactSearchService();
            while (true) {
                try {
                System.out.println("Enter Option :: ");
                System.out.println("1) Add Contact, 2) Search Contact, 3) Exit");
                String input = scanner.nextLine();
                switch (input) {
                    case "1":
                        System.out.println("Enter Name :: ");
                        service.requestHandler.insert(scanner.nextLine());
                        break;
                    case "2":
                        System.out.println("Enter text to be searched :: ");
                        Set<Contact> contacts = service.requestHandler.search(scanner.nextLine());
                        AtomicInteger idx = new AtomicInteger(1);
                        contacts.stream().forEachOrdered(contact -> System.out.println(idx.getAndIncrement()
                            +" "+ contact.getFirstName()+" "+contact.getLastName()));
                        break;
                    case "3":
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Please choose valid option");
                        break;
                }
                }catch (ContactSearchException ex)
                {
                    System.out.println(ex.getMessage()+", Please try again");
                }
            }
    }

}
