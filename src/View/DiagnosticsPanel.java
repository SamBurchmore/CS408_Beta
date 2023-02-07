package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class DiagnosticsPanel extends JPanel {

    // The label where the current simulation step is displayed
    private JLabel currentStepLabel;

    private Object[][] agentStatistics = {{"Agent 1", 0, 0.0, 0.0, 0}, {"Agent 2", 0, 0.0, 0.0, 0}, {"Agent 3", 0, 0.0, 0.0, 0}, {"Agent 4", 0, 0.0, 0.0, 0}, {"Agent 5", 0, 0.0, 0.0, 0}, {"Agent 6", 0, 0.0, 0.0, 0}, {"Agent 7", 0, 0.0, 0.0, 0}, {"Agent 8", 0, 0.0, 0.0, 0}};
    private String[] agentStatNames = {"<html>Agent<br></html>" , "<html>Population<br></html>", "<html>Average<br>Energy</html>", "<html>Average<br>Age</html>", "<html>Born Last<br>Step</html>"}; //{"<html>Population<br></html>", "<html>Average<br>Energy</html>", "<html>Average<br>Age</html>"};
    private JTable agentStatsTable;
    private JScrollPane agentStatsTableScrollPane;

    // This is where log messages will be output, stuff like "[AGENT] step 5343: blue agent has gone extinct", or [ENVIRONMENT] step 1000: year 4
    private JTextArea logTextArea;
    private JScrollPane logScrollPane;
    private JLabel logTextAreaLabel;

    // This is where the environments statistics will be shown, stuff like total energy, available space, etc
    private JPanel environmentStatsPanel;

    private JLabel maxEnvironmentEnergyLabel;
    private JLabel maxEnvironmentEnergyValueLabel;

    private JLabel currentEnvironmentEnergyLabel;
    private JLabel currentEnvironmentEnergyValueLabel;

    private JLabel currentEnvironmentEnergyPercentLabel;
    private JLabel currentEnvironmentEnergyPercentValueLabel;


    public DiagnosticsPanel() {
        super();
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(410, 765));
        setBorder(BorderFactory.createLineBorder(Color.darkGray));

        // First all elements are defined and their attributes set
        currentStepLabel = new JLabel("Day 0");
        currentStepLabel.setHorizontalAlignment(SwingConstants.CENTER);
        currentStepLabel.setPreferredSize(new Dimension(400, 35));
        currentStepLabel.setFont(new Font("Dialog", Font.BOLD, 20));

        // Here we build the agent stats table
        TableModel tableModel = new DefaultTableModel(agentStatistics, agentStatNames);
        agentStatsTable = new JTable(tableModel);
        agentStatsTableScrollPane = new JScrollPane(agentStatsTable);
        agentStatsTableScrollPane.setBorder(null);
        agentStatsTableScrollPane.setPreferredSize(new Dimension(400, 202));
        agentStatsTable.setFillsViewportHeight(true);
        agentStatsTable.setDefaultEditor(Object.class, null);
        agentStatsTable.getTableHeader().setPreferredSize(new Dimension(400, 35));
        agentStatsTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));

        // Now we configure the log output text area and its label
        logTextArea = new JTextArea(18, 8);
        logScrollPane = new JScrollPane(logTextArea);
        logScrollPane.setPreferredSize(new Dimension(400, 300));
        logTextAreaLabel = new JLabel("Info Log:");
        logTextAreaLabel.setOpaque(true);
        logTextAreaLabel.setPreferredSize(new Dimension(400, 15));
        logTextAreaLabel.setFont(new Font("Dialog", Font.BOLD, 12));

        // Now we configure the environment statistics panel
        environmentStatsPanel = new JPanel(new GridLayout(3, 2));
        environmentStatsPanel.setPreferredSize(new Dimension(396, 100));
        //environmentStatsPanel.setBorder(BorderFactory.createLineBorder(new Color(214, 214, 214), 2));

        maxEnvironmentEnergyLabel = new JLabel("     Max Environment Energy: ");
        maxEnvironmentEnergyLabel.setPreferredSize(new Dimension(130, 15));
        currentEnvironmentEnergyLabel = new JLabel("     Current Environment Energy: ");
        currentEnvironmentEnergyLabel.setPreferredSize(new Dimension(130, 15));
        currentEnvironmentEnergyPercentLabel = new JLabel("     Available Energy Percent: ");
        currentEnvironmentEnergyPercentLabel.setPreferredSize(new Dimension(130, 15));

        maxEnvironmentEnergyValueLabel = new JLabel();
        maxEnvironmentEnergyValueLabel.setPreferredSize(new Dimension(150, 15));
        maxEnvironmentEnergyLabel.setHorizontalAlignment(SwingConstants.LEFT);
        currentEnvironmentEnergyValueLabel = new JLabel();
        currentEnvironmentEnergyValueLabel.setHorizontalAlignment(SwingConstants.LEFT);
        currentEnvironmentEnergyValueLabel.setPreferredSize(new Dimension(150, 15));
        currentEnvironmentEnergyPercentValueLabel = new JLabel();
        currentEnvironmentEnergyPercentValueLabel.setHorizontalAlignment(SwingConstants.LEFT);
        currentEnvironmentEnergyPercentValueLabel.setPreferredSize(new Dimension(150, 15));

        environmentStatsPanel.add(maxEnvironmentEnergyLabel);
        environmentStatsPanel.add(maxEnvironmentEnergyValueLabel);
        environmentStatsPanel.add(currentEnvironmentEnergyLabel);
        environmentStatsPanel.add(currentEnvironmentEnergyValueLabel);
        environmentStatsPanel.add(currentEnvironmentEnergyPercentLabel);
        environmentStatsPanel.add(currentEnvironmentEnergyPercentValueLabel);

        // The GridBag constraints we'll be using to build this panel
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 2;
        c.insets = new Insets(1, 1, 1, 1);
        c.weightx = 1;
        c.weighty = 0;

        // Now we add the top row components
        c.gridy = 0;
        add(currentStepLabel, c);

        // Now we add the second row components
        c.gridy = 1;
        add(agentStatsTableScrollPane, c);

        // Now we add the third row components
        c.gridy = 2;
        add(environmentStatsPanel, c);

        // Now we add the forth row components
        c.gridy = 3;
        c.insets = new Insets(1, 1, 0, 1);
        add(logTextAreaLabel, c);

        // Now we add the fifth row components
        c.gridy = 4;
        c.insets = new Insets(0, 1, 0, 1);
        add(logScrollPane, c);

    }

    public void setAgentStats(Object[][] agentStats) {
        DefaultTableModel model = (DefaultTableModel) agentStatsTable.getModel();
        Object[] row = new Object[8];
        for (int i = 0; i < 8; i++) {
            row[0] = agentStats[0][i];
            row[1] = agentStats[1][i];
            row[2] = agentStats[2][i];
            row[3] = agentStats[3][i];
            row[4] = agentStats[4][i];
            model.removeRow(i);
            model.insertRow(i, row);
        }
        agentStatsTable.setModel(model);
    }

    public void setEnvironmentStats(Object[] environmentStats) {
        maxEnvironmentEnergyValueLabel.setText(environmentStats[0].toString());
        currentEnvironmentEnergyValueLabel.setText(environmentStats[1].toString());
        currentEnvironmentEnergyPercentValueLabel.setText(environmentStats[2].toString() + "%");
    }

    public void clearLog() {
        logTextArea.setText("");
    }

    public void addLogMessage(String logMessage) {
        logTextArea.append(logMessage + "\n");
    }

    public void setStepLabel(long step) {
        currentStepLabel.setText("Step: " + step);
    }

    public void clearStepLabel() {
        currentStepLabel.setText("Step: 0");
    }

}
