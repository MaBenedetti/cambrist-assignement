/**
 * 
 */
package it.java.cambrist.assignment.domain;

/**
 * @author mbenedetti
 *
 */
public class UserObject {

	private transient long recn;
	private String name;
	private long yob;
	private String legalID;
	private String address;
	
	private transient boolean isDuplicate;
	
	/**
	 * 
	 */
	public UserObject() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param recn
	 * @param name
	 * @param yob
	 * @param legalID
	 * @param address
	 */
	public UserObject(long recn, String name, long yob, String legalID, String address) {
		super();
		this.recn = recn;
		this.name = name;
		this.yob = yob;
		this.legalID = legalID;
		this.address = address;
	}

	/**
	 * @return the recn
	 */
	public long getRecn() {
		return recn;
	}

	/**
	 * @param recn the recn to set
	 */
	public void setRecn(long recn) {
		this.recn = recn;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the yob
	 */
	public long getYob() {
		return yob;
	}

	/**
	 * @param yob the yob to set
	 */
	public void setYob(long yob) {
		this.yob = yob;
	}

	/**
	 * @return the legalID
	 */
	public String getLegalID() {
		return legalID;
	}

	/**
	 * @param legalID the legalID to set
	 */
	public void setLegalID(String legalID) {
		this.legalID = legalID;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the isDuplicate
	 */
	public boolean isDuplicate() {
		return isDuplicate;
	}

	/**
	 * @param isDuplicate the isDuplicate to set
	 */
	public void setDuplicate(boolean isDuplicate) {
		this.isDuplicate = isDuplicate;
	}

	@Override
	public String toString() {
		return "UserObject [name=" + name + ", yob=" + yob + ", legalID=" + legalID + ", address=" + address + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((legalID == null) ? 0 : legalID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserObject other = (UserObject) obj;
		if (legalID == null) {
			if (other.legalID != null)
				return false;
		} else if (!legalID.equals(other.legalID))
			return false;
		return true;
	}

}
