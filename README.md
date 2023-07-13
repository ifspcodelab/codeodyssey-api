# CodeOdyssey API
API for the CodeOdyssey project. A platform to make and correct coding-related exercises.

---

## Code quality plugins
### Spotless
How to run:

Check
```bash
./gradlew spotlessCheck
```

Apply format
```bash
./gradlew spotlessApply
```

### Spotbugs
How to run:

Checks files in the `main` folder
```bash
./gradlew spotbugsMain
```

Checks files in the `test` folder
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
Run it with any of the commands below. Do note that PMD also runs the other code quality plugins.

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

How to run:
```bash
./gradlew dependencyCheckAnalyze
```