# Bibliotekssystem

Ett konsolbaserat bibliotekssystem utvecklat i Java som grupparbete i kursen **Objektorienterad analys och design** vid
**Teknikhögskolan**.

Projektet är byggt för att träna på objektorientering, trelagersarkitektur, JDBC och databasintegration mot MySQL. Fokus
har varit att skapa en tydligt strukturerad Java-applikation där låntagare och bibliotekarier har olika flöden och
behörigheter.

## Grupp

- Nils Paulsson (`nils-fjp`)
- Dhannea Pearl Pettersson (`dhannea-pearl`)
- Olena Vitkovska (`olenavitkovska86-ops`)

## Projektöversikt

Systemet hanterar ett bibliotek med böcker, författare, kategorier, medlemmar och lån. Applikationen är textbaserad och
används via menyer i terminalen. Den är uppdelad i separata lager för presentation, affärslogik och dataåtkomst, vilket
gör den enkel att följa och vidareutveckla.

## Funktioner

### Låntagare

- Visa alla böcker
- Söka efter böcker på titel, författare eller kategori
- Låna en bok
- Se aktiva lån
- Förlänga ett aktivt lån en gång, om det inte är försenat
- Lämna tillbaka ett lån
- Se lånehistorik
- Visa och uppdatera egen profil
- Byta eget lösenord

### Bibliotekarie

- Se alla aktiva lån i systemet
- Registrera återlämning av lån
- Visa alla böcker
- Söka efter böcker
- Lägga till nya böcker
- Justera antalet exemplar för en bok
- Mjukradera en bok som inte är utlånad
- Visa alla låntagare
- Söka fram en låntagare
- Skapa nya låntagarkonton
- Uppdatera låntagare
- Ta bort låntagare utan aktiva lån
- Byta lösenord åt låntagare
- Visa, söka, skapa och uppdatera författare

## Roller och regler

Systemet arbetar med rollerna `READER` och `LIBRARIAN`.

Medlemsstatus påverkar vad som är tillåtet:

- `active` ger vanlig åtkomst enligt roll
- `suspended` tillåter inloggning men blockerar nya lån och förlängning
- `expired` blockerar inloggning

Lån skapas med en standardlånetid på två veckor och kan förlängas en gång så länge lånet inte redan är försenat.

## Teknisk lösning

- Java
- JDBC mot MySQL
- Trelagersarkitektur
- DTO:er och mapper-klasser mellan lager
- Streams, lambda-uttryck och `Optional`
- `record` för vissa DTO:er i lånehanteringen
- SQL-transaktion vid skapande av bok med kopplad författare och kategori

## Projektstruktur

- `src/Main.java` - startklass för applikationen
- `src/ui/` - menyer, konsolutskrifter och autentisering
- `src/book/` - bokhantering
- `src/loan/` - lånehantering
- `src/member/` - medlemmar, profiler och adminflöden
- `src/author/` - författarhantering
- `src/category/` - kategorier
- `src/base/` - gemensamma basklasser för controller, service och repository
- `src/assets/` - SQL-skript, exempelkonfiguration och MySQL Connector
- `src/docs/` - kravspecifikation, menyskiss och ERD

## Databas

Projektet använder en lokal MySQL-databas. SQL-skriptet skapar schema och seed-data för bland annat:

- 200 böcker
- 60 författare
- 12 kategorier
- 50 medlemmar
- ett antal aktiva och avslutade lån

Databasen läses in via en lokal `database.properties`-fil.

## Kom igång

### Förutsättningar

- En installerad JDK
- En lokal MySQL-server
- En IDE, till exempel IntelliJ IDEA

### Starta projektet

1. Skapa databasen genom att köra `src/assets/generate-library.sql` i MySQL.
2. Skapa filen `src/database.properties` och lägg in era lokala uppgifter.
3. Kontrollera att `src/assets/mysql-connector-j-9.6.0.jar` finns med på projektets classpath.
4. Öppna projektet i er IDE och starta applikationen via `Main`.

Exempel på `src/database.properties`:

```properties
DB_URL=jdbc:mysql://localhost:3306/library
DB_USERNAME=root
DB_PASSWORD=your_password
```

Projektet är idag uppsatt som ett vanligt Java-projekt utan Maven eller Gradle, så beroenden och körning hanteras främst
via IDE.

## Demokonton

| Roll          | Status    | E-post                      | Lösenord   |
|---------------|-----------|-----------------------------|------------|
| Bibliotekarie | Active    | `emma.hill88@email.com`     | `QaMzTpLs` |
| Låntagare     | Active    | `abigail.thomas4@email.com` | `PlMnBcXa` |
| Låntagare     | Suspended | `amelia.wright8@email.com`  | `CrFvTgBy` |
| Låntagare     | Expired   | `ava.white25@email.com`     | `NyUiOpAs` |

Kontot med status `expired` är framför allt användbart för att demonstrera systemets åtkomstregler, eftersom det inte
kan logga in.

## Dokumentation i repot

- `src/docs/kravspec.md`
- `src/docs/menyskiss`
- `src/docs/Bibliotek_ERD.png`

## Avgränsningar

- Projektet är en konsolapplikation och har inget webbgränssnitt.
- Adminfunktionen för att "uppdatera bok" fungerar i nuläget som en justering av antal exemplar, inte som full
  redigering av bokmetadata.
- Kategorier används när böcker skapas men har ingen egen färdig meny för administration.
- Författare kan visas, sökas, skapas och uppdateras. Borttagning finns förberedd i koden men är inte aktiverad i
  bibliotekariemenyn.
- Tabeller för till exempel böter, notifikationer och recensioner finns i datamodellen och seed-datan, men ingår inte i
  den nuvarande konsolappen.

## Om projektet

Det här projektet har varit ett praktiskt sätt att omsätta kursens innehåll i kod: objektorienterad design, separation
of concerns, databaskoppling via JDBC och tydlig struktur mellan lager. Målet har inte varit att bygga ett fullständigt
bibliotekssystem, utan ett genomarbetat skolprojekt som visar hur de centrala delarna i kursen kan användas i en
verklighetsnära applikation.
