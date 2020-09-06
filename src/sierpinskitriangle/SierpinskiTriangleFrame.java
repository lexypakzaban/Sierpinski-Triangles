/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sierpinskitriangle;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author harlan.howe
 */
public class SierpinskiTriangleFrame extends JFrame implements ChangeListener{

    private SierpinskiPanel visualPanel;
    private JSlider mySlider;
    
    public SierpinskiTriangleFrame()
    {
        super("Membrane");
        setSize(1000,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        visualPanel = new SierpinskiPanel();
        getContentPane().add(visualPanel,BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel();
        mySlider = new JSlider(1,10,1);
        mySlider.setMajorTickSpacing(1);
        mySlider.setPaintLabels(true);
        mySlider.setPaintTicks(true);
        mySlider.setSnapToTicks(true);
        mySlider.addChangeListener(this);
        bottomPanel.add(mySlider);
        getContentPane().add(bottomPanel,BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    public void stateChanged(ChangeEvent ae)
    {
        if (mySlider.getValueIsAdjusting())
            visualPanel.setDepth(mySlider.getValue());
        
    }
    
    
    
}
