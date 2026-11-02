# Architecture / 架构说明

This document describes the internal architecture of the iOS Calculator for Android.

本文档描述 iOS Calculator for Android 的内部架构。

---

## Overview / 概述

The app follows a simple two-layer architecture:

```
┌─────────────────────────┐
│      MainActivity       │  ← UI Layer (View)
│   (activity_main.xml)   │
└──────────┬──────────────┘
           │ calls
┌──────────▼──────────────┐
│    CalculatorEngine     │  ← Business Logic (Model)
│   (CalculatorEngine.kt) │
└─────────────────────────┘
```

- **MainActivity** handles UI rendering and user input
- **CalculatorEngine** contains all calculation logic

---

## UI Layer / UI 层

### Layouts / 布局

| Layout | 文件 | Mode | 模式 |
|--------|------|------|------|
| `res/layout/activity_main.xml` | Portrait / 竖屏 | Basic calculator | 基础计算 |
| `res/layout-land/activity_main.xml` | Landscape / 横屏 | Scientific calculator | 科学计算 |

The system automatically selects the correct layout based on orientation. `MainActivity` calls `setContentView(R.layout.activity_main)` and Android resolves to the appropriate layout directory.

### Button Mapping / 按键映射

Each button has a `tag` attribute that identifies its function:

| Tag | Function |
|-----|----------|
| `0`-`9` | Digit input |
| `.` | Decimal point |
| `+`, `−`, `×`, `÷` | Arithmetic operators |
| `=` | Equals / evaluate |
| `AC` | All clear / reset |
| `±` | Negate |
| `%` | Percent |
| `(`, `)` | Parentheses (scientific mode) |
| `sin`, `cos`, `tan` | Trigonometric (degrees) |
| `ln`, `log` | Natural log, log base 10 |
| `√` | Square root |
| `x²`, `x³` | Square, cube |
| `⅟x` | Reciprocal |
| `π`, `e` | Constants |
| `rand` | Random number (0-1) |
| `EE` | Scientific notation |

---

## Calculator Engine / 计算引擎

### State Variables / 状态变量

| Variable | 变量 | Type | Description |
|----------|------|------|-------------|
| `current` | 当前值 | String | Current input/result string |
| `pendingOp` | 待处理运算符 | String? | Pending operator (e.g., "+") |
| `acc` | 累加器 | Double | Accumulated value for pending operation |
| `startedTyping` | 正在输入 | Boolean | Whether user is typing a number |
| `justEvaluated` | 刚计算 | Boolean | Whether `=` was just pressed |
| `lastOp` | 上次运算符 | String? | Last operator for repeat-equals |
| `lastRhs` | 上次右操作数 | Double | Last right operand for repeat-equals |
| `stack` | 括号栈 | ArrayDeque | Stack for nested parentheses |

### iOS "Immediate Calculation" Model / iOS 即时运算模型

Unlike standard calculators that evaluate on `=`, iOS evaluates immediately when an operator is pressed:

1. `2` → current = "2"
2. `+` → acc = 2, pendingOp = "+", startedTyping = false
3. `3` → current = "3"
4. `=` → evaluatePending: 2 + 3 = 5, current = "5"

**Repeat-equals behavior:**
- After `2 + 3 =`, pressing `=` again: applyOp("+", 5, 3) = 8
- `5 + =`: evaluatePending uses current value (5) as rhs → 5 + 5 = 10

### Precision / 精度

- Uses `Double` for calculations
- Formats output with `BigDecimal(MathContext(12))` for up to 12 significant digits
- Integer results (< 1e15) display without decimal point
- `NaN` and `Infinity` display as "错误" (Error)

### Parentheses / 括号

Parentheses use a stack-based approach:
- `(` pushes the current state (acc, pendingOp, startedTyping) and resets
- `)` evaluates the current sub-expression, pops the state, and merges the result

---

## Color Scheme / 配色方案

| Element | 元素 | Color | 颜色 |
|---------|------|-------|------|
| Background | 背景 | `#000000` | Black |
| Digit buttons | 数字键 | `#333333` | Dark gray |
| Function buttons | 功能键 | `#A5A5A5` | Light gray |
| Operator buttons | 运算符 | `#FF9F0A` | Orange |
| Display text | 显示文本 | `#FFFFFF` | White |

---

## Configuration / 配置

### AndroidManifest.xml

- `configChanges`: orientation, screenSize, screenLayout, keyboardHidden, smallestScreenSize, uiMode — prevents activity recreation on rotation
- `screenOrientation`: sensor — allows both portrait and landscape

### build.gradle.kts

- `minSdk`: 24 (Android 7.0)
- `targetSdk`: 34 (Android 14)
- `compileSdk`: 34
- `versionCode`: 1, `versionName`: "1.0"
- Java/Kotlin compatibility: 17
