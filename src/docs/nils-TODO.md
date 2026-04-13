# Nils TODO

## Presentation Schedule

Deadline: `2026-04-20`

Recommended workload:

- Weekdays: `2.5-3.5 focused hours`
- Weekend days: `4-5 focused hours`
- Presentation day: `1-2 hours max`
- Real internal deadline: `2026-04-19`

### Fri 2026-04-10 (`2-3h`)

- [ ] Confirm the open questions with the group if possible.
- [ ] If the group cannot answer in time, write down temporary assumptions for loan period, extend rules, and delete
  rules.
- [ ] Review the current files again before coding: `Loan.java`, `LoanRepository.java`, `LoanService.java`,
  `LoanController.java`, `MemberController.java`.
- [ ] Target TODO mapping: `Open Questions 1-5`, `Reference Files`.

### Sat 2026-04-11 (`4-5h`)

- [ ] Clean up `src/loan/Loan.java`.
- [ ] Add a no-arg constructor if needed for easy mapping.
- [ ] Replace the current inconsistent constructors with a clear set of constructors.
- [ ] Make `returnDate` settable from database values.
- [ ] Add helper methods such as `isReturned()`, `isActive()`, and `isOverdue()`.
- [ ] Target TODO mapping: `Section 1, items 1-5`.

### Sun 2026-04-12 (`4-5h`)

- [ ] Create a DTO for a reader's active loans.
- [ ] Create a DTO for a reader's loan history.
- [ ] Create a DTO for the librarian's loan overview.
- [ ] Include human-readable fields such as book title and member name where needed.
- [ ] Fix `LoanRepository` to use proper generics: `BaseRepository<Loan, Integer>`.
- [ ] Implement `getById(Integer id)`.
- [ ] Target TODO mapping: `Section 2, items 1-4` and `Section 3, items 1-2`.

### Mon 2026-04-13 (`3h`)

- [ ] Implement `getAll()` or a better overview query for loans.
- [ ] Add `getActiveLoansByMember(Integer memberId)`.
- [ ] Add `getLoanHistoryByMember(Integer memberId)`.
- [ ] Add `getActiveLoans()` for librarian view.
- [ ] Target TODO mapping: `Section 3, items 3-6`.

### Tue 2026-04-14 (`3h`)

- [ ] Implement `save(Loan entity)` for creating loans.
- [ ] Implement `update(Loan entity)` for modifying a loan.
- [ ] Implement `deleteById(Integer id)`.
- [ ] Rework `returnLoan(...)` to update `loans` and `books` safely in one transaction.
- [ ] Target TODO mapping: `Section 3, items 7-10`.

### Wed 2026-04-15 (`3h`)

- [ ] Fix `LoanService` to use proper generics and return a working repository from `getRepository()`.
- [ ] Add validation for loan IDs, member IDs, and book IDs.
- [ ] Implement borrow logic.
- [ ] Set `loanDate` and `dueDate` when creating a loan.
- [ ] Prevent borrowing when no copies are available.
- [ ] Target TODO mapping: `Section 4, items 1-5`.

### Thu 2026-04-16 (`3h`)

- [ ] Implement return logic for active loans only.
- [ ] Implement extend logic.
- [ ] Add librarian service methods for create, modify, delete, and view active loans.
- [ ] Do a quick repository/service smoke test after each method group.
- [ ] Target TODO mapping: `Section 4, items 6-8`.

### Fri 2026-04-17 (`3h`)

- [x] Update the loan menu flow so it knows which member is logged in.
- [x] Replace placeholder options in `showMyLoansMenu()`.
- [ ] Implement `showActiveLoansMenu()` using real data.
- [ ] Implement `showLoanHistoryMenu()` using real data.
- [ ] Target TODO mapping: `Section 5, items 1-4` and `Section 7, item 1`.

### Sat 2026-04-18 (`4-5h`)

- [ ] Add actions for returning and extending a selected loan.
- [ ] Add a reader-side flow for borrowing a book.
- [ ] Implement `showManageLoansMenu()` with real actions.
- [ ] Add a view for all active loans.
- [ ] Add a create-loan flow.
- [ ] Target TODO mapping: `Section 5, items 5-6` and `Section 6, items 1-3`.

### Sun 2026-04-19 (`4-5h`)

- [ ] Add a modify-loan flow.
- [ ] Add a delete-loan flow.
- [ ] Add a manual return flow for librarians.
- [ ] Decide whether profile view should show active loan info now or later.
- [ ] Reuse `ConsolePrinter` patterns where useful.
- [ ] Run full manual tests:
- [ ] Borrowing decreases `books.available_copies`.
- [ ] Returning increases `books.available_copies`.
- [ ] Active loans only show loans with `return_date IS NULL`.
- [ ] Loan history shows returned loans.
- [ ] Extending changes `due_date` correctly.
- [ ] Librarian views and actions work from the menu.
- [ ] Target TODO mapping: `Section 6, items 4-6`, `Section 7, items 2-3`, and `Section 8, items 1-6`.

### Mon 2026-04-20 (`1-2h`)

- [ ] Run one final smoke test only.
- [ ] Prepare your demo order: reader borrow, view loans, extend/return, librarian manage loans.
- [ ] Write down any assumptions you had to make so you can mention them during the presentation.
- [ ] Freeze the feature set. Do not start new work today unless there is a bug.

## Scope

This file covers the `loan` part of the project, including both reader-side loan functionality and librarian-side loan
management.

## Recommended Order

### 1. Finalize the loan domain model

1. [ ] Clean up `src/loan/Loan.java`.
2. [ ] Add a no-arg constructor if needed for easy mapping.
3. [ ] Replace the current inconsistent constructors with a clear set of constructors.
4. [ ] Make `returnDate` settable from database values.
5. [ ] Add small helper methods such as `isReturned()`, `isActive()`, and `isOverdue()`.

### 2. Add loan DTOs for UI output

1. [ ] Create a DTO for a reader's active loans.
2. [ ] Create a DTO for a reader's loan history.
3. [ ] Create a DTO for the librarian's loan overview.
4. [ ] Include human-readable fields such as book title and member name where needed.

### 3. Implement repository queries

1. [ ] Fix `LoanRepository` to use proper generics: `BaseRepository<Loan, Integer>`.
2. [ ] Implement `getById(Integer id)`.
3. [ ] Implement `getAll()` or a better overview query for loans.
4. [ ] Add `getActiveLoansByMember(Integer memberId)`.
5. [ ] Add `getLoanHistoryByMember(Integer memberId)`.
6. [ ] Add `getActiveLoans()` for librarian view.
7. [ ] Implement `save(Loan entity)` for creating loans.
8. [ ] Implement `update(Loan entity)` for modifying a loan.
9. [ ] Implement `deleteById(Integer id)`.
10. [ ] Rework `returnLoan(...)` to update `loans` and `books` safely in one transaction.

### 4. Implement service rules

1. [ ] Fix `LoanService` to use proper generics and return a working repository from `getRepository()`.
2. [ ] Add validation for loan IDs, member IDs, and book IDs.
3. [ ] Implement borrow logic.
4. [ ] Set `loanDate` and `dueDate` when creating a loan.
5. [ ] Prevent borrowing when no copies are available.
6. [ ] Implement return logic for active loans only.
7. [ ] Implement extend logic.
8. [ ] Add librarian methods for create, modify, delete, and view active loans.

### 5. Connect the reader menus

1. [x] Update the loan menu flow so it knows which member is logged in.
2. [x] Replace placeholder options in `showMyLoansMenu()`.
3. [ ] Implement `showActiveLoansMenu()` using real data.
4. [ ] Implement `showLoanHistoryMenu()` using real data.
5. [ ] Add actions for returning and extending a selected loan.
6. [ ] Add a reader-side flow for borrowing a book.

### 6. Connect the librarian menus

1. [ ] Implement `showManageLoansMenu()` with real actions.
2. [ ] Add a view for all active loans.
3. [ ] Add a create-loan flow.
4. [ ] Add a modify-loan flow.
5. [ ] Add a delete-loan flow.
6. [ ] Add a manual return flow for librarians.

### 7. Wire the integration points outside the loan package

1. [x] Update `MemberController` so loan menus receive the current logged-in member.
2. [ ] Decide whether profile view should show active loan info now or later.
3. [ ] Reuse existing `ConsolePrinter` patterns where possible.

### 8. Test the full flow manually

1. [ ] Borrowing decreases `books.available_copies`.
2. [ ] Returning increases `books.available_copies`.
3. [ ] Active loans only show loans with `return_date IS NULL`.
4. [ ] Loan history shows returned loans.
5. [ ] Extending changes `due_date` correctly.
6. [ ] Librarian views and actions work from the menu.

## Open Questions To Confirm With The Group

1. [ ] What is the official loan period for new loans? The sample data suggests 21 days.
2. [ ] How many times can a loan be extended?
3. [ ] Can overdue loans be extended?
4. [ ] Should librarians be allowed to delete historical loan records, or only active ones?
5. [ ] Should member profile include a loan summary as part of this task?

## Reference Files

- `src/docs/kravspec.md`
- `src/docs/menyskiss`
- `src/docs/generate-library.sql`
- `src/member/MemberService.java`
- `src/member/MemberRepository.java`
- `src/member/MemberController.java`
