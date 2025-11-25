/*
package csv;

import base.Participant;
import utilities.Interest;
import utilities.PersonalityType;
import utilities.Role;

import java.util.ArrayList;
import java.util.List;

public class RowHandler {

    public static class Result {
        private final Participant participant;
        private final List<String> warnings;

        public Result(Participant participant, List<String> warnings) {
            this.participant = participant;
            this.warnings = warnings != null ? warnings : new ArrayList<>();
        }

        public Participant getParticipant() {
            return participant;
        }

        public List<String> getWarnings() {
            return warnings;
        }

        public boolean isValidLine() {
            return (participant != null) && (warnings.isEmpty());
        }
    }

    public Result readRow(String[] line) {
        List<String> warnings = new ArrayList<>();

        if (line == null || line.length == 0) {
            warnings.add("Row is empty or null");
            return new Result(null, warnings);
        }

        Participant participant = parseRow(line, warnings);

        if (participant == null) {
            if (warnings.isEmpty()) {
                warnings.add("Failed to parse participant from row");
            }
            return new Result(null, warnings);
        }

        validateLine(participant, warnings);

        if (!warnings.isEmpty()) {
            return new Result(null, warnings);
        }

        return new Result(participant, warnings);
    }

    private Participant parseRow(String[] line, List<String> warnings) {
        if (line.length != 8) {
            warnings.add("Expected 8 columns, found " + line.length);
            return null;
        }

        try {
            String id = line[0].trim();
            String fullName = line[1].trim();
            String email = line[2].trim();

            Interest interest = parseInterestCol(line[3].trim(), warnings);
            int skillLevel = parseLevelCol(line[4].trim(), warnings);
            Role role = parseRoleCol(line[5].trim(), warnings);
            int personScore = parsePersonalityScoreCol(line[6].trim(), warnings);
            PersonalityType perType = parsePersonalityType(line[7].trim(), warnings);

            if (interest == null || role == null || perType == null ||
                    skillLevel == -1 || personScore == -1) {
                return null;
            }

            try {
                return new Participant(id, fullName, email, interest, skillLevel, role, personScore, perType);
            } catch (IllegalArgumentException e) {
                warnings.add("Participant validation failed: " + e.getMessage());
                return null;
            }

        } catch (Exception e) {
            warnings.add("Error parsing row: " + e.getMessage());
            return null;
        }
    }

    private void validateLine(Participant part, List<String> warnings) {
        if (part.getId() == null || part.getId().isEmpty()) {
            warnings.add("Missing ID");
        }

        if (part.getFullName() == null || part.getFullName().isEmpty()) {
            warnings.add("Missing full name");
        }

        if (part.getEmail() == null || !part.getEmail().contains("@") || !part.getEmail().contains(".")) {
            warnings.add("Invalid email format");
        }

        if (part.getInterest() == null) {
            warnings.add("Invalid interest");
        }

        if (part.getRole() == null) {
            warnings.add("Invalid role");
        }

        if (part.getPersonalityType() == null) {
            warnings.add("Invalid personality type");
        }

        if (part.getSkillLevel() < 1 || part.getSkillLevel() > 10) {
            warnings.add("Skill level out of range (1-10): " + part.getSkillLevel());
        }

        if (part.getPersonalityScore() < 0 || part.getPersonalityScore() > 100) {
            warnings.add("Personality score out of range (0-100): " + part.getPersonalityScore());
        }
    }

    private Interest parseInterestCol(String col, List<String> warnings) {
        if (col == null || col.isEmpty()) {
            warnings.add("Interest column is empty");
            return null;
        }

        try {
            String normalized = col.toUpperCase()
                    .replace(":", "")
                    .replace(" ", "")
                    .trim();
            return Interest.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            warnings.add("Invalid interest: '" + col + "'");
            return null;
        }
    }

    private Role parseRoleCol(String col, List<String> warnings) {
        if (col == null || col.isEmpty()) {
            warnings.add("Role column is empty");
            return null;
        }

        try {
            return Role.valueOf(col.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            warnings.add("Invalid role: '" + col + "'");
            return null;
        }
    }

    private PersonalityType parsePersonalityType(String col, List<String> warnings) {
        if (col == null || col.isEmpty()) {
            warnings.add("Personality type column is empty");
            return null;
        }

        try {
            return PersonalityType.valueOf(col.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            warnings.add("Invalid personality type: '" + col + "'");
            return null;
        }
    }

    private int parseLevelCol(String col, List<String> warnings) {
        if (col == null || col.isEmpty()) {
            warnings.add("Skill level column is empty");
            return -1;
        }

        try {
            return Integer.parseInt(col.trim());
        } catch (NumberFormatException e) {
            warnings.add("Invalid skill level: '" + col + "'");
            return -1;
        }
    }

    private int parsePersonalityScoreCol(String col, List<String> warnings) {
        if (col == null || col.isEmpty()) {
            warnings.add("Personality score column is empty");
            return -1;
        }

        try {
            return Integer.parseInt(col.trim());
        } catch (NumberFormatException e) {
            warnings.add("Invalid personality score: '" + col + "'");
            return -1;
        }
    }




}

 */