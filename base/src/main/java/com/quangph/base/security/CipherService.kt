package com.quangph.base.security

import android.util.Base64
import java.lang.IllegalArgumentException
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

/**
 * Created by QuangPH on 2020-01-15.
 */
class CipherService(transformation: String) {

    companion object {
        var TRANSFORMATION_ASYMMETRIC = "RSA/ECB/PKCS1Padding"
        var TRANSFORMATION_SYMMETRIC = "AES/CBC/PKCS7Padding"

    }

    private val IV_SEPARATOR = "_IV_SeParaTor]"

    private val cipher: Cipher = Cipher.getInstance(transformation)

    fun encrypt(data: String, key: Key, useIV: Boolean = false): String {
        cipher.init(Cipher.ENCRYPT_MODE, key)
        var result = ""
        if (useIV) {
            val ivEncode = Base64.encodeToString(cipher.iv, Base64.DEFAULT)
            result = ivEncode + IV_SEPARATOR
        }

        val bytes = cipher.doFinal(data.toByteArray())
        result += Base64.encodeToString(bytes, Base64.DEFAULT)
        return result
    }

    fun decrypt(data: String, key: Key, useIV: Boolean = false) : String {
        var encode: String
        if (useIV) {
            val splits = data.split(IV_SEPARATOR.toRegex())
            if (splits.size != 2) {
                throw IllegalArgumentException("No IV attach to encrypted data. Maybe encrypted data is wrong")
            }

            val ivSpec = IvParameterSpec(Base64.decode(splits[0], Base64.DEFAULT))
            encode = splits[1]
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
        } else {
            encode = data
            cipher.init(Cipher.DECRYPT_MODE, key)
        }

        val encrypted = Base64.decode(encode, Base64.DEFAULT)
        val decrypted = cipher.doFinal(encrypted)
        return String(decrypted)
    }

    fun wrapKey(keyTobeWrapped: Key, keyToWrap: Key): String {
        cipher.init(Cipher.WRAP_MODE, keyToWrap)
        val decoded = cipher.wrap(keyTobeWrapped)
        return Base64.encodeToString(decoded, Base64.DEFAULT)
    }

    fun unWrapKey(wrappedKeyData: String, algorithm: String, wrappedKeyType: Int, keyToUnWrapped: Key) : Key {
        val encryptedKey = Base64.decode(wrappedKeyData, Base64.DEFAULT)
        cipher.init(Cipher.UNWRAP_MODE, keyToUnWrapped)
        return cipher.unwrap(encryptedKey, algorithm, wrappedKeyType)
    }
}