# KBTU University System

A university management system built in Java for the OOP & Design final project at Kazakh-British Technical University.

---

## How to Run

**In Eclipse:**
1. `File → Import → Existing Projects into Workspace`
2. Select the `UniversitySystem` folder
3. Run `main/Main.java`

> On first run, `src/data/` is created automatically and seeded with default accounts.  
> To reset all data, delete everything inside `src/data/`.

---

## Login

You can log in using either your **ID** or **email** together with your password.

| Name | Role | ID | Email | Password |
|---|---|---|---|---|
| Aibek Seitkali | Admin | `AD0001` | `a_seitkali@kbtu.kz` | `admin123` |
| Nursultan Akhmetov | Manager | `MG030001` | `n_akhmetov@kbtu.kz` | `manager123` |
| Olga Ivanova | Teacher (Professor) | `T030001` | `o_ivanova@kbtu.kz` | `teacher123` |
| Bekzat Akhanov | Teacher (Lector) | `T040001` | `b_akhanov@kbtu.kz` | `pass123` |
| Daniyar Bekov | Student (CS, Year 2) | `23B030001` | `d_bekov@kbtu.kz` | `student123` |
| Aliya Ospanova | Student (IS, Year 1) | `23B030002` | `a_ospanova@kbtu.kz` | `pass123` |
| Madina Nurova | Master Student | `23M030001` | `m_nurova@kbtu.kz` | `grad123` |
| Arman Zhukov | Tech Support | `TS0001` | `a_zhukov@kbtu.kz` | `tech123` |

---

## ID Format

IDs are generated automatically when creating new accounts.

| Role | Format | Example |
|---|---|---|
| Bachelor Student | `YYB[school]nnnn` | `25B030042` |
| Master Student | `YYM[school]nnnn` | `25M030001` |
| PhD Student | `YYP[school]nnnn` | `25P040003` |
| Teacher | `T[school]nnnn` | `T030007` |
| Manager | `MG[school]nnnn` | `MG030001` |
| Admin | `ADnnnn` | `AD0001` |
| Tech Support | `TSnnnn` | `TS0001` |

`YY` = last two digits of enrollment year. `[school]` = two-digit school code.

### School Codes

| Code | School |
|---|---|
| `01` | Faculty of Energy and Oil & Gas Industry |
| `02` | Faculty of Geology and Geological Exploration |
| `03` | Faculty of Information Technology (FIT / SITE) |
| `04` | School of Mathematics and Cybernetics |
| `05` | School of Chemical Engineering |
| `06` | Business School (KBS) |
| `07` | International School of Economics (ISE) |
| `08` | Kazakhstan Maritime Academy (KMA) |
| `09` | Faculty of General Education |

---

## Project Structure

```
src/
├── main/                   Entry point
│   └── Main.java
├── model/
│   ├── academic/           Course, Enrollment, LessonGroup, Mark, StudentOrganization
│   ├── communication/      Complaint, Message, News, Request
│   ├── enums/              All enumerations
│   ├── exceptions/         Custom exceptions
│   ├── observer/           JournalObserver interface
│   ├── research/           Journal, ResearchPaper, ResearchProject, Researcher
│   └── users/              User, Employee, Admin, Manager, Teacher,
│                           Student, GraduateStudent, TechSupportSpecialist
├── storage/                DataStorage (Singleton), ActionLogger (Singleton)
├── system/                 University (Singleton)
├── ui/                     Console menus for each user role
└── util/                   UserCredentialGenerator
```

---

## Key Classes

### Users

| Class | Extends | Description |
|---|---|---|
| `User` | — | Abstract base. Holds id, name, email, language. Implements `Serializable` and `JournalObserver`. |
| `Employee` | `User` | Abstract. Adds `department`. |
| `Admin` | `Employee` | Manages users and requests. Reads action logs. |
| `Manager` | `Employee` | Manages courses, news, registrations. Has `ManagerType`. |
| `Teacher` | `Employee` | Implements `Researcher`. Has `TeacherPosition`. Professors always research-capable. |
| `Student` | `User` | MAX 21 credits, MAX 3 fails. Registers via `registerForCourse(course, group)`. |
| `GraduateStudent` | `Student` | Implements `Researcher`. Has supervisor (h-index ≥ 3 required) and diploma projects. |
| `TechSupportSpecialist` | `Employee` | Handles support requests. |

### Academic

| Class | Description |
|---|---|
| `Course` | ID, name, credits, type, target major/year, lecturers, enrolled students. |
| `LessonGroup` | Scheduled slot — type, teacher, day, time, room, capacity, semester. |
| `Enrollment` | Links student + course + lesson group. Created on registration. `drop()` deactivates it. |
| `Mark` | First attestation (max 30) + second attestation (max 30) + final exam (max 40). `isPassed()` = total ≥ 50. |
| `StudentOrganization` | Club with members list and a `head` field. |

### Research

| Class | Description |
|---|---|
| `Researcher` | Interface: `getPapers()`, `calculateHindex()`, `printPapers(Comparator)`, `publishPaper()`. |
| `ResearchPaper` | Title, authors, journal, pages, doi, citations. Static comparators: `BY_CITATIONS`, `BY_DATE`, `BY_LENGTH`. `getCitation(CitationFormat)` returns PLAIN_TEXT or BIBTEX. |
| `ResearchProject` | Topic, participants, published papers. `addParticipant(User)` throws `NotAResearcherException`. |
| `Journal` | Subscribers notified via `onPaperPublished()` on paper publish. Returns pinned `News` announcement. |

### Communication

| Class | Description |
|---|---|
| `Complaint` | From `Teacher` about `Student`. Has urgency level LOW/MEDIUM/HIGH. |
| `Message` | Direct message between any two employees. |
| `News` | Title, content, topic, author, comments, pinned flag. Research topic auto-pinned. |
| `Request` | Status lifecycle: NEW → VIEWED → ACCEPTED / REJECTED / DONE. |

### Storage & System

| Class | Description |
|---|---|
| `DataStorage` | Singleton. Serializes all collections to `src/data/*.txt`. Seeds on first run. |
| `ActionLogger` | Singleton. Appends to `src/data/actions.log`. |
| `University` | Singleton. Top cited researcher queries by university, year, and school. |
| `UserCredentialGenerator` | Generates KBTU-format IDs, `firstInitial_lastname@kbtu.kz` emails, 8-char passwords. |

### Enumerations

| Enum | Values |
|---|---|
| `CitationFormat` | `PLAIN_TEXT`, `BIBTEX` |
| `CourseType` | `MAJOR`, `MINOR`, `FREE_ELECTIVE` |
| `GraduateType` | `MASTER`, `PHD` |
| `Language` | `KZ`, `EN`, `RU` |
| `LessonType` | `LECTURE`, `PRACTICE` |
| `ManagerType` | `OR`, `DEPARTMENT`, `DEAN_OFFICE` |
| `RequestStatus` | `NEW`, `VIEWED`, `ACCEPTED`, `REJECTED`, `DONE` |
| `TeacherPosition` | `TUTOR`, `LECTOR`, `SENIOR`, `PROFESSOR` |
| `UrgencyLevel` | `LOW`, `MEDIUM`, `HIGH` |
| `SchoolCode` | `01`–`09` (see School Codes table above) |

### Exceptions

| Exception | When thrown |
|---|---|
| `CreditLimitExceededException` | Student exceeds 21 credits |
| `LowHIndexSupervisorException` | Supervisor h-index < 3 |
| `NotAResearcherException` | Non-Researcher tries to join a ResearchProject |
| `MaxFailsExceededException` | Student exceeds 3 course failures |

---

## Features by Role

### Admin
- View all users (with ID)
- Add / remove / update user accounts (all roles)
- View / accept / reject requests
- View action logs
- View system statistics

### Manager
- Create new user accounts
- Assign courses to teachers
- Approve student course registration
- Add courses for registration (specify major and year)
- Manage news (create, delete; Research topic auto-pinned)
- View students sorted by GPA or alphabetically
- View teachers sorted alphabetically
- Generate academic performance report
- Print all researchers' papers sorted by citations, date, or length
- View top cited researcher (university-wide, by year, by school)
- View complaints filed by teachers

### Teacher
- View assigned courses and schedule
- Put and update student marks
- View students and their marks per course
- Send complaint about a student (LOW / MEDIUM / HIGH)
- Send messages to employees and view inbox
- Research (Professor only): view and publish papers

### Student
- View available courses and register (credit limit enforced)
- Drop a course
- View enrollments, marks, and transcript
- View teacher info for a course
- Join student organizations
- View and comment on news
- Rate teachers (1–5)
- Submit tech support requests

### Graduate Student
All student options, plus:
- View / assign supervisor (h-index ≥ 3 required)
- Publish research papers (diploma projects)
- View papers sorted by citations, date, or length
- View / join / create research projects

### Tech Support Specialist
- View new requests (auto-marked VIEWED on open)
- Filter requests by status
- Accept / reject / mark as done
- Submit new requests

---

## Design Patterns

| Pattern | Where |
|---|---|
| **Singleton** | `DataStorage`, `ActionLogger`, `University` |
| **Observer** | `Journal` notifies subscribed `User` objects via `JournalObserver` on paper publish |
| **Strategy** | `printPapers(Comparator<ResearchPaper>)` — `BY_CITATIONS`, `BY_DATE`, `BY_LENGTH` |
| **Iterator** | Java Collections throughout (`for-each`, `stream()`, `Comparator`) |

---

## Data Persistence

All data is serialized to binary `.txt` files in `src/data/` using Java `ObjectOutputStream` / `ObjectInputStream`. Saved automatically after every change, loaded on startup.

| File | Contents |
|---|---|
| `users.txt` | All user accounts |
| `courses.txt` | Courses |
| `journals.txt` | Journals and papers |
| `lessonGroups.txt` | Lesson schedules |
| `organizations.txt` | Student organizations |
| `news.txt` | News and comments |
| `requests.txt` | Support requests |
| `marks.txt` | Student grades |
| `researchProjects.txt` | Research projects |
| `messages.txt` | Employee messages |
| `complaints.txt` | Teacher complaints |
| `actions.log` | Plain-text action log |
