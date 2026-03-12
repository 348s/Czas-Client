# Jak skompilować Czas Client

## Wymagania
- **Java 17+** (zalecana Java 17)
- **Git** (do klonowania repozytorium)
- **Gradle 8.1.1+** (automatycznie pobierany)

## Kompilacja przez GitHub Actions

### Automatyczna kompilacja
1. **Push do GitHuba:**
   ```bash
   git add .
   git commit -m "Initial commit"
   git push origin main
   ```

2. **GitHub Actions automatycznie:**
   - Skompiluje projekt
   - Przeprowadzi testy
   - Stworzy plik JAR
   - Umieści artefakty w sekcji Actions

3. **Pobieranie gotowego JAR:**
   - Wejdź w `GitHub` → `Actions` → `Workflow`
   - Wybierz ostatnie uruchomienie
   - Pobierz artefakt `CzasClient-[numer]`
   - Rozpakuj i użyj pliku `CzasClient.jar`

### Ręczne tworzenie Release
1. Stwórz tag na GitHubie:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

2. Stwórz Release na GitHubie
3. GitHub Actions automatycznie stworzy JAR i doda do release

## Kompilacja lokalna

### 1. Konfiguracja środowiska
```bash
# Sprawdź wersję Java
java -version

# Ustaw JAVA_HOME (jeśli potrzebne)
export JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-17.x.x"
```

### 2. Klonowanie repozytorium
```bash
git clone https://github.com/TWOJ-USERNAME/Czas-Client.git
cd Czas-Client
```

### 3. Kompilacja
```bash
# Windows
.\gradlew.bat build

# Linux/Mac
./gradlew build
```

### 4. Pobieranie JAR
Gotowy plik JAR znajdziesz w:
```
build/libs/CzasClient.jar
```

## Wersja i Silnik

### Informacje o wersji
- **Wersja moda:** 1.0.0
- **Autor:** 348 s
- **Data wydania:** 2026-03-12

### Silnik i technologia
- **Silnik:** Minecraft Forge
- **Wersja Forge:** 47.3.0 (dla Minecraft 1.20.1)
- **Wersja Java:** 17+
- **Build system:** Gradle 8.1.1
- **Język:** Java

### Kompatybilność
- **Minecraft:** 1.18.2 - 1.21.4
- **Forge:** 40.0.0+
- **Java:** 17 lub nowsza

## Instalacja

1. Pobierz plik `CzasClient.jar`
2. Umieść go w folderze `mods` Minecrafta
3. Uruchom Minecraft z Forge
4. Naciśnij **Prawy Shift** aby otworzyć GUI

## Funkcje

### Movement (Ruch)
- While Speed - Zwiększa prędkość ruchu
- Fake Creative - Daje możliwości trybu kreatywnego
- Spider - Wspinanie się po ścianach
- Long Jump - Dłuższe skoki
- No Fall - Ochrona przed upadkiem
- Scaffold - Automatyczne stawianie bloków

### Combat (Walka)
- Aim Assist - Pomoc w celowaniu
- Auto Clicker - Automatyczne klikanie
- No Slow - Eliminacja spowolnień
- Nuker - Automatyczne niszczenie bloków

### Utilities (Narzędzia)
- Auto Regen - Automatyczna regeneracja życia
- Inventory Walk - Chodzenie z otwartym ekwipunkiem
- Auto Armor - Automatyczne zakładanie zbroi

### World (Świat)
- Fast Break - Szybsze niszczenie bloków
- Timer - Modyfikacja szybkości gry

### Render (Wygląd)
- Full Bright - Zwiększona jasność

## GUI i Konfiguracja

- **Otwieranie:** Prawy Shift
- **Logo:** Czerwony zegar
- **Motywy:** Red/Black, Blue, Green
- **Języki:** Polski, Angielski
- **Konfiguracja:** Zapis/odczyt ustawień

## Problemy i Rozwiązania

### Java nie znaleziona
```bash
# Zainstaluj Java 17
# Lub ustaw JAVA_HOME
set JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-17.x.x"
```

### Błędy kompilacji
```bash
# Czyść cache Gradle
.\gradlew.bat clean
.\gradlew.bat build
```

### Błędy w Minecraft
- Upewnij się, że Forge jest zainstalowany
- Sprawdź kompatybilność wersji Minecraft
- Usuń stare wersje moda

## Support

- **GitHub:** https://github.com/TWOJ-USERNAME/Czas-Client
- **Issues:** Raportuj błędy na GitHub
- **Discord:** [Link do serwera Discord]
