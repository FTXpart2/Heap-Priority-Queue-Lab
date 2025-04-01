import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HospitalManagementGUI {
    private MinHeap<Patient> priorityQueue;
    private DLList<Patient> dischargedPatients;
    private int timestampCounter;

    public HospitalManagementGUI() {
        priorityQueue = new MinHeap<>(Patient::compareTo);
        dischargedPatients = new DLList<>();
        timestampCounter = 0;

        // Declare doctorDisplayArea before updateDoctorDisplay
        JTextArea doctorDisplayArea = new JTextArea(5, 30);
        doctorDisplayArea.setEditable(false);

        // Helper method to update the doctorDisplayArea
        Runnable updateDoctorDisplay = () -> {
            if (!priorityQueue.isEmpty()) {
                doctorDisplayArea.setText(priorityQueue.peek().toString());
            } else {
                doctorDisplayArea.setText("No patients in queue.");
            }
        };

        // Initialize GUI components
        JFrame frame = new JFrame("Hospital Management");
        JTabbedPane tabbedPane = new JTabbedPane();

        // Triage Nurse View
        JPanel triagePanel = new JPanel(new BorderLayout());
        JPanel triageInputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        JTextField nameField = new JTextField();
        JTextField illnessField = new JTextField();
        JComboBox<String> priorityBox = new JComboBox<>(new String[]{"high", "medium", "low"});
        JComboBox<String> ageGroupBox = new JComboBox<>(new String[]{"child", "adult"});
        JButton addButton = new JButton("Add Patient");
        JTextArea triageDisplayArea = new JTextArea(10, 30);
        triageDisplayArea.setEditable(false);

        triageInputPanel.setBorder(BorderFactory.createTitledBorder("Add New Patient"));
        triageInputPanel.add(new JLabel("Name:"));
        triageInputPanel.add(nameField);
        triageInputPanel.add(new JLabel("Illness:"));
        triageInputPanel.add(illnessField);
        triageInputPanel.add(new JLabel("Priority:"));
        triageInputPanel.add(priorityBox);
        triageInputPanel.add(new JLabel("Age Group:"));
        triageInputPanel.add(ageGroupBox);
        triageInputPanel.add(new JLabel());
        triageInputPanel.add(addButton);

        JPanel triageDisplayPanel = new JPanel(new BorderLayout());
        triageDisplayPanel.setBorder(BorderFactory.createTitledBorder("Patient List"));
        triageDisplayPanel.add(new JScrollPane(triageDisplayArea), BorderLayout.CENTER);

        triagePanel.add(triageInputPanel, BorderLayout.NORTH);
        triagePanel.add(triageDisplayPanel, BorderLayout.CENTER);

        // Helper method to update the triageDisplayArea
        Runnable updateTriageDisplay = () -> {
            triageDisplayArea.setText(""); // Clear the display area
            for (Patient patient : priorityQueue) {
                triageDisplayArea.append(patient + "\n"); // Append each patient in the queue
            }
        };

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String illness = illnessField.getText();
            String priority = (String) priorityBox.getSelectedItem();
            String ageGroup = (String) ageGroupBox.getSelectedItem();
            Patient patient = new Patient(name, illness, priority, ageGroup, timestampCounter++);
            priorityQueue.add(patient);
            updateTriageDisplay.run(); // Update the Triage Nurse View
            updateDoctorDisplay.run(); // Update the Doctor's View with the new top-priority patient
        });

        // Doctor View
        JPanel doctorPanel = new JPanel(new BorderLayout());
        JTextArea dischargedDisplayArea = new JTextArea(10, 30);
        dischargedDisplayArea.setEditable(false);
        JTextField doctorNoteField = new JTextField();
        JButton dischargeButton = new JButton("Discharge Patient");

        JPanel currentPatientPanel = new JPanel(new BorderLayout());
        currentPatientPanel.setBorder(BorderFactory.createTitledBorder("Current Patient"));
        currentPatientPanel.add(new JScrollPane(doctorDisplayArea), BorderLayout.CENTER);

        JPanel dischargePanel = new JPanel(new BorderLayout(5, 5));
        dischargePanel.setBorder(BorderFactory.createTitledBorder("Discharge Patient"));
        dischargePanel.add(new JLabel("Doctor's Note:"), BorderLayout.WEST);
        dischargePanel.add(doctorNoteField, BorderLayout.CENTER);
        dischargePanel.add(dischargeButton, BorderLayout.EAST);

        JPanel dischargedPatientsPanel = new JPanel(new BorderLayout());
        dischargedPatientsPanel.setBorder(BorderFactory.createTitledBorder("Discharged Patients"));
        dischargedPatientsPanel.add(new JScrollPane(dischargedDisplayArea), BorderLayout.CENTER);

        doctorPanel.add(currentPatientPanel, BorderLayout.NORTH);
        doctorPanel.add(dischargePanel, BorderLayout.CENTER);
        doctorPanel.add(dischargedPatientsPanel, BorderLayout.SOUTH);

        // Initialize the doctorDisplayArea
        updateDoctorDisplay.run();

        dischargeButton.addActionListener(e -> {
            // Use poll() to remove the top-priority patient from the queue
            Patient patient = priorityQueue.poll();
            if (patient != null) {
                String doctorNote = doctorNoteField.getText();
                patient.setDoctorNote(doctorNote);
                dischargedPatients.add(patient); // Add the patient to the discharged list
                dischargedDisplayArea.append("Discharged: " + patient.getName() + " - Note: " + doctorNote + "\n");
                doctorNoteField.setText(""); // Clear the doctor's note field
                updateTriageDisplay.run(); // Update the Triage Nurse View
                updateDoctorDisplay.run(); // Update the Doctor's View with the next top-priority patient
            }
        });

        tabbedPane.addTab("Triage Nurse", triagePanel);
        tabbedPane.addTab("Doctor", doctorPanel);

        frame.add(tabbedPane);
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
