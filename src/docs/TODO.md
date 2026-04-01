# TODO.md
# Att göra först

1. [x] Skapa Book.Book-klassen
2. [x] Skapa controller-lagret för Book.Book-klassen
3. [x] Skapa Base.BaseRepository-lagret
4. [x] Skapa Base.BaseService och Base.BaseController
5. [ ] Koppla Service-lagret till Base.BaseRepository


## Viktiga klasser:

1. [x] Book.Book
2. [x] Loan.Loan
3. [x] Member.Member

Mindre viktiga klasser:

- [ ] Author
- [ ] Categories

### Funktionalitet som behöver skapas först:
1. **Söka böcker**
    1. [ ] alla böcker
2. **Hantera lån**
    1. [ ] Låna en bok
    2. [ ] Lämna tillbaka bok 
3. **Hantera konto**
    1. [ ] Logga in/ut

#### Nice-to-have (senare):

1. **Söka böcker**
    1. [x] ~~alla böcker~~
    2. [ ] enl. författare
    3. [ ] enl kategorier
2. **Hantera lån**
    1. [x] ~~Låna en bok~~
    2. [ ] Se sina lån
    3. [ ] Lämna tillbaka en bok
3. **Hantera konto**
    1. [x] Logga in/ut
    2. [ ] Skapa konto
4. **Admin-funktionalitet**
    1. [ ] Lägga till ny bok
    2. [ ] Uppdatera en bok
    3. [ ] Ta bort en bok


Repositories:
Book.BookRepository
Member.MemberRepository

Controllers:
Book.BookController
Member.MemberController

Services:
Book.BookService
Member.MemberService

### Arv: 
Class User
-> Admin extends User
-> Member.Member extends User

Abstract classes: X extends Y

Interfaces: X implements Y

DTO


loan userstories
member userStories
admin


## Menyer:

1. Huvudmeny (Main.java)
   1. Logga in --> UserController.java (inloggningsmeny)
   2. Sök böcker --> Book.BookController.java (bok-meny)
   3. Låna --> Loan.LoanController.java (lån-meny)
   4. Avsluta
2. Inloggningsmeny
   1. 
