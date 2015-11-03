package com.hp.ts.dwf.gss;


/**
 * Container for gss session items.
 * 
 * @author kelly
 */
public class GssItem {

	private String name;
	private Object value;
	private String lastApp;
	private long updateTs;
	
	public GssItem(String name, Object value, String lastApp, long updateTs) {
		this.name = name;
		this.value = value;
		this.lastApp = lastApp;
		this.updateTs = updateTs;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public String getLastApp() {
		return lastApp;
	}
	
	public void setLastApp(String lastApp) {
		this.lastApp = lastApp;
	}
	
	public long getUpdateTs() {
		return updateTs;
	}
	
	public void setUpdateTs(long updateTs) {
		this.updateTs = updateTs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		GssItem other = (GssItem) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public boolean isDeleted() {
		return value == null;
	}

	public void delete() {
		this.value = null;
	}

	@Override
	public String toString() {
		return String.format(
				"GssItem [name=%s, value=%s, lastApp=%s, updateTs=%s]", name,
				value, lastApp, updateTs);
	}
	
}
