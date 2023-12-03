package feedsubscriber.auth.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * OAuth2 Client Persistence Extension.
 *
 * @author: ReLive
 * @date: 2022/7/30 22:05
 */
@SuppressWarnings({"JavadocDeclaration", "SqlNoDataSourceInspection", "SameParameterValue",
    "SqlResolve"})
public class JdbcClientRegistrationRepository
    implements ClientRegistrationRepository, Iterable<ClientRegistration> {
  private static final String COLUMN_NAMES =
      "registration_id,client_id,client_secret,client_authentication_method,"
          + "authorization_grant_type,client_name,redirect_uri,scopes,authorization_uri,token_uri,"
          + "jwk_set_uri,issuer_uri,user_info_uri,user_info_authentication_method,"
          + "user_name_attribute_name,configuration_metadata";
  private static final String TABLE_NAME = "oauth2_client_registered";
  private static final String LOAD_CLIENT_REGISTERED_SQL =
      "SELECT " + COLUMN_NAMES + " FROM " + TABLE_NAME;
  private static final String LOAD_CLIENT_REGISTERED_QUERY_SQL =
      LOAD_CLIENT_REGISTERED_SQL + " WHERE ";
  private static final String INSERT_CLIENT_REGISTERED_SQL =
      "INSERT INTO " + TABLE_NAME + "(" + COLUMN_NAMES + ")"
          + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  private static final String UPDATE_CLIENT_REGISTERED_SQL =
      "UPDATE " + TABLE_NAME
          + " SET client_id = ?,client_secret = ?,client_authentication_method = ?,"
          + "authorization_grant_type = ?,client_name = ?,redirect_uri = ?,scopes = ?,"
          + "authorization_uri = ?,token_uri = ?,jwk_set_uri = ?,issuer_uri = ?,user_info_uri = ?,"
          + "user_info_authentication_method = ?,user_name_attribute_name = ?,"
          + "configuration_metadata = ? WHERE registration_id = ?";
  private final JdbcOperations jdbcOperations;
  private final RowMapper<ClientRegistration> clientRegistrationRowMapper;
  private final Function<ClientRegistration, List<SqlParameterValue>>
      clientRegistrationListParametersMapper;

  /**
   * Constructs a new instance of {@code JdbcClientRegistrationRepository}.
   *
   * @param jdbcOperations The JDBC operations for interacting with the database.
   * @throws IllegalArgumentException if {@code jdbcOperations} is {@code null}.
   */
  public JdbcClientRegistrationRepository(JdbcOperations jdbcOperations) {
    Assert.notNull(jdbcOperations, "JdbcOperations cannot be null");
    this.jdbcOperations = jdbcOperations;
    clientRegistrationRowMapper = new ClientRegistrationRowMapper();
    clientRegistrationListParametersMapper = new ClientRegistrationParametersMapper();
  }

  @Override
  public ClientRegistration findByRegistrationId(String registrationId) {
    Assert.hasText(registrationId, "registrationId cannot be empty");
    return findBy("registration_id = ?", registrationId);
  }

  private ClientRegistration findBy(String filter, Object... args) {
    List<ClientRegistration> result = jdbcOperations
        .query(LOAD_CLIENT_REGISTERED_QUERY_SQL + filter, clientRegistrationRowMapper, args);
    return !result.isEmpty() ? result.get(0) : null;
  }

  /**
   * Saves a client registration by either updating an existing one or inserting a new one.
   *
   * @param clientRegistration The client registration to save.
   */
  public void save(ClientRegistration clientRegistration) {
    Assert.notNull(clientRegistration, "clientRegistration cannot be null");
    ClientRegistration existingClientRegistration =
        findByRegistrationId(clientRegistration.getRegistrationId());
    if (existingClientRegistration != null) {
      updateRegisteredClient(clientRegistration);
    } else {
      insertClientRegistration(clientRegistration);
    }
  }

  private void updateRegisteredClient(ClientRegistration clientRegistration) {
    List<SqlParameterValue> parameterValues =
        clientRegistrationListParametersMapper.apply(clientRegistration);
    PreparedStatementSetter statementSetter =
        new ArgumentPreparedStatementSetter(parameterValues.toArray());
    jdbcOperations.update(UPDATE_CLIENT_REGISTERED_SQL, statementSetter);
  }

  private void insertClientRegistration(ClientRegistration clientRegistration) {
    List<SqlParameterValue> parameterValues =
        clientRegistrationListParametersMapper.apply(clientRegistration);
    PreparedStatementSetter statementSetter =
        new ArgumentPreparedStatementSetter(parameterValues.toArray());
    jdbcOperations.update(INSERT_CLIENT_REGISTERED_SQL, statementSetter);
  }

  /**
   * Retrieves a list of all client registrations stored in the database.
   *
   * @return A list of client registrations, or an empty list if none are found.
   */
  public List<ClientRegistration> findAny() {
    List<ClientRegistration> result = jdbcOperations
        .query(LOAD_CLIENT_REGISTERED_SQL, clientRegistrationRowMapper);
    return result.isEmpty() ? Collections.emptyList() : result;
  }

  @NotNull
  public Iterator<ClientRegistration> iterator() {
    return findAny().iterator();
  }

  /**
   * RowMapper implementation for mapping ResultSet to ClientRegistration.
   */
  public static class ClientRegistrationRowMapper implements RowMapper<ClientRegistration> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructs a new instance of {@code ClientRegistrationRowMapper}.
     * Initializes the ObjectMapper with security modules for deserializing JSON to
     * ClientRegistration objects.
     */
    public ClientRegistrationRowMapper() {
      ClassLoader classLoader = JdbcClientRegistrationRepository.class.getClassLoader();
      List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
      objectMapper.registerModules(securityModules);
    }

    @Override
    public ClientRegistration mapRow(ResultSet rs, int rowNum) throws SQLException {
      Set<String> scopes = StringUtils.commaDelimitedListToSet(rs.getString("scopes"));
      ClientRegistration.Builder builder = ClientRegistration
          .withRegistrationId(rs.getString("registration_id"))
          .clientId(rs.getString("client_id"))
          .clientSecret(rs.getString("client_secret"))
          .clientAuthenticationMethod(
              resolveClientAuthenticationMethod(
                  rs.getString("client_authentication_method")))
          .authorizationGrantType(
              resolveAuthorizationGrantType(
                  rs.getString("authorization_grant_type")))
          .clientName(rs.getString("client_name"))
          .redirectUri(rs.getString("redirect_uri"))
          .scope(scopes)
          .authorizationUri(rs.getString("authorization_uri"))
          .tokenUri(rs.getString("token_uri"))
          .jwkSetUri(rs.getString("jwk_set_uri"))
          .issuerUri(rs.getString("issuer_uri"))
          .userInfoUri(rs.getString("user_info_uri"))
          .userInfoAuthenticationMethod(
              resolveUserInfoAuthenticationMethod(
                  rs.getString("user_info_authentication_method")))
          .userNameAttributeName(rs.getString("user_name_attribute_name"));

      Map<String, Object> configurationMetadata =
          parseMap(rs.getString("configuration_metadata"));
      builder.providerConfigurationMetadata(configurationMetadata);
      return builder.build();
    }

    private static AuthorizationGrantType resolveAuthorizationGrantType(
        String authorizationGrantType
    ) {
      if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue()
          .equals(authorizationGrantType)) {
        return AuthorizationGrantType.AUTHORIZATION_CODE;
      } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()
          .equals(authorizationGrantType)) {
        return AuthorizationGrantType.CLIENT_CREDENTIALS;
      } else {
        return AuthorizationGrantType.REFRESH_TOKEN.getValue()
            .equals(authorizationGrantType)
            ? AuthorizationGrantType.REFRESH_TOKEN :
            new AuthorizationGrantType(authorizationGrantType);
      }
    }

    private static ClientAuthenticationMethod resolveClientAuthenticationMethod(
        String clientAuthenticationMethod
    ) {
      if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue()
          .equals(clientAuthenticationMethod)) {
        return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
      } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue()
          .equals(clientAuthenticationMethod)) {
        return ClientAuthenticationMethod.CLIENT_SECRET_POST;
      } else {
        return ClientAuthenticationMethod.NONE.getValue()
            .equals(clientAuthenticationMethod)
            ? ClientAuthenticationMethod.NONE :
            new ClientAuthenticationMethod(clientAuthenticationMethod);
      }
    }

    private static AuthenticationMethod resolveUserInfoAuthenticationMethod(
        String userInfoAuthenticationMethod
    ) {
      if (AuthenticationMethod.FORM.getValue()
          .equals(userInfoAuthenticationMethod)) {
        return AuthenticationMethod.FORM;
      } else if (AuthenticationMethod.HEADER.getValue()
          .equals(userInfoAuthenticationMethod)) {
        return AuthenticationMethod.HEADER;
      } else {
        return AuthenticationMethod.QUERY.getValue()
            .equals(userInfoAuthenticationMethod)
            ? AuthenticationMethod.QUERY
            : new AuthenticationMethod(userInfoAuthenticationMethod);
      }
    }

    private Map<String, Object> parseMap(String data) {
      try {
        return objectMapper.readValue(data, new TypeReference<>() {
        });
      } catch (Exception var3) {
        throw new IllegalArgumentException(var3.getMessage(), var3);
      }
    }
  }

  /**
   * Mapper implementation for converting ClientRegistration to a list of SQL parameter values.
   * Implements the Function interface to facilitate transformation.
   */
  public static class ClientRegistrationParametersMapper
      implements Function<ClientRegistration, List<SqlParameterValue>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructs a new instance of {@code ClientRegistrationParametersMapper}.
     * Initializes the ObjectMapper with security modules for serializing ClientRegistration
     * objects to SQL parameters.
     */
    public ClientRegistrationParametersMapper() {
      ClassLoader classLoader = JdbcClientRegistrationRepository.class.getClassLoader();
      List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
      objectMapper.registerModules(securityModules);
    }

    @Override
    public List<SqlParameterValue> apply(ClientRegistration clientRegistration) {
      return Arrays.asList(
          new SqlParameterValue(12,
              clientRegistration.getRegistrationId()),
          new SqlParameterValue(12,
              clientRegistration.getClientId()),
          new SqlParameterValue(12,
              clientRegistration.getClientSecret()),
          new SqlParameterValue(12,
              clientRegistration.getClientAuthenticationMethod().getValue()),
          new SqlParameterValue(12,
              clientRegistration.getAuthorizationGrantType().getValue()),
          new SqlParameterValue(12,
              clientRegistration.getClientName()),
          new SqlParameterValue(12,
              clientRegistration.getRedirectUri()),
          new SqlParameterValue(12,
              StringUtils.collectionToCommaDelimitedString(
                  clientRegistration.getScopes())),
          new SqlParameterValue(12,
              clientRegistration
                  .getProviderDetails()
                  .getAuthorizationUri()),
          new SqlParameterValue(12,
              clientRegistration
                  .getProviderDetails()
                  .getTokenUri()),
          new SqlParameterValue(12,
              clientRegistration
                  .getProviderDetails()
                  .getJwkSetUri()),
          new SqlParameterValue(12,
              clientRegistration
                  .getProviderDetails()
                  .getIssuerUri()),
          new SqlParameterValue(12,
              clientRegistration
                  .getProviderDetails()
                  .getUserInfoEndpoint()
                  .getUri()),
          new SqlParameterValue(12,
              clientRegistration
                  .getProviderDetails()
                  .getUserInfoEndpoint()
                  .getAuthenticationMethod()
                  .getValue()),
          new SqlParameterValue(12,
              clientRegistration.getProviderDetails()
                  .getUserInfoEndpoint()
                  .getUserNameAttributeName()),
          new SqlParameterValue(12,
              writeMap(
                  clientRegistration
                      .getProviderDetails()
                      .getConfigurationMetadata())));
    }

    private String writeMap(Map<String, Object> data) {
      try {
        return objectMapper.writeValueAsString(data);
      } catch (Exception var3) {
        throw new IllegalArgumentException(var3.getMessage(), var3);
      }
    }
  }
}
