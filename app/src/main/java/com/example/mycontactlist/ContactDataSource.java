package com.example.mycontactlist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;

public class ContactDataSource {

    private SQLiteDatabase database;
    private ContactDBHelper dbHelper;

    public ContactDataSource(Context context) {
        dbHelper = new ContactDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertContact(Contact c){
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put("contactname", c.getContactName());
            initialValues.put("streetaddress", c.getStreetAddress());
            initialValues.put("city", c.getCity());
            initialValues.put("state", c.getState());
            initialValues.put("zipcode", c.getZipCode());
            initialValues.put("phonenumber", c.getPhoneNumber());
            initialValues.put("cellnumber", c.getCellNumber());
            initialValues.put("email", c.geteMail());
            initialValues.put("birthday", String.valueOf(c.getBirthday().getTimeInMillis()));
            didSucceed = database.insert("contact", null, initialValues) > 0;
        }
        catch (Exception e) {
            // Do nothing - will return false if there is an exception.
        }
        return didSucceed;
    }

    public boolean updateContact(Contact c) {
        boolean didSucceed = false;
        try {
            long rowId= c.getContactID();
            ContentValues updateValues = new ContentValues();
            updateValues.put("contactname", c.getContactName());
            updateValues.put("streetaddress", c.getStreetAddress());
            updateValues.put("city", c.getCity());
            updateValues.put("state", c.getState());
            updateValues.put("zipcode", c.getZipCode());
            updateValues.put("phonenumber", c.getPhoneNumber());
            updateValues.put("cellnumber", c.getCellNumber());
            updateValues.put("email", c.geteMail());
            updateValues.put("birthday", String.valueOf(c.getBirthday().getTimeInMillis()));
            didSucceed = database.update("contact", updateValues, "_id=" + rowId, null) > 0;
        }
        catch (Exception e) {
            // Do nothing - will return false if there is an exception.
        }
        return didSucceed;
    }

    public int getLastContactID() {
        int lastId;
        try {
            String query = "Select MAX(_id) from contact";
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }

    public ArrayList<String> getContactName() {
        ArrayList<String> contactNames = new ArrayList<>();
        try {
            String query = "Select contactname from contact";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                contactNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            contactNames = new ArrayList<String>();
        }
        return contactNames;
    }

    public ArrayList<Contact> getContacts(String sortBy, String sortOrder) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        try {
            String query = "SELECT * FROM contact ORDER BY "+ sortBy + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Contact newContact;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newContact = new Contact();
                newContact.setContactID(cursor.getInt(0));
                newContact.setContactName(cursor.getString(1));
                newContact.setStreetAddress(cursor.getString(2));
                newContact.setCity(cursor.getString(3));
                newContact.setState(cursor.getString(4));
                newContact.setZipCode(cursor.getString(5));
                newContact.setPhoneNumber(cursor.getString(6));
                newContact.setCellNumber(cursor.getString(7));
                newContact.seteMail(cursor.getString(8));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
                newContact.setBirthday(calendar);
                contacts.add(newContact);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            contacts = new ArrayList<Contact>();
        }
        return contacts;
    }

    public Contact getSpecificContact(int contactID) {
        Contact contact = new Contact();
        String query = "SELECT * FROM contact WHERE _id =" + contactID;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
                Contact.setContactID(cursor.getInt(0));
                Contact.setContactName(cursor.getString(1));
                Contact.setStreetAddress(cursor.getString(2));
                Contact.setCity(cursor.getString(3));
                Contact.setState(cursor.getString(4));
                Contact.setZipCode(cursor.getString(5));
                Contact.setPhoneNumber(cursor.getString(6));
                Contact.setCellNumber(cursor.getString(7));
                Contact.seteMail(cursor.getString(8));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
                Contact.setBirthday(calendar);

                cursor.close();
        }
        return contact;
    }

}
