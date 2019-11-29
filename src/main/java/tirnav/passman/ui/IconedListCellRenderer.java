package tirnav.passman.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import tirnav.passman.data.DataModel;
import tirnav.passman.util.IconStorage;
import tirnav.passman.xml.bind.Entry;

/**
 * Cell renderer which puts a favicon in front of a list entry.
 *
 * @author Daniil Bubnov
 */
public class IconedListCellRenderer extends DefaultListCellRenderer implements TableCellRenderer {

	private final IconStorage iconStorage = IconStorage.newInstance();

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component label = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (!iconStorage.isEnabled()) {
            return label;
        }
        Entry entry = DataModel.getInstance().getEntryByTitle(value.toString());
        if (entry != null) {
            ImageIcon icon = iconStorage.getIcon(entry.getUrl());
            if (icon != null) {
                JPanel row = new JPanel(new BorderLayout());
                row.add(label, BorderLayout.CENTER);
                JLabel iconLabel = new JLabel();
                iconLabel.setIcon(icon);
                row.add(iconLabel, BorderLayout.WEST);
                return row;
            }
        }
        return label;
    }

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component label = new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (!iconStorage.isEnabled()) {
            return label;
        }
        Entry entry = DataModel.getInstance().getEntryByTitle(value.toString());
        if (entry != null) {
            ImageIcon icon = iconStorage.getIcon(entry.getUrl());
            if (icon != null) {
                JLabel iconLabel = new JLabel();
                iconLabel.setIcon(icon);
                return iconLabel;
            }
        }
        setToolTipText(value.toString());
        return label;
	}
}
