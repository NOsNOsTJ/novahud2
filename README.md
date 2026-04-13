# NovaHUD - Mod Fabric 1.21.1

HUD overlay customizabil pentru Minecraft, inspirat din Lunar Client.

## Ce include
- **FPS Counter** — verde/galben/rosu in functie de valoare
- **Ping** — latenta serverului in ms
- **CPS** — click-uri stanga si dreapta pe secunda
- **Taste WASD** — se aprind cand le apesi
- **Coordonate XYZ** — pozitia ta in lume
- **Vitezometru** — blocuri pe secunda
- **Timp in-game** — ora din zi in Minecraft
- **Armura** — puncte de armura

## Cum deschizi meniul
Apasa **Shift Dreapta** in joc — se deschide meniul cu toggle pentru fiecare modul.

---

## Cum compilezi (GitHub Actions — FARA sa instalezi nimic)

### Pasul 1 — Urca proiectul pe GitHub
1. Mergi la [github.com](https://github.com) si creeaza un cont daca nu ai
2. Click **New repository** → numeste-l `novahud` → **Create repository**
3. Descarca [GitHub Desktop](https://desktop.github.com/) sau foloseste comanda:
```
git init
git add .
git commit -m "NovaHUD initial"
git remote add origin https://github.com/USERNAME/novahud.git
git push -u origin main
```

### Pasul 2 — Lasa GitHub sa compileze
1. Mergi pe pagina repository-ului tau pe GitHub
2. Click pe tab-ul **Actions**
3. Vei vedea workflow-ul "Build NovaHUD" rulând automat
4. Asteapta ~3-5 minute pana devine verde

### Pasul 3 — Descarca JAR-ul
1. Click pe build-ul verde din Actions
2. Jos la **Artifacts** → click pe **NovaHUD-1.21.1**
3. Se descarca un ZIP — dezarhiveaza-l
4. Vei gasi `novahud-1.0.0.jar`

---

## Instalare in TLauncher

### Cerinte
- TLauncher cu profil **Fabric 1.21.1**
- [Fabric API](https://modrinth.com/mod/fabric-api) pus in mods/

### Pasi
1. Deschide TLauncher → selecteaza versiunea **Fabric 1.21.1**
2. Apasa **...** langa versiune → **Folder .minecraft**
3. Intra in folderul `mods/`
4. Copiaza `novahud-1.0.0.jar` si `fabric-api-*.jar` acolo
5. Porneste jocul

---

## Structura proiect
```
novahud/
├── src/main/java/dev/novahud/
│   ├── NovaHUD.java          ← entrypoint principal
│   ├── config/HudConfig.java ← salveaza setarile
│   ├── hud/
│   │   ├── HudRenderer.java  ← deseneaza HUD-ul pe ecran
│   │   ├── HudMenu.java      ← meniul Shift Dreapta
│   │   └── CpsTracker.java   ← numara click-urile
│   └── mixin/
│       └── GameRendererMixin.java ← intercepteaza mouse-ul
├── .github/workflows/build.yml   ← compilare automata
└── build.gradle
```
