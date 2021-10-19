import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * Point.java
 *
 * Description: Point class
 *
 * Sources: None
 *
 * @author Lakshmi Valaboju
 * @version July 23, 2021
 */

public class Paint extends JComponent implements Runnable {
    private Image image; // the canvas
    private Graphics2D graphics2D;  // this will enable drawing
    private int curX; // current mouse x coordinate
    private int curY; // current mouse y coordinate
    private int oldX; // previous mouse x coordinate
    private int oldY; // previous mouse y coordinate

    JButton hexButton;
    JButton rgbButton;
    JButton clearButton;
    JButton fillButton;
    JButton eraseButton;
    JButton randomButton;

    JTextField hexText;
    JTextField redText;
    JTextField greenText;
    JTextField blueText;

    Paint paint;
    JFrame frame;

    public Paint() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // set oldX and oldY coordinates to beginning mouse press
                oldX = e.getX();
                oldY = e.getY();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // set current coordinates to where mouse is being dragged
                curX = e.getX();
                curY = e.getY();

                // draw the line between old coordinates and new ones
                graphics2D.drawLine(oldX, oldY, curX, curY);

                // refresh frame and reset old coordinates
                repaint();
                oldX = curX;
                oldY = curY;

            }
        });

    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent a) {
            String hex;
            String red;
            String green;
            String blue;
            Color randomColor = null;
            paint.draw();

            if (a.getSource() == clearButton) {
                paint.clear();
                redText.setText("");
                greenText.setText("");
                blueText.setText("");
                hexText.setText("#");
            }
            if (a.getSource() == eraseButton) {
                paint.erase();

            }
            if (a.getSource() == randomButton) {
                randomColor = paint.randomColor();

                hex = String.format("#%02x%02x%02x", randomColor.getRed(),
                        randomColor.getGreen(), randomColor.getBlue());
                red = Integer.toString(randomColor.getRed());
                green = Integer.toString(randomColor.getGreen());
                blue = Integer.toString(randomColor.getBlue());

                redText.setText(red);
                greenText.setText(green);
                blueText.setText(blue);
                hexText.setText(hex);

            }
            if (a.getSource() == fillButton) {
                paint.fill();
                redText.setText("");
                greenText.setText("");
                blueText.setText("");
                hexText.setText("#");
            }
            if (a.getSource() == hexButton) {
                try {
                    hex = hexText.getText();
                    Color clr = Color.decode(hex);
                    paint.hex(clr);

                    clr.getBlue();
                    clr.getRed();
                    clr.getGreen();

                    green = Integer.toString(clr.getGreen());
                    red = Integer.toString(clr.getRed());
                    blue = Integer.toString(clr.getBlue());

                    redText.setText(red);
                    greenText.setText(green);
                    blueText.setText(blue);
                } catch (NumberFormatException ae) {
                    JOptionPane.showMessageDialog(null, "Not a valid Hex Value", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            if (a.getSource() == rgbButton) {
                if (redText.getText().isBlank() || redText == null) {
                    redText.setText("0");
                }
                if (blueText.getText().isBlank() || blueText == null) {
                    blueText.setText("0");
                }
                if (greenText.getText().isBlank() || greenText == null) {
                    greenText.setText("0");
                }

                try {
                    int r = Integer.valueOf(redText.getText());
                    int g = Integer.valueOf(greenText.getText());
                    int b = Integer.valueOf(blueText.getText());
                    Color color = new Color(r, g, b);

                    hex = String.format("#%02x%02x%02x", r, g, b);
                    hexText.setText(hex);

                    paint.rgb(color);
                    hexText.setText(hex);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Not a valid RGB Value", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ie) {
                    JOptionPane.showMessageDialog(null, "Not a valid RGB Value", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    };


    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);

            // this lets us draw on the image (ie. the canvas)
            graphics2D = (Graphics2D) image.getGraphics();

            // gives us better rendering quality for the drawing lines
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // set canvas to white with default paint color
            graphics2D.setPaint(Color.white);
            graphics2D.fillRect(0, 0, getSize().width, getSize().height);
            graphics2D.setPaint(Color.black);
            repaint();
        }
        g.drawImage(image, 0, 0, null);
    }


    public Color randomColor() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        Color randomColor = new Color(r, g, b);
        graphics2D.setColor(randomColor);
        return randomColor;
    }

    public void clear() {
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(Color.black);
        repaint();
    }

    public void fill() {
        graphics2D.setPaint(graphics2D.getColor());
        graphics2D.setBackground(graphics2D.getColor());
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(Color.black);
        repaint();
    }

    public void draw() {
        graphics2D.setStroke(new BasicStroke(5));
    }

    public void erase() {
        graphics2D.setColor(graphics2D.getBackground());
    }

    public void hex(Color clr) {
        graphics2D.setPaint(clr);
    }

    public void rgb(Color clr) {
        graphics2D.setPaint(clr);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Paint());
    }


    @Override
    public void run() {
        frame = new JFrame("Paint");

        Container content = frame.getContentPane();

        content.setLayout(new BorderLayout());
        paint = new Paint();
        content.add(paint, BorderLayout.CENTER);

        content.add(paint);

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        JPanel panelNorth = new JPanel();

        fillButton = new JButton("Fill");
        fillButton.addActionListener(actionListener);

        eraseButton = new JButton("Erase");
        eraseButton.addActionListener(actionListener);

        randomButton = new JButton("Random");
        randomButton.addActionListener(actionListener);

        clearButton = new JButton("Clear");
        clearButton.addActionListener(actionListener);

        panelNorth.add(fillButton);
        panelNorth.add(eraseButton);
        panelNorth.add(randomButton);
        panelNorth.add(clearButton);
        content.add(panelNorth, BorderLayout.NORTH);

        JPanel panelSouth = new JPanel();

        hexButton = new JButton("Hex");
        hexButton.addActionListener(actionListener);

        rgbButton = new JButton("RGB");
        rgbButton.addActionListener(actionListener);

        hexText = new JTextField("#", 10);

        redText = new JTextField("0", 5);
        greenText = new JTextField("0", 5);
        blueText = new JTextField("0", 5);

        panelSouth.add(hexButton);
        panelSouth.add(rgbButton);
        panelSouth.add(hexText);

        panelSouth.add(redText);
        panelSouth.add(greenText);
        panelSouth.add(blueText);

        content.add(panelSouth, BorderLayout.SOUTH);

    }
}