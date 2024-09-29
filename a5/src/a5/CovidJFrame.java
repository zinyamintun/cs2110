package a5;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import functional.Supplier;
import graph.GraphPanel;

/** An instance is a JFrame with a Covid-spreading tree and a panel to display information
 *
 * @author Mshnik */
public class CovidJFrame extends JFrame {

    private static final long serialVersionUID= 1L; // Used for serializing

    /** Return a new CovidFrame to display dt and s, the number of time steps to create it. */
    public static CovidJFrame show(CovidTree dt, int s) {
        return new CovidJFrame(dt, s);
    }

    /** Store the screen width and height of the current screen's size. <br>
     * This method is called when the class is initially loaded */
    static {
        var s= Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_WIDTH= s.width;
        SCREEN_HEIGHT= s.height;
    }

    /** Width and height of the screen. */
    private static final int SCREEN_WIDTH, SCREEN_HEIGHT;

    /** Space between left of screen and window. */
    private static final int X_OFFSET= 50;

    /** Space between top of screen and window. */
    private static final int Y_OFFSET= 50;

    /** Minimum size of Info panel in windows. */
    private final int INFO_PANEL_MIN_WIDTH;

    /** Height of color bar. */
    private static final int WIDTH_COLOR_PANEL_HEIGHT= 38;

    /** Color for dead people in trees. */
    private static final Color DEAD_COLOR= Color.RED;

    /** Color of immune people in trees. */
    private static final Color IMMUNE_COLOR= new Color(14, 186, 11, 255);

    /** CovidTree being displayed. */
    private CovidTree root;

    /** Number of time steps it took to create tree root. */
    private int steps;

    /** Split pane that takes up center space. */
    private final JSplitPane splitPane= new JSplitPane();

    /** GraphPanel that shows the CovidTree. */
    private GraphPanel<Human, HumanConnection> graphPanel;

    /** Display the width of the selected node. */
    private JLabel selectedDepthLabel= new JLabel();

    /** Display the width at the selected depth. */
    private JLabel selectedWidthLabel= new JLabel();

    /** Colored panel that highlights a particular depth level. */
    private ColoredPanel widthColorPanel= new ColoredPanel(new Color(63, 191, 191, 178));
    // private ColoredPanel widthColorPanel= new ColoredPanel(new Color(230, 191, 191, 178));

    /** Label displaying the "Depth". */
    private JLabel depthHeaderLabel= new JLabel("Depth");

    /** Labels that count up the depth labels. */
    private JLabel[] depthLabels;

    /** Display the number of frames in the simulation. */
    private JLabel framesLabel= new JLabel();

    /** Display the selected human. */
    private JLabel selectedHumanLabel= new JLabel();

    /** Display the parent of the selected human. */
    private JLabel selectedHumanParentLabel= new JLabel();

    /** Display the number of children of the selected human. */
    private JLabel selectedHumanChildrenCountLabel= new JLabel();

    /** Display the size of the subtree represented by the selected human. */
    private JLabel selectedHumanSubtreeCountLabel= new JLabel();

    /** Display the maximum depth of the subtree represented by the selected human, */
    private JLabel selectedHumanMaxDepthLabel= new JLabel();

    /** Display the maximum width of the subtree represented by the selected human. */
    private JLabel selectedHumanMaxWidthLabel= new JLabel();

    /** Display the previously selected human. */
    private JLabel twoHumanSelectedLabel= new JLabel();

    /** Display the Covid route from the previously selected human to the selected human. */
    private JLabel twoHumanCovidRouteLabel= new JLabel();

    /** Display the common ancestor of the previously selected human and the selected human. */
    private JLabel twoHumanCommonAncestorLabel= new JLabel();

    /** The previously selected human. */
    private Human previouslySelectedHuman;

    /** The currently selected human. */
    private Human selectedHuman;

    /** The circle that represents the currently selected human. */
    private GraphPanel<Human, HumanConnection>.Circle selectedCircle;

    /** The Covid route from the previously selected human to the currently selected human. */
    private List<Human> CovidRoutePrevToSelected;

    /** Constructor: a visible GUI for dt, with s time steps needed to construct it. */
    private CovidJFrame(CovidTree dt, int s) {
        super();
        setTitle("A4 Spreading Covid");

        root= dt;
        steps= s;
        getContentPane().add(splitPane, BorderLayout.CENTER);
        splitPane.setResizeWeight(1.0);

        fixGraphPanel(dt);
        splitPane.setRightComponent(graphPanel); // was left
        pack();

        var infoPanel= new JPanel();
        fixInformationPanel(infoPanel);
        splitPane.setLeftComponent(infoPanel); // was right

        // Double pack so that infoPanel.getWidth() can be used here
        pack();
        INFO_PANEL_MIN_WIDTH= infoPanel.getWidth();
        infoPanel.setMinimumSize(new Dimension(INFO_PANEL_MIN_WIDTH, 0));
        graphPanel
            .setPreferredSize(new Dimension(SCREEN_WIDTH - INFO_PANEL_MIN_WIDTH - X_OFFSET * 2,
                SCREEN_HEIGHT - Y_OFFSET * 2));
        pack();
        setLocationRelativeTo(null);

        moveCirclesToCorrectLocations();

        splitPane.addPropertyChangeListener(new PropertyChangeListener() {
            /** Handle fixing layout when splitpane line moves */
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt != null &&
                    evt.getPropertyName().equals(JSplitPane.LAST_DIVIDER_LOCATION_PROPERTY)) {
                    widthColorPanel
                        .setSize(new Dimension(graphPanel.getWidth(), widthColorPanel.getHeight()));
                    layoutNodes();
                }
            }
        });
        createAndAddCircleMouseListener();

        // Apply colors
        colorNodes(dt);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    /** Create and add a circle mouse listener for selecting people to the graphPanel */
    private void createAndAddCircleMouseListener() {
        graphPanel.addCircleMouseListener(new MouseListener() {
            @Override
            @SuppressWarnings("unchecked")
            /** Handle change of info when a circle is clicked */
            public void mouseClicked(MouseEvent e) {
                selectedCircle= (GraphPanel<Human, HumanConnection>.Circle) e.getSource();

                var p= selectedCircle.getRepresents();
                previouslySelectedHuman= selectedHuman;
                selectedHuman= p;

                try {
                    CovidRoutePrevToSelected= root.node(previouslySelectedHuman)
                        .CovidRouteTo(selectedHuman);
                } catch (Exception excp) {}

                widthColorPanel.setLocation(0,
                    selectedCircle.getY1() - widthColorPanel.getHeight() / 2);

                populateLabels();
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    /** Move circles on the graphPanel to correct locations. */
    public void moveCirclesToCorrectLocations() {
        // Move circles to correct locations
        widthColorPanel.setSize(new Dimension(graphPanel.getWidth(), WIDTH_COLOR_PANEL_HEIGHT));
        widthColorPanel.setLocation(0, -WIDTH_COLOR_PANEL_HEIGHT);
        layoutNodes();
        addComponentListener(new ComponentListener() {
            /** Handle fixing layout when the window is resized */
            @Override
            public void componentResized(ComponentEvent e) {
                widthColorPanel
                    .setSize(new Dimension(graphPanel.getWidth(), widthColorPanel.getHeight()));
                layoutNodes();

                if (selectedCircle != null) {
                    widthColorPanel.setLocation(0,
                        selectedCircle.getY1() - widthColorPanel.getHeight() / 2);
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {}

            @Override
            public void componentShown(ComponentEvent e) {}

            @Override
            public void componentHidden(ComponentEvent e) {}
        });

    }

    /** Store in field graphPanel the graph for dt. */
    private void fixGraphPanel(CovidTree dt) {
        graphPanel= new GraphPanel<>(new Network(dt));
        graphPanel.setLinesBendable(false);
        graphPanel.setCirclesDraggable(false);
        graphPanel.setCircleStringToDrawFunc((c) -> c.getRepresents().name());
        graphPanel.setLineStringToDrawFunc((l) -> "");
        graphPanel.setLineStrokeFunc(this::getStrokeFor);

        graphPanel.add(widthColorPanel);

        try {
            var depth= dt.maxDepth();
            depthLabels= new JLabel[depth + 1];
            for (var i= 0; i <= depth; i= i + 1) {
                depthLabels[i]= new JLabel(i + "");
                depthLabels[i].setSize(new Dimension(50, 50));
                graphPanel.add(depthLabels[i]);
                graphPanel.setComponentZOrder(depthLabels[i], 0);
            }
            graphPanel.add(depthHeaderLabel);
            depthHeaderLabel.setSize(new Dimension(50, 20));
            depthHeaderLabel.setLocation(20, 2);
        } catch (Exception e) {
            depthLabels= null;
        }
    }

    /** Place in infoPanel all the labels that give information about the selected human(s) */
    private void fixInformationPanel(JPanel infoPanel) {
        infoPanel.setBackground(new Color(216, 255, 235));
        // infoPanel.setBackground(new Color(240, 235, 255));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

//		infoPanel.setBackground(new Color(216, 255, 235));
//		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        infoPanel.add(new JLabel("                                                             "));
        infoPanel.add(framesLabel);
        infoPanel.add(new JLabel("                                                             "));
        infoPanel.add(selectedDepthLabel);
        infoPanel.add(selectedWidthLabel);
        infoPanel.add(new JLabel("  "));
        infoPanel.add(selectedHumanLabel);
        infoPanel.add(selectedHumanParentLabel);
        infoPanel.add(selectedHumanChildrenCountLabel);
        infoPanel.add(selectedHumanSubtreeCountLabel);
        infoPanel.add(selectedHumanMaxDepthLabel);
        infoPanel.add(selectedHumanMaxWidthLabel);
        infoPanel.add(new JLabel("  "));
        infoPanel.add(twoHumanSelectedLabel);
        infoPanel.add(twoHumanCovidRouteLabel);
        infoPanel.add(twoHumanCommonAncestorLabel);
    }

    /** Lay out the nodes in the CovidTree. Specifically, call recLayoutNodes(...) [below], with
     * particular starting arguments, and set the location of all of the depth labels. */
    private void layoutNodes() {
        var yPercent= 0.05f;
        var yInc= 1.0f / (root.maxDepth() + 1);

        recLayoutNodes(root, 0.025f, 0.975f, yPercent, yInc);
        if (depthLabels != null) {
            for (var i= 0; i < depthLabels.length; i++ ) {
                depthLabels[i].setLocation(20, (int) (yPercent * graphPanel.getHeight()) - 30);
                yPercent+= yInc;
            }
        }
    }

    /** Lay out the nodes of dt on the GraphPanel.<br>
     * xMin is the minimum percentage of the width this layout is allowed to take.<br>
     * xMax is the maximum percentage of the width this layout is allowed to take.<br>
     * yPercent is the current height percentage for this recursive call.<br>
     * yInc is the amount to increase yPercent in each recursive call.<br>
     * Precondition: 0 <= xMin <= yMax <= 1. */
    private void recLayoutNodes(CovidTree dt, float xMin, float xMax, float yPercent,
        float yInc) {
        var xCenter= (xMin + xMax) / 2.0f;
        var c= graphPanel.getCircle(dt.human());

        c.setX1((int) (graphPanel.getWidth() * xCenter));
        c.setY1((int) (graphPanel.getHeight() * yPercent));

        var maxWidthTotals= 0.0f;
        for (CovidTree child : dt.copyOfChildren()) {
            maxWidthTotals+= child.maxWidth();
        }

        var xInc= (xMax - xMin) / maxWidthTotals;
        yPercent+= yInc;
        for (CovidTree child : dt.copyOfChildren()) {
            float width= child.maxWidth();
            recLayoutNodes(child, xMin, xMin + xInc * width, yPercent, yInc);
            xMin+= xInc * width;
        }
    }

    /** Return a stroke (for drawing) for line l. If the line is part of the CovidRoute, <br>
     * return a bolded stroke; otherwise, a simple stroke. */
    private Stroke getStrokeFor(GraphPanel<Human, HumanConnection>.Line l) {
        if (CovidRoutePrevToSelected != null) {
            var p1= l.getC1().getRepresents();
            var p2= l.getC2().getRepresents();
            if (CovidRoutePrevToSelected.contains(p1) &&
                CovidRoutePrevToSelected.contains(p2)) {
                return new BasicStroke(3.0f);
            }
        }
        return new BasicStroke();
    }

    /** Color the nodes of dt. <br>
     * Nodes for dead people are colored DEAD_COLOR. Nodes for immune people are colored
     * IMMUNE_COLOR */
    private void colorNodes(CovidTree dt) {
        var c= graphPanel.getCircle(dt.human());
        c.setColor(dt.human().isDead() ? DEAD_COLOR : IMMUNE_COLOR);
        for (CovidTree child : dt.copyOfChildren()) {
            colorNodes(child);
        }
    }

    /** Repopulate the information labels that have been added to the infoPanel. Uses lambdas - not
     * covered in CS2110, you'll learn about them in CS3110 */
    private void populateLabels() {
        framesLabel.setText("No. steps in simulation: " + steps);

        var p= selectedHuman;
        populateLabel(() -> root.depth(p), selectedDepthLabel, "Selected human Depth");
        populateLabel(() -> root.widthAtDepth(root.depth(p)), selectedWidthLabel,
            "Selected Level Width");
        populateLabel(() -> p.name(), selectedHumanLabel, "Selected human");

        populateLabel(() -> {
            var parent= root.getParent(p);
            if (parent != null) return parent.name();
            else return "null";
        }, selectedHumanParentLabel, "Parent");

        populateLabel(() -> root.node(p).childrenSize(), selectedHumanChildrenCountLabel,
            "Children");
        populateLabel(() -> root.node(p).size(), selectedHumanSubtreeCountLabel,
            "Subtree Size");
        populateLabel(() -> root.node(p).maxDepth(), selectedHumanMaxDepthLabel,
            "Subtree height");
        populateLabel(() -> root.node(p).maxWidth(), selectedHumanMaxWidthLabel,
            "Subtree width");

        var pp= previouslySelectedHuman;
        populateLabel(() -> (pp != null ? pp.name() : "null") +
            " and " + p.name(), twoHumanSelectedLabel, "Selected People");
        if (pp != null) {
            populateLabel(() -> {
                var route= root.node(pp).CovidRouteTo(p);
                if (route == null) return null;
                else return Arrays.deepToString(route.stream().map(a -> a.name()).toArray());
            }, twoHumanCovidRouteLabel, "Covid Route");
            populateLabel(() -> {
                var parent= root.commonAncestor(pp, p);
                return parent == null ? "null" : parent.name();
            }, twoHumanCommonAncestorLabel, "Common Ancestor");
        }
    }

    /** Call f and put its output into l.<br>
     * If f returns, set l to text catenated with the output of the call.<br>
     * If the call results in an exception, set the text of l to text and the type of exception
     * instead. Uses lambdas - not covered in CS2110, you'll learn about them in CS3110 */
    private static void populateLabel(Supplier<?> f, JLabel l, String text) {
        final var INDENT= "   ";
        try {
            l.setText(INDENT + text + ": " + f.apply() + INDENT);
        } catch (Throwable t) {
            l.setText(INDENT + text + ": " + "Exception occurred - " + t.toString() + INDENT);
        }
    }

    /** An instance is a simple colored rectangle that is used to highlight a particular depth level
     * in the tree.
     *
     * @author Patashnik */
    @SuppressWarnings("serial")
    private static class ColoredPanel extends JPanel {
        private Color color;

        /** Constructor: a new ColoredPanel with the specified color. */
        private ColoredPanel(Color c) {
            color= c;
        }

        /** Paint this ColoredPanel by drawing a rectangle of its color, filling the whole area of
         * the Panel. */

        public @Override void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(color);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
