/**
 * 
 */
package tirnav.passman.model;

import java.util.Date;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

import tirnav.passman.xml.bind.Entry;

/**
 * @author jainj
 *
 */
public class EntryTableModel extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1629196065486883494L;

	public String[] m_colNames = { "","Title", "User Name", "Modified Date", "Last Password change","Change Password In Days" };

	public Class[] m_colTypes = { Icon.class, String.class,String.class, Date.class, Date.class, Integer.class };

	List<Entry> entryDataList;

	public EntryTableModel(List<Entry> entryDataVector) {
		super();
		this.entryDataList = entryDataVector;
	}

	public int getColumnCount() {
		return m_colNames.length;
	}

	/*public int getRowCount() {
		return null != entryDataList?entryDataList.size():0;
	}*/

	public String getColumnName(int col) {
		return m_colNames[col];
	}

	public Class getColumnClass(int col) {
		return m_colTypes[col];
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	/*public Object getValueAt(int row, int col) {
		Entry data = (Entry) (entryDataList.get(row));

		switch (col) {
		case 0:
			return data.getTitle();
		case 1:
			return data.getUser();
		case 2:
			return data.getModifiedDate();
		case 3:
			return data.getLastPasswordChanged();
		case 4:
			return data.getChangePasswordInDays();
		}

		return new String();
	}*/
}
