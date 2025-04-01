import java.time.LocalDateTime;

public class Patient implements Comparable<Patient> {
    private String name;
    private String illnessDescription;
    private String medicalPriority; // "high", "medium", "low"
    private String ageGroup; // "child", "adult"
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
        if (!this.medicalPriority.equals(other.medicalPriority)) {
            return this.medicalPriority.compareTo(other.medicalPriority);
        }
        if (!this.ageGroup.equals(other.ageGroup)) {
            return this.ageGroup.equals("child") ? -1 : 1;
        }
        return Integer.compare(this.timestamp, other.timestamp);
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Illness: %s, Priority: %s, Age Group: %s, Timestamp: %d",
                name, illnessDescription, medicalPriority, ageGroup, timestamp);
    }
}