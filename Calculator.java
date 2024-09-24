import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame {
    private JTextField resultField;
    private JButton[] numberButtons;
    private JButton[] operatorButtons;
    private JButton clearButton;
    private JButton equalsButton;

    public Calculator() {
        // Create the display field for showing results
        resultField = new JTextField(20);
        resultField.setEditable(false);
        resultField.setHorizontalAlignment(JTextField.RIGHT);  // Align text to the right
        resultField.setFont(new Font("Arial", Font.BOLD, 24));

        // Create the number buttons
        numberButtons = new JButton[10];
        for (int i = 0; i < 10; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].setBackground(new Color(255, 192, 203));
            numberButtons[i].setForeground(Color.BLACK);
            numberButtons[i].setFont(new Font("Arial", Font.BOLD, 18));
            numberButtons[i].addActionListener(new NumberButtonListener());
        }

        // Create the operator buttons
        operatorButtons = new JButton[4];
        String[] operators = {"+", "-", "x", "/"};
        for (int i = 0; i < 4; i++) {
            operatorButtons[i] = new JButton(operators[i]);
            operatorButtons[i].setBackground(new Color(255, 192, 203));
            operatorButtons[i].setForeground(Color.BLACK);
            operatorButtons[i].setFont(new Font("Arial", Font.BOLD, 18));
            operatorButtons[i].addActionListener(new OperatorButtonListener());
        }

        // Create clear and equals buttons
        clearButton = new JButton("C");
        clearButton.setBackground(new Color(255, 192, 203));
        clearButton.setForeground(Color.BLACK);
        clearButton.setFont(new Font("Arial", Font.BOLD, 18));
        clearButton.addActionListener(new ClearButtonListener());

        equalsButton = new JButton("=");
        equalsButton.setBackground(new Color(255, 192, 203));
        equalsButton.setForeground(Color.BLACK);
        equalsButton.setFont(new Font("Arial", Font.BOLD, 18));
        equalsButton.addActionListener(new EqualsButtonListener());

        // Set up panel layout for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4, 5, 5));  // Grid with space between buttons

        // Add buttons to the panel
        buttonPanel.add(numberButtons[7]);
        buttonPanel.add(numberButtons[8]);
        buttonPanel.add(numberButtons[9]);
        buttonPanel.add(operatorButtons[0]);  // +

        buttonPanel.add(numberButtons[4]);
        buttonPanel.add(numberButtons[5]);
        buttonPanel.add(numberButtons[6]);
        buttonPanel.add(operatorButtons[1]);  // -

        buttonPanel.add(numberButtons[1]);
        buttonPanel.add(numberButtons[2]);
        buttonPanel.add(numberButtons[3]);
        buttonPanel.add(operatorButtons[2]);  // x

        buttonPanel.add(clearButton);
        buttonPanel.add(numberButtons[0]);
        buttonPanel.add(equalsButton);
        buttonPanel.add(operatorButtons[3]);  // /

        // Add the components to the frame
        setLayout(new BorderLayout(5, 5));  // Add padding between components
        add(resultField, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // Set up the frame
        setTitle("Calculator");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // ActionListener for number buttons
    private class NumberButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            resultField.setText(resultField.getText() + button.getText());
        }
    }

    // ActionListener for operator buttons
    private class OperatorButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String operator = button.getText();
            if (operator.equals("x")) {
                operator = "*";  // Replace "x" with "*" for evaluation
            }
            resultField.setText(resultField.getText() + " " + operator + " ");
        }
    }

    // ActionListener for the clear button
    private class ClearButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            resultField.setText("");
        }
    }

    // ActionListener for the equals button
    private class EqualsButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                double result = eval(resultField.getText());
                resultField.setText(String.valueOf(result));
            } catch (Exception ex) {
                resultField.setText("Error");
            }
        }
    }

    // Evaluation logic for the calculator
    private double eval(String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double v = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return v;
            }

            double parseExpression() {
                double v = parseTerm();
                while (true) {
                    if (eat('+')) {
                        v += parseTerm();
                    } else if (eat('-')) {
                        v -= parseTerm();
                    } else {
                        break;
                    }
                }
                return v;
            }

            double parseTerm() {
                double v = parseFactor();
                while (true) {
                    if (eat('*')) {
                        v *= parseFactor();
                    } else if (eat('/')) {
                        v /= parseFactor();
                    } else {
                        break;
                    }
                }
                return v;
            }

            double parseFactor() {
                double v;
                if (eat('(')) {
                    v = parseExpression();
                    eat(')');
                } else {
                    v = 0;
                    while (ch >= '0' && ch <= '9') {
                        v = v * 10 + (ch - '0');
                        nextChar();
                    }
                }
                return v;
            }
        }.parse();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Calculator();
        });
    }
}
