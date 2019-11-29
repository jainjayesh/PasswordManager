/*
 * PasswordManager
 *
 * Copyright (c) 2009-2019 Gabor Bata
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package tirnav.passman.ui;

import static tirnav.passman.ui.MessageDialog.NO_OPTION;
import static tirnav.passman.ui.MessageDialog.YES_NO_CANCEL_OPTION;
import static tirnav.passman.ui.MessageDialog.YES_OPTION;
import static tirnav.passman.ui.MessageDialog.getIcon;
import static tirnav.passman.ui.MessageDialog.showQuestionMessage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import tirnav.passman.data.DataModel;
import tirnav.passman.model.EntryTableModel;
import tirnav.passman.ui.action.Callback;
import tirnav.passman.ui.action.CloseListener;
import tirnav.passman.ui.action.ListListener;
import tirnav.passman.ui.action.MenuActionType;
import tirnav.passman.ui.helper.EntryHelper;
import tirnav.passman.ui.helper.FileHelper;
import tirnav.passman.util.Configuration;
import tirnav.passman.util.IconStorage;
import tirnav.passman.xml.bind.Entry;

/**
 * The main frame for PasswordManager.
 *
 * @author Gabor_Bata
 *
 */
public final class PasswordManagerFrame extends JFrame {

    private static final Logger LOG = Logger.getLogger(PasswordManagerFrame.class.getName());
    private static final long serialVersionUID = -4114209356464342368L;

    private static volatile PasswordManagerFrame INSTANCE;

    public static final String PROGRAM_NAME = "PasswordManager Password Manager";
    public static final String PROGRAM_VERSION = "0.1.18-SNAPSHOT";

    private final JPopupMenu popup;
    private final JPanel topContainerPanel;
    private final JMenuBar menuBar;
    private final SearchPanel searchPanel;
    private final JMenu fileMenu;
    private final JMenu editMenu;
    private final JMenu toolsMenu;
    private final JMenu helpMenu;
    private final JToolBar toolBar;
    private final JScrollPane scrollPane;
    private final JList entryTitleList;
    private final DefaultListModel entryTitleListModel;
    private final DataModel model = DataModel.getInstance();
    private final JTable dataTable;
    private final DefaultTableModel dataModel;
    private final StatusPanel statusPanel;
    private volatile boolean processing = false;

    private PasswordManagerFrame(String fileName) {
        try {
            setIconImage(getIcon("lock").getImage());
        } catch (Exception e) {
            LOG.log(Level.CONFIG, "Could not set application icon.", e);
        }

        this.toolBar = new JToolBar();
        toolBar.setFloatable(false);
        this.toolBar.add(MenuActionType.NEW_FILE.getAction());
        this.toolBar.add(MenuActionType.OPEN_FILE.getAction());
        this.toolBar.add(MenuActionType.SAVE_FILE.getAction());
        this.toolBar.addSeparator();
        this.toolBar.add(MenuActionType.ADD_ENTRY.getAction());
        this.toolBar.add(MenuActionType.EDIT_ENTRY.getAction());
        this.toolBar.add(MenuActionType.DUPLICATE_ENTRY.getAction());
        this.toolBar.add(MenuActionType.DELETE_ENTRY.getAction());
        this.toolBar.addSeparator();
        this.toolBar.add(MenuActionType.COPY_URL.getAction());
        this.toolBar.add(MenuActionType.COPY_USER.getAction());
        this.toolBar.add(MenuActionType.COPY_PASSWORD.getAction());
        this.toolBar.add(MenuActionType.OPEN_URL.getAction());
        this.toolBar.add(MenuActionType.CLEAR_CLIPBOARD.getAction());
        this.toolBar.addSeparator();
        this.toolBar.add(MenuActionType.ABOUT.getAction());
        this.toolBar.add(MenuActionType.EXIT.getAction());

        this.searchPanel = new SearchPanel(new Callback() {
            @Override
            public void call(boolean enabled) {
                if (enabled) {
                    refreshEntryTitleList(null);
                }
            }
        });

        this.topContainerPanel = new JPanel(new BorderLayout());
        this.topContainerPanel.add(this.toolBar, BorderLayout.NORTH);
        this.topContainerPanel.add(this.searchPanel, BorderLayout.SOUTH);

        this.menuBar = new JMenuBar();

        this.fileMenu = new JMenu("File");
        this.fileMenu.setMnemonic(KeyEvent.VK_F);
        this.fileMenu.add(MenuActionType.NEW_FILE.getAction());
        this.fileMenu.add(MenuActionType.OPEN_FILE.getAction());
        this.fileMenu.add(MenuActionType.SAVE_FILE.getAction());
        this.fileMenu.add(MenuActionType.SAVE_AS_FILE.getAction());
        this.fileMenu.addSeparator();
        this.fileMenu.add(MenuActionType.EXPORT_XML.getAction());
        this.fileMenu.add(MenuActionType.EXPORT_JSON.getAction());
        this.fileMenu.add(MenuActionType.EXPORT_CSV.getAction());
        this.fileMenu.add(MenuActionType.IMPORT_XML.getAction());
        this.fileMenu.addSeparator();
        this.fileMenu.add(MenuActionType.CHANGE_PASSWORD.getAction());
        this.fileMenu.addSeparator();
        this.fileMenu.add(MenuActionType.EXIT.getAction());
        this.menuBar.add(this.fileMenu);

        this.editMenu = new JMenu("Edit");
        this.editMenu.setMnemonic(KeyEvent.VK_E);
        this.editMenu.add(MenuActionType.ADD_ENTRY.getAction());
        this.editMenu.add(MenuActionType.EDIT_ENTRY.getAction());
        this.editMenu.add(MenuActionType.DUPLICATE_ENTRY.getAction());
        this.editMenu.add(MenuActionType.DELETE_ENTRY.getAction());
        this.editMenu.addSeparator();
        this.editMenu.add(MenuActionType.COPY_URL.getAction());
        this.editMenu.add(MenuActionType.COPY_USER.getAction());
        this.editMenu.add(MenuActionType.COPY_PASSWORD.getAction());
        this.editMenu.addSeparator();
        this.editMenu.add(MenuActionType.FIND_ENTRY.getAction());
        this.menuBar.add(this.editMenu);

        this.toolsMenu = new JMenu("Tools");
        this.toolsMenu.setMnemonic(KeyEvent.VK_T);
        this.toolsMenu.add(MenuActionType.GENERATE_PASSWORD.getAction());
        this.toolsMenu.add(MenuActionType.CLEAR_CLIPBOARD.getAction());
        this.menuBar.add(this.toolsMenu);

        this.helpMenu = new JMenu("Help");
        this.helpMenu.setMnemonic(KeyEvent.VK_H);
        this.helpMenu.add(MenuActionType.LICENSE.getAction());
        this.helpMenu.addSeparator();
        this.helpMenu.add(MenuActionType.ABOUT.getAction());
        this.menuBar.add(this.helpMenu);

        this.popup = new JPopupMenu();
        this.popup.add(MenuActionType.ADD_ENTRY.getAction());
        this.popup.add(MenuActionType.EDIT_ENTRY.getAction());
        this.popup.add(MenuActionType.DUPLICATE_ENTRY.getAction());
        this.popup.add(MenuActionType.DELETE_ENTRY.getAction());
        this.popup.addSeparator();
        this.popup.add(MenuActionType.COPY_URL.getAction());
        this.popup.add(MenuActionType.COPY_USER.getAction());
        this.popup.add(MenuActionType.COPY_PASSWORD.getAction());
        this.popup.add(MenuActionType.OPEN_URL.getAction());
        this.popup.addSeparator();
        this.popup.add(MenuActionType.FIND_ENTRY.getAction());

        this.entryTitleListModel = new DefaultListModel();
        this.entryTitleList = new JList(this.entryTitleListModel);
        this.entryTitleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.entryTitleList.addMouseListener(new ListListener());
        this.entryTitleList.setCellRenderer(new IconedListCellRenderer());

        //table
        this.dataModel= new EntryTableModel(model.getEntries().getEntry());
        this.dataTable= new JTable(dataModel){
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
                Component returnComp = super.prepareRenderer(renderer, row, column);
                Color alternateColor = new Color(252,242,206);
                Color whiteColor = Color.WHITE;
                if (!returnComp.getBackground().equals(getSelectionBackground())){
                    Color bg = (row % 2 == 0 ? alternateColor : whiteColor);
                    returnComp .setBackground(bg);
                    bg = null;
                }
                return returnComp;
        }
            public String getToolTipText(MouseEvent event) {
                Point p = event.getPoint();

                // Locate the renderer under the event location
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                
                if(colIndex == 0) {
                	return "";
                }
                
                Object value = this.getModel().getValueAt(rowIndex, colIndex);
                return null != value?value.toString():"";
            }
        }; 
        this.dataTable.setAutoCreateRowSorter(Boolean.TRUE);
        
        TableRowSorter<TableModel> sorter = new TableRowSorter(this.dataTable.getModel());
        this.dataTable.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList();
         
        int columnIndexToSort = 1;
        sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
         
        sorter.setSortKeys(sortKeys);
        sorter.sort();
        
        this.dataTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        this.dataTable.getColumnModel().getColumn(1).setPreferredWidth(175);
        //this.dataTable.getColumnModel().getColumn(0).setCellRenderer(new IconedListCellRenderer());
        this.dataTable.getColumnModel().getColumn(2).setPreferredWidth(175);
        this.dataTable.getColumnModel().getColumn(3).setPreferredWidth(90);
        this.dataTable.getColumnModel().getColumn(4).setPreferredWidth(90);
        this.dataTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        
        this.dataTable.addMouseListener(new ListListener());
        this.dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.scrollPane = new JScrollPane(this.dataTable);
        MenuActionType.bindAllActions(this.dataTable);
        
        //this.scrollPane = new JScrollPane(this.entryTitleList);
        //MenuActionType.bindAllActions(this.entryTitleList);

        this.statusPanel = new StatusPanel();

        refreshAll();

        getContentPane().add(this.topContainerPanel, BorderLayout.NORTH);
        getContentPane().add(this.scrollPane, BorderLayout.CENTER);
        getContentPane().add(this.statusPanel, BorderLayout.SOUTH);

        setJMenuBar(this.menuBar);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setSize(650, 400);
        setMinimumSize(new Dimension(650, 200));
        addWindowListener(new CloseListener());
        setLocationRelativeTo(null);
        setVisible(true);
        FileHelper.doOpenFile(fileName, this);

        // set focus to the list for easier keyboard navigation
        this.entryTitleList.requestFocusInWindow();
    }

    public static PasswordManagerFrame getInstance() {
        return getInstance(null);
    }

    public static PasswordManagerFrame getInstance(String fileName) {
        if (INSTANCE == null) {
            synchronized (PasswordManagerFrame.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PasswordManagerFrame(fileName);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Gets the entry title list.
     *
     * @return entry title list
     */
    public JList getEntryTitleList() {
        return this.entryTitleList;
    }

    /**
	 * @return the dataTable
	 */
	public JTable getDataTable() {
		return dataTable;
	}

	/**
     * Gets the data model of this frame.
     *
     * @return data model
     */
    public DataModel getModel() {
        return this.model;
    }

    /**
     * Clears data model.
     */
    public void clearModel() {
        this.model.clear();
        this.entryTitleListModel.clear();
        this.dataModel.setRowCount(0);
    }

    /**
     * Refresh frame title based on data model.
     */
    public void refreshFrameTitle() {
        setTitle((getModel().isModified() ? "*" : "")
                + (getModel().getFileName() == null ? "Untitled" : getModel().getFileName()) + " - "
                + PROGRAM_NAME);
    }

    /**
     * Refresh the entry titles based on data model.
     *
     * @param selectTitle title to select, or {@code null} if nothing to select
     */
    public void refreshEntryTitleList(String selectTitle) {
    	IconStorage iconStorage = IconStorage.newInstance();
    	
        this.entryTitleListModel.clear();
        this.dataModel.setRowCount(0);
        List<String> titles = this.model.getTitles();
        Collections.sort(titles, String.CASE_INSENSITIVE_ORDER);

        String searchCriteria = this.searchPanel.getSearchCriteria();
        for (String title : titles) {
            if (searchCriteria.isEmpty() || title.toLowerCase().contains(searchCriteria.toLowerCase())) {
                this.entryTitleListModel.addElement(title);
                Entry entry = this.model.getEntryByTitle(title);
                JLabel label = new JLabel(title);
                label.setIcon(iconStorage.getIcon(entry.getUrl()));
                this.dataModel.addRow(new Object[]{ label.getIcon() , title, entry.getUser(), entry.getModifiedDate(),entry.getLastPasswordChanged(),entry.getChangePasswordInDays()});
            }
        }

        if (selectTitle != null) {
            this.entryTitleList.setSelectedValue(selectTitle, true);
        }

        if (searchCriteria.isEmpty()) {
            this.statusPanel.setText("Entries count: " + titles.size());
        } else {
            this.statusPanel.setText("Entries found: " + this.entryTitleListModel.size() + " / " + titles.size());
        }
    }

    /**
     * Refresh frame title and entry list.
     */
    public void refreshAll() {
        refreshFrameTitle();
        refreshEntryTitleList(null);
    }

    /**
     * Exits the application.
     */
    public void exitFrame() {
        if (Configuration.getInstance().is("clear.clipboard.on.exit.enabled", false)) {
            EntryHelper.copyEntryField(this, null);
        }

        if (this.processing) {
            return;
        }
        if (this.model.isModified()) {
            int option = showQuestionMessage(this,
                    "The current file has been modified.\nDo you want to save the changes before closing?", YES_NO_CANCEL_OPTION);
            if (option == YES_OPTION) {
                FileHelper.saveFile(this, false, new Callback() {
                    @Override
                    public void call(boolean result) {
                        if (result) {
                            System.exit(0);
                        }
                    }
                });
                return;
            } else if (option != NO_OPTION) {
                return;
            }
        }
        //System.exit(0);
        this.setVisible(Boolean.FALSE);
    }

    public JPopupMenu getPopup() {
        return this.popup;
    }

    /**
     * Sets the processing state of this frame.
     *
     * @param processing processing state
     */
    public void setProcessing(boolean processing) {
        this.processing = processing;
        for (MenuActionType actionType : MenuActionType.values()) {
            actionType.getAction().setEnabled(!processing);
        }
        this.searchPanel.setEnabled(!processing);
        this.entryTitleList.setEnabled(!processing);
        this.statusPanel.setProcessing(processing);
    }

    /**
     * Gets the processing state of this frame.
     *
     * @return processing state
     */
    public boolean isProcessing() {
        return this.processing;
    }

    /**
     * Get search panel.
     *
     * @return the search panel
     */
    public SearchPanel getSearchPanel() {
        return searchPanel;
    }
}
