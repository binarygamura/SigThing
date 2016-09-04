package de.fomad.sigthing.model;

/**
 *
 * @author binary gamura
 */
public class AuthData 
{
    private String accessCode;
    
    private String accessToken;
    
    private String refreshToken;
    
    private long lastTokenRefresh;
    
    private long expiresIn;
    
    public AuthData(){
	
    }

    public long getExpiresIn()
    {
	return expiresIn;
    }

    public void setExpiresIn(long expiresIn)
    {
	this.expiresIn = expiresIn;
    }

    public String getAccessCode()
    {
	return accessCode;
    }

    public void setAccessCode(String accessCode)
    {
	this.accessCode = accessCode;
    }

    public String getAccessToken()
    {
	return accessToken;
    }

    public void setAccessToken(String accessToken)
    {
	this.accessToken = accessToken;
    }

    public String getRefreshToken()
    {
	return refreshToken;
    }

    public void setRefreshToken(String refreshToken)
    {
	this.refreshToken = refreshToken;
    }

    public long getLastTokenRefresh()
    {
	return lastTokenRefresh;
    }

    public void setLastTokenRefresh(long lastTokenRefresh)
    {
	this.lastTokenRefresh = lastTokenRefresh;
    }
    
    public boolean isAccessTokenExpired(){
	return System.currentTimeMillis() > lastTokenRefresh + (expiresIn * 1000l) - 5000l;
    }
    
}
