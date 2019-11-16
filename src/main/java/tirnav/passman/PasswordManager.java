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
package tirnav.passman;

import static tirnav.passman.ui.MessageDialog.getIcon;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import tirnav.passman.ui.PasswordManagerFrame;
import tirnav.passman.util.Configuration;

/**
 * Entry point of PasswordManager.
 *
 * @author Gabor_Bata
 *
 */
public class PasswordManager {

    private static final Logger LOG = Logger.getLogger(PasswordManager.class.getName());
    private static final String METAL_LOOK_AND_FEEL = "javax.swing.plaf.metal.MetalLookAndFeel";

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
    }

    public static void main(final String[] args) {
        try {
            String lookAndFeel;
            if (Configuration.getInstance().is("system.look.and.feel.enabled", true)) {
                lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            } else {
                lookAndFeel = METAL_LOOK_AND_FEEL;
            }

            if (METAL_LOOK_AND_FEEL.equals(lookAndFeel)) {
                MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme() {
                    private final ColorUIResource primary1 = new ColorUIResource(0x4d6781);
                    private final ColorUIResource primary2 = new ColorUIResource(0x7a96b0);
                    private final ColorUIResource primary3 = new ColorUIResource(0xc8d4e2);
                    private final ColorUIResource secondary1 = new ColorUIResource(0x000000);
                    private final ColorUIResource secondary2 = new ColorUIResource(0xaaaaaa);
                    private final ColorUIResource secondary3 = new ColorUIResource(0xdfdfdf);

                    @Override
                    protected ColorUIResource getPrimary1() {
                        return this.primary1;
                    }

                    @Override
                    protected ColorUIResource getPrimary2() {
                        return this.primary2;
                    }

                    @Override
                    protected ColorUIResource getPrimary3() {
                        return this.primary3;
                    }

                    @Override
                    protected ColorUIResource getSecondary1() {
                        return this.secondary1;
                    }

                    @Override
                    protected ColorUIResource getSecondary2() {
                        return this.secondary2;
                    }

                    @Override
                    protected ColorUIResource getSecondary3() {
                        return this.secondary3;
                    }
                });

                UIManager.put("swing.boldMetal", Boolean.FALSE);
            }

            UIManager.setLookAndFeel(lookAndFeel);
            
            TrayIcon trayIcon = null;
            if (SystemTray.isSupported()) {
                // get the SystemTray instance
                SystemTray tray = SystemTray.getSystemTray();
                // load an image
                Image image = Toolkit.getDefaultToolkit().getImage("your_image/path_here.gif");
                
                // create a popup menu
                PopupMenu popup =  new PopupMenu();
                // create menu item for the default action
                MenuItem exitItem = new MenuItem("Exit");
                exitItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
                popup.add(exitItem);
                popup.addSeparator();
                MenuItem openItem = new MenuItem("Open");
                openItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						PasswordManagerFrame.getInstance().setVisible(Boolean.TRUE);
					}
				});
                popup.add(openItem);
                // construct a TrayIcon
                trayIcon = new TrayIcon(image, "PasswordManager", popup);
                // set the TrayIcon properties
                trayIcon.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
							PasswordManagerFrame.getInstance().setVisible(Boolean.TRUE);
					}
				});
                trayIcon.addMouseListener(new MouseListener() {
					
					@Override
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount() == 2) {
							PasswordManagerFrame.getInstance().setVisible(Boolean.TRUE);
						}
					}

					@Override
					public void mousePressed(MouseEvent e) {
					}

					@Override
					public void mouseReleased(MouseEvent e) {
					}

					@Override
					public void mouseEntered(MouseEvent e) {
					}

					@Override
					public void mouseExited(MouseEvent e) {
					}
				});
                // ...
                // add the tray image
                try {
                    tray.add(trayIcon);
                } catch (AWTException e) {
                    System.err.println(e);
                }
                // ...
            } else {
                // disable tray option in your application or
                // perform other actions
            }
            // ...
            // some time later
            // the application state has changed - update the image
            if (trayIcon != null) {
                trayIcon.setImage(getIcon("lock").getImage());
            }
        } catch (Exception e) {
            LOG.log(Level.CONFIG, "Could not set look and feel for the application", e);
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PasswordManagerFrame.getInstance((args.length > 0) ? args[0] : null);
            }
        });
    }
}
