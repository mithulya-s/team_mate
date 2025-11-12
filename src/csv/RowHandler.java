package csv;

import base.Participant;
import utilities.Interest;
import utilities.PersonalityType;
import utilities.Role;


import java.util.ArrayList;
import java.util.List;

public class RowHandler {
    //Utility functions to parser validate and format CSV lines when readign or wrotong

    public static class Result{
        private final Participant participant;
        private final List<String> warnings; //to catch the warnings thrown during the reading
        public Result(Participant participant, List<String> warnings) {
            this.participant = participant;
            this.warnings = warnings;
        }

        //Getters for the validator to get the stored info
        public Participant getParticipant() {
            return participant;
        }

        public List<String> getWarnings() {
            return warnings;
        }

        // To check if the line is valid finally
        public boolean isValidLine() {
            return ((participant != null) && (warnings.isEmpty()));
        }

    }

    public Result readRow(String[] line) {
        //debugs
        System.out.println("DEBUG: Parsing row with " + line.length + " columns");
        for (int i = 0; i < line.length; i++) {
            System.out.println("  Column " + i + ": '" + line[i] + "'");
        }



            List<String> warnings = new ArrayList<>();

        Participant dummy=parseRow(line,warnings);
        if (dummy==null) {
            warnings.add("Parsing failure for the row");
            return new Result(null,warnings);
        }
        //calling the validate function
        validateLine(dummy,warnings);

        //return if all good
        if  (!warnings.isEmpty()) {
            return new Result(null,warnings);
        }
        return new Result(dummy,warnings);
    }

    // Function for the parsing logic
    private Participant parseRow(String[] line, List<String> warnings) {
        if (!(line.length ==8)) {
            warnings.add("Column count is invalid.");
            return null;
        }

        // Biodata section
        String id = line[0].trim();
        String fullName = line[1].trim();
        String email = line[2].trim();
        //String username = null;

        //Other sections
        //calling the warnings to catch and add if errored
        Interest interest = parseInterestCol(line[3].trim(), warnings);
        int skillLevel=parseLevelCol(line[4].trim(), warnings);
        Role role=parseRoleCol(line[5].trim(),warnings);
        int personScore=parsePersonalityScoreCol(line[6].trim(),warnings);
        PersonalityType perType= parsePersonalityType(line[7].trim(),warnings);

        //build the new participant, with the passed data
        return new Participant(id,fullName,email,interest,skillLevel,role,personScore,perType);

    }


    // Function to validate the thrown values and input values
    private void validateLine(Participant part, List<String> warnings) {
        if (part.getId() == null || part.getId().isEmpty())
            warnings.add("Missing ID.");

        if (part.getFullName() == null || part.getFullName().isEmpty())
            warnings.add("Missing full name.");

        if (part.getEmail() == null || !part.getEmail().contains("@") ||
                !part.getEmail().contains("."))
                warnings.add("Invalid email format.");

        if (part.getInterest() == null)
            warnings.add("Invalid interest.");

        if (part.getRole() == null)
            warnings.add("Invalid role.");

        if (part.getPersonalityType() == null)
            warnings.add("Invalid personality type.");

        if (part.getSkillLevel() < 1 || part.getSkillLevel() > 10)
            warnings.add("Skill level out of range: " + part.getSkillLevel());

        if (part.getPersonalityScore() < 0 || part.getPersonalityScore() > 100)
            warnings.add("Personality score out of range: " + part.getPersonalityScore());
    }

    // Helpers
    private Interest parseInterestCol(String col, List<String> warnings) {
        try {
            return Interest.valueOf(col.toUpperCase().
                    replace(":", "").
                    replace(" ", "")); // to chatch the csgo
        }catch (IllegalArgumentException e) {
            warnings.add("Invalid interest type."+ col);
            return null;
        }
    }
    private Role parseRoleCol(String col, List<String> warnings) {
        try {
            return Role.valueOf(col.toUpperCase());
        }catch (IllegalArgumentException e){
            warnings.add("Invalid personality type."+ col);
            return null;
        }
    }

    private PersonalityType parsePersonalityType(String col, List<String> warnings) {
        try {
            return PersonalityType.valueOf(col.toUpperCase());
        } catch (IllegalArgumentException e) {
            warnings.add("Invalid personality type: " + col);
            return null;
        }
    }

    private int parseLevelCol(String col, List<String> warnings) {
        try {
            return Integer.parseInt(col);
        } catch (NumberFormatException e) {
            warnings.add("Invalid skill level value."+ col);
            return -1;
        }
    }

    private int parsePersonalityScoreCol(String col, List<String> warnings) {
        try {
            return Integer.parseInt(col);
        } catch (NumberFormatException e) {
            warnings.add("Invalid personality score: "+ col);
            return -1;
        }
    }

}
