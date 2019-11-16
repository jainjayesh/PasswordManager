package tirnav.passman.util;
// Java program that creates the toast message 
//(which is a selectively translucent JWindow) 
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow; 
public class Toast extends JFrame { 
  
    /**
	 * 
	 */
	private static final long serialVersionUID = 8129151104450052009L;

	//String of toast 
    String s; 
  
    // JWindow 
    JWindow w; 
  
    public Toast(final String message, int x, int y) 
    {
    	setUndecorated(true);
        setLayout(new GridBagLayout());
        setBackground(new Color(240,240,240,250));
        setLocationRelativeTo(null);
        setSize(300, 50);
        add(new JLabel(message)); 
         
        addComponentListener(new ComponentAdapter() {
           @Override
           public void componentResized(ComponentEvent e) {
              setShape(new  RoundRectangle2D.Double(0,0,getWidth(),
              getHeight(), 20, 20));                      
           }
        }); 
    } 
  
    // function to pop up the toast 
    public void showToast() 
    {
    	try {
            setOpacity(1);
            setVisible(true);
            Thread.sleep(2000);

            //hide the toast message in slow motion
            for (double d = 1.0; d > 0.2; d -= 0.1) {
               Thread.sleep(100);
               setOpacity((float)d);
            }

            // set the visibility to false
            setVisible(false);
         }catch (Exception e) {
            System.out.println(e.getMessage());
         }
      }
} 