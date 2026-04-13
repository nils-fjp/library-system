# TODO

## Current Project State

### Architecture

- [x] Project is structured into `controller`, `service`, and `repository` layers.
- [x] `BaseRepository` handles database connection setup.
- [x] `BaseService` provides shared CRUD-style validation flow.
- [ ] `BaseController` is still only a minimal stub and is not yet providing shared controller behavior.
- [ ] Generic base integration is inconsistent across packages. `member` and `book` follow it better than `loan`.

### Book

- [x] `Book` entity exists.
- [x] `BookRepository.getAll()` can list available books.
- [x] `BookRepository.getById()` exists, though the SQL still has a noted issue/comment.
- [x] `BookController` can show available books and fetch a book by ID.
- [x] Search books is still not implemented.
- [x] Search by author is still not implemented.
- [ ] Librarian book management is still not implemented.
- [ ] Create/update/delete book persistence is still not implemented.

### Member

- [x] `Member` entity exists.
- [x] Login/authentication works through `MemberService.authenticate(...)`.
- [x] Reader and librarian menus exist.
- [x] Reader profile view works through DTO mapping.
- [x] Librarian can view all members and search member by email.
- [x] Librarian can update member information.
- [ ] Add member is not implemented.
- [ ] Delete member is not implemented.
- [ ] Profile loan summary is not implemented.
- [ ] Change password flow is not implemented.

### Loan

- [x] `Loan` package exists with entity, repository, service, and controller classes.
- [x] Database schema and seeded data for `loans` exist.
- [x] Loan menus have been scaffolded.
- [x] A partial return flow exists in `LoanRepository.returnLoan(...)`.
- [ ] `Loan` model design is incomplete and needs cleanup.
- [ ] `LoanRepository` CRUD methods are mostly stubs.
- [ ] `LoanService` is mostly a stub and not connected to `BaseService` properly.
- [ ] Reader loan flows are not implemented.
- [ ] Librarian loan management flows are not implemented.
- [ ] Loan DTO/view models are not implemented.
- [x] Current logged-in member is passed into loan flows.

### Author and Category

- [ ] `Author` is still an empty placeholder.
- [ ] `Category` is still an empty placeholder.
- [ ] Author management is not implemented.
- [ ] Category management is not implemented.

## Current Priorities

### Highest Priority

1. [ ] Finish the loan functionality end-to-end.
2. [x] Connect the loan menus to the logged-in member.
3. [ ] Implement librarian-side loan management.

### Next Priority

1. [ ] Complete book search features.
2. [ ] Complete member creation and deletion.
3. [ ] Add profile information related to current loans.

### Later

1. [ ] Author CRUD and menu flow.
2. [ ] Category CRUD and menu flow.
3. [ ] Optional features from the requirements such as fines, notifications, reservations, and statistics.

## Loan Work Breakdown

### Reader-side loan functionality

1. [ ] View my active loans.
2. [ ] View my loan history.
3. [ ] Borrow a book.
4. [ ] Return a book.
5. [ ] Extend a loan.

### Librarian-side loan functionality

1. [ ] View all active loans.
2. [ ] Create loan.
3. [ ] Modify loan.
4. [ ] Delete loan.
5. [ ] Manually register returned loan.

### Loan package technical work

1. [ ] Clean up `Loan` constructors, setters, and helper methods.
2. [ ] Add loan DTOs for reader and librarian views.
3. [ ] Implement repository queries for active loans, loan history, and CRUD operations.
4. [ ] Implement service rules for borrow, return, extend, create, modify, and delete.
5. [ ] Connect `LoanController` menu actions to service methods.
6. [ ] Verify `books.available_copies` is updated correctly when borrowing and returning.

## Notes

- `src/docs/kravspec.md` contains the user stories and minimum requirements.
- `src/docs/menyskiss` shows the intended menu structure.
- `member` is currently the best reference package for how repository/service/controller code is organized.
- `loan` is the package with the largest gap between scaffold and working functionality.
