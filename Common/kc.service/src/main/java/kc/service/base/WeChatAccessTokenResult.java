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
public class WeChatAccessTokenResult implements java.io.Serializable{
    private static final long serialVersionUID = -5429222736793133180L;
    
    @JsonProperty("access_token")
    private String accessToken;
    
    @JsonProperty("refresh_token")
    private String refreshToken;
    
    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("scope")
    private String scope;
    
    private String openid;
    private String unionid;
 
    public String getAccess_token() {
        return accessToken;
    }
 
    public void setAccess_token(String access_token) {
        this.accessToken = access_token;
    }
 
    public int getExpires_in() {
        return expiresIn;
    }
 
    public void setExpires_in(int expires_in) {
        this.expiresIn = expires_in;
    }
 
    public String getRefresh_token() {
        return refreshToken;
    }
 
    public void setRefresh_token(String refresh_token) {
        this.refreshToken = refresh_token;
    }
 
    public String getOpenid() {
        return openid;
    }
 
    public void setOpenid(String openid) {
        this.openid = openid;
    }
 
    public String getScope() {
        return scope;
    }
 
    public void setScope(String scope) {
        this.scope = scope;
    }
 
    public String getUnionid() {
        return unionid;
    }
 
    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
 
    @Override
    public int hashCode() {
    	int result = super.hashCode();
		result = 31 * result + (accessToken != null ? accessToken.hashCode() : 0 );
		result = 31 * result + expiresIn;
		result = 31 * result + (refreshToken != null ? refreshToken.hashCode() : 0 );
		result = 31 * result + (openid != null ? openid.hashCode() : 0 );
		return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof WeChatAccessTokenResult) == false) {
            return false;
        }
        WeChatAccessTokenResult rhs = ((WeChatAccessTokenResult) other);
        if (expiresIn != rhs.expiresIn)
			return false;
		if (accessToken != null ? !accessToken.equals(rhs.accessToken) : rhs.accessToken != null)
			return false;
		if (refreshToken != null ? !refreshToken.equals(rhs.refreshToken) : rhs.refreshToken != null)
			return false;
		if (openid != null ? !openid.equals(rhs.openid) : rhs.openid != null)
			return false;
		return true;
    }
    
    @Override
    public String toString() {
        return "WeChatAccessTokenResult{" +
                "access_token='" + accessToken + '\'' +
                ", expires_in=" + expiresIn +
                ", refresh_token='" + refreshToken + '\'' +
                ", openid='" + openid + '\'' +
                ", scope='" + scope + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}

