
package SUDOKU;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class design extends javax.swing.JFrame {
    private JTextField[][] cells;
    private int[][] solution;
    private int[][] puzzle;
    private Random random;
    private Border defaultBorder;
    private Border highlightBorder;
    
//initialize the components   
public design() {
        initComponents();
        initializeGame();
        addActionListeners();
        styleComponents();
    }
    
    private void initializeGame() {
    cells = new JTextField[9][9];
    solution = new int[9][9];
    puzzle = new int[9][9];
    random = new Random();
    
    // Map and style the text fields for cells and add 3x3 grid borders
    mapTextFields();
    addBordersFor3x3Grids();
    styleComponents();
}
 
//mapping all the textFields
    private void mapTextFields() {
    JTextField[] allFields = {
        jTextField1, jTextField2, jTextField3, jTextField4, jTextField5, jTextField6, jTextField7, jTextField8, jTextField9,
        jTextField10, jTextField11, jTextField12, jTextField13, jTextField14, jTextField15, jTextField16, jTextField17, jTextField18,
        jTextField19, jTextField20, jTextField21, jTextField22, jTextField23, jTextField24, jTextField25, jTextField26, jTextField27,
        jTextField28, jTextField29, jTextField30, jTextField31, jTextField32, jTextField33, jTextField34, jTextField35, jTextField36,
        jTextField37, jTextField38, jTextField39, jTextField40, jTextField41, jTextField42, jTextField43, jTextField44, jTextField45,
        jTextField46, jTextField47, jTextField48, jTextField49, jTextField50, jTextField51, jTextField52, jTextField53, jTextField54,
        jTextField55, jTextField56, jTextField57, jTextField58, jTextField59, jTextField60, jTextField61, jTextField62, jTextField63,
        jTextField64, jTextField65, jTextField66, jTextField67, jTextField68, jTextField69, jTextField70, jTextField71, jTextField72,
        jTextField73, jTextField74, jTextField75, jTextField76, jTextField77, jTextField78, jTextField79, jTextField80, jTextField81
    };
    
    int index = 0;
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            cells[i][j] = allFields[index++];
            cells[i][j].setHorizontalAlignment(JTextField.CENTER);
            cells[i][j].setFont(new Font("SansSerif", Font.BOLD, 20));
        }
    }
    }

//Add borders to each 3*3 squre set    
private void addBordersFor3x3Grids() {
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            Border border = BorderFactory.createMatteBorder(
                i % 3 == 0 ? 2 : 1, // Top
                j % 3 == 0 ? 2 : 1, // Left
                i == 8 ? 2 : 1,     // Bottom
                j == 8 ? 2 : 1      // Right
            , Color.BLACK);
            cells[i][j].setBorder(border);
        }
    }
}

// add the border and define cellFont
  private void styleCells() {
    Font cellFont = new Font("SansSerif", Font.BOLD, 20);

    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            cells[i][j].setHorizontalAlignment(JTextField.CENTER);
            cells[i][j].setFont(cellFont);

            // Add 3x3 grid borders for each cell
            if (i % 3 == 0 && i != 0) cells[i][j].setBorder(new LineBorder(Color.BLACK, 2));
            if (j % 3 == 0 && j != 0) cells[i][j].setBorder(new LineBorder(Color.BLACK, 2));

            // Additional border settings for corner boxes (optional for aesthetics)
            if (i % 3 == 0 && j % 3 == 0) cells[i][j].setBorder(new LineBorder(Color.BLACK, 2));
            else cells[i][j].setBorder(new LineBorder(Color.GRAY, 1));
        }
    }
}
    
    private void styleComponents() {
        levelCombo.setFont(new Font("SansSerif", Font.BOLD, 14));
        newBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        resetBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        solutionBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        // Style buttons
        Color buttonColor = new Color(70, 130, 180);
        Color buttonTextColor = Color.WHITE;
        
        newBtn.setBackground(buttonColor);
        newBtn.setForeground(buttonTextColor);
        resetBtn.setBackground(buttonColor);
        resetBtn.setForeground(buttonTextColor);
        solutionBtn.setBackground(buttonColor);
        solutionBtn.setForeground(buttonTextColor);
    }
    
// Adds action listeners to buttons and text fields
private void addActionListeners() {
    newBtn.addActionListener(e -> generateNewPuzzle());
    resetBtn.addActionListener(e -> resetPuzzle());
    solutionBtn.addActionListener(e -> showSolution());

    // Add DocumentListener for immediate validation on typing
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            final int row = i;
            final int col = j;
            cells[i][j].getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    validateCell(row, col);
                }

                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    validateCell(row, col);
                }

                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    validateCell(row, col);
                }
            });
        }
    }
}
 
// Generates a new Sudoku puzzle based on the selected difficulty level
private void generateNewPuzzle() {
    String level = levelCombo.getSelectedItem().toString();
    if (level.equals("Level")) {
        JOptionPane.showMessageDialog(this, "Please select a difficulty level before starting a new game.");
        return;
    }

    resetPuzzle();
    generateSolution();
    createPuzzle(level);
}
    
private void generateSolution() {
        // Initialize solution grid
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                solution[i][j] = 0;
            }
        }
        
        // Generate a valid solution using backtracking
        solveSudoku(solution);
    }
    
private boolean solveSudoku(int[][] grid) {
        int row = -1;
        int col = -1;
        boolean isEmpty = false;
        
        // Find empty cell
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) {
                    row = i;
                    col = j;
                    isEmpty = true;
                    break;
                }
            }
            if (isEmpty) break;
        }
        
        // No empty cell found
        if (!isEmpty) return true;
        
        // Try digits 1-9
        for (int num = 1; num <= 9; num++) {
            if (isSafe(grid, row, col, num)) {
                grid[row][col] = num;
                if (solveSudoku(grid)) return true;
                grid[row][col] = 0;
            }
        }
        return false;
    }
    
private boolean isSafe(int[][] grid, int row, int col, int num) {
    // Check the row
    for (int j = 0; j < 9; j++) {
        if (grid[row][j] == num) return false;
    }

    // Check the column
    for (int i = 0; i < 9; i++) {
        if (grid[i][col] == num) return false;
    }

    // Check the 3x3 box
    int boxRowStart = row - row % 3;
    int boxColStart = col - col % 3;
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (grid[boxRowStart + i][boxColStart + j] == num) return false;
        }
    }

    return true;
}

private void resetPuzzle() {
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            if (puzzle[i][j] == 0) {  // Only clear cells that are empty in the generated puzzle
                cells[i][j].setText("");
                cells[i][j].setEditable(true);
                cells[i][j].setForeground(Color.BLACK);  // Reset text color
                cells[i][j].setBackground(Color.WHITE);  // Reset background color
            }
        }
    }
}
    
private void createPuzzle(String level) {
        // Copy solution to puzzle
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                puzzle[i][j] = solution[i][j];
            }
        }
        
        // Determine cells to remove based on difficulty
        int cellsToRemove;
        switch (level) {
            case "Easy": cellsToRemove = 40; break;
            case "Medium": cellsToRemove = 50; break;
            case "Hard": cellsToRemove = 60; break;
            default: cellsToRemove = 40;
        }
        
        // Remove cells randomly
        while (cellsToRemove > 0) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);
            if (puzzle[row][col] != 0) {
                puzzle[row][col] = 0;
                cellsToRemove--;
            }
        }
        
        // Display puzzle
        displayPuzzle();
    }
    
private void displayPuzzle() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (puzzle[i][j] == 0) {
                    cells[i][j].setText("");
                    cells[i][j].setEditable(true);
                    cells[i][j].setBackground(Color.WHITE);
                } else {
                    cells[i][j].setText(String.valueOf(puzzle[i][j]));
                    cells[i][j].setEditable(false);
                    cells[i][j].setBackground(new Color(240, 240, 240));
                }
            }
        }
    }
    
private void showSolution() {
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            if (puzzle[i][j] == 0) {  // Only change color and set text for user-entered cells
                cells[i][j].setText(String.valueOf(solution[i][j]));
                cells[i][j].setForeground(Color.GREEN);  // Display solution in green
                cells[i][j].setEditable(false);  // Make solution cells non-editable
            } else {
                // Keep the original puzzle cells in their default color and non-editable state
                cells[i][j].setForeground(Color.BLACK);
                cells[i][j].setEditable(false);
            }
        }
    }
}   
private void validateCell(int row, int col) {
    String input = cells[row][col].getText().trim();

    // Check if the input is a valid number between 1 and 9
    if (!input.matches("[1-9]")) {
        cells[row][col].setForeground(Color.RED);  // Highlight out-of-range numbers
        return;
    }

    int value = Integer.parseInt(input);
    
    // Reset the color initially
    cells[row][col].setForeground(Color.BLACK);

    // Temporarily set the cell to 0 to ignore self-conflict
    puzzle[row][col] = 0;

    // Check for duplicates in the row, column, and 3x3 grid
    if (!isSafe(puzzle, row, col, value)) {
        cells[row][col].setForeground(Color.RED);  // Highlight duplicate numbers
    } else {
        cells[row][col].setForeground(Color.BLACK);  // Valid entry, set to default color
        puzzle[row][col] = value;  // Update the puzzle array with the entered value
    }
}

// Helper methods to check for repeated numbers
private boolean isRepeatedInRow(int row, int col, int value) {
    for (int j = 0; j < 9; j++) {
        if (j != col && !cells[row][j].getText().isEmpty() && 
            Integer.parseInt(cells[row][j].getText()) == value) {
            return true;
        }
    }
    return false;
}

private boolean isRepeatedInColumn(int row, int col, int value) {
    for (int i = 0; i < 9; i++) {
        if (i != row && !cells[i][col].getText().isEmpty() && 
            Integer.parseInt(cells[i][col].getText()) == value) {
            return true;
        }
    }
    return false;
}

private boolean isRepeatedInBox(int row, int col, int value) {
    int boxRow = row - row % 3;
    int boxCol = col - col % 3;
    
    for (int i = boxRow; i < boxRow + 3; i++) {
        for (int j = boxCol; j < boxCol + 3; j++) {
            if ((i != row || j != col) && !cells[i][j].getText().isEmpty() && 
                Integer.parseInt(cells[i][j].getText()) == value) {
                return true;
            }
        }
    }
    return false;
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        levelCombo = new javax.swing.JComboBox<>();
        newBtn = new javax.swing.JButton();
        solutionBtn = new javax.swing.JButton();
        resetBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jTextField13 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jTextField19 = new javax.swing.JTextField();
        jTextField20 = new javax.swing.JTextField();
        jTextField21 = new javax.swing.JTextField();
        jTextField22 = new javax.swing.JTextField();
        jTextField23 = new javax.swing.JTextField();
        jTextField24 = new javax.swing.JTextField();
        jTextField25 = new javax.swing.JTextField();
        jTextField26 = new javax.swing.JTextField();
        jTextField27 = new javax.swing.JTextField();
        jTextField28 = new javax.swing.JTextField();
        jTextField29 = new javax.swing.JTextField();
        jTextField30 = new javax.swing.JTextField();
        jTextField31 = new javax.swing.JTextField();
        jTextField32 = new javax.swing.JTextField();
        jTextField33 = new javax.swing.JTextField();
        jTextField34 = new javax.swing.JTextField();
        jTextField35 = new javax.swing.JTextField();
        jTextField36 = new javax.swing.JTextField();
        jTextField37 = new javax.swing.JTextField();
        jTextField38 = new javax.swing.JTextField();
        jTextField39 = new javax.swing.JTextField();
        jTextField40 = new javax.swing.JTextField();
        jTextField41 = new javax.swing.JTextField();
        jTextField42 = new javax.swing.JTextField();
        jTextField43 = new javax.swing.JTextField();
        jTextField44 = new javax.swing.JTextField();
        jTextField45 = new javax.swing.JTextField();
        jTextField46 = new javax.swing.JTextField();
        jTextField47 = new javax.swing.JTextField();
        jTextField48 = new javax.swing.JTextField();
        jTextField49 = new javax.swing.JTextField();
        jTextField50 = new javax.swing.JTextField();
        jTextField51 = new javax.swing.JTextField();
        jTextField52 = new javax.swing.JTextField();
        jTextField53 = new javax.swing.JTextField();
        jTextField54 = new javax.swing.JTextField();
        jTextField55 = new javax.swing.JTextField();
        jTextField56 = new javax.swing.JTextField();
        jTextField57 = new javax.swing.JTextField();
        jTextField58 = new javax.swing.JTextField();
        jTextField59 = new javax.swing.JTextField();
        jTextField60 = new javax.swing.JTextField();
        jTextField61 = new javax.swing.JTextField();
        jTextField62 = new javax.swing.JTextField();
        jTextField63 = new javax.swing.JTextField();
        jTextField64 = new javax.swing.JTextField();
        jTextField65 = new javax.swing.JTextField();
        jTextField66 = new javax.swing.JTextField();
        jTextField67 = new javax.swing.JTextField();
        jTextField68 = new javax.swing.JTextField();
        jTextField69 = new javax.swing.JTextField();
        jTextField70 = new javax.swing.JTextField();
        jTextField71 = new javax.swing.JTextField();
        jTextField72 = new javax.swing.JTextField();
        jTextField73 = new javax.swing.JTextField();
        jTextField74 = new javax.swing.JTextField();
        jTextField75 = new javax.swing.JTextField();
        jTextField76 = new javax.swing.JTextField();
        jTextField77 = new javax.swing.JTextField();
        jTextField78 = new javax.swing.JTextField();
        jTextField79 = new javax.swing.JTextField();
        jTextField80 = new javax.swing.JTextField();
        jTextField81 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SUDOKU/pastime (5).png"))); // NOI18N
        jLabel1.setText("Sudoku");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 0, 200, 40));

        jPanel2.setBackground(new java.awt.Color(255, 102, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        levelCombo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        levelCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Level", "Easy", "Medium", "Hard" }));
        levelCombo.setToolTipText("Select a difficulty level");
        jPanel2.add(levelCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 90, -1));

        newBtn.setBackground(new java.awt.Color(102, 204, 255));
        newBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        newBtn.setForeground(new java.awt.Color(255, 255, 255));
        newBtn.setText("New");
        newBtn.setToolTipText("Start a new game");
        newBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newBtnActionPerformed(evt);
            }
        });
        jPanel2.add(newBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 117, -1));

        solutionBtn.setBackground(new java.awt.Color(102, 204, 255));
        solutionBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        solutionBtn.setForeground(new java.awt.Color(255, 255, 255));
        solutionBtn.setText("Solution");
        solutionBtn.setToolTipText("Solution");
        solutionBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                solutionBtnActionPerformed(evt);
            }
        });
        jPanel2.add(solutionBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 10, 117, -1));

        resetBtn.setBackground(new java.awt.Color(102, 204, 255));
        resetBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        resetBtn.setForeground(new java.awt.Color(255, 255, 255));
        resetBtn.setText("Reset");
        resetBtn.setToolTipText("Reset ");
        jPanel2.add(resetBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, 117, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 810, 50));

        jPanel3.setAlignmentY(20.0F);
        jPanel3.setLayout(new java.awt.GridLayout(9, 9));

        jTextField1.setEditable(false);
        jPanel3.add(jTextField1);

        jTextField2.setEditable(false);
        jPanel3.add(jTextField2);

        jTextField3.setEditable(false);
        jPanel3.add(jTextField3);

        jTextField4.setEditable(false);
        jPanel3.add(jTextField4);

        jTextField5.setEditable(false);
        jPanel3.add(jTextField5);

        jTextField6.setEditable(false);
        jPanel3.add(jTextField6);

        jTextField7.setEditable(false);
        jPanel3.add(jTextField7);

        jTextField8.setEditable(false);
        jPanel3.add(jTextField8);

        jTextField9.setEditable(false);
        jPanel3.add(jTextField9);

        jTextField10.setEditable(false);
        jPanel3.add(jTextField10);

        jTextField11.setEditable(false);
        jPanel3.add(jTextField11);

        jTextField12.setEditable(false);
        jTextField12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField12ActionPerformed(evt);
            }
        });
        jPanel3.add(jTextField12);

        jTextField13.setEditable(false);
        jPanel3.add(jTextField13);

        jTextField14.setEditable(false);
        jPanel3.add(jTextField14);

        jTextField15.setEditable(false);
        jPanel3.add(jTextField15);

        jTextField16.setEditable(false);
        jPanel3.add(jTextField16);

        jTextField17.setEditable(false);
        jPanel3.add(jTextField17);

        jTextField18.setEditable(false);
        jPanel3.add(jTextField18);

        jTextField19.setEditable(false);
        jPanel3.add(jTextField19);

        jTextField20.setEditable(false);
        jPanel3.add(jTextField20);

        jTextField21.setEditable(false);
        jPanel3.add(jTextField21);

        jTextField22.setEditable(false);
        jPanel3.add(jTextField22);

        jTextField23.setEditable(false);
        jPanel3.add(jTextField23);

        jTextField24.setEditable(false);
        jPanel3.add(jTextField24);

        jTextField25.setEditable(false);
        jPanel3.add(jTextField25);

        jTextField26.setEditable(false);
        jPanel3.add(jTextField26);

        jTextField27.setEditable(false);
        jPanel3.add(jTextField27);

        jTextField28.setEditable(false);
        jPanel3.add(jTextField28);

        jTextField29.setEditable(false);
        jPanel3.add(jTextField29);

        jTextField30.setEditable(false);
        jPanel3.add(jTextField30);

        jTextField31.setEditable(false);
        jPanel3.add(jTextField31);

        jTextField32.setEditable(false);
        jPanel3.add(jTextField32);

        jTextField33.setEditable(false);
        jPanel3.add(jTextField33);

        jTextField34.setEditable(false);
        jPanel3.add(jTextField34);

        jTextField35.setEditable(false);
        jPanel3.add(jTextField35);

        jTextField36.setEditable(false);
        jPanel3.add(jTextField36);

        jTextField37.setEditable(false);
        jPanel3.add(jTextField37);

        jTextField38.setEditable(false);
        jPanel3.add(jTextField38);

        jTextField39.setEditable(false);
        jPanel3.add(jTextField39);

        jTextField40.setEditable(false);
        jPanel3.add(jTextField40);

        jTextField41.setEditable(false);
        jPanel3.add(jTextField41);

        jTextField42.setEditable(false);
        jPanel3.add(jTextField42);

        jTextField43.setEditable(false);
        jPanel3.add(jTextField43);

        jTextField44.setEditable(false);
        jPanel3.add(jTextField44);

        jTextField45.setEditable(false);
        jPanel3.add(jTextField45);

        jTextField46.setEditable(false);
        jPanel3.add(jTextField46);

        jTextField47.setEditable(false);
        jPanel3.add(jTextField47);

        jTextField48.setEditable(false);
        jPanel3.add(jTextField48);

        jTextField49.setEditable(false);
        jPanel3.add(jTextField49);

        jTextField50.setEditable(false);
        jPanel3.add(jTextField50);

        jTextField51.setEditable(false);
        jPanel3.add(jTextField51);

        jTextField52.setEditable(false);
        jPanel3.add(jTextField52);

        jTextField53.setEditable(false);
        jPanel3.add(jTextField53);

        jTextField54.setEditable(false);
        jPanel3.add(jTextField54);

        jTextField55.setEditable(false);
        jPanel3.add(jTextField55);

        jTextField56.setEditable(false);
        jPanel3.add(jTextField56);

        jTextField57.setEditable(false);
        jPanel3.add(jTextField57);

        jTextField58.setEditable(false);
        jPanel3.add(jTextField58);

        jTextField59.setEditable(false);
        jPanel3.add(jTextField59);

        jTextField60.setEditable(false);
        jPanel3.add(jTextField60);

        jTextField61.setEditable(false);
        jPanel3.add(jTextField61);

        jTextField62.setEditable(false);
        jPanel3.add(jTextField62);

        jTextField63.setEditable(false);
        jPanel3.add(jTextField63);

        jTextField64.setEditable(false);
        jPanel3.add(jTextField64);

        jTextField65.setEditable(false);
        jPanel3.add(jTextField65);

        jTextField66.setEditable(false);
        jPanel3.add(jTextField66);

        jTextField67.setEditable(false);
        jPanel3.add(jTextField67);

        jTextField68.setEditable(false);
        jPanel3.add(jTextField68);

        jTextField69.setEditable(false);
        jPanel3.add(jTextField69);

        jTextField70.setEditable(false);
        jPanel3.add(jTextField70);

        jTextField71.setEditable(false);
        jPanel3.add(jTextField71);

        jTextField72.setEditable(false);
        jPanel3.add(jTextField72);

        jTextField73.setEditable(false);
        jPanel3.add(jTextField73);

        jTextField74.setEditable(false);
        jPanel3.add(jTextField74);

        jTextField75.setEditable(false);
        jPanel3.add(jTextField75);

        jTextField76.setEditable(false);
        jPanel3.add(jTextField76);

        jTextField77.setEditable(false);
        jPanel3.add(jTextField77);

        jTextField78.setEditable(false);
        jPanel3.add(jTextField78);

        jTextField79.setEditable(false);
        jPanel3.add(jTextField79);

        jTextField80.setEditable(false);
        jPanel3.add(jTextField80);

        jTextField81.setEditable(false);
        jPanel3.add(jTextField81);

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 450, 400));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 827, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void solutionBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_solutionBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_solutionBtnActionPerformed

    private void jTextField12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField12ActionPerformed

    private void newBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(design.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(design.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(design.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(design.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new design().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField37;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField39;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField40;
    private javax.swing.JTextField jTextField41;
    private javax.swing.JTextField jTextField42;
    private javax.swing.JTextField jTextField43;
    private javax.swing.JTextField jTextField44;
    private javax.swing.JTextField jTextField45;
    private javax.swing.JTextField jTextField46;
    private javax.swing.JTextField jTextField47;
    private javax.swing.JTextField jTextField48;
    private javax.swing.JTextField jTextField49;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField50;
    private javax.swing.JTextField jTextField51;
    private javax.swing.JTextField jTextField52;
    private javax.swing.JTextField jTextField53;
    private javax.swing.JTextField jTextField54;
    private javax.swing.JTextField jTextField55;
    private javax.swing.JTextField jTextField56;
    private javax.swing.JTextField jTextField57;
    private javax.swing.JTextField jTextField58;
    private javax.swing.JTextField jTextField59;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField60;
    private javax.swing.JTextField jTextField61;
    private javax.swing.JTextField jTextField62;
    private javax.swing.JTextField jTextField63;
    private javax.swing.JTextField jTextField64;
    private javax.swing.JTextField jTextField65;
    private javax.swing.JTextField jTextField66;
    private javax.swing.JTextField jTextField67;
    private javax.swing.JTextField jTextField68;
    private javax.swing.JTextField jTextField69;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField70;
    private javax.swing.JTextField jTextField71;
    private javax.swing.JTextField jTextField72;
    private javax.swing.JTextField jTextField73;
    private javax.swing.JTextField jTextField74;
    private javax.swing.JTextField jTextField75;
    private javax.swing.JTextField jTextField76;
    private javax.swing.JTextField jTextField77;
    private javax.swing.JTextField jTextField78;
    private javax.swing.JTextField jTextField79;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField80;
    private javax.swing.JTextField jTextField81;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JComboBox<String> levelCombo;
    private javax.swing.JButton newBtn;
    private javax.swing.JButton resetBtn;
    private javax.swing.JButton solutionBtn;
    // End of variables declaration//GEN-END:variables
}
