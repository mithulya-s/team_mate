# 🎮 TeamMate - Intelligent Team Formation Application

**TeamMate** is an intelligent team forming application built for a university gaming club. It leverages a sophisticated heuristic teaming algorithm to optimally form balanced teams based on participants' personality types, skill levels, roles, and interests.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Architecture & Design Patterns](#architecture--design-patterns)
- [Getting Started](#getting-started)
- [Usage Guide](#usage-guide)
  - [Participant Flow](#participant-flow)
  - [Organizer Flow](#organizer-flow)
- [Technical Details](#technical-details)
- [Requirements](#requirements)
- [File Format](#file-format)

---

## Overview

TeamMate is a comprehensive Java-based application designed to automate and optimize team formation for gaming club events. The application distinguishes between two user roles:

1. **Participants** - Complete surveys and lookup their assigned teams
2. **Organizers** - Upload participant records, form teams, and export results

The core algorithm prioritizes **personality types** (especially leaders and thinkers) and **skill balance** to ensure fair, functional, and effective team compositions.

---

## ✨ Features

### Participant Features
- 📝 **Interactive Survey** - Participants provide personal information and answer personality assessment questions
- 🔍 **Team Lookup** - Search for assigned teams using participant ID
- 💾 **Survey Data Persistence** - Responses saved for team formation

### Organizer Features
- 🔐 **Secure Authentication** - Role-based access control with login credentials
- 📂 **Batch Processing** - Load and validate participant data from CSV files
- 🧩 **Intelligent Team Formation** - Heuristic-based algorithm for optimal team composition
- 📊 **Team Management** - View, analyze, and export formed teams
- 💾 **CSV Export** - Save teams to `formed_teams.csv` for distribution

### Core Capabilities
- **Data Validation** - Comprehensive validation of participant attributes (email, skill levels, personality scores)
- **Concurrent Processing** - Multi-threaded CSV reading for improved performance
- **Error Handling** - Detailed warnings and user-friendly error messages
- **Flexible Team Sizing** - Customizable team sizes with boundary checks
- **Personality Assessment** - 5-question assessment to categorize participants into personality types

---

## Project Structure

```
team_mate/
├── src/
│   ├── Main.java                          # Application entry point
│   ├── base/
│   │   ├── Participant.java              # Participant data model
│   │   └── Team.java                     # Team data model
│   ├── services/
│   │   ├── SurveyService.java            # Participant survey orchestration
│   │   ├── SurveyPrompter.java           # Survey UI/UX handling
│   │   ├── OrganizerService.java         # Organizer functionality coordinator
│   │   ├── FormationController.java      # Team formation workflow
│   │   ├── FormationRunner.java          # Parallel team formation execution
│   │   ├── TeamBuilder.java              # Core teaming algorithm
│   │   └── ParticipantLookup.java        # Participant search functionality
│   ├── csv/
│   │   ├── ParticipantCsvReader.java     # Read participant data from CSV
│   │   ├── TeamCsvReader.java            # Read formed teams from CSV
│   │   ├── TeamsCsvWriter.java           # Write teams to CSV
│   │   ├── CsvReadable.java              # CSV reading interface
│   │   ├── CsvWritable.java              # CSV writing interface
│   │   └── ProcessCsvResult.java         # CSV processing result wrapper
│   ├── utilities/
│   │   ├── Authenticator.java            # Singleton authentication handler
│   │   ├── Interest.java                 # Enum for gaming interests
│   │   ├── Role.java                     # Enum for team roles
│   │   ├── PersonalityType.java          # Enum for personality types
│   ├── older/                            # Legacy/archived code
│   └── README.md
├── participants.csv                       # Sample participant data
└── TeamMate.iml                           # IntelliJ project file
```

---

## Architecture & Design Patterns

### Design Patterns Implemented

1. **Singleton Pattern**
   - `Authenticator`: Ensures single authentication state throughout application runtime

2. **Strategy Pattern**
   - `CsvReadable` / `CsvWritable` interfaces enable flexible CSV handling implementations

3. **Factory Pattern**
   - `TeamBuilder`: Creates teams using a sophisticated heuristic algorithm

4. **Observer/MVC Pattern**
   - Service layer separates business logic from presentation (surveys, team formation)

5. **Repository Pattern**
   - `ParticipantCsvReader` / `TeamCsvReader` abstract data persistence

### Separation of Concerns

- **Main.java** - Flow orchestration only
- **Services** - Business logic and coordination
- **Base** - Data models with validation
- **CSV** - Persistence layer
- **Utilities** - Cross-cutting concerns (auth, enums)

### Concurrency

- **Multi-threaded CSV Processing**: Uses `ExecutorService` with thread pool size of 4
- **Parallel Team Formation**: `FormationRunner` uses 8 threads for attempting multiple formation strategies

---

## Getting Started

### Prerequisites

- Java 11 or higher
- An IDE (IntelliJ IDEA, Eclipse, or VS Code with Java extensions)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/mithulya-s/team_mate.git
   cd team_mate
   ```

2. Open the project in your IDE

3. Compile the project:
   ```bash
   javac -d bin src/**/*.java
   ```

4. Run the application:
   ```bash
   java -cp bin Main
   ```

---

## Usage Guide

### Participant Flow

#### 1. Survey Completion
- Select "Participant" from main menu
- Choose "Fill out the survey"
- Provide:
  - Full name (2+ characters with at least one letter)
  - University email address (must include @ and .)
  - Personality assessment (5 questions, scores 1-5)
  - Gaming interest (e.g., FPS, STRATEGY, RPG)
  - Skill level (1-10 scale)
  - Preferred role (LEADER, SUPPORT, PLAYER)

#### 2. Team Lookup
- Select "Participant" → "Lookup your assigned team"
- Enter your participant ID (minimum 4 characters)
- View assigned team members and their details

### Organizer Flow

#### 1. Authentication
- Select "Organizer" from main menu
- Login with credentials:
  - Default username: `admin`
  - Default password: `admin`
- Up to 5 login attempts allowed

#### 2. Team Formation
- Select "Upload participant records and form teams"
- Provide path to CSV file or press ENTER for default (`participants.csv`)
- System loads and validates participants (displays warnings for invalid rows)
- Specify desired team size
- Algorithm forms teams with balanced personality types and skills

#### 3. Team Review & Export
- View all formed teams with member details
- Choose to export to CSV (`formed_teams.csv`)
- Exported file contains: TeamNumber, ParticipantID, Name, Email, Interest, SkillLevel, Role, PersonalityScore, PersonalityType

---

## Technical Details

### Teaming Algorithm

The core algorithm in `TeamBuilder.java` works as follows:

1. **Sort Participants** by personality importance and skill level:
   - LEADER (priority 1) - Foundation of team dynamics
   - THINKER (priority 2) - Strategic thinking
   - Others (priority 3)
   - Secondary sort by skill level (descending)

2. **Distribute to Teams** - Each participant assigned to team with:
   - Lowest number of members
   - Balanced personality distribution (cap of 2 similar interests)
   - Skill diversity (prevents skill stacking)
   - Role variety

3. **Handle Remainder** - Participants exceeding perfect team divisions go to a "pool" for manual placement

### Personality Assessment

5 questions designed to assess:
- Leadership tendencies
- Strategic thinking
- Collaboration skills
- Pressure handling
- Adaptability

Scores range: 5-25 (5 questions × 1-5 scale)

### Enums

**Interest**: FPS, STRATEGY, RPG, SPORTS, OTHER

**Role**: LEADER, SUPPORT, PLAYER

**PersonalityType**: LEADER, THINKER, COLLABORATOR, STABILIZER, ADAPTER

---

## Requirements

### Participant Data (CSV Format)

**Header:**
```
ParticipantID,FullName,Email,Interest,SkillLevel,Role,PersonalityScore,PersonalityType
```

**Example:**
```
P001,John Doe,john@university.edu,FPS,8,LEADER,22,LEADER
P002,Jane Smith,jane@university.edu,STRATEGY,7,SUPPORT,18,THINKER
```

### Constraints

- ParticipantID: Non-empty string
- FullName: 2+ characters, contains at least one letter
- Email: Valid format with @ and .
- SkillLevel: 1-10 integer
- PersonalityScore: 0-100 integer
- Interest, Role, PersonalityType: Must match enum values

### CSV Processing

- **Concurrent Threads**: 4 threads for row processing
- **Error Handling**: Invalid rows generate warnings but don't halt processing
- **Partial Success**: Valid participants processed even if some rows fail

---

## File Format

### Input: participants.csv
Participant records for team formation (see Requirements section)

### Output: formed_teams.csv
Generated after team formation export:
```
TeamNumber,ParticipantID,Name,Email,Interest,SkillLevel,Role,PersonalityScore,PersonalityType
1,P001,John Doe,john@university.edu,FPS,8,LEADER,22,LEADER
1,P002,Jane Smith,jane@university.edu,STRATEGY,7,SUPPORT,18,THINKER
```
---

## 📝 Notes

- Authentication uses a Singleton pattern to maintain state across the application
- Survey data is saved to participant records for later team formation
- The teaming algorithm prioritizes creating **functional** teams over **random** teams
- Teams can be reformed with different sizes without losing original data
- Leftover participants not fitting into complete teams are pooled for manual assignment

---

**Built with 🍵,Java & 💝**
