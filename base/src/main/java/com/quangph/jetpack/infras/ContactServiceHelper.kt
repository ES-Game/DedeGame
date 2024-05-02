package com.quangph.jetpack.infras

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.quangph.jetpack.validate.EmailValidate
import com.quangph.jetpack.validate.PhoneNumberValidate

object ContactServiceHelper {
    fun getAllContactFromPhone(context: Context): List<DeviceContact> {
        val listContactInfo = mutableListOf<DeviceContact>()
        val contentResolver = context.applicationContext.contentResolver
        val contactCursor: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
        )

        if (contactCursor?.count ?: 0 > 0) {
            while (contactCursor!!.moveToNext()) {
                val id = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID))
                if (contactCursor.getInt(contactCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val phoneNumer = getPhoneNumber(contentResolver, id)
                    val email = getEmail(contentResolver, id)
                    val name = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val contactInfo = generateContactInfo(name, phoneNumer, email)
                    if (contactInfo != null) {
                        listContactInfo.add(contactInfo)
                    }
                }
            }
        }
        contactCursor?.close()
        return listContactInfo
    }

    private fun getPhoneNumber(contentResolver: ContentResolver, id: String): MutableList<String> {
        var phones = mutableListOf<String>()
        val validatePhone = PhoneNumberValidate()
        val phoneCursor: Cursor? = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                arrayOf(id),
                null
        )
        if (phoneCursor != null && phoneCursor.count > 0 && phoneCursor.moveToFirst()) {
            do {
                val rawPhoneNo = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val numberPhone = rawPhoneNo.replace(" ", "")
                if (numberPhone.isNotEmpty() && validatePhone.isSatisfiedBy(numberPhone.trim { it <= ' ' })) {
                    phones.add(numberPhone)
                }
            } while (phoneCursor.moveToNext())
        }
        phoneCursor?.close()
        return phones
    }

    private fun getEmail(contentResolver: ContentResolver, id: String): MutableList<String> {
        val emails = mutableListOf<String>()
        val validateEmail = EmailValidate()
        val emailCursor: Cursor? = contentResolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                arrayOf(id),
                null
        )
        if (emailCursor != null && emailCursor.count > 0 && emailCursor.moveToFirst()) {
            do {
                var email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                if (validateEmail.isSatisfiedBy(email.trim { it <= ' ' })) {
                    emails.add(email)
                }
            } while (emailCursor.moveToNext())
        }
        emailCursor?.close()
        return emails
    }

    private fun generateContactInfo(name: String, phoneNumbers: MutableList<String>, emails: MutableList<String>): DeviceContact? {
        val contactInfo = DeviceContact()
        contactInfo.displayName = name
        contactInfo.phoneNumberList = phoneNumbers
        contactInfo.emailList = emails
        return contactInfo
    }
}