/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sierpinskitriangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author harlan.howe
 */
public class SierpinskiPanel extends JPanel {
        private int myDepth;

        private BufferedImage myCanvas; // an offscreen image where we'll do the drawing and occasionally copy to screen.
        private Object myCanvasMutex; // a lock to make sure only one thing uses myCanvas at a time.

        private TriangleThread drawingThread; // the portion of code that will do the drawing simultaneously with
                                              // occasional screen updates.

        public SierpinskiPanel()
        {
            super();
            myDepth = 1;
            myCanvasMutex = new Object();
            drawingThread = new TriangleThread();
            drawingThread.start();
        }
        
        public void setDepth(int depth)
        {
            if (depth>0)
            {
                myDepth = depth;
                System.out.println("Setting depth to "+myDepth+".");
                drawingThread.interrupt();
                drawingThread.startDrawing();
            }

        }
        
        public void paintComponent(Graphics g)
        {
            synchronized (myCanvasMutex) // wait until myCanvas is available, then lock it for my use....
            {
                if (myCanvas != null)
                    g.drawImage(myCanvas,0,0,this);
            } // now I'm done with myCanvas, so I am unlocking the mutex.
        }

        class TriangleThread extends Thread
        {
            private boolean needsRestart;
            private boolean shouldInterrupt;

            public TriangleThread()
            {
                needsRestart = true;
                shouldInterrupt = true;
            }

            public void interrupt()
            {
                shouldInterrupt = true;
            }

            public void startDrawing()
            {
                needsRestart = true;
            }

            public void run() // this is what gets called when we say "start()." Never call this method directly.
            {
                while(true) // loop forever
                {
                    if (needsRestart & getHeight() > 5 & getWidth() > 5) // if we need to restart and the window has non-zero size
                    {
                        System.out.println("Making new canvas.");
                        synchronized (myCanvasMutex) // wait for myCanvas to be free and lock it for my use.
                        {
                            myCanvas = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
                            Graphics g = myCanvas.getGraphics();
                            g.setColor(Color.BLUE);
                            g.fillRect(0,0,getWidth(),getHeight());
                        } // ok. I'm done with myCanvas for now, unlock the mutex.

                        // draw the triangle - the points given are the corners of the outside of the triangle.
                        drawSierpinskiTriangle(getWidth()/2,10,    10, getHeight()-10,       getWidth()-10, getHeight()-10,    myDepth);
                        if (!shouldInterrupt)
                            needsRestart = false; // we're done and didn't get interrupted.
                    }
                    try
                    {
                        Thread.sleep((250)); // wait for 1/4 second before checking whether to draw again.
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    shouldInterrupt = false;
                }
            }

            public void drawSierpinskiTriangle(double x1, double y1,      double x2, double y2,    double x3, double y3,   int depthToGo)
            {
                if (shouldInterrupt) // bail out quickly....
                    return;


                // TODO: This is the method you need to write!

                // you should have a base case and a case in which you continue the recursion, one step closer to the base case.
                if (depthToGo == 0)
                {

                    // --> --> --> --> NOTE:You should ONLY draw a triangle in the base case! To do so, you will need something like this...
                    synchronized (myCanvasMutex) // wait until myCanvas is free, then lock it for my use...
                    {
                        Graphics myCanv_g = myCanvas.getGraphics();
                        myCanv_g.setColor(Color.white);

                        // ..... and draw the triangle via myCanv_g.drawLine() or myCanv_g.drawPolygon() here.
                        // reminder: you'll need to typecast the doubles to ints to draw.
                        myCanv_g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
                        myCanv_g.drawLine((int) x2, (int) y2, (int) x3, (int) y3);
                        myCanv_g.drawLine((int) x3, (int) y3, (int) x1, (int) y1);


                    } // k, now I'm done with myCanvas for now. Release it.
                    repaint();
                } else
                {
                    double x12 = (x1 + x2) / 2;
                    double y12 = (y1 + y2) / 2;
                    double x23 = (x2 + x3) / 2;
                    double y23 = (y2 + y3) / 2;
                    double x31 = (x3 + x1) / 2;
                    double y31 = (y3 + y1) / 2;

                    drawSierpinskiTriangle(x1, y1, x2, y2, x3, y3, depthToGo - 1);
                    drawSierpinskiTriangle(x12, y12, x2,y2,x23,y23, depthToGo - 1);
                    drawSierpinskiTriangle(x31,y31,x23,y23,x3,y3, depthToGo - 1);
                    drawSierpinskiTriangle(x1,y1,x12,y12,x31,y31, depthToGo - 1);
                }

            }
        }
}
