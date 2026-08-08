# 🔨 Whac-A-Mole Quizbee Showdown

A competitive, turn-based local multiplayer arcade and trivia game built from scratch using Java. This application seamlessly integrates a classic 3x3 grid "Whac-A-Mole" board game with an educational, dynamic "Quizbee" pop-up engine powered by a local database system.

---

## 👤 Author Information
* **Developer:** II Landicho (BurntShuji) and multiple anonymous programmers
* **Project Completion Date:** May 29, 2026
* **Language:** Java SE
* **IDE/Environment:** Apache NetBeans
* **Database System:** Java Database Connectivity (JDBC)

---

## 🎯 Project Scope
* **Development Environment:** The game is fully created using **JAVA** via **Apache Netbeans**.
* **Local Multiplayer Showdown:** Features a 2-player competitive structure where players take turns on a single machine to race toward a target score of **30 points**.
* **Absolute Victory:** Only one (1) definitive winner will be declared at the end of the match.
* **Dual Simultaneous Interfaces:** The Quizbee has a completely separate scoring engine from the Whac-A-Mole board; however, both game frames are displayed simultaneously.
* **Sequential Execution Flow:** The Whac-A-Mole board game is executed first. The Quizbee questions will only pop up once specific Whac-A-Mole milestone conditions are met.
* **Dynamic Escalating Difficulty:** As one or both players near the peak score threshold, the gameplay difficulty for both the Quizbee and Whac-A-Mole scales dynamically through three tiers: **Easy, Medium, and Hard**.
* **Persistent Data Management:** The high scores, profiles, and match historical records of each player are safely recorded and modified using structural **JAVA Database commands**.
* **Visual Grid System:** The Whac-A-Mole module utilizes high-quality custom graphics mapped across a **3 x 3 Grid layout** for an optimized arcade user experience.

---

## 🛑 Limitations
* **Single Hardware Constraint:** The JAVA application is designed to run exclusively on **one (1) laptop** sharing local inputs.
* **No Online Features:** The system does not support online network multiplayer capabilities or remote simultaneous gameplay.
* **Topic Restraints:** The Quizbee question bank is strictly limited to **Java Programming Concepts** only.
* **Match-Ending Trigger:** The overall game match immediately terminates the moment any player hits the **30-point milestone** inside the Quizbee tracking engine.
* **Leaderboard Constraints:** To preserve processing performance, only the **top ten (10) all-time highest scorers** are displayed on the global database leaderboard framework.
* **Asset Dependency:** Graphic assets may fail to render correctly or crash if file paths are corrupted when migrating properties or dependencies across separate operating systems.
* **Programmed Sequence Priority:** Despite both graphic windows appearing on screen simultaneously, Whac-A-Mole and Quizbee are **not to be played at the same time**; they strictly follow a hardcoded algorithmic sequence.

---

## 🎮 Game Mechanics

### 🔄 1. Turn-Based Play & Initialization
* Two players compete head-to-head by taking sequential turns using a single laptop.
* **Player 1** always receives the initial turn to start the gameplay cycle.

### 🔨 2. Phase 1: The Board Game (Whac-A-Mole)
* The active player interacts with a 3x3 grid populated by dynamic sprites. They must left-click **moles** or **gold** blocks while completely avoiding hidden **traps**.
* **Hitting the Trap:** Accidentally clicking a trap results in an immediate **10-point deduction** from the active player's Whac-A-Mole session score.
* **Target Score Thresholds:** Players must accumulate a specific score within a strict **60-second time limit per round** to trigger the next phase:
  * 🟢 **Easy Mode:** 50 points required
  * 🟡 **Medium Mode:** 200 points required
  * 🔴 **Hard Mode:** 300 points required

### ❓ 3. Phase 2: The Quizbee Pop-up
* **Phase Transition:** If the player successfully meets or exceeds the required target score before the 60-second countdown expires, a **Quizbee question pop-up** activates.
* **Correct Answer:** Answering the conceptual Java question correctly awards the player **1 overall game point** toward the 30-point winning condition.
* **Turn Forfeiture:** The active player's turn instantly ends, and control transitions to **Player 2** if:
  * The Quizbee question is answered **incorrectly**.
  * The player **fails to reach the required target score** within the 60-second Whac-A-Mole window.

### 🪙 4. Special Item: The Gold Mechanics
Striking a rare **gold** asset once halts the standard arcade loop and presents the player with a critical tactical fork:
1. **Choice 1: "Pass"**
   * The player skips the remainder of their active round.
   * They are instantly awarded **2 permanent game points**.
   * The turn terminates cleanly, passing control over to **Player 2**.
2. **Choice 2: "Play"**
   * A random bonus question displays immediately.
   * **Correct Answer:** Rewarded with a massive payload of **5 permanent game points**.
   * **Wrong Answer:** Rewarded with **0 points** for the question.
   * **Turn Continuation:** Regardless of whether the answer is right or wrong, the active player's turn **continues** without interruption.

### ⏱️ 5. Match Termination & Victory Condition
* **Time Expiration:** If the internal timer runs out entirely during a phase, the active turn expires and transitions over to the next player.
* **The Victory Line:** The entire game is a high-stakes race to **30 total match points**. The very first player to register this score profile via database checks is crowned the winner.

---
