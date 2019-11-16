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
package tirnav.passman.ui.action;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import tirnav.passman.ui.GeneratePasswordDialog;
import tirnav.passman.ui.PasswordManagerFrame;
import tirnav.passman.ui.MessageDialog;
import tirnav.passman.ui.helper.EntryHelper;
import tirnav.passman.util.Toast;
import tirnav.passman.xml.bind.Entry;

import static javax.swing.KeyStroke.getKeyStroke;
import static tirnav.passman.ui.MessageDialog.getIcon;
import static tirnav.passman.ui.helper.FileHelper.createNew;
import static tirnav.passman.ui.helper.FileHelper.exportFile;
import static tirnav.passman.ui.helper.FileHelper.importFile;
import static tirnav.passman.ui.helper.FileHelper.openFile;
import static tirnav.passman.ui.helper.FileHelper.saveFile;
import static java.awt.event.InputEvent.CTRL_MASK;
import static java.awt.event.InputEvent.ALT_MASK;

/**
 * Enumeration which holds menu actions and related data.
 *
 * @author Gabor_Bata
 *
 */
public enum MenuActionType {
    NEW_FILE(new AbstractMenuAction("New", getIcon("new"), getKeyStroke(KeyEvent.VK_N, CTRL_MASK)) {
        private static final long serialVersionUID = -8823457568905830188L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            createNew(PasswordManagerFrame.getInstance());
        }
    }),
    OPEN_FILE(new AbstractMenuAction("Open File...", getIcon("open"), getKeyStroke(KeyEvent.VK_O, CTRL_MASK)) {
        private static final long serialVersionUID = -441032579227887886L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            openFile(PasswordManagerFrame.getInstance());
        }
    }),
    SAVE_FILE(new AbstractMenuAction("Save", getIcon("save"), getKeyStroke(KeyEvent.VK_S, CTRL_MASK)) {
        private static final long serialVersionUID = 8657273941022043906L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            saveFile(PasswordManagerFrame.getInstance(), false);
        }
    }),
    SAVE_AS_FILE(new AbstractMenuAction("Save As...", getIcon("save_as"), null) {
        private static final long serialVersionUID = 1768189708479045321L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            saveFile(PasswordManagerFrame.getInstance(), true);
        }
    }),
    EXPORT_XML(new AbstractMenuAction("Export to XML...", getIcon("export"), null) {
        private static final long serialVersionUID = 7673408373934859054L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            exportFile(PasswordManagerFrame.getInstance());
        }
    }),
    IMPORT_XML(new AbstractMenuAction("Import from XML...", getIcon("import"), null) {
        private static final long serialVersionUID = -1331441499101116570L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            importFile(PasswordManagerFrame.getInstance());
        }
    }),
    CHANGE_PASSWORD(new AbstractMenuAction("Change Password...", getIcon("lock"), null) {
        private static final long serialVersionUID = 616220526614500130L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            PasswordManagerFrame parent = PasswordManagerFrame.getInstance();
            byte[] password = MessageDialog.showPasswordDialog(parent, true);
            if (password == null) {
                MessageDialog.showInformationMessage(parent, "Password has not been modified.");
            } else {
                parent.getModel().setPassword(password);
                parent.getModel().setModified(true);
                parent.refreshFrameTitle();
                MessageDialog.showInformationMessage(parent,
                        "Password has been successfully modified.\n\nSave the file now in order to\nget the new password applied.");
            }
        }
    }),
    GENERATE_PASSWORD(new AbstractMenuAction("Generate Password...", getIcon("generate"), getKeyStroke(KeyEvent.VK_Z, CTRL_MASK)) {
        private static final long serialVersionUID = 2865402858056954304L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            new GeneratePasswordDialog(PasswordManagerFrame.getInstance());
        }
    }),
    EXIT(new AbstractMenuAction("Exit", getIcon("exit"), getKeyStroke(KeyEvent.VK_F4, ALT_MASK)) {
        private static final long serialVersionUID = -2741659403416846295L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            PasswordManagerFrame.getInstance().exitFrame();
        	
        }
    }),
    ABOUT(new AbstractMenuAction("About PasswordManager...", getIcon("info"), getKeyStroke(KeyEvent.VK_F1, 0)) {
        private static final long serialVersionUID = -8935177434578353178L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            StringBuilder sb = new StringBuilder();
            sb.append("<b>" + PasswordManagerFrame.PROGRAM_NAME + "</b>\n");
            sb.append("version: " + PasswordManagerFrame.PROGRAM_VERSION + "\n");
            sb.append("Copyright &copy; 2009-"+Calendar.getInstance().get(Calendar.YEAR)+" Tirnav Solutions\n");
            sb.append("\n");
            sb.append("Java version: ").append(System.getProperties().getProperty("java.version")).append("\n");
            sb.append(System.getProperties().getProperty("java.vendor"));
            MessageDialog.showInformationMessage(PasswordManagerFrame.getInstance(), sb.toString());
        }
    }),
    LICENSE(new AbstractMenuAction("License", getIcon("license"), null) {
        private static final long serialVersionUID = 2476765521818491911L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            MessageDialog.showTextFile(PasswordManagerFrame.getInstance(), "License", "license.txt");
        }
    }),
    ADD_ENTRY(new AbstractMenuAction("Add Entry...", getIcon("entry_new"), getKeyStroke(KeyEvent.VK_Y, CTRL_MASK)) {
        private static final long serialVersionUID = 6793989246928698613L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            EntryHelper.addEntry(PasswordManagerFrame.getInstance());
        }
    }),
    EDIT_ENTRY(new AbstractMenuAction("Edit Entry...", getIcon("entry_edit"), getKeyStroke(KeyEvent.VK_E, CTRL_MASK)) {
        private static final long serialVersionUID = -3234220812811327191L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            EntryHelper.editEntry(PasswordManagerFrame.getInstance());
        }
    }),
    DUPLICATE_ENTRY(new AbstractMenuAction("Duplicate Entry...", getIcon("entry_duplicate"), getKeyStroke(KeyEvent.VK_K, CTRL_MASK)) {
        private static final long serialVersionUID = 6728896867346523861L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            EntryHelper.duplicateEntry(PasswordManagerFrame.getInstance());
        }
    }),
    DELETE_ENTRY(new AbstractMenuAction("Delete Entry...", getIcon("entry_delete"), getKeyStroke(KeyEvent.VK_D, CTRL_MASK)) {
        private static final long serialVersionUID = -1306116722130641659L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            EntryHelper.deleteEntry(PasswordManagerFrame.getInstance());
        }
    }),
    COPY_URL(new AbstractMenuAction("Copy URL", getIcon("url"), getKeyStroke(KeyEvent.VK_U, CTRL_MASK)) {
        private static final long serialVersionUID = 3321559756310744862L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            PasswordManagerFrame parent = PasswordManagerFrame.getInstance();
            Entry entry = EntryHelper.getSelectedEntry(parent);
            if (entry != null) {
                EntryHelper.copyEntryField(parent, entry.getUrl());
            }
        }
    }),
    COPY_USER(new AbstractMenuAction("Copy User Name", getIcon("user"), getKeyStroke(KeyEvent.VK_B, CTRL_MASK)) {
        private static final long serialVersionUID = -1126080607846730912L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            PasswordManagerFrame parent = PasswordManagerFrame.getInstance();
            Entry entry = EntryHelper.getSelectedEntry(parent);
            if (entry != null) {
                EntryHelper.copyEntryField(parent, entry.getUser());
            }
        }
    }),
    COPY_PASSWORD(new AbstractMenuAction("Copy Password", getIcon("keyring"), getKeyStroke(KeyEvent.VK_C, CTRL_MASK)) {
        private static final long serialVersionUID = 2719136744084762599L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            PasswordManagerFrame parent = PasswordManagerFrame.getInstance();
            Entry entry = EntryHelper.getSelectedEntry(parent);
            if (entry != null) {
                EntryHelper.copyEntryField(parent, entry.getPassword());
            }
        }
    }),
    CLEAR_CLIPBOARD(new AbstractMenuAction("Clear Clipboard", getIcon("clear"), getKeyStroke(KeyEvent.VK_X, CTRL_MASK)) {
        private static final long serialVersionUID = -7621614933053924326L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            EntryHelper.copyEntryField(PasswordManagerFrame.getInstance(), null);
        }
    }),
    FIND_ENTRY(new AbstractMenuAction("Find Entry", getIcon("find"), getKeyStroke(KeyEvent.VK_F, CTRL_MASK)) {
        private static final long serialVersionUID = -7621614933053924326L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            PasswordManagerFrame.getInstance().getSearchPanel().setVisible(true);
        }
    }),
    OPEN_URL(new AbstractMenuAction("Open URL in Browser", getIcon("open_url"), getKeyStroke(KeyEvent.VK_U, CTRL_MASK)) {
        private static final long serialVersionUID = -7621614933053924326L;

        @Override
        public void actionPerformed(ActionEvent ev) {
        	PasswordManagerFrame parent = PasswordManagerFrame.getInstance();
            Entry entry = EntryHelper.getSelectedEntry(parent);
            if (entry != null) {
                EntryHelper.copyEntryField(parent, entry.getPassword());
            	if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            	    try {
            	    	//MessageDialog.showWarningMessage(parent, "Username copied to clipboard.");
            	    	// create a toast message 
            	    	new Thread(new Runnable() {
							public void run() {
								Toast t = new Toast("Username copied to clipboard.", 150, 400); 
		            	        // call the method 
		            	        t.showToast(); 
							}
						});
            	        
            	    	EntryHelper.copyEntryField(parent, entry.getUser());
						Desktop.getDesktop().browse(new URI(entry.getUrl()));
					} catch (IOException e) {
						e.printStackTrace();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
            	}
            }
        }
    });

    private final String name;
    private final AbstractMenuAction action;

    private MenuActionType(AbstractMenuAction action) {
        this.name = String.format("tirnav.passman.menu.%s_action", this.name().toLowerCase());
        this.action = action;
    }

    public String getName() {
        return this.name;
    }

    public AbstractMenuAction getAction() {
        return this.action;
    }

    public KeyStroke getAccelerator() {
        return (KeyStroke) this.action.getValue(Action.ACCELERATOR_KEY);
    }

    public static final void bindAllActions(JComponent component) {
        ActionMap actionMap = component.getActionMap();
        InputMap inputMap = component.getInputMap();
        for (MenuActionType type : values()) {
            actionMap.put(type.getName(), type.getAction());
            KeyStroke acc = type.getAccelerator();
            if (acc != null) {
                inputMap.put(type.getAccelerator(), type.getName());
            }
        }
    }
}
