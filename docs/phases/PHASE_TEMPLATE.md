# Phase Template

Copy this folder structure for each phase:

```
docs/phases/phase-N-<name>/
  01-product-requirements.md
  02-scope.md
  03-user-flows.md
  04-data-models.md
  05-architecture-decisions.md
  06-screen-specifications.md
  07-api-firebase-design.md
  08-test-cases.md
  09-definition-of-done.md
  10-code-review-checklist.md
```

---

## 📄 01 — Product Requirements

**Problem**

**Who**

**Success looks like**

**Non-goals (high level)**

---

## 🎯 02 — Scope

### In

-

### Out

-

### Assumptions

-

---

## 🧭 03 — User Flows

### Happy path

1.

### Edge / failure paths

1.

---

## 🧩 04 — Data Models

| Model | Fields | Notes |
|-------|--------|-------|
| | | |

---

## 🏗️ 05 — Architecture Decisions

| Decision | Why | Trade-offs | Scalability |
|----------|-----|------------|-------------|
| | | | |

---

## 📱 06 — Screen Specifications

| Screen | States | Primary actions | Nav in/out |
|--------|--------|-----------------|------------|
| | Loading / Content / Empty / Error | | |

---

## 🔌 07 — API / Firebase Design

### Collections / paths

### Auth & security notes

### Offline / sync

---

## 🧪 08 — Test Cases

| ID | Scenario | Expected |
|----|----------|----------|
| | | |

---

## ✅ 09 — Definition of Done

- [ ] Product requirements covered
- [ ] Scope respected (nothing Out shipped)
- [ ] Navigation matches flows
- [ ] Domain free of Firebase/UI
- [ ] Repository returns domain models only
- [ ] UDF (UiState / UiEvent / UiEffect)
- [ ] Validation, loading, empty, error states
- [ ] Design system used (no raw Material in features)
- [ ] Tests / manual checklist passed
- [ ] Code review checklist done
- [ ] Docs updated

---

## 👀 10 — Code Review Checklist

- [ ] No feature → feature dependencies
- [ ] No SDK calls in UI
- [ ] No business logic in Composables
- [ ] Errors mapped to domain (`AppError` / friendly messages)
- [ ] Naming & module boundaries clear
- [ ] No unnecessary code / dead stubs left behind
