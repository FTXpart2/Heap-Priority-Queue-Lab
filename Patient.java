

public class Patient implements Comparable<Patient> {
    private String name;
    private String illnessDescription;
    private String medicalPriority;
    private String ageGroup;
    private int timestamp;
    private String doctorNote;

    public Patient(String name, String illnessDescription, String medicalPriority, String ageGroup, int timestamp) {
        this.name = name;
        this.illnessDescription = illnessDescription;
        this.medicalPriority = medicalPriority;
        this.ageGroup = ageGroup;
        this.timestamp = timestamp;
        this.doctorNote = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIllnessDescription() {
        return illnessDescription;
    }

    public void setIllnessDescription(String illnessDescription) {
        this.illnessDescription = illnessDescription;
    }

    public String getMedicalPriority() {
        return medicalPriority;
    }

    public void setMedicalPriority(String medicalPriority) {
        this.medicalPriority = medicalPriority;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getDoctorNote() {
        return doctorNote;
    }

    public void setDoctorNote(String doctorNote) {
        this.doctorNote = doctorNote;
    }

    @Override
    public int compareTo(Patient other) {
        int priorityComparison = comparePriority(this.medicalPriority, other.medicalPriority);
        if (priorityComparison != 0) {
            return priorityComparison;
        }
        if (!this.ageGroup.equals(other.ageGroup)) {
            return this.ageGroup.equals("child") ? -1 : 1;
        }
        return Integer.compare(this.timestamp, other.timestamp);
    }

    private int comparePriority(String priority1, String priority2) {
        if (priority1.equals(priority2)) return 0;
        if (priority1.equals("high")) return -1;
        if (priority1.equals("medium") && priority2.equals("low")) return -1;
        return 1;
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Illness: %s, Priority: %s, Age Group: %s, Timestamp: %d",
                name, illnessDescription, medicalPriority, ageGroup, timestamp);
    }
}