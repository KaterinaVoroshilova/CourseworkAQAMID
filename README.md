# Название проекта

Автоматизация тестирования комплексного сервиса, взаимодействующего с СУБД и API банка.

## Начало работы

* Склонировать проект себе на ПК с помощью команды git clone git@github.com:KaterinaVoroshilova/CourseworkAQAMID.git в терминале

### Prerequisites
Git, браузер Google Chrome, InetelliJ IDEA, Docker Desktop.

###  Установка и запуск
* *Установить Git на ПК. [Руководство по установке Git](https://netology-code.github.io/guides/git/)*
* *Установить Google Chrome на ПК. [Руководство по установке Google Chrome](https://support.google.com/chrome/answer/95346?hl=RU&ref_topic=7439538)*
* *Установить на ПК IntelliJ IDEA. [Руководство по установле InetelliJ IDEA ](https://harrix.dev/blog/2019/install-intellij-idea/)*
* *Установить на ПК Docker Desktop. [Руководство по установке Docker](https://github.com/netology-code/aqa-homeworks/blob/master/docker/installation.md)*
* *Запустить Docker Desktop*
* *Запустить IntelliJ IDEA*
* *Открыть проект CourseworkAQAMID (File-Open-выбрать папку проекта)*
* *Открыть терминал в InetelliJ IDEA*
* *Набрать команду docker-compose up и нажать на кнопку enter*
* *После того, как появится надпись ready for connections, открыть еще одно окно терминала и запустить jar файл с помощью команд:*

для mysql

java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar

для postgresql

java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar

* *После этого можно запускать авто-тесты. (Открыть новое окно терминала и прописать команды:*

для mysql

./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"

для postgresql

./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"


* [План автоматизации](https://github.com/KaterinaVoroshilova/CourseworkAQAMID/blob/main/docs/Plan.md)
* [Отчет по итогам тестирования](https://github.com/KaterinaVoroshilova/CourseworkAQAMID/blob/main/docs/Report.md)
* [Отчет по итогам автоматизации](https://github.com/KaterinaVoroshilova/CourseworkAQAMID/blob/main/docs/Summary.md)

### Бэйдж сборки
[![Build status](https://ci.appveyor.com/api/projects/status/apnt7ly94fvrw4rx?svg=true)](https://ci.appveyor.com/project/KaterinaVoroshilova/courseworkaqamid)

Сборка в статусе failing,т.к. не все тесты проходят
