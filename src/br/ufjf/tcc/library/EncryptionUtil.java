package br.ufjf.tcc.library;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class EncryptionUtil {
	// some random salt
	private static final byte[] SALT = { (byte) 0x21, (byte) 0x21, (byte) 0xF0,
			(byte) 0x55, (byte) 0xC3, (byte) 0x9F, (byte) 0x5A, (byte) 0x75 };

	private final static int ITERATION_COUNT = 31;

	private EncryptionUtil() {
	}

	public static String encode(String input) {
		if (input == null) {
			throw new IllegalArgumentException();
		}
		try {

			KeySpec keySpec = new PBEKeySpec(null, SALT, ITERATION_COUNT);
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT,
					ITERATION_COUNT);

			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
					.generateSecret(keySpec);

			Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

			byte[] enc = ecipher.doFinal(input.getBytes());

			String res = new String(Base64.encodeBase64(enc));
			// escapes for url
			res = res.replace('+', '-').replace('/', '_').replace("%", "%25")
					.replace("\n", "%0A");

			return res;

		} catch (Exception e) {
		}

		return "";

	}

	public static String decode(String token) {
		if (token == null) {
			return null;
		}
		try {

			String input = token.replace("%0A", "\n").replace("%25", "%")
					.replace('_', '/').replace('-', '+');

			byte[] dec = Base64.decodeBase64(input.getBytes());

			KeySpec keySpec = new PBEKeySpec(null, SALT, ITERATION_COUNT);
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT,
					ITERATION_COUNT);

			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
					.generateSecret(keySpec);

			Cipher dcipher = Cipher.getInstance(key.getAlgorithm());
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

			byte[] decoded = dcipher.doFinal(dec);

			String result = new String(decoded);
			return result;

		} catch (Exception e) {
			// use logger in production code
			e.printStackTrace();
		}

		return null;
	}

}