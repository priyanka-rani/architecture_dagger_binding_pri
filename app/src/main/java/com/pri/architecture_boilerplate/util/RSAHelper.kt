package com.pri.architecture_boilerplate.util

import android.util.Base64
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

/**
 * Created by macuser on 7/6/17.
 */

object RSAHelper {

    fun encryptRSAToString(text: String?): String {
        var encoded = ""
        if(!text.isNullOrBlank()) {
            val encrypted: ByteArray
            try {
                val publicBytes = Base64.decode("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArs4iWsavJd0s4zdT7lDp" +
                        "pJOKm/65wf1vnOm+52QpD52Z6AyJg0bOD8yx+2uFzc9TTDTWpDVKDEymMfBGniE4" +
                        "tbNizZaM2VoDukIPBt9cbYuaaEltuTwMmPlTr89v56S7wQPJrNKf/YBkKA3Fn3Ex" +
                        "/V0zrZe7Qv8WgtBKFt/KfN7c9pqvPPjrV3WLNERfdn+qJUuFLXWQ7355mkDSBY7I" +
                        "Zr+JqhgU9jJstRj3CIwlX7PIZe6cQxhZeDHBe5BK4lmDdQ9UQG+DDDh2fl98ucyu" +
                        "ruhKOOsTaCzP24V5J31CPOgN4x9B7yyOXOKPKrlIeKmXV4ep8oVGlCbNVI8SJdL2" +
                        "gQIDAQAB", Base64.DEFAULT)
                val keySpec = X509EncodedKeySpec(publicBytes)
                val keyFactory = KeyFactory.getInstance("RSA")
                val pubKey = keyFactory.generatePublic(keySpec)
                val cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING") //or try with "RSA"
                cipher.init(Cipher.ENCRYPT_MODE, pubKey)
                encrypted = cipher.doFinal(text.toByteArray())
                encoded = Base64.encodeToString(encrypted, Base64.DEFAULT)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return encoded
    }
}
