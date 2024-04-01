# 🚀 Essentia-Framework 🚀

## 🌟 Overview

Essentia is a lightweight Java framework designed to simplify and enhance Java project development. It contains features
that many developers commonly need in their projects, presented in a lightweight and easy-to-incorporate manner.

## 🧩 Modular Design

Essentia is thoughtfully organized into a series of submodules, each dedicated to a specific aspect of Java project
development:

- 📝 **essentia-configure**: Simplify configuration management with a lightweight, OOP-driven config system supporting
  both `.properties` and `.yml` formats.
- 💉 **essentia-inject**: Easily manage dependency injection in your project with a lightweight mechanism that enhances
  modularity without the overhead of heavy frameworks.
- ⏰ **essentia-schedule**: Effortlessly schedule methods within your project, allowing for recurring tasks, periodic
  updates, and background processing.

## 📦 Flexible Integration

Essentia integrates seamlessly into Java projects using Gradle or Maven. Whether you prefer Gradle's flexibility or
Maven's convention-over-configuration approach, Essentia supports both build systems, ensuring compatibility with a wide
range of project setups.

## 🚀 Getting Started

### 🛠️ Installation

Incorporate Essentia into your project with ease. Choose your preferred build system:

- **Gradle (Kotlin DSL)**:
  Add the desired Essentia modules as dependencies in your `build.gradle.kts` file:

  ```kotlin
  dependencies {
      implementation("me.xra1ny:essentia-configure:1.0")
      implementation("me.xra1ny.essentia-except:1.0")
      implementation("me.xra1ny:essentia-inject:1.0")
      implementation("me.xra1ny:essentia-schedule:1.0")
  }
  ```

- **Gradle (Groovy DSL)**:
  Add the desired Essentia modules as dependencies in your `build.gradle` file:

  ```groovy
  dependencies {
      implementation 'me.xra1ny:essentia-configure:1.0'
      implementation 'me.xra1ny.essentia-except:1.0'
      implementation 'me.xra1ny:essentia-inject:1.0'
      implementation 'me.xra1ny:essentia-schedule:1.0'
  }
  ```

- **Maven**:
  Include Essentia dependencies in your `pom.xml`:

  ```xml
  <dependencies>
      <dependency>
          <groupId>me.xra1ny</groupId>
          <artifactId>essentia-configure</artifactId>
          <version>1.0</version>
      </dependency>
      <dependency>
          <groupId>me.xra1ny</groupId>
          <artifactId>essentia-except</artifactId>
          <version>1.0</version>
      </dependency>
      <dependency>
          <groupId>me.xra1ny</groupId>
          <artifactId>essentia-inject</artifactId>
          <version>1.0</version>
      </dependency>
      <dependency>
          <groupId>me.xra1ny</groupId>
          <artifactId>essentia-schedule</artifactId>
          <version>1.0</version>
      </dependency>
  </dependencies>
  ```

### 📖 Documentation

Unlock the full potential of Essentia with comprehensive guides and examples. Whether you're configuring settings,
managing dependency injection, or scheduling methods, our documentation will guide you every step of the way.

## 🤝 Contribute

Join us in making Essentia even better! Whether you want to report a bug, suggest a feature, or contribute code
improvements, your input is invaluable to the Essentia community. Submit pull requests and let's work together to create
a framework that empowers Java developers everywhere!