# Changelog / 更新日志

All notable changes to this project will be documented in this file.

本文件记录项目的所有重要变更。

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.0.0] - 2026-10-27

### Added / 新增
- Initial release of the iOS-style calculator for Android.
- Portrait mode: classic 4-column iOS keypad (AC, ±, %, ÷, ×, −, +, =).
- Landscape mode: scientific functions panel (sin, cos, tan, ln, log, √, x², x³, 1/x, π, e, rand, EE, parentheses).
- iOS "immediate calculation" engine with repeat-equals behavior.
- Division by zero shows "错误" (Error).
- Trigonometric functions use degrees (matching iOS).
- Auto orientation switching between basic and scientific layouts.
- Pixel-perfect iOS color scheme and rounded button design.
- Wide "0" button in pill shape.
- App launcher icon (dark background with orange "=").

### Technical / 技术细节
- Native Android development with Kotlin 1.9.24.
- AGP 8.5.2, compileSdk 34, minSdk 24, targetSdk 34.
- Repository mirrors configured for Aliyun (China-friendly builds).
- CalculatorEngine with BigDecimal for precision formatting.

---

## [Unreleased]

### Planned / 计划中
- Add Gradle wrapper for easier builds.
- Add unit tests for CalculatorEngine.
- Add haptic feedback on button press.
- Add history panel for past calculations.
