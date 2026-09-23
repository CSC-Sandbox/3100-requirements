package edu.calpoly.provided;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Course-provided encryption utility for CSC 3100.
 *
 * @author Javier Gonzalez-Sanchez (javiergs)
 * @version 1.0 (2026-09-01)
 */
public final class Encryption {
  private static final String TRANSFORMATION = "AES/GCM/NoPadding";
  private static final byte[] KEY_BYTES = "CSC3100-SECURE!!".getBytes(StandardCharsets.UTF_8);
  private static final int IV_LENGTH = 12;
  private static final int TAG_LENGTH_BITS = 128;
  private static final SecureRandom RANDOM = new SecureRandom();

  /**
   * Encrypts a UTF-8 message and returns a Base64-encoded encrypted representation.
   */
  public static String encrypt(String message) {
    if (message == null) {
      throw new IllegalArgumentException("Message cannot be null.");
    }

    try {
      byte[] iv = new byte[IV_LENGTH];
      RANDOM.nextBytes(iv);

      Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      SecretKeySpec key = new SecretKeySpec(KEY_BYTES, "AES");
      cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BITS, iv));

      byte[] ciphertext = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
      byte[] combined = ByteBuffer.allocate(iv.length + ciphertext.length)
          .put(iv)
          .put(ciphertext)
          .array();

      return Base64.getEncoder().encodeToString(combined);
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException("Unable to encrypt message.", e);
    }
  }

  /**
   * Decrypts a Base64-encoded message produced by {@link #encrypt(String)}.
   */
  public static String decrypt(String encryptedMessage) {
    if (encryptedMessage == null || encryptedMessage.isBlank()) {
      throw new IllegalArgumentException("Encrypted message cannot be empty.");
    }

    try {
      byte[] combined = Base64.getDecoder().decode(encryptedMessage);
      if (combined.length <= IV_LENGTH) {
        throw new IllegalArgumentException("Encrypted message is not in the expected format.");
      }

      ByteBuffer buffer = ByteBuffer.wrap(combined);
      byte[] iv = new byte[IV_LENGTH];
      buffer.get(iv);
      byte[] ciphertext = new byte[buffer.remaining()];
      buffer.get(ciphertext);

      Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      SecretKeySpec key = new SecretKeySpec(KEY_BYTES, "AES");
      cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BITS, iv));

      byte[] plaintext = cipher.doFinal(ciphertext);
      return new String(plaintext, StandardCharsets.UTF_8);
    } catch (IllegalArgumentException e) {
      throw e;
    } catch (GeneralSecurityException e) {
      throw new IllegalArgumentException("Message could not be decrypted.", e);
    }
  }
}
