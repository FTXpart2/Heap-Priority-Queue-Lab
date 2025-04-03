import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Screen {
    private MinHeap<Patient> patientQueue;
    private DLList<Patient> dischargedPatientList;
    private int patientTimestamp;

    public Screen() {
        patientQueue = new MinHeap<Patient>(Comparator.naturalOrder());
        dischargedPatientList = new DLList<>();
        patientTimestamp = 0;

        JTextArea doctorViewArea = new JTextArea(5, 30);
        doctorViewArea.setEditable(false);

        Runnable updateDoctorView = () -> {
            if (!patientQueue.isEmpty()) {
                doctorViewArea.setText(patientQueue.peek().toString());
            } else {
                doctorViewArea.setText("No patients in queue.");
            }
        };

        JFrame mainFrame = new JFrame("Wesley's Hospital");
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel triagePanel = new JPanel(new BorderLayout());
        JPanel triageInputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        JTextField nameInputField = new JTextField();
        JTextField illnessInputField = new JTextField();
        JComboBox<String> priorityDropdown = new JComboBox<>(new String[]{"high", "medium", "low"});
        JComboBox<String> ageGroupDropdown = new JComboBox<>(new String[]{"child", "adult"});
        JButton addPatientButton = new JButton("Add Patient");
        JTextArea triageViewArea = new JTextArea(10, 30);
        triageViewArea.setEditable(false);

        triageInputPanel.setBorder(BorderFactory.createTitledBorder("Add New Patient"));
        triageInputPanel.add(new JLabel("Name:"));
        triageInputPanel.add(nameInputField);
        triageInputPanel.add(new JLabel("Illness:"));
        triageInputPanel.add(illnessInputField);
        triageInputPanel.add(new JLabel("Priority:"));
        triageInputPanel.add(priorityDropdown);
        triageInputPanel.add(new JLabel("Age Group:"));
        triageInputPanel.add(ageGroupDropdown);
        triageInputPanel.add(new JLabel());
        triageInputPanel.add(addPatientButton);

        JPanel triageDisplayPanel = new JPanel(new BorderLayout());
        triageDisplayPanel.setBorder(BorderFactory.createTitledBorder("Patient List"));
        triageDisplayPanel.add(new JScrollPane(triageViewArea), BorderLayout.CENTER);

        triagePanel.add(triageInputPanel, BorderLayout.NORTH);
        triagePanel.add(triageDisplayPanel, BorderLayout.CENTER);

        Runnable updateTriageView = () -> {
            triageViewArea.setText("");
            List<Patient> sortedPatients = new ArrayList<>(patientQueue.getElements());
            sortedPatients.sort(Comparator.naturalOrder());
            for (Patient patient : sortedPatients) {
                triageViewArea.append(patient + "\n");
            }
        };

        addPatientButton.addActionListener(e -> {
            String name = nameInputField.getText();
            String illness = illnessInputField.getText();
            String priority = (String) priorityDropdown.getSelectedItem();
            String ageGroup = (String) ageGroupDropdown.getSelectedItem();
            Patient patient = new Patient(name, illness, priority, ageGroup, patientTimestamp++);
            patientQueue.add(patient);
            updateTriageView.run();
            updateDoctorView.run();
        });

        JPanel updatePanel = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField searchNameField = new JTextField();
        JTextField updateIllnessField = new JTextField();
        JComboBox<String> updatePriorityDropdown = new JComboBox<>(new String[]{"high", "medium", "low"});
        JButton updatePatientButton = new JButton("Update Patient");

        updatePanel.setBorder(BorderFactory.createTitledBorder("Update Patient"));
        updatePanel.add(new JLabel("Search by Name:"));
        updatePanel.add(searchNameField);
        updatePanel.add(new JLabel("Update Illness:"));
        updatePanel.add(updateIllnessField);
        updatePanel.add(new JLabel("Update Priority:"));
        updatePanel.add(updatePriorityDropdown);
        updatePanel.add(new JLabel());
        updatePanel.add(updatePatientButton);

        triagePanel.add(updatePanel, BorderLayout.SOUTH);

        updatePatientButton.addActionListener(e -> {
            String searchName = searchNameField.getText();
            String newIllness = updateIllnessField.getText();
            String newPriority = (String) updatePriorityDropdown.getSelectedItem();

            boolean patientFound = false;
            for (Patient patient : patientQueue) {
                if (patient.getName().equalsIgnoreCase(searchName)) {
                    patientQueue.remove(patient);
                    patient.setIllnessDescription(newIllness);
                    patient.setMedicalPriority(newPriority);
                    patientQueue.add(patient);
                    updateTriageView.run();
                    updateDoctorView.run();
                    patientFound = true;
                    break;
                }
            }

            if (!patientFound) {
                JOptionPane.showMessageDialog(mainFrame, "Patient not found!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Patient updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel doctorPanel = new JPanel(new BorderLayout());
        JTextArea dischargedViewArea = new JTextArea(10, 30);
        dischargedViewArea.setEditable(false);
        JTextField doctorNoteField = new JTextField();
        JButton dischargePatientButton = new JButton("Discharge Patient");

        JPanel currentPatientPanel = new JPanel(new BorderLayout());
        currentPatientPanel.setBorder(BorderFactory.createTitledBorder("Current Patient"));
        currentPatientPanel.add(new JScrollPane(doctorViewArea), BorderLayout.CENTER);

        JPanel dischargePanel = new JPanel(new BorderLayout(5, 5));
        dischargePanel.setBorder(BorderFactory.createTitledBorder("Discharge Patient"));
        dischargePanel.add(new JLabel("Doctor's Note:"));
        dischargePanel.add(doctorNoteField, BorderLayout.CENTER);
        dischargePanel.add(dischargePatientButton, BorderLayout.EAST);

        JPanel dischargedPatientsPanel = new JPanel(new BorderLayout());
        dischargedPatientsPanel.setBorder(BorderFactory.createTitledBorder("Discharged Patients"));
        dischargedPatientsPanel.add(new JScrollPane(dischargedViewArea), BorderLayout.CENTER);

        doctorPanel.add(currentPatientPanel, BorderLayout.NORTH);
        doctorPanel.add(dischargePanel, BorderLayout.CENTER);
        doctorPanel.add(dischargedPatientsPanel, BorderLayout.SOUTH);

        updateDoctorView.run();

        dischargePatientButton.addActionListener(e -> {
            Patient patient = patientQueue.poll();
            if (patient != null) {
                String doctorNote = doctorNoteField.getText();
                patient.setDoctorNote(doctorNote);
                dischargedPatientList.add(patient);
                dischargedViewArea.append("Discharged: " + patient.getName() + " - Note: " + doctorNote + "\n");
                doctorNoteField.setText("");
                updateTriageView.run();
                updateDoctorView.run();
            }
        });

        tabbedPane.addTab("Triage Nurse", triagePanel);
        tabbedPane.addTab("Doctor", doctorPanel);

        mainFrame.add(tabbedPane);
        mainFrame.setSize(600, 500);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }
}
