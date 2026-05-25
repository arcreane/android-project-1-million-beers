# Technical Documentation — RPG Party Manager

## 1. Project Overview

RPG Party Manager is an Android application developed in Java for managing RPG-style characters and their statistics.

The application allows users to:

- Create characters
- Edit characters
- Delete characters
- Manage HP and Mana values
- Store character data permanently
- Roll RPG dice with critical hit/failure detection

The project demonstrates core Android development concepts including Room database integration, RecyclerView implementation, fragments, intents, activities, and broadcast receivers.

---

# 2. Objectives

The objectives of this project are:

- Build a multi-screen Android application
- Implement persistent local storage
- Demonstrate CRUD operations
- Practice Android UI design
- Use RecyclerView for dynamic data display
- Understand Android component communication

CRUD stands for:

- Create
- Read
- Update
- Delete

---

# 3. Technologies Used

| Technology | Purpose |
|---|---|
| Java | Main programming language |
| Android SDK | Android application framework |
| AndroidX | Modern Android support libraries |
| Room Database | Persistent local storage |
| RecyclerView | Dynamic list rendering |
| Fragments | Reusable UI components |
| Material Design | UI styling and layouts |

---

# 4. Application Architecture

The project follows a layered architecture.

```text
UI Layer
    ↓
Business Logic
    ↓
Room Database Layer
```

This structure improves:

- maintainability
- scalability
- separation of concerns

---

# 5. Project Structure

```text
com.example.rpgpartymanager
│
├── activities
│   ├── MainActivity
│   ├── CharacterListActivity
│   ├── CharacterDetailActivity
│   ├── CreateCharacterActivity
│   ├── EditCharacterActivity
│   └── DiceRollActivity
│
├── adapters
│   └── CharacterAdapter
│
├── data
│   ├── AppDatabase
│   ├── CharacterDao
│   └── CharacterEntity
│
├── fragments
│   └── StatsFragment
│
├── models
│   └── Character
│
└── receivers
    └── DiceReceiver
```

---

# 6. Activities

## MainActivity

Purpose:
- Entry point of the application
- Navigation hub

Functions:
- Opens Character List screen
- Opens Dice Roller screen

---

## CharacterListActivity

Purpose:
- Displays all characters

Main Responsibilities:
- Load characters from Room database
- Display RecyclerView
- Open detail screen
- Delete characters
- Launch character creation

RecyclerView was used because it efficiently handles dynamic lists and view recycling.

---

## CharacterDetailActivity

Purpose:
- Displays a selected character

Features:
- View HP and Mana
- Increase/decrease stats
- Save stat changes
- Open edit screen
- Share character data

Stat updates are immediately persisted to Room database.

---

## CreateCharacterActivity

Purpose:
- Create new characters

Features:
- Input character information
- Insert character into Room database

---

## EditCharacterActivity

Purpose:
- Edit existing characters

Features:
- Load existing character
- Update name, role, HP, and Mana
- Save updates to Room database

---

## DiceRollActivity

Purpose:
- Simulate RPG dice rolling

Features:
- Random D20 roll generation
- Critical hit detection
- Critical failure detection
- Broadcast system integration

---

# 7. RecyclerView Implementation

RecyclerView is used to efficiently display a list of characters.

Advantages:
- Efficient memory usage
- Smooth scrolling
- View recycling
- Dynamic updates

The RecyclerView consists of:

- RecyclerView
- Adapter
- ViewHolder

---

## CharacterAdapter

Responsibilities:
- Inflate character item layouts
- Bind character data to UI
- Handle click events
- Handle delete actions

---

# 8. Room Database

The application uses Room Persistence Library for permanent local storage.

Advantages of Room:
- Simplified SQLite integration
- Persistent storage
- Type safety
- Easier database management

---

## CharacterEntity

Represents the database table.

Fields:

| Field | Type | Purpose |
|---|---|---|
| id | int | Unique character ID |
| name | String | Character name |
| role | String | Character class |
| hp | int | Health points |
| mana | int | Mana points |

The ID is auto-generated to ensure uniqueness.

---

## CharacterDao

DAO stands for Data Access Object.

Responsibilities:
- Insert records
- Update records
- Delete records
- Query records

Main functions:

```java
insert()
update()
delete()
getAll()
getById()
```

---

## AppDatabase

Purpose:
- Main Room database instance

A singleton pattern is used to ensure only one database instance exists during application runtime.

Advantages:
- Better memory management
- Prevents duplicate database connections
- Centralized database access

---

# 9. Fragments

## StatsFragment

Purpose:
- Display HP and Mana UI

Reason for using Fragment:
- Reusable UI component
- Better UI modularity
- Lifecycle-aware component structure

---

# 10. Broadcast Receiver System

## DiceReceiver

Purpose:
- Listen for dice roll broadcasts

Features:
- Detect critical hits
- Detect critical failures
- Trigger vibration feedback
- Display Toast notifications

Critical conditions:

| Event | Condition |
|---|---|
| Critical Hit | Roll >= 18 |
| Critical Failure | Roll <= 2 |

---

# 11. Intents and Navigation

Intents are used for:

- screen navigation
- passing character data
- launching activities

Example:

```java
Intent i = new Intent(this, CharacterDetailActivity.class);
```

Data is passed using extras:

```java
i.putExtra("id", character.getId());
```

---

# 12. Data Flow

```text
User Action
    ↓
Activity
    ↓
Room DAO
    ↓
Database
    ↓
Updated UI
```

Room database acts as the single source of truth.

Advantages:
- Prevents stale data
- Improves consistency
- Simplifies synchronization

---

# 13. ID-Based Character System

Originally, characters were identified by name.

Problem:
- Duplicate names caused update and deletion issues

Solution:
- Use unique database IDs

Advantages:
- Reliable updates
- Reliable deletes
- Better scalability

---

# 14. Testing

The application was tested using:

- Android Emulator
- Manual UI testing
- Database persistence testing

Test cases included:

- Character creation
- Character editing
- Character deletion
- HP/Mana updates
- App restart persistence
- Dice rolling functionality

---

# 15. Future Improvements

Potential future features include:

- Inventory system
- Equipment management
- Health bars
- RPG card UI redesign
- Firebase cloud synchronization
- Multiplayer party sharing
- Animation effects

---

# 16. Conclusion

RPG Party Manager demonstrates core Android development concepts including:

- Activity navigation
- RecyclerView implementation
- Room persistence
- Broadcast receivers
- Fragment usage
- CRUD operations
- Android UI architecture

The project successfully implements a persistent RPG character management system using modern Android development practices.