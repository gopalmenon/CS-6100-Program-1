package menon.cs6100.program1;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class AStarSearchDemo {

	private static final int VERTICAL_RECTANGLE_MODE = 0;
	private static final int HORIZONTAL_RECTANGLE_MODE = 1;
	private static final int ADD_START_MODE = 2;
	private static final int ADD_END_MODE = 3;
	private static final int NARROW_DIMENSION = 10;
	private static final int WIDE_DIMENSION = 75;
	private static final int START_END_DIMENSION = 10;
	static final int TOP_OFFSET = 45;	
	private static final int TERRAIN_WIDTH = 640;
	private static final int TERRAIN_HEIGHT = 480;
	
	private static int selectedMode = 0;

	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("A* Search Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //Create the menu bar.  Make it have a green background.
        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque(true);        
        menuBar.setPreferredSize(new Dimension(TERRAIN_WIDTH, 20));
        
        //Create user menus
        JMenu fileMenu, obstaclesMenu, routeMenu;
        JMenuItem exitMenuItem, addVerticalRectangleMenuItem, addHorizontalRectangleMenuItem, addStartMenuItem, addEndMenuItem, findRouteMenuItem, showMetricsMenuItem;
        
        //File menu
        fileMenu = new JMenu("File");
        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {System.exit(0);} });
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        
        //Obstacles menu
        obstaclesMenu = new JMenu("Obstacles");
        addVerticalRectangleMenuItem = new JMenuItem("Vertical Rectangle");
        addVerticalRectangleMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {selectedMode = VERTICAL_RECTANGLE_MODE;} });
        addHorizontalRectangleMenuItem = new JMenuItem("Horizontal Rectangle");
        addHorizontalRectangleMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {selectedMode = HORIZONTAL_RECTANGLE_MODE;} });
        obstaclesMenu.add(addVerticalRectangleMenuItem);
        obstaclesMenu.add(addHorizontalRectangleMenuItem);
        menuBar.add(obstaclesMenu);
        
        //Route menu
        routeMenu = new JMenu("Route");
        addStartMenuItem = new JMenuItem("Add Start");
        addStartMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {selectedMode = ADD_START_MODE;} });
        addEndMenuItem = new JMenuItem("Add End");
        addEndMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {selectedMode = ADD_END_MODE;} });
        routeMenu.add(addStartMenuItem);
        routeMenu.add(addEndMenuItem);
        menuBar.add(routeMenu);
        
        //Create the obstacles terrain
        final ObstaclesTerrain obstaclesTerrain = new ObstaclesTerrain();
        obstaclesTerrain.setPreferredSize(new Dimension(TERRAIN_WIDTH, TERRAIN_HEIGHT));

        findRouteMenuItem = new JMenuItem("Find Route");
        findRouteMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {obstaclesTerrain.retrieveRoute();} });
        routeMenu.add(findRouteMenuItem);
        showMetricsMenuItem = new JMenuItem("Show Metrics");
        showMetricsMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {JOptionPane.showMessageDialog(obstaclesTerrain, obstaclesTerrain.getMetricsMessage());} });
        routeMenu.add(showMetricsMenuItem);
        
        //Set the menu bar and add the label to the content pane.
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(obstaclesTerrain, BorderLayout.CENTER);
        frame.addMouseListener(obstaclesTerrain);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    //Define the terrain that will contain the obstacles, start an d end points
    @SuppressWarnings("serial")
	static class ObstaclesTerrain extends JPanel implements MouseListener {
    	
    	private static ArrayList<Point> horizontalRectangles = new ArrayList<Point>();
    	private static ArrayList<Point> verticalRectangles = new ArrayList<Point>();
    	private static ArrayList<PointOnTerrain> route = null;
    	private static PointOnTerrain startPoint = null;
    	private static PointOnTerrain endPoint = null;
    	private static TerrainGrid terrainGrid = new TerrainGrid(TERRAIN_WIDTH, TERRAIN_HEIGHT);
    	private static TerrainNavigationAgent terrainNavigationAgent = new TerrainNavigationAgent(terrainGrid);
    	private static boolean routingAttempted = false;
    	private static boolean routefound = false;
    	
    	private int routeLength = 0;
    	private int numberOfNodesExpanded = 0;
    	
    	public void paintComponent(Graphics g) {
        	
            super.paintComponent(g);

            //Draw vertical rectangles
            g.setColor(Color.BLACK);
            for (Point point : verticalRectangles) {
            	g.fillRect(point.x, point.y, NARROW_DIMENSION, WIDE_DIMENSION);
            }

            //Draw horizontal rectangles
            g.setColor(Color.BLACK);
            for (Point point : horizontalRectangles) {
            	g.fillRect(point.x, point.y, WIDE_DIMENSION, NARROW_DIMENSION);
            }
            
            //Draw start point
            g.setColor(Color.GREEN);
            if (startPoint != null) {
            	g.fillOval(startPoint.getxCoordinate() - START_END_DIMENSION / 2, startPoint.getyCoordinate() - TOP_OFFSET - START_END_DIMENSION / 2, START_END_DIMENSION, START_END_DIMENSION);
            }
            
            //Draw end point
            g.setColor(Color.RED);
            if (endPoint != null) {
            	g.fillOval(endPoint.getxCoordinate() - START_END_DIMENSION / 2, endPoint.getyCoordinate() - TOP_OFFSET - START_END_DIMENSION / 2, START_END_DIMENSION, START_END_DIMENSION);
            }
            
        	if (route != null) {
        		g.setColor(Color.ORANGE);
        		drawRoute(g, route);
        	}
    }

		@Override
		public void mouseClicked(MouseEvent e) {
			
			switch (selectedMode) {
			
			case VERTICAL_RECTANGLE_MODE:
				if (!routingAttempted) {
					verticalRectangles.add(new Point(e.getX(), e.getY() - TOP_OFFSET));
					terrainGrid.setRectangularBarrier(e.getX(), e.getY() - TOP_OFFSET, NARROW_DIMENSION, WIDE_DIMENSION);
				}
				break;
				
			case HORIZONTAL_RECTANGLE_MODE:
				if (!routingAttempted) {
					horizontalRectangles.add(new Point(e.getX(), e.getY() - TOP_OFFSET));
					terrainGrid.setRectangularBarrier(e.getX(), e.getY() - TOP_OFFSET, WIDE_DIMENSION, NARROW_DIMENSION);
				}
				break;
				
			case ADD_START_MODE:
				if (!routingAttempted) {
					startPoint = new PointOnTerrain(e.getX(), e.getY());
					terrainNavigationAgent.setStartPoint(startPoint);
				}
				break;
							
			case ADD_END_MODE:
				if (!routingAttempted) {
					endPoint = new PointOnTerrain(e.getX(), e.getY());
					terrainNavigationAgent.setEndPoint(endPoint);
				}
				break;
				
			}
			
			repaint();
		}
		
		public void retrieveRoute() {
			if (!routingAttempted && startPoint != null && endPoint != null) {
				route = terrainNavigationAgent.navigateTerrain();
				if (route != null) {
					routefound = true;
				}
				routingAttempted = true;
				this.numberOfNodesExpanded = terrainNavigationAgent.getNumberOfNodesExpanded();
				this.routeLength = terrainNavigationAgent.getRouteLength();
				repaint();
			}
		}

		public String getMetricsMessage() {
			
			if (routingAttempted) {
				StringBuffer metricsMessage = new StringBuffer();
				if (routefound) {
					metricsMessage.append(this.numberOfNodesExpanded).append(" nodes were expanded to find the route, which is ").append(this.routeLength).append(" pixels long.");
					return metricsMessage.toString();
				} else {
					metricsMessage.append(this.numberOfNodesExpanded).append(" nodes were expanded, but the route was not found.");
					return metricsMessage.toString();
				}
			} else {
				return "The route has not been mapped yet.";
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
    }
    
    private static void drawRoute(Graphics g, ArrayList<PointOnTerrain> route) {
    	
    	PointOnTerrain previousRouteNode = null, nextRouteNode = null;
    	for (PointOnTerrain routeNode : route) {
    		
    		if (previousRouteNode == null && nextRouteNode == null) {
    			previousRouteNode = routeNode;
    		} else {
    			nextRouteNode = routeNode;
    			g.drawLine(previousRouteNode.getxCoordinate(), previousRouteNode.getyCoordinate(), nextRouteNode.getxCoordinate(), nextRouteNode.getyCoordinate());
    			previousRouteNode = null;
    			nextRouteNode = null;
    		}
    		
    	}
    	
    }
    
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
