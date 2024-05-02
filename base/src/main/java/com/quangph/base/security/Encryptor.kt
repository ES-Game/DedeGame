package com.quangph.base.security

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import javax.crypto.Cipher
import javax.crypto.SecretKey

/**
 * Asymmetric encrypt RSA encrypt data with maximum of 4096 bits.
 * Symmetric algorithm support it but from API 23.
 * SOLUTION: API >= 23 use symmetric algorithm. 18 <= API < 23, create symmetric key by default java provider,
 * asymmetric encrypt it, then save it in storage. To encrypt/decrypt, asymmetric decrypt the symmetric key,
 * and then use the key to encrypt/decrypt data
 *
 * Created by QuangPH on 2020-01-16.
 */
class Encryptor(val context: Context) {

    companion object {
        val MASTER_KEY = "MASTER_KEY"
        val ALGORITHM_AES = "AES"
    }

    private val keyStoreWrapper = KeyStoreService(context)

    fun encrypt(data: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return try {
                encryptWithSymmetric(data)
            } catch (t: Throwable) {
                t.printStackTrace()
                data
            }

        } else {
            return try {
                encryptWithDefaultSymmetricKey(data)
            } catch (t: Throwable) {
                t.printStackTrace()
                return data
            }

        }
    }

    fun decrypt(data: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return try {
                decryptWithSymmetric(data)
            } catch (t: Throwable) {
                t.printStackTrace()
                return data
            }
        } else {
            return try {
                decryptWithDefaultSymmetricKey(data)
            } catch (t: Throwable) {
                t.printStackTrace()
                data
            }
        }
    }

    /**
     * From API 23, use symmetric
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun encryptWithSymmetric(data: String): String {
        val masterKey = keyStoreWrapper.getKeystoreSymmetricKey(MASTER_KEY)
        return CipherService(CipherService.TRANSFORMATION_SYMMETRIC).encrypt(data, masterKey, true)
    }

    /**
     * From API 18 - API 23, use asymmetric key to encrypt symmetric key, and store it to share reference.
     * When encrypt/decrypt, using asymmetric key to decrypt symmetric key, after that use encrypt/decrypted symmetric key to decrypt data
     */
    private fun encryptWithDefaultSymmetricKey(data: String): String {
        var encryptedKey = KeyStorage.getEncryptedKey(context)
        if (encryptedKey == null) {
            encryptedKey = createAndStoreDefaultSymmetricKey()
        }
        val asymmetricKey = keyStoreWrapper.getKeyStoreAsymmetricKeyPair(MASTER_KEY)
        val symmetricKey = CipherService(CipherService.TRANSFORMATION_ASYMMETRIC).unWrapKey(encryptedKey,
            ALGORITHM_AES, Cipher.SECRET_KEY, asymmetricKey!!.private) as SecretKey
        return CipherService(CipherService.TRANSFORMATION_SYMMETRIC).encrypt(data, symmetricKey, true)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun decryptWithSymmetric(data: String): String {
        val masterKey = keyStoreWrapper.getKeystoreSymmetricKey(MASTER_KEY)
        return CipherService(CipherService.TRANSFORMATION_SYMMETRIC).decrypt(data, masterKey, true)
    }

    private fun decryptWithDefaultSymmetricKey(data: String): String {
        val encryptedKey = KeyStorage.getEncryptedKey(context)
        val asymmetricKey = keyStoreWrapper.getKeyStoreAsymmetricKeyPair(MASTER_KEY)
        val symmetricKey = CipherService(CipherService.TRANSFORMATION_ASYMMETRIC)
            .unWrapKey(encryptedKey!!, ALGORITHM_AES, Cipher.SECRET_KEY, asymmetricKey!!.private) as SecretKey
        return CipherService(CipherService.TRANSFORMATION_SYMMETRIC).decrypt(data, symmetricKey, true)
    }

    /**
     * Create asymmetric key to encrypt symmetric key. After encrypting symmetric key, store it in shared preference
     */
    private fun createAndStoreDefaultSymmetricKey(): String {
        val symmetricKey = keyStoreWrapper.generateDefaultSymmetricKey()
        val asymmetricKey = keyStoreWrapper.createKeystoreAsymmetricKey(MASTER_KEY)
        val encryptedSymmetricKey = CipherService(CipherService.TRANSFORMATION_ASYMMETRIC)
            .wrapKey(symmetricKey, asymmetricKey.public)
        KeyStorage.saveEncryptedKey(context, encryptedSymmetricKey)
        return encryptedSymmetricKey
    }
}