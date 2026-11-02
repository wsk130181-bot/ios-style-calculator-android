# Contributing Guide / 参与贡献指南

Thank you for your interest in contributing to the iOS Calculator for Android! We welcome contributions from the community.

感谢您对 iOS Calculator for Android 项目的贡献兴趣！我们欢迎社区的贡献。

---

## How Can I Contribute? / 如何贡献？

### Reporting Bugs / 报告缺陷

If you find a bug, please open an issue using the [Bug Report template](https://github.com/wsk130181-bot/ios-style-calculator-android/issues/new?template=bug_report.yml). Include:

- A clear description of the bug
- Steps to reproduce
- Expected vs. actual behavior
- Device and Android version
- App version

### Suggesting Features / 建议功能

Have an idea for a new feature? Open an issue using the [Feature Request template](https://github.com/wsk130181-bot/ios-style-calculator-android/issues/new?template=feature_request.yml). Describe:

- The problem your feature solves
- The solution you'd like
- Any alternatives you've considered

### Pull Requests / 提交 Pull Request

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Commit your changes (`git commit -m 'feat: add amazing feature'`)
5. Push to the branch (`git push origin feature/amazing-feature`)
6. Open a Pull Request

---

## Development Setup / 开发环境设置

### Prerequisites / 前置条件

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK Platform 34
- Gradle 8.7

### Steps / 步骤

1. Fork and clone the repository
2. Open the project in Android Studio
3. Wait for Gradle sync to complete
4. Make your changes
5. Build and test on a device or emulator

---

## Code Style / 代码风格

- Follow the existing code style in the project
- Use 4 spaces for indentation in Kotlin
- Keep line length under 120 characters
- Use descriptive variable and function names
- Add comments for complex logic

### Kotlin Guidelines / Kotlin 规范

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use `camelCase` for variables and functions
- Use `PascalCase` for classes
- Use `UPPER_SNAKE_CASE` for constants
- Prefer `val` over `var` when possible
- Add null safety (`?`) where appropriate

### Commit Messages / 提交信息

Follow the [Conventional Commits](https://www.conventionalcommits.org/) format:

```
<type>: <description>

Types:
- feat: A new feature
- fix: A bug fix
- docs: Documentation only changes
- style: Formatting, missing semi-colons, etc.
- refactor: A code change that neither fixes a bug nor adds a feature
- test: Adding missing tests
- chore: Changes to the build process or auxiliary tools
```

Examples:
- `feat: add haptic feedback on button press`
- `fix: resolve division by zero crash in scientific mode`
- `docs: update README with build instructions`

---

## Pull Request Process / Pull Request 流程

1. Ensure your changes don't break the build
2. Update documentation if needed
3. Fill out the PR template completely
4. Request a review from maintainers
5. Address any feedback promptly

---

## Code of Conduct / 行为准则

Please note that this project is released with a [Contributor Code of Conduct](CODE_OF_CONDUCT.md). By participating, you agree to abide by its terms.

本项目遵循 [行为准则](CODE_OF_CONDUCT.md)。参与即表示您同意遵守其条款。

---

## Questions? / 有问题？

Feel free to open an issue or start a discussion. We're happy to help!

欢迎提出 issue 或开启讨论，我们很乐意帮助！
