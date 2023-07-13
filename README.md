# CodeOdyssey API
API for the CodeOdyssey project. A platform to make and correct coding-related exercises.

---

## Code quality plugins
### Spotless
Check
```bash
./gradlew spotlessCheck
```

Apply format
```bash
./gradlew spotlessApply
```

### Spotbugs
```bash
./gradlew spotbugsMain
```

```bash
./gradlew spotbugsTest
```

---

## Security plugins
### OWASP Dependency Check
```bash
./gradlew dependencyCheckAnalyze
```