/**
 * 
 */
package it.java.cambrist.assignment.domain;

/**
 * @author mbenedetti
 *
 */
public class UserScore {

	private UserObject userObject;
	private String uuid;
	private Double score;
	
	/**
	 * 
	 */
	public UserScore() {
		// TODO Auto-generated constructor stub
	}

	public UserScore(UserObject userObject, String uuid, Double d) {
		super();
		this.userObject = userObject;
		this.uuid = uuid;
		this.score = d;
	}

	/**
	 * @return the userObject
	 */
	public UserObject getUserObject() {
		return userObject;
	}

	/**
	 * @param userObject the userObject to set
	 */
	public void setUserObject(UserObject userObject) {
		this.userObject = userObject;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the score
	 */
	public Double getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(Double score) {
		this.score = score;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserScore [userObject=" + userObject + ", uuid=" + uuid + ", score=" + score + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserScore other = (UserScore) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

}
