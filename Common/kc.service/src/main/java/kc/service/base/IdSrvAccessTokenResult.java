package kc.service.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "access_token",
    "expires_in",
    "token_type"
})
public class IdSrvAccessTokenResult implements java.io.Serializable{
	private static final long serialVersionUID = -7662296632146373999L;
	
	@JsonProperty("access_token")
    private String accessToken;
    
	@JsonProperty("expires_in")
    private Integer expiresIn;
    
	@JsonProperty("token_type")
    private String tokenType;
 
    /**
     * 
     * @return
     *     The accessToken
     */
    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * 
     * @param accessToken
     *     The access_token
     */
    @JsonProperty("access_token")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * 
     * @return
     *     The expiresIn
     */
    @JsonProperty("expires_in")
    public Integer getExpiresIn() {
        return expiresIn;
    }

    /**
     * 
     * @param expiresIn
     *     The expires_in
     */
    @JsonProperty("expires_in")
    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     * 
     * @return
     *     The tokenType
     */
    @JsonProperty("token_type")
    public String getTokenType() {
        return tokenType;
    }

    /**
     * 
     * @param tokenType
     *     The token_type
     */
    @JsonProperty("token_type")
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public int hashCode() {
    	int result = super.hashCode();
		result = 31 * result + (accessToken != null ? accessToken.hashCode() : 0 );
		result = 31 * result + expiresIn;
		result = 31 * result + (tokenType != null ? tokenType.hashCode() : 0 );
		return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof IdSrvAccessTokenResult) == false) {
            return false;
        }
        IdSrvAccessTokenResult rhs = ((IdSrvAccessTokenResult) other);
        if (expiresIn != rhs.expiresIn)
			return false;
		if (accessToken != null ? !accessToken.equals(rhs.accessToken) : rhs.accessToken != null)
			return false;
		if (tokenType != null ? !tokenType.equals(rhs.tokenType) : rhs.tokenType != null)
			return false;
		return true;
    }
    
    @Override
    public String toString() {
        return "IdSrvAccessTokenResult{" +
                "access_token='" + accessToken + '\'' +
                ", expires_in=" + expiresIn +
                ", token_type='" + tokenType + '\'' +
                '}';
    }
}

