package feedsubscriber.auth.jose;

import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.KeyPair;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
import javax.crypto.SecretKey;

/**
 * Utility class for generating JSON Web Key Sets (JWKS).
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public final class Jwks {
  private Jwks() {
  }

  /**
   * Generates a new RSA JSON Web Key (JWK) pair.
   *
   * @return RSAKey object representing the generated key pair.
   */
  public static RSAKey generateRsa() {
    KeyPair keyPair = KeyGeneratorUtils.generateRsaKey();
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    return new RSAKey.Builder(publicKey)
        .privateKey(privateKey)
        .keyID(UUID.randomUUID().toString())
        .build();
  }

  /**
   * Generates a new Elliptic Curve (EC) JSON Web Key (JWK) pair.
   *
   * @return ECKey object representing the generated key pair.
   */
  public static ECKey generateEc() {
    KeyPair keyPair = KeyGeneratorUtils.generateEcKey();
    ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
    ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
    Curve curve = Curve.forECParameterSpec(publicKey.getParams());
    return new ECKey.Builder(curve, publicKey)
        .privateKey(privateKey)
        .keyID(UUID.randomUUID().toString())
        .build();
  }

  /**
   * Generates a new Octet Sequence JSON Web Key (JWK) for a secret key.
   *
   * @return OctetSequenceKey object representing the generated secret key.
   */
  public static OctetSequenceKey generateSecret() {
    SecretKey secretKey = KeyGeneratorUtils.generateSecretKey();
    return new OctetSequenceKey.Builder(secretKey)
        .keyID(UUID.randomUUID().toString())
        .build();
  }
}
