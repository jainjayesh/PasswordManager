package jpass.xml.bind;

import java.util.Date;

/**
 * <p>
 * Java class for entry complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="entry"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="title"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value=".*\S.*"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="user" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="notes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
public class Entry {

    protected String title;
    protected String url;
    protected String user;
    protected String password;
    protected String notes;
    protected Date modifiedDate;
    protected Date lastPasswordChanged;
    protected int changePasswordInDays;
    protected boolean isModified;
    protected boolean isPasswordChanged;

    /**
	 * @return the isModified
	 */
	public boolean isModified() {
		return isModified;
	}

	/**
	 * @param isModified the isModified to set
	 */
	public void setModified(boolean isModified) {
		this.isModified = isModified;
	}

	/**
     * Gets the value of the title property.
     *
     * @return possible object is {@link String}
     *
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     *
     * @param value allowed object is {@link String}
     *
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the url property.
     *
     * @return possible object is {@link String}
     *
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     *
     * @param value allowed object is {@link String}
     *
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Gets the value of the user property.
     *
     * @return possible object is {@link String}
     *
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     *
     * @param value allowed object is {@link String}
     *
     */
    public void setUser(String value) {
        this.user = value;
    }

    /**
     * Gets the value of the password property.
     *
     * @return possible object is {@link String}
     *
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     *
     * @param value allowed object is {@link String}
     *
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the notes property.
     *
     * @return possible object is {@link String}
     *
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the value of the notes property.
     *
     * @param value allowed object is {@link String}
     *
     */
    public void setNotes(String value) {
        this.notes = value;
    }

	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the lastPasswordChanged
	 */
	public Date getLastPasswordChanged() {
		return lastPasswordChanged;
	}

	/**
	 * @param lastPasswordChanged the lastPasswordChanged to set
	 */
	public void setLastPasswordChanged(Date lastPasswordChanged) {
		this.lastPasswordChanged = lastPasswordChanged;
	}

	/**
	 * @return the changePasswordInDays
	 */
	public int getChangePasswordInDays() {
		return changePasswordInDays;
	}

	/**
	 * @param changePasswordInDays the changePasswordInDays to set
	 */
	public void setChangePasswordInDays(int changePasswordInDays) {
		this.changePasswordInDays = changePasswordInDays;
	}

	/**
	 * @return the isPasswordChanged
	 */
	public boolean isPasswordChanged() {
		return isPasswordChanged;
	}

	/**
	 * @param isPasswordChanged the isPasswordChanged to set
	 */
	public void setPasswordChanged(boolean isPasswordChanged) {
		this.isPasswordChanged = isPasswordChanged;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + changePasswordInDays;
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Entry)) {
			return false;
		}
		Entry other = (Entry) obj;
		if (changePasswordInDays != other.changePasswordInDays) {
			return false;
		}
		if (notes == null) {
			if (other.notes != null) {
				return false;
			}
		} else if (!notes.equals(other.notes)) {
			return false;
		}
		if (password == null) {
			if (other.password != null) {
				return false;
			}
		} else if (!password.equals(other.password)) {
			return false;
		}
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!title.equals(other.title)) {
			return false;
		}
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}
		return true;
	}

}
