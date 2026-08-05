# 10 — Code Review Checklist (Phase 3)

- [ ] No feature → feature dependencies
- [ ] No SDK calls in UI / ViewModels (UseCase → Repository only)
- [ ] No business logic in Composables
- [ ] Errors mapped to `AppError` / friendly messages
- [ ] Accept is race-safe (transaction or conditional)
- [ ] Status enum does not use `Shopping` as a status name
- [ ] Naming & module boundaries clear (`:feature:request`)
- [ ] No Create Request stub / dead fake seed as source of truth
- [ ] Insets: screens own system bar padding once
