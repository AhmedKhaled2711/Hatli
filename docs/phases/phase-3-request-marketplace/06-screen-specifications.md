# 06 — Screen Specifications (Phase 3)

| Screen | States | Primary actions | Nav |
|--------|--------|-----------------|-----|
| Category picker | Content | Select category | from FAB/Hero → wizard step 1 of branch |
| Shopping steps | Content / Error | Next / Back | within request graph |
| Pickup steps | Content / Error | Next / Back | |
| Bills steps | Content / Error | Next / Back | |
| Custom steps | Content / Error | Next / Back | |
| Fee + Review | Content / Loading / Error | Submit / Edit fee | Submit → Main Shell |
| Runner Pending card | Content / Empty / Error | Accept | Home / Jobs |
| Requester Current Request | Pending / Accepted | Open detail (later) | Home |

## Design notes

- Large category cards (design system `HatliCard` + typography)
- Suggested fee chip/field with edit
- EN/AR via strings / configuration locale
- No raw Material buttons in feature UI
