# Название проекта

Автоматизация тестирования комплексного сервиса, взаимодействующего с СУБД и API банка.

## Начало работы

* Склонировать проект себе на ПК с помощью команды git-clone в терминале

### Prerequisites
Git, браузер Google Chrome, InetelliJ IDEA, Docker Desktop.

###  Установка и запуск
* *Установить Git на ПК. [Руководство по установке Git](https://netology-code.github.io/guides/git/)*
* *Установить Google Chrome на ПК. [Руководство по установке Google Chrome](https://support.google.com/chrome/answer/95346?hl=RU&ref_topic=7439538)*
* *Установить на ПК InetelliJ IDEA. [Руководство по установле InetelliJ IDEA ](https://harrix.dev/blog/2019/install-intellij-idea/)*
* *Установить на ПК Docker Desktop. [Руководство по установке Docker](https://github.com/netology-code/aqa-homeworks/blob/master/docker/installation.md)*
* *Запустить Docker Desktop*
* *Запустить InetelliJ IDEA*
* *Открыть проект CourseworkAQAMID (File-Open-выбрать папку проекта)*
* *Открыть терминал в InetelliJ IDEA*
* *Набрать команду docker-compose up и нажать на кнопку enter*
* *После того, как появится надпись ready for connections, открыть еще одно окно терминала и с помощью команды java -jar aqa-shop.jar -P: jdbc.url=jdbc:mysql://127.0.0.1:3306/app либо java -jar aqa-shop.jar -P: jdbc.url=jdbc:postgresql://127.0.0.1:5432/app  запустить jar файл и нажать на кнопку enter*
* *После этого можно запускать авто-тесты. (Открыть новое окно терминала и прописать команду ./gradlew clean test и нажать на кнопку enter)*

### Бэйдж сборки
[![Build status](https://ci.appveyor.com/api/projects/status/apnt7ly94fvrw4rx?svg=true)](https://ci.appveyor.com/project/KaterinaVoroshilova/courseworkaqamid)
