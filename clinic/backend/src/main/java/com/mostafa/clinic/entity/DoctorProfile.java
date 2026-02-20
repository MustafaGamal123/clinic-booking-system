package com.mostafa.clinic.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * DoctorProfile without Lombok. Manual builder + getters/setters.
 */
@Entity
@Table(name = "doctor_profiles")
public class DoctorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // owning side
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String specialty;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private int experienceYears = 0;

    @Column(precision = 10, scale = 2)
    private BigDecimal consultationFee = BigDecimal.ZERO;

    // Comma-separated days: "MONDAY,TUESDAY,..."
    private String workingDays = "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY";

    // e.g. "09:00-17:00"
    private String workingHours = "09:00-17:00";

    private boolean available = true;

    public DoctorProfile() {}

    public DoctorProfile(Long id, User user, String specialty, String bio, int experienceYears,
                         BigDecimal consultationFee, String workingDays, String workingHours, boolean available) {
        this.id = id;
        this.user = user;
        this.specialty = specialty;
        this.bio = bio;
        this.experienceYears = experienceYears;
        this.consultationFee = consultationFee == null ? BigDecimal.ZERO : consultationFee;
        this.workingDays = workingDays;
        this.workingHours = workingHours;
        this.available = available;
    }

    // Manual builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id;
        private User user;
        private String specialty;
        private String bio;
        private int experienceYears;
        private BigDecimal consultationFee;
        private String workingDays;
        private String workingHours;
        private boolean available = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder specialty(String specialty) { this.specialty = specialty; return this; }
        public Builder bio(String bio) { this.bio = bio; return this; }
        public Builder experienceYears(int experienceYears) { this.experienceYears = experienceYears; return this; }
        public Builder consultationFee(BigDecimal consultationFee) { this.consultationFee = consultationFee; return this; }
        public Builder workingDays(String workingDays) { this.workingDays = workingDays; return this; }
        public Builder workingHours(String workingHours) { this.workingHours = workingHours; return this; }
        public Builder available(boolean available) { this.available = available; return this; }

        public DoctorProfile build() {
            DoctorProfile p = new DoctorProfile(id, user, specialty, bio, experienceYears,
                    consultationFee, workingDays, workingHours, available);
            if (user != null && user.getDoctorProfile() != p) {
                user.setDoctorProfile(p);
            }
            return p;
        }
    }

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }

    public BigDecimal getConsultationFee() { return consultationFee; }
    public void setConsultationFee(BigDecimal consultationFee) { this.consultationFee = consultationFee; }

    public String getWorkingDays() { return workingDays; }
    public void setWorkingDays(String workingDays) { this.workingDays = workingDays; }

    public String getWorkingHours() { return workingHours; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoctorProfile)) return false;
        DoctorProfile that = (DoctorProfile) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "DoctorProfile{id=" + id + ", specialty='" + specialty + "'}";
    }
}
