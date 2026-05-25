# RPG Party Manager (Android App)

An Android application for managing RPG-style characters with stats such as HP, Mana, and role classes. The app is built using Java, AndroidX components, and Room for persistent storage.

---

## Features

- Add new characters with name, class, HP, and Mana
- View a list of all characters using RecyclerView
- View detailed character stats
- Edit character information including HP and Mana
- Delete characters with confirmation dialog
- Persistent storage using Room database
- Dice rolling system with critical hit and failure detection
- Real-time updates to character stats

---

## Tech Stack

- Java (Android)
- AndroidX AppCompat
- RecyclerView
- Fragments
- Room Persistence Library
- Material Design components

---

## Project Structure

The application is organized into the following packages:

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
│   └── CharacterAdapter (RecyclerView logic)
│
├── data
│   ├── AppDatabase
│   ├── CharacterDao
│   └── CharacterEntity (Room model)
│
├── fragments
│   └── StatsFragment (HP / Mana UI display)
│
├── models
│   └── Character (UI model used by RecyclerView)
│
├── utils
│   └── DiceManager
│
└── receivers
└── DiceReceiver (handles dice roll broadcast events)

---

## Database Schema

The app uses a Room database with a single entity:

CharacterEntity
- id (Primary Key, auto-generated)
- name (String)
- role (String)
- hp (Integer)
- mana (Integer)

---

## How to Run

1. Clone or download the project
2. Open the project in Android Studio
3. Allow Gradle to sync dependencies
4. Run the app on an emulator or physical device (minimum API 24)

---

## Application Flow

- Main screen provides navigation to character list and dice roller
- Character list loads data from Room database
- Selecting a character opens the detail screen
- Detail screen allows stat modification and editing
- Changes are persisted immediately in Room database

---

## Future Improvements

- Inventory system for characters
- Equipment and item management
- Character classes with passive bonuses
- Improved UI with health and mana bars
- Animations for stat changes
- Cloud synchronization support

---

## Notes

This project demonstrates Android fundamentals including:
- Activity navigation
- Persistent local storage using Room
- RecyclerView implementation
- Basic game mechanics simulation
- UI state management