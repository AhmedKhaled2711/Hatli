# Hatli fonts (`:core:designsystem`)

## Runtime files

```
core/designsystem/src/main/res/font/
```

| Family | Files | Role |
|--------|-------|------|
| **Mada** | `mada_extralight` … `mada_extrabold` | Primary UI (`HatliFontFamily`) |
| **Tajawal** | `tajawal_extralight` … `tajawal_bold` | Alternate (`HatliFonts.Tajawal`) |

Android rules: lowercase `a-z0-9_`, `.ttf` / `.otf` → `R.font.<name>`.

## Use everywhere

```kotlin
Text("Hatli", style = MaterialTheme.typography.titleLarge) // Mada

Text(
    text = "هاتلي",
    fontFamily = HatliFonts.Tajawal,
    fontWeight = FontWeight.Bold,
)
```

Do **not** put fonts under `feature/*/res/font`.
