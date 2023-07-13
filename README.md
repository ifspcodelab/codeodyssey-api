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

### Checkstyle
Rules for checkstyle: `config/checkstyle/checkstyle.xml`

Rules for suppressions: `config/checkstyle/checkstyle-suppressions.xml`

Reports: `build/reports/checkstyle`

```bash
 ./gradlew checkstyleMain
```

```bash
 ./gradlew checkstyleTest
```

### PMD
```bash
./gradlew check
```

or

```bash
./gradlew ch
```

---

## Security plugins
### OWASP Dependency Check
```bash
./gradlew dependencyCheckAnalyze
```