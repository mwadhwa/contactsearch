package com.helpshift.contactsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

/**
 * Created by milap.wadhwa.
 */
@AllArgsConstructor
@Getter
public class Contact {

    @NonNull
    private String firstName;
    private String lastName;

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Contact contact = (Contact)o;

        if (!firstName.equals(contact.firstName))
            return false;
        return lastName != null ? lastName.equals(contact.lastName) : contact.lastName == null;

    }

    @Override public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }
}
