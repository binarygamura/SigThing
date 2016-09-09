package de.fomad.sigthing.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 *
 * @author binary
 */
public class CharacterInfo {

    @SerializedName("CharacterID")
    private int id;

    @SerializedName("CharacterName")
    private String name;

    @SerializedName("ExpiresOn")
    private Date expires;

    @SerializedName("Scopes")
    private String scopes;

    @SerializedName("TokenType")
    private String tokenType;

    @SerializedName("CharacterOwnerHash")
    private String ownerHash;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Date getExpires() {
	return new Date(expires.getTime());
    }

    public void setExpires(Date expires) {
	this.expires = new Date(expires.getTime());
    }

    public String getScopes() {
	return scopes;
    }

    public void setScopes(String scopes) {
	this.scopes = scopes;
    }

    public String getTokenType() {
	return tokenType;
    }

    public void setTokenType(String tokenType) {
	this.tokenType = tokenType;
    }

    public String getOwnerHash() {
	return ownerHash;
    }

    public void setOwnerHash(String ownerHash) {
	this.ownerHash = ownerHash;
    }

}
