package View;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainView extends JFrame {

    private EnvironmentSettingsPanel environmentSettingsPanel;
    private SimulationControlPanel simulationControlPanel;
    private AgentEditorPanel agentEditorPanel;
    private WorldPanel worldPanel;

    public MainView() {

        getContentPane().setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        setSize(900, 900);
        setTitle("MainView");

        // The GridBag constraints we'll be using to build the GUI
        GridBagConstraints c = new GridBagConstraints();

        worldPanel = new WorldPanel();
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;this.add(worldPanel, c);

        agentEditorPanel = new AgentEditorPanel();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        this.add(agentEditorPanel, c);

        simulationControlPanel = new SimulationControlPanel();
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        this.add(simulationControlPanel, c);

        environmentSettingsPanel = new EnvironmentSettingsPanel();
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        this.add(environmentSettingsPanel, c);

        pack();
    }

    public void updateWorldPanel(BufferedImage worldImage, int i) {
        this.worldPanel.updateWorldImage(worldImage);
        this.repaint();
        this.setTitle("Step: " + ((Integer) i).toString());
    }

    public JButton getRunStepButton() {
        return simulationControlPanel.getRunStepButton();
    }

    public JButton getRunNStepsButton() {
        return simulationControlPanel.getRunNStepsButton();
    }

    public JSpinner getRunNStepsSpinner() {
        return simulationControlPanel.getRunNStepsSpinner();
    }

    public JButton getPopulateButton() {
        return simulationControlPanel.getPopulateButton();
    }

    public JButton getClearButton() {
        return simulationControlPanel.getClearButton();
    }

}
